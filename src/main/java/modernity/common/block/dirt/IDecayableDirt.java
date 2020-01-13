/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@FunctionalInterface
public interface IDecayableDirt {

    default boolean canDecay( World world, BlockPos pos, BlockState state ) {
        return ! DirtlikeBlock.canRemain( world, pos, state );
    }

    BlockState getDecayState( World world, BlockPos pos, BlockState state );

    default void decay( World world, BlockPos pos, BlockState state ) {
        BlockState decayState = getDecayState( world, pos, state );
        world.setBlockState( pos, decayState, 3 );
    }

    default boolean decayTick( World world, BlockPos pos, BlockState state, Random rand ) {
        if( canDecay( world, pos, state ) ) {
            decay( world, pos, state );
            return true;
        }
        return false;
    }
}
