/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.block.fluid;

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
