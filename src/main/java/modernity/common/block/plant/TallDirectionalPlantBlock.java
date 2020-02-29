/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Function;

public abstract class TallDirectionalPlantBlock extends DirectionalPlantBlock {
    public static final BooleanProperty ROOT = MDBlockStateProperties.ROOT;
    public static final BooleanProperty END = MDBlockStateProperties.END;

    // Set to true when the plant is being killed by 'kill(...)', to prevent the world from breaking the other blocks
    // automatically before we have removed it (because of 'updatePostPlacement')
    private final ThreadLocal<Boolean> dying = ThreadLocal.withInitial( () -> false );

    public TallDirectionalPlantBlock( Properties properties, Direction growDir ) {
        super( properties, growDir );

        setDefaultState( stateContainer.getBaseState().with( ROOT, false ).with( END, false ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( ROOT, END );
    }

    public boolean isSelfState( IWorldReader reader, BlockPos pos, BlockState state ) {
        return state.getBlock() == this;
    }

    @Override
    public boolean canRemainOn( IWorldReader world, BlockPos pos, BlockState state, BlockState selfState ) {
        return canBlockSustain( world, pos, state ) || isSelfState( world, pos, state );
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        if( ! dying.get() ) {
            state = super.updatePostPlacement( state, dir, adjState, world, pos, adjPos );
            if( state.getBlock() != this ) return state;

            if( dir == growDir ) {
                boolean end = ! isSelfState( world, adjPos, adjState );
                state = state.with( END, end );
            }

            if( dir == growDir.getOpposite() ) {
                boolean root = ! isSelfState( world, adjPos, adjState );
                state = state.with( ROOT, root );
            }
        }

        return state;
    }

    public int getDefaultGenerationHeight( Random rand ) {
        return rand.nextInt( 3 );
    }

    @Override
    public BlockState computeStateForPos( IWorldReader world, BlockPos pos, BlockState state ) {
        state = super.computeStateForPos( world, pos, state );
        BlockPos forward = pos.offset( growDir );
        BlockPos backward = pos.offset( growDir, - 1 );
        BlockState fwState = world.getBlockState( forward );
        BlockState bwState = world.getBlockState( backward );
        boolean end = ! isSelfState( world, forward, fwState );
        boolean root = ! isSelfState( world, backward, bwState );
        return state.with( END, end ).with( ROOT, root );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        return provide( world, pos, rand, this::getDefaultGenerationHeight );
    }

    public int deformGenerationHeight( int h, Random rand ) {
        return h;
    }

    public boolean provide( IWorld world, BlockPos pos, Random rand, Function<Random, Integer> heightFunction ) {
        BlockPos support = pos.offset( growDir, - 1 );
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && canBlockSustain( world, support, world.getBlockState( support ) ) ) {
            int h = deformGenerationHeight( heightFunction.apply( rand ), rand );
            MovingBlockPos mpos = new MovingBlockPos( pos );

            BlockState lastPlaced = null;
            for( int i = 0; i < h; i++ ) {
                if( canGenerateAt( world, mpos, world.getBlockState( mpos ) ) ) {
                    BlockState state = computeStateForPos( world, mpos, getDefaultState() );
                    if( state == null ) break;

                    state = state.with( ROOT, i == 0 );
                    state = state.with( END, false );

                    lastPlaced = state;

                    world.setBlockState( mpos, state, 2 );

                    mpos.move( growDir );
                } else {
                    break;
                }
            }

            mpos.move( growDir, - 1 );
            if( lastPlaced != null ) {
                world.setBlockState( mpos, lastPlaced.with( END, true ), 2 );
            }

            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public Vec3d getOffset( BlockState state, IBlockReader reader, BlockPos pos ) {
        if( state.get( ROOT ) ) return super.getOffset( state, reader, pos );
        else {
            BlockState last = state;
            MovingBlockPos mpos = new MovingBlockPos( pos );
            while( state.getBlock() == this && ! state.get( ROOT ) ) {
                last = state;
                mpos.move( growDir, - 1 );
                state = reader.getBlockState( mpos );
            }

            if( state.getBlock() != this ) {
                mpos.move( growDir, 1 );
                state = last;
            }

            return super.getOffset( state, reader, mpos );
        }
    }

    @Override
    public void kill( World world, BlockPos pos, BlockState state ) {
        dying.set( true );
        while( isSelfState( world, pos, world.getBlockState( pos ) ) ) {
            world.removeBlock( pos, false );
            pos = pos.up();
        }
        dying.set( false );
    }

    @Override
    public BlockPos getRootPos( World world, BlockPos pos, BlockState state ) {
        MovingBlockPos mpos = new MovingBlockPos( pos );
        while( isSelfState( world, mpos, world.getBlockState( mpos ) ) ) {
            mpos.move( growDir, - 1 );
        }
        return mpos.offset( growDir );
    }
}
