/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDangerousPlant {
    boolean dealsDamage( World world, BlockPos pos, BlockState state, Entity entity );
    DamageSource getDamageSource( World world, BlockPos pos, BlockState state, Entity entity );
    float getDamage( World world, BlockPos pos, BlockState state, Entity entity );
}
