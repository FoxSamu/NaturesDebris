/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface ISpreadableDirt {

    default int getRequiredSpreadLight() {
        return 9;
    }

    default boolean canSpread( World world, BlockPos pos, BlockState state ) {
        return world.getLight( pos.up() ) >= getRequiredSpreadLight();
    }

    boolean canGrowUpon( BlockState state );

    default boolean canGrowAt( World world, BlockPos pos, BlockState state ) {
        BlockPos up = pos.up();
        BlockState upState = world.getBlockState( up );
        return canGrowUpon( state ) && DirtlikeBlock.canRemain( world, pos, state ) && ! preventsGrow( world, up, upState );
    }

    default boolean preventsGrow( World world, BlockPos pos, BlockState state ) {
        return state.getFluidState().isTagged( FluidTags.WATER );
    }

    BlockState getGrowState( World world, BlockPos pos, BlockState state );

    default void growAt( World world, BlockPos pos, BlockState state ) {
        BlockState growState = getGrowState( world, pos, state );
        world.setBlockState( pos, growState );
    }

    default void spread( World world, BlockPos pos, BlockState state, Random rand ) {
        for( int i = 0; i < 4; i++ ) {
            BlockPos growPos = pos.add( rand.nextInt( 3 ) - 1, rand.nextInt( 5 ) - 3, rand.nextInt( 3 ) - 1 );
            BlockState currState = world.getBlockState( growPos );
            if( canGrowAt( world, growPos, currState ) ) {
                growAt( world, growPos, currState );
            }
        }
    }

    default void spreadTick( World world, BlockPos pos, BlockState state, Random rand ) {
        if( canSpread( world, pos, state ) ) {
            spread( world, pos, state, rand );
        }
    }
}
