/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDangerousPlant {
    boolean dealsDamage(World world, BlockPos pos, BlockState state, Entity entity);
    DamageSource getDamageSource(World world, BlockPos pos, BlockState state, Entity entity);
    float getDamage(World world, BlockPos pos, BlockState state, Entity entity);
}
