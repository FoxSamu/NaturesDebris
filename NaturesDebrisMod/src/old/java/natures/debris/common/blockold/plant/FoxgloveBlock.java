/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.blockold.plant.growing.WetGrowLogic;
import natures.debris.common.entity.MDEntityTags;
import natures.debris.common.util.MDDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FoxgloveBlock extends DoublePlantBlock {
    public FoxgloveBlock(Properties properties) {
        super(properties, REGULAR_SHAPE);
        setGrowLogic(new WetGrowLogic(this));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof PlayerEntity && (((PlayerEntity) entity).isCreative() || entity.isSpectator()))
            return;
        if (!entity.getType().isContained(MDEntityTags.FOXGLOVE_IMMUNE)) {
            entity.attackEntityFrom(MDDamageSource.FOXGLOVE, 1);
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(
                    Effects.NAUSEA, 200, 2, false, false
                ));
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(
                    Effects.POISON, 200, 2, false, false
                ));
            }
        }
    }
}
