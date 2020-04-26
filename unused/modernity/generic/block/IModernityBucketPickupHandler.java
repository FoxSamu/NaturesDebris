/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 21 - 2019
 * Author: rgsw
 */

package modernity.generic.block;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@FunctionalInterface
public interface IModernityBucketPickupHandler {
    Fluid pickupFluidModernity( IWorld world, BlockPos pos, BlockState state );
}
