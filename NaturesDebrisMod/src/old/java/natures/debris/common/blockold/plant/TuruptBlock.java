/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.blockold.plant.growing.FertilityGrowLogic;
import natures.debris.common.entity.MDEntityTags;
import natures.debris.common.util.MDDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TuruptBlock extends SimplePlantBlock implements IDangerousPlant {
    public TuruptBlock(Properties properties) {
        super(properties, makePlantShape(7, 7));
        setGrowLogic(new FertilityGrowLogic(this));
    }

    @Override
    public boolean dealsDamage(World world, BlockPos pos, BlockState state, Entity entity) {
        return !entity.getType().isContained(MDEntityTags.TURUPT_IMMUNE);
    }

    @Override
    public DamageSource getDamageSource(World world, BlockPos pos, BlockState state, Entity entity) {
        return MDDamageSource.TURUPT;
    }

    @Override
    public float getDamage(World world, BlockPos pos, BlockState state, Entity entity) {
        return 1.5F;
    }
}
