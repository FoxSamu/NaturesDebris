/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import java.util.Collection;
import java.util.Random;

public abstract class LimitedTallDirectionalPlantBlock extends TallDirectionalPlantBlock {
    protected final IntegerProperty length;
    protected final int maxLength;

    public LimitedTallDirectionalPlantBlock( Properties properties, Direction growDir ) {
        super( properties, growDir );

        length = getLengthProperty();
        Collection<Integer> allowed = length.getAllowedValues();
        int max = 0;
        for( int i : allowed ) {
            if( i > max ) max = i;
        }
        maxLength = max;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( getLengthProperty() );
    }

    public abstract IntegerProperty getLengthProperty();

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        state = super.updatePostPlacement( state, dir, adjState, world, pos, adjPos );
        if( state.getBlock() != this ) return state;

        if( dir == growDir.getOpposite() ) {
            boolean self = isSelfState( world, adjPos, adjState );
            int len = self ? adjState.get( length ) + 1 : 1;
            if( len > maxLength ) {
                return Blocks.AIR.getDefaultState();
            } else {
                return state.with( length, len );
            }
        }
        return state;
    }

    @Override
    public BlockState computeStateForPos( IWorldReader world, BlockPos pos, BlockState state ) {
        state = super.computeStateForPos( world, pos, state );

        BlockPos backward = pos.offset( growDir, - 1 );
        BlockState bwState = world.getBlockState( backward );
        boolean self = isSelfState( world, backward, bwState );
        int len = self ? bwState.get( length ) + 1 : 1;
        if( len > maxLength ) {
            return null;
        } else {
            return state.with( length, len );
        }
    }

    @Override
    public int deformGenerationHeight( int h, Random rand ) {
        return Math.min( h, maxLength );
    }
}
