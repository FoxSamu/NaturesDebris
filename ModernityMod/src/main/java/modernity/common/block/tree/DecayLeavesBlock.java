/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.tree;

import modernity.common.block.MDBlockStateProperties;
import modernity.common.event.MDBlockEvents;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * Describes leaf blocks that decay.
 */
public class DecayLeavesBlock extends LeavesBlock {
    public static final int MAX_DIST = 10;
    public static final IntegerProperty DISTANCE = MDBlockStateProperties.DISTANCE_0_10;

    private final Tag<Block> logTag;

    public DecayLeavesBlock( Tag<Block> logTag, Properties properties ) {
        super( properties );
        this.logTag = logTag;
        setDefaultState( getStateContainer().getBaseState().with( DISTANCE, MAX_DIST ) );
    }

    @Override
    public boolean ticksRandomly( BlockState state ) {
        return state.get( DISTANCE ) == MAX_DIST || super.ticksRandomly( state );
    }

    @Override
    public void randomTick( BlockState state, ServerWorld world, BlockPos pos, Random random ) {
        super.randomTick( state, world, pos, random );
        if( state.get( DISTANCE ) == MAX_DIST ) {
            spawnDrops( state, world, pos );
            world.removeBlock( pos, false );
            MDBlockEvents.LEAVES_DECAY.play( world, pos, state );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void tick( BlockState state, ServerWorld world, BlockPos pos, Random random ) {
        world.setBlockState( pos, updateDistance( state, world, pos ), 2 | 4 );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        int dist = getDistance( facingState ) + 1;
        if( dist != 1 || state.get( DISTANCE ) != dist ) {
            world.getPendingBlockTicks().scheduleTick( currentPos, this, 1 );
        }

        return state;
    }

    /**
     * Updates the distance of a leaves block
     */
    private BlockState updateDistance( BlockState state, IWorld world, BlockPos pos ) {
        // Persistent leaves
        if( state.get( DISTANCE ) == 0 ) return state;

        int dist = MAX_DIST;

        MovingBlockPos rpos = new MovingBlockPos();
        for( Direction facing : Direction.values() ) {
            rpos.setPos( pos ).move( facing );
            dist = Math.min( dist, getDistance( world.getBlockState( rpos ) ) + 1 );
            if( dist == 1 ) {
                break;
            }
        }

        return state.with( DISTANCE, dist );
    }

    /**
     * Returns the path distance through leaves to a log block
     */
    private int getDistance( BlockState neighbor ) {
        if( logTag.contains( neighbor.getBlock() ) ) {
            return 0;
        } else {
            int dist = neighbor.getBlock() instanceof DecayLeavesBlock ? neighbor.get( DISTANCE ) : MAX_DIST;
            if( dist == 0 ) return 1;
            return dist;
        }
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( DISTANCE );
    }

    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return getDefaultState().with( DISTANCE, 0 );
    }

    @Override
    protected boolean hasFallingLeaf( BlockState state, World world, BlockPos pos, Random rand ) {
        if( state.get( DISTANCE ) == MAX_DIST ) {
            return rand.nextInt( 48 ) == 1;
        }
        return super.hasFallingLeaf( state, world, pos, rand );
    }

}
