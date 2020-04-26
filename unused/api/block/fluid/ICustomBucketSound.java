/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
 */

package modernity.generic.block.fluid;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface ICustomBucketSound {
    SoundEvent getPickupSound( IWorld world, BlockPos pos );
    SoundEvent getPlaceSound( IWorld world, BlockPos pos );
}
