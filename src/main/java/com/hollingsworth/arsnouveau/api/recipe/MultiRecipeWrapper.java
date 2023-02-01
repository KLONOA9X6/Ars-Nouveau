package com.hollingsworth.arsnouveau.api.recipe;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.common.block.tile.WixieCauldronTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.brewing.BrewingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Stores a list of recipes and provides methods to check if a given inventory contains the items needed to craft any of the recipes.
 * These recipes typically all correspond to the same output item.
 */
public class MultiRecipeWrapper implements IRecipeWrapper{

    public Set<SingleRecipe> recipes;

    public static Map<Item, MultiRecipeWrapper> RECIPE_CACHE = new HashMap<>();

    public MultiRecipeWrapper() {
        recipes = new HashSet<>();
    }

    public static MultiRecipeWrapper fromStack(ItemStack stack, Level level){
        MultiRecipeWrapper wrapper = new MultiRecipeWrapper();
        if (stack.getItem() == Items.POTION) {
            for (BrewingRecipe r : ArsNouveauAPI.getInstance().getAllPotionRecipes()) {
                if (ItemStack.matches(stack, r.getOutput())) {
                    List<Ingredient> list = new ArrayList<>();
                    list.add(new PotionIngredient(r.getInput().getItems()[0]));
                    list.add(r.getIngredient());
                    wrapper.addRecipe(list, r.getOutput(), null);
                }
            }
        } else {
            if(RECIPE_CACHE.containsKey(stack.getItem())){
                return RECIPE_CACHE.get(stack.getItem());
            }
            for (Recipe r : level.getServer().getRecipeManager().getRecipes()) {
                if (r.getResultItem() == null || r.getResultItem().getItem() != stack.getItem())
                    continue;

                if (r instanceof ShapedRecipe) {
                    ShapedHelper helper = new ShapedHelper((ShapedRecipe) r);
                    for (List<Ingredient> iList : helper.getPossibleRecipes()) {
                        wrapper.addRecipe(iList, r.getResultItem(), r);
                    }
                }

                if (r instanceof ShapelessRecipe)
                    wrapper.addRecipe(r.getIngredients(), r.getResultItem(), r);

            }
            RECIPE_CACHE.put(stack.getItem(), wrapper);
        }
        return wrapper;
    }

    @Nullable
    @Override
    public InstructionsForRecipe canCraft(Map<Item, Integer> inventory, Level world, BlockPos pos) {
        for (SingleRecipe recipe : recipes) {
            List<ItemStack> itemsNeeded = getItemsNeeded(inventory, world, pos, recipe);
            if (itemsNeeded != null) {
                return new InstructionsForRecipe(recipe, itemsNeeded);
            }
        }
        return null;
    }

    /**
     * Returns a list of items needed to craft the recipe, or null if the recipe can't be crafted.
     * This method tracks that the inventory has enough of each item to craft the recipe.
     */
    @Nullable
    public List<ItemStack> getItemsNeeded(Map<Item, Integer> inventory, Level world, BlockPos pos, SingleRecipe recipe){
        Map<Item, Integer> map = new HashMap<>(inventory);

        List<ItemStack> items = new ArrayList<>();
        for (Ingredient i : recipe.recipeIngredients) {
            boolean foundStack = false;
            for (ItemStack stack : i.getItems()) {
                // Return success if we could consume this potion as a liquid from a jar
                if (stack.getItem() == Items.POTION) {
                    Potion potion = PotionUtils.getPotion(stack);
                    if (potion == Potions.WATER || WixieCauldronTile.findNeededPotion(potion, 300, world, pos) != null) {
                        foundStack = true;
                        break;
                    }
                }
                // If our inventory has the item, decrease the effective count
                if (inventory.containsKey(stack.getItem()) && map.get(stack.getItem()) > 0) {
                    map.put(stack.getItem(), map.get(stack.getItem()) - 1);
                    foundStack = true;
                    items.add(stack.copy());
                    break;
                }
            }
            if (!foundStack)
                return null;
        }
        return items;
    }


    public boolean addRecipe(List<Ingredient> recipe, ItemStack outputStack, Recipe iRecipe) {
        return recipes.add(new SingleRecipe(recipe, outputStack, iRecipe));
    }

    public boolean isEmpty(){
        return recipes.isEmpty();
    }
}
