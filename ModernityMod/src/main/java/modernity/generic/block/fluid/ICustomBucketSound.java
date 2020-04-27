/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.block.fluid;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Implementing fluids have a custom bucket sound for picking and placing.
 *
 * @author RGSW
 */
public interface ICustomBucketSound {
    SoundEvent getPickupSound( IWorld world, BlockPos pos );
    SoundEvent getPlaceSound( IWorld world, BlockPos pos );
}
