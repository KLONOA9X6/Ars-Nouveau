package com.hollingsworth.arsnouveau.common.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hollingsworth.arsnouveau.common.crafting.recipes.CrushRecipe;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CrushRecipeProvider implements DataProvider {
    public final DataGenerator generator;
    public List<CrushRecipe> recipes = new ArrayList<>();
    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    public static final Logger LOGGER = LogManager.getLogger();

    public CrushRecipeProvider(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        recipes.add(new CrushRecipe("stone", Ingredient.of(Tags.Items.STONE))
                .withItems(Items.GRAVEL.getDefaultInstance(), 1.0f));
        recipes.add(new CrushRecipe("gravel", Ingredient.of(Tags.Items.GRAVEL))
                .withItems(Items.SAND.getDefaultInstance(), 1.0f)
                .withItems(Items.FLINT.getDefaultInstance(), 0.02f));
        recipes.add(new CrushRecipe("cobblestone", Ingredient.of(Tags.Items.COBBLESTONE)).withItems(Items.GRAVEL.getDefaultInstance(), 1.0f));
        recipes.add(new CrushRecipe("white_dye", Ingredient.of(Items.LILY_OF_THE_VALLEY)).withItems(new ItemStack(Items.WHITE_DYE, 2)));
        recipes.add(new CrushRecipe("orange_dye", Ingredient.of(Items.ORANGE_TULIP)).withItems(new ItemStack(Items.ORANGE_DYE, 2)));
        recipes.add(new CrushRecipe("magenta_dye", Ingredient.of(Items.ALLIUM)).withItems(new ItemStack(Items.MAGENTA_DYE, 2)));
        recipes.add(new CrushRecipe("light_blue_dye", Ingredient.of(Items.BLUE_ORCHID)).withItems(new ItemStack(Items.LIGHT_BLUE_DYE, 2)));
        recipes.add(new CrushRecipe("yellow_dye", Ingredient.of(Items.DANDELION)).withItems(new ItemStack(Items.YELLOW_DYE, 2)));
        recipes.add(new CrushRecipe("pink_dye", Ingredient.of(Items.PINK_TULIP)).withItems(new ItemStack(Items.PINK_DYE, 2)));
        recipes.add(new CrushRecipe("light_gray_dye_oxeye", Ingredient.of(Items.OXEYE_DAISY)).withItems(new ItemStack(Items.LIGHT_GRAY_DYE, 2)));
        recipes.add(new CrushRecipe("light_gray_dye_azure", Ingredient.of(Items.AZURE_BLUET)).withItems(new ItemStack(Items.LIGHT_GRAY_DYE, 2)));
        recipes.add(new CrushRecipe("light_gray_dye_tulip", Ingredient.of(Items.WHITE_TULIP)).withItems(new ItemStack(Items.LIGHT_GRAY_DYE, 2)));
        recipes.add(new CrushRecipe("blue_dye", Ingredient.of(Items.CORNFLOWER)).withItems(new ItemStack(Items.BLUE_DYE, 2)));
        recipes.add(new CrushRecipe("brown_dye", Ingredient.of(Items.COCOA_BEANS)).withItems(new ItemStack(Items.BROWN_DYE, 2)));
        recipes.add(new CrushRecipe("red_dye_tulip", Ingredient.of(Items.RED_TULIP)).withItems(new ItemStack(Items.RED_DYE, 2)));
        recipes.add(new CrushRecipe("red_dye_beetroot", Ingredient.of(Items.BEETROOT)).withItems(new ItemStack(Items.RED_DYE, 2)));
        recipes.add(new CrushRecipe("red_dye_poppy", Ingredient.of(Items.POPPY)).withItems(new ItemStack(Items.RED_DYE, 2)));
        recipes.add(new CrushRecipe("red_dye_rose_bush", Ingredient.of(Items.ROSE_BUSH)).withItems(new ItemStack(Items.RED_DYE, 4)));
        recipes.add(new CrushRecipe("yellow_dye_sunflower", Ingredient.of(Items.SUNFLOWER)).withItems(new ItemStack(Items.YELLOW_DYE, 4)));
        recipes.add(new CrushRecipe("magenta_dye_lilac", Ingredient.of(Items.LILAC)).withItems(new ItemStack(Items.MAGENTA_DYE, 4)));
        recipes.add(new CrushRecipe("pink_dye_peony", Ingredient.of(Items.PEONY)).withItems(new ItemStack(Items.PINK_DYE, 4)));


        recipes.add(new CrushRecipe("terracotta", Ingredient.of(Items.TERRACOTTA)).withItems(Items.RED_SAND.getDefaultInstance()));
        recipes.add(new CrushRecipe("sugar_cane", Ingredient.of(Items.SUGAR_CANE)).withItems(new ItemStack(Items.SUGAR, 2)));
        recipes.add(new CrushRecipe("sandstone_to_sand", Ingredient.of(Items.SANDSTONE)).withItems(Items.SAND.getDefaultInstance()));
        Path output = this.generator.getOutputFolder();
        for (CrushRecipe g : recipes) {
            Path path = getRecipePath(output, g.getId().getPath());
            DataProvider.saveStable(cache, g.asRecipe(), path);
        }
    }

    private static Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/ars_nouveau/recipes/" + str + ".json");
    }

    @Override
    public String getName() {
        return "Crush";
    }
}
