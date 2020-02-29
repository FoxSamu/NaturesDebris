/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.plant.growing.WetGrowLogic;
import modernity.common.entity.MDEntityTags;
import modernity.common.util.MDDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FoxgloveBlock extends DoublePlantBlock {
    public FoxgloveBlock( Properties properties ) {
        super( properties, REGULAR_SHAPE );
        setGrowLogic( new WetGrowLogic( this ) );
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( entity instanceof PlayerEntity && ( ( (PlayerEntity) entity ).isCreative() || entity.isSpectator() ) )
            return;
        if( ! entity.getType().isContained( MDEntityTags.FOXGLOVE_IMMUNE ) ) {
            entity.attackEntityFrom( MDDamageSource.FOXGLOVE, 1 );
            if( entity instanceof LivingEntity ) {
                ( (LivingEntity) entity ).addPotionEffect( new EffectInstance(
                    Effects.NAUSEA, 200, 2, false, false
                ) );
                ( (LivingEntity) entity ).addPotionEffect( new EffectInstance(
                    Effects.POISON, 200, 2, false, false
                ) );
            }
        }
    }
}
