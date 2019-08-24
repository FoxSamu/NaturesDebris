/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 24 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import modernity.api.util.EWaterlogType;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

@SuppressWarnings( "deprecation" )
public class BlockStairs extends BlockWaterlogged {
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    protected static final VoxelShape BOTTOM_SHAPE = makeCuboidShape( 0, 0, 0, 16, 8, 16 );
    protected static final VoxelShape TOP_SHAPE = makeCuboidShape( 0, 8, 0, 16, 16, 16 );
    protected static final VoxelShape EMPTY = VoxelShapes.empty();

    protected static final VoxelShape CORNER_000 = makeCuboidShape( 0, 0, 0, 8, 8, 8 );
    protected static final VoxelShape CORNER_001 = makeCuboidShape( 0, 0, 8, 8, 8, 16 );
    protected static final VoxelShape CORNER_010 = makeCuboidShape( 0, 8, 0, 8, 16, 8 );
    protected static final VoxelShape CORNER_011 = makeCuboidShape( 0, 8, 8, 8, 16, 16 );
    protected static final VoxelShape CORNER_100 = makeCuboidShape( 8, 0, 0, 16, 8, 8 );
    protected static final VoxelShape CORNER_101 = makeCuboidShape( 8, 0, 8, 16, 8, 16 );
    protected static final VoxelShape CORNER_110 = makeCuboidShape( 8, 8, 0, 16, 16, 8 );
    protected static final VoxelShape CORNER_111 = makeCuboidShape( 8, 8, 8, 16, 16, 16 );

    protected static final VoxelShape[] TOP_STAIRS_CONNS = createShapeArray( TOP_SHAPE, CORNER_000, CORNER_100, CORNER_001, CORNER_101 );
    protected static final VoxelShape[] BOTTOM_STAIRS_CONNS = createShapeArray( BOTTOM_SHAPE, CORNER_010, CORNER_110, CORNER_011, CORNER_111 );

    protected static final VoxelShape[] BOTTOM_STEP_CONNS = createShapeArray( EMPTY, CORNER_000, CORNER_100, CORNER_001, CORNER_101 );
    protected static final VoxelShape[] TOP_STEP_CONNS = createShapeArray( EMPTY, CORNER_010, CORNER_110, CORNER_011, CORNER_111 );

    private static final int[] CONN_INDEX_MAP = {
            12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8
    };

    private final boolean isStep;

    public BlockStairs( String id, boolean isStep, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        this.isStep = isStep;

        setDefaultState( stateContainer.getBaseState().with( SHAPE, StairsShape.STRAIGHT ).with( HALF, Half.BOTTOM ).with( FACING, EnumFacing.NORTH ) );
    }

    public BlockStairs( String id, boolean isStep, Properties properties ) {
        super( id, properties );
        this.isStep = isStep;

        setDefaultState( stateContainer.getBaseState().with( SHAPE, StairsShape.STRAIGHT ).with( HALF, Half.BOTTOM ).with( FACING, EnumFacing.NORTH ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( SHAPE, HALF, FACING );
    }

    @Override
    public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        int index = CONN_INDEX_MAP[ getConnIndex( state ) ];
        boolean top = state.get( HALF ) == Half.TOP;
        if( isStep ) return ( top ? TOP_STEP_CONNS : BOTTOM_STEP_CONNS )[ index ];
        else return ( top ? TOP_STAIRS_CONNS : BOTTOM_STAIRS_CONNS )[ index ];
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext ctx ) {
        EnumFacing face = ctx.getFace();
        IBlockState state = getDefaultState().with( FACING, ctx.getPlacementHorizontalFacing() )
                                             .with( HALF, face != EnumFacing.DOWN && ( face == EnumFacing.UP || ! ( ctx.getHitY() > 0.5 ) ) ? Half.BOTTOM : Half.TOP )
                                             .with( WATERLOGGED, EWaterlogType.getType( ctx.getWorld().getFluidState( ctx.getPos() ) ) );
        return state.with( SHAPE, computeStairsShape( state, ctx.getWorld(), ctx.getPos() ) );
    }

    @Override
    public boolean propagatesSkylightDown( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return false;
    }

    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        state = super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        return facing.getAxis().isHorizontal() ? state.with( SHAPE, computeStairsShape( state, world, currentPos ) ) : state;
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        if( isStep )
            return BlockFaceShape.UNDEFINED;
        if( face.getAxis() == EnumFacing.Axis.Y ) {
            return face == EnumFacing.UP == ( state.get( HALF ) == Half.TOP ) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        } else {
            StairsShape shape = state.get( SHAPE );
            if( shape != StairsShape.OUTER_LEFT && shape != StairsShape.OUTER_RIGHT ) {
                EnumFacing facing = state.get( FACING );
                switch( shape ) {
                    case STRAIGHT:
                        return facing == face ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
                    case INNER_LEFT:
                        return facing != face && facing != face.rotateY() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
                    case INNER_RIGHT:
                        return facing != face && facing != face.rotateYCCW() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
                    default:
                        return BlockFaceShape.UNDEFINED;
                }
            } else {
                return BlockFaceShape.UNDEFINED;
            }
        }
    }

    public boolean isBlockStairs( IBlockState state ) {
        return state.getBlock() instanceof BlockStairs && ( (BlockStairs) state.getBlock() ).isStep == isStep;
    }

    private StairsShape computeStairsShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        EnumFacing facing = state.get( FACING );
        IBlockState frontState = world.getBlockState( pos.offset( facing ) );
        if( isBlockStairs( frontState ) && state.get( HALF ) == frontState.get( HALF ) ) {
            EnumFacing frontFacing = frontState.get( FACING );
            if( frontFacing.getAxis() != state.get( FACING ).getAxis() && isDifferentStairs( state, world, pos, frontFacing.getOpposite() ) ) {
                if( frontFacing == facing.rotateYCCW() ) {
                    return StairsShape.OUTER_LEFT;
                }
                return StairsShape.OUTER_RIGHT;
            }
        }

        IBlockState backState = world.getBlockState( pos.offset( facing.getOpposite() ) );
        if( isBlockStairs( backState ) && state.get( HALF ) == backState.get( HALF ) ) {
            EnumFacing backFacing = backState.get( FACING );
            if( backFacing.getAxis() != state.get( FACING ).getAxis() && isDifferentStairs( state, world, pos, backFacing ) ) {
                if( backFacing == facing.rotateYCCW() ) {
                    return StairsShape.INNER_LEFT;
                }
                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private boolean isDifferentStairs( IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face ) {
        IBlockState offState = world.getBlockState( pos.offset( face ) );
        return ! isBlockStairs( offState ) || offState.get( FACING ) != state.get( FACING ) || offState.get( HALF ) != state.get( HALF );
    }

    public IBlockState rotate( IBlockState state, Rotation rot ) {
        return state.with( FACING, rot.rotate( state.get( FACING ) ) );
    }

    public IBlockState mirror( IBlockState state, Mirror mirrorIn ) {
        EnumFacing facing = state.get( FACING );
        StairsShape shape = state.get( SHAPE );
        switch( mirrorIn ) {
            case LEFT_RIGHT:
                if( facing.getAxis() == EnumFacing.Axis.Z ) {
                    switch( shape ) {
                        case INNER_LEFT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.INNER_RIGHT );
                        case INNER_RIGHT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.INNER_LEFT );
                        case OUTER_LEFT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.OUTER_RIGHT );
                        case OUTER_RIGHT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.OUTER_LEFT );
                        default:
                            return state.rotate( Rotation.CLOCKWISE_180 );
                    }
                }
                break;
            case FRONT_BACK:
                if( facing.getAxis() == EnumFacing.Axis.X ) {
                    switch( shape ) {
                        case STRAIGHT:
                            return state.rotate( Rotation.CLOCKWISE_180 );
                        case INNER_LEFT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.INNER_LEFT );
                        case INNER_RIGHT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.INNER_RIGHT );
                        case OUTER_LEFT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.OUTER_RIGHT );
                        case OUTER_RIGHT:
                            return state.rotate( Rotation.CLOCKWISE_180 ).with( SHAPE, StairsShape.OUTER_LEFT );
                    }
                }
        }

        return super.mirror( state, mirrorIn );
    }

    private static VoxelShape[] createShapeArray( VoxelShape base, VoxelShape conn1, VoxelShape conn2, VoxelShape conn3, VoxelShape conn4 ) {
        return IntStream.range( 0, 16 ).mapToObj( connections -> joinShapesForConnection( connections, base, conn1, conn2, conn3, conn4 ) ).toArray( VoxelShape[]::new );
    }

    private static VoxelShape joinShapesForConnection( int connections, VoxelShape base, VoxelShape conn1, VoxelShape conn2, VoxelShape conn3, VoxelShape conn4 ) {
        VoxelShape shape = base;
        if( ( connections & 1 ) != 0 ) {
            shape = VoxelShapes.or( shape, conn1 );
        }

        if( ( connections & 2 ) != 0 ) {
            shape = VoxelShapes.or( shape, conn2 );
        }

        if( ( connections & 4 ) != 0 ) {
            shape = VoxelShapes.or( shape, conn3 );
        }

        if( ( connections & 8 ) != 0 ) {
            shape = VoxelShapes.or( shape, conn4 );
        }

        return shape;
    }

    private static int getConnIndex( IBlockState state ) {
        return state.get( SHAPE ).ordinal() * 4 + state.get( FACING ).getHorizontalIndex();
    }
}
