/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.Random;

public interface ISpreadingLogicType extends IDirtLogicType {

    default int getRequiredSpreadLight() {
        return 9;
    }

    default boolean canSpread( World world, BlockPos pos, BlockState state ) {
        BlockPos up = pos.up();
        int block = world.getLightFor( LightType.BLOCK, up );
        int sky = world.getLightFor( LightType.SKY, up );
        return Math.max( block, sky ) >= getRequiredSpreadLight();
    }

    boolean canGrowUpon( DirtLogic logic );

    default boolean canGrowAt( World world, BlockPos pos, BlockState state ) {
        DirtLogic logic = DirtLogic.getLogic( world, pos );
        if( logic == null ) return false;
        BlockPos up = pos.up();
        BlockState upState = world.getBlockState( up );
        return canGrowUpon( logic ) && DirtLogic.canRemain( world, pos, state ) && ! preventsGrow( world, up, upState );
    }

    default boolean preventsGrow( World world, BlockPos pos, BlockState state ) {
        return state.getFluidState().isTagged( FluidTags.WATER );
    }

    IDirtLogicType getGrowType( World world, BlockPos pos, BlockState state );

    default void growAt( World world, BlockPos pos, BlockState state ) {
        IDirtLogicType type = getGrowType( world, pos, state );
        DirtLogic.switchType( world, pos, type );
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
        if( ! world.isAreaLoaded( pos, 3 ) ) return;
        if( canSpread( world, pos, state ) ) {
            spread( world, pos, state, rand );
        }
    }
}
