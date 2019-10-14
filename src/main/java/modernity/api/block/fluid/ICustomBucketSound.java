/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
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
