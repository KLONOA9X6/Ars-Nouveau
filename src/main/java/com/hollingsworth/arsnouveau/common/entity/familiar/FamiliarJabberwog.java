package com.hollingsworth.arsnouveau.common.entity.familiar;

import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.common.entity.ModEntities;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class FamiliarJabberwog extends FlyingFamiliarEntity {

    public FamiliarJabberwog(EntityType<? extends CreatureEntity> ent, World world) {
        super(ent, world);

    }

    @SubscribeEvent
    public void spellResolveEvent(SpellModifierEvent event) {
        if(isAlive() && getOwner() != null && getOwner().equals(event.caster)){
            event.builder.addDamageModifier(3.0f);
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public PlayState walkPredicate(AnimationEvent event) {
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hop"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.ENTITY_FAMILIAR_JABBERWOG;
    }
}
