/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.tree;

import modernity.common.block.MDPlantBlocks;
import modernity.common.block.MDTreeBlocks;
import modernity.common.block.base.AxisBlock;
import modernity.common.block.tree.HangLeavesBlock;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import java.util.Random;
import java.util.function.Consumer;

public class TinyBlackwoodTree extends Tree {
    private static final BlockState LEAVES = MDTreeBlocks.BLACKWOOD_LEAVES.getDefaultState();
    private static final BlockState HANGING_LEAVES = MDTreeBlocks.BLACKWOOD_LEAVES.getDefaultState().with( HangLeavesBlock.DISTANCE, - 1 );
    private static final BlockState MURINA = MDPlantBlocks.MURINA.getDefaultState();
    private static final BlockState LOG_X = MDTreeBlocks.BLACKWOOD_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.X );
    private static final BlockState LOG_Y = MDTreeBlocks.BLACKWOOD_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Y );
    private static final BlockState LOG_Z = MDTreeBlocks.BLACKWOOD_LOG.getDefaultState().with( AxisBlock.AXIS, Direction.Axis.Z );
    private static final BlockState WOOD = MDTreeBlocks.BLACKWOOD.getDefaultState();

    @Override
    public boolean canGenerate( IWorldReader world, Random rand, BlockPos pos ) {
        int height = rand.nextInt( 4 ) + 4;

        MovingBlockPos mpos = new MovingBlockPos();

        for( int y = 1; y < height + 2; y++ ) {
            for( int x = - 1; x <= 1; x++ ) {
                for( int z = - 1; z <= 1; z++ ) {
                    mpos.setPos( pos ).addPos( x, y, z );
                    if( ! isAirOrLeaves( world, mpos ) ) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected void generateTree( Consumer<BlockPos> logs, IWorld world, Random rand, BlockPos pos ) {
        int height = rand.nextInt( 4 ) + 4;

        int l = height + 2;

        MovingBlockPos mpos = new MovingBlockPos();

        for( int y = - 1; y < l; y++ ) {
            int i = l - y - 1;

            mpos.setPos( pos ).moveUp( y );

            if( y < height ) setLogState( world, mpos, LOG_Y, logs );

            if( y == - 1 ) createRoots( logs, world, y, pos, mpos );
            if( i == 2 ) createBranches( logs, world, y, pos, mpos );

            int rad = getLeafRadius( i );
            int dist = getLeafDistance( i );

            if( rad >= 0 ) {
                for( int x = - rad; x <= rad; x++ ) {
                    for( int z = - rad; z <= rad; z++ ) {
                        int dx = Math.abs( x );
                        int dz = Math.abs( z );
                        if( dx + dz < dist ) {
                            mpos.setPos( pos ).addPos( x, y, z );

                            if( isAir( world, mpos ) ) {
                                setBlockState( world, mpos, LEAVES );
                                if( i == 2 ) {
                                    createHangingLeaves( world, x, y, z, pos, mpos, rand );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createBranches( Consumer<BlockPos> logs, IWorld world, int y, BlockPos pos, MovingBlockPos mpos ) {
        for( Direction dir : Direction.Plane.HORIZONTAL ) {
            mpos.setPos( pos ).moveUp( y ).move( dir );

            BlockState log = dir.getAxis() == Direction.Axis.X ? LOG_X : LOG_Z;
            setLogState( world, mpos, log, logs );
        }
    }

    private void createRoots( Consumer<BlockPos> logs, IWorld world, int y, BlockPos pos, MovingBlockPos mpos ) {
        for( Direction dir : Direction.Plane.HORIZONTAL ) {
            mpos.setPos( pos ).moveUp( y ).move( dir );
            setLogState( world, mpos, WOOD, logs );
        }
    }

    private void createHangingLeaves( IWorld world, int x, int y, int z, BlockPos pos, MovingBlockPos mpos, Random rand ) {
        if( rand.nextBoolean() ) {
            int len = rand.nextInt( 3 );
            if( len == 0 ) return;

            mpos.setPos( pos ).addPos( x, y, z ).moveDown();

            for( int i = 0; i < len; i++ ) {
                if( ! isAir( world, mpos ) ) {
                    break;
                }

                setBlockState( world, mpos, HANGING_LEAVES );
                mpos.moveDown();
            }
        } else if( rand.nextBoolean() ) {
            int len = rand.nextInt( 5 );
            if( len == 0 ) return;

            mpos.setPos( pos ).addPos( x, y, z ).moveDown();

            for( int i = 0; i < len; i++ ) {
                if( ! isAir( world, mpos ) ) {
                    break;
                }

                setBlockState( world, mpos, MURINA );
                mpos.moveDown();
            }
        }
    }

    private static int getLeafRadius( int y ) {
        if( y == 0 ) return 2;
        if( y == 1 || y == 2 ) return 3;
        return - 1;
    }

    private static int getLeafDistance( int y ) {
        if( y == 0 ) return 3;
        if( y == 1 || y == 2 ) return 5;
        return - 1;
    }

    private static boolean isAirOrLeaves( IWorldReader world, BlockPos pos ) {
        BlockState state = world.getBlockState( pos );
        Material mat = state.getMaterial();
        IFluidState fluid = state.getFluidState();
        if( ! mat.blocksMovement() && fluid.isEmpty() ) return true;
        if( mat == Material.LEAVES ) return true;
        if( state.isIn( BlockTags.LEAVES ) ) return true;
        return state.isAir( world, pos );
    }

    private static boolean isAir( IWorldReader world, BlockPos pos ) {
        BlockState state = world.getBlockState( pos );
        Material mat = state.getMaterial();
        IFluidState fluid = state.getFluidState();
        return ! mat.blocksMovement() && fluid.isEmpty();
    }


    @Override
    protected boolean isDecayableLeaf( BlockState state ) {
        return state.has( HangLeavesBlock.DISTANCE ) && state.get( HangLeavesBlock.DISTANCE ) >= 0;
    }

    @Override
    protected Property<Integer> getLeafDistanceProperty() {
        return HangLeavesBlock.DISTANCE;
    }

    @Override
    protected int getLeafDistanceMax() {
        return 10;
    }
}
