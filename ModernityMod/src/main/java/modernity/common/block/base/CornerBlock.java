/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.fluid.WaterlogType;
import modernity.common.block.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

import static net.minecraft.util.Direction.*;

/**
 * Describes the corner blocks: blocks that are subdivided in 8 smaller blocks.
 */
public class CornerBlock extends WaterloggedBlock {
    public static final BooleanProperty CORNER_000 = MDBlockStateProperties.CORNER_000;
    public static final BooleanProperty CORNER_001 = MDBlockStateProperties.CORNER_001;
    public static final BooleanProperty CORNER_010 = MDBlockStateProperties.CORNER_010;
    public static final BooleanProperty CORNER_011 = MDBlockStateProperties.CORNER_011;
    public static final BooleanProperty CORNER_100 = MDBlockStateProperties.CORNER_100;
    public static final BooleanProperty CORNER_101 = MDBlockStateProperties.CORNER_101;
    public static final BooleanProperty CORNER_110 = MDBlockStateProperties.CORNER_110;
    public static final BooleanProperty CORNER_111 = MDBlockStateProperties.CORNER_111;

    private static final BooleanProperty[] CORNERS = {
        CORNER_000,
        CORNER_001,
        CORNER_010,
        CORNER_011,
        CORNER_100,
        CORNER_101,
        CORNER_110,
        CORNER_111
    };

    private static final BooleanProperty[][] FACE_PROPERTIES = {
        {
            CORNER_000,
            CORNER_001,
            CORNER_100,
            CORNER_101
        },
        {
            CORNER_010,
            CORNER_011,
            CORNER_110,
            CORNER_111
        },
        {
            CORNER_000,
            CORNER_010,
            CORNER_100,
            CORNER_110
        },
        {
            CORNER_001,
            CORNER_011,
            CORNER_101,
            CORNER_111
        },
        {
            CORNER_000,
            CORNER_001,
            CORNER_010,
            CORNER_011
        },
        {
            CORNER_100,
            CORNER_101,
            CORNER_110,
            CORNER_111
        }
    };

    private static final VoxelShape[] CORNER_SHAPES = new VoxelShape[ 8 ];
    private static final VoxelShape[] STATE_SHAPES = new VoxelShape[ 256 ];

    static {
        for( int i = 0; i < 8; i++ ) {
            int minx = ( i & 4 ) == 0 ? 0 : 8;
            int miny = ( i & 2 ) == 0 ? 0 : 8;
            int minz = ( i & 1 ) == 0 ? 0 : 8;

            CORNER_SHAPES[ i ] = makeCuboidShape( minx, miny, minz, minx + 8, miny + 8, minz + 8 );
        }

        for( int i = 0; i < 256; i++ ) {
            VoxelShape shape = VoxelShapes.empty();
            if( ( i & 1 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 0 ] );
            if( ( i & 2 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 1 ] );
            if( ( i & 4 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 2 ] );
            if( ( i & 8 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 3 ] );
            if( ( i & 16 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 4 ] );
            if( ( i & 32 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 5 ] );
            if( ( i & 64 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 6 ] );
            if( ( i & 128 ) != 0 ) shape = VoxelShapes.or( shape, CORNER_SHAPES[ 7 ] );
            STATE_SHAPES[ i ] = shape;
        }
    }

    public CornerBlock( Properties properties ) {
        super( properties );

        setDefaultState(
            stateContainer.getBaseState()
                          .with( CORNER_000, false )
                          .with( CORNER_001, false )
                          .with( CORNER_010, false )
                          .with( CORNER_011, false )
                          .with( CORNER_100, false )
                          .with( CORNER_101, false )
                          .with( CORNER_110, false )
                          .with( CORNER_111, false )
        );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( CORNER_000 );
        builder.add( CORNER_001 );
        builder.add( CORNER_010 );
        builder.add( CORNER_011 );
        builder.add( CORNER_100 );
        builder.add( CORNER_101 );
        builder.add( CORNER_110 );
        builder.add( CORNER_111 );
    }

    /**
     * Checks if the side is solid for a specific facing
     */
    public boolean isSolidSide( Direction facing, BlockState state ) {
        for( BooleanProperty corner : FACE_PROPERTIES[ facing.ordinal() ] ) {
            if( ! state.get( corner ) ) return false;
        }
        return true;
    }

    /**
     * Check if all corners are filled
     */
    public boolean isFull( BlockState state ) {
        for( BooleanProperty corner : CORNERS ) {
            if( ! state.get( corner ) ) return false;
        }
        return true;
    }

    /**
     * Check if none of the corners is filled
     */
    public boolean isEmpty( BlockState state ) {
        for( BooleanProperty corner : CORNERS ) {
            if( state.get( corner ) ) return false;
        }
        return true;
    }

    /**
     * Checks if a corner exists at the specified corner
     */
    public boolean hasCorner( int corner, BlockState state ) {
        return state.get( CORNERS[ corner ] );
    }

    /**
     * Returns the index in the voxel shape array
     */
    public int getIndex( BlockState state ) {
        int index = 0;
        if( state.get( CORNER_000 ) ) index |= 1;
        if( state.get( CORNER_001 ) ) index |= 2;
        if( state.get( CORNER_010 ) ) index |= 4;
        if( state.get( CORNER_011 ) ) index |= 8;
        if( state.get( CORNER_100 ) ) index |= 16;
        if( state.get( CORNER_101 ) ) index |= 32;
        if( state.get( CORNER_110 ) ) index |= 64;
        if( state.get( CORNER_111 ) ) index |= 128;
        return index;
    }

    /**
     * Returns the amount of corners in a state
     */
    public int getQuantity( BlockState state ) {
        int quantity = 0;
        for( BooleanProperty corner : CORNERS ) {
            if( state.get( corner ) ) quantity++;
        }
        return quantity;
    }

    @Override
    public int getLightValue( BlockState state, IBlockReader world, BlockPos pos ) {
        return 0;
    }

    @Override
    public int getOpacity( BlockState state, IBlockReader reader, BlockPos pos ) {
        return reader.getMaxLightLevel();
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        state = super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        if( isEmpty( state ) ) return state.with( CORNER_000, true ); // Fix empty block
        return state;
    }

    private int getCornerFromUseContext( BlockItemUseContext ctx ) {
        int corner = 0;

        Direction face = ctx.getFace();
        if( ! ctx.replacingClickedOnBlock() ) face = face.getOpposite();

        Vec3d hit = ctx.getHitVec();
        BlockPos pos = ctx.getPos();
        double hitX = hit.x - pos.getX(), hitY = hit.y - pos.getY(), hitZ = hit.z - pos.getZ();

        double hitA, hitB;
        int cornerA, cornerB;

        if( face.getAxis() == Axis.X ) {
            if( face.getAxisDirection() == AxisDirection.POSITIVE ) corner |= 4;
            hitA = hitZ;
            hitB = hitY;
            cornerA = 1;
            cornerB = 2;
        } else if( face.getAxis() == Axis.Y ) {
            if( face.getAxisDirection() == AxisDirection.POSITIVE ) corner |= 2;
            hitA = hitZ;
            hitB = hitX;
            cornerA = 1;
            cornerB = 4;
        } else {
            if( face.getAxisDirection() == AxisDirection.POSITIVE ) corner |= 1;
            hitA = hitY;
            hitB = hitX;
            cornerA = 2;
            cornerB = 4;
        }

        if( hitA > 0.5D ) {
            corner |= cornerA;
        }

        if( hitB > 0.5D ) {
            corner |= cornerB;
        }

        return corner;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext ctx ) {
        IFluidState fluid = ctx.getWorld().getFluidState( ctx.getPos() );
        BlockState state = ctx.getWorld().getBlockState( ctx.getPos() );
        int corner = getCornerFromUseContext( ctx );
        if( state.getBlock() == this ) {
            if( hasCorner( corner, state ) ) return null;
            else {
                // Add corner
                state = state.with( CORNERS[ corner ], true );

                // Remove waterlogging on full blocks
                if( isFull( state ) ) state = state.with( WATERLOGGED, WaterlogType.NONE );
                return state;
            }
        }
        return getDefaultState().with( CORNERS[ corner ], true ).with( WATERLOGGED, WaterlogType.getType( fluid ) );
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext ctx ) {
        ItemStack stack = ctx.getItem();
        if( ! isFull( state ) && stack.getItem() == asItem() ) {
            int corner = getCornerFromUseContext( ctx );
            return ! hasCorner( corner, state );
        } else {
            return false;
        }
    }

    @Override
    public boolean propagatesSkylightDown( BlockState state, IBlockReader reader, BlockPos pos ) {
        return false;
    }

    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, BlockState state ) {
        if( isFull( state ) ) return Fluids.EMPTY;
        return super.pickupFluid( world, pos, state );
    }

    @Override
    public Fluid pickupFluidModernity( IWorld world, BlockPos pos, BlockState state ) {
        if( isFull( state ) ) return Fluids.EMPTY;
        return super.pickupFluidModernity( world, pos, state );
    }

    @Override
    public boolean canContainFluid( IBlockReader world, BlockPos pos, BlockState state, Fluid fluid ) {
        if( isFull( state ) ) return false;
        return super.canContainFluid( world, pos, state, fluid );
    }

    @Override
    public boolean receiveFluid( IWorld world, BlockPos pos, BlockState state, IFluidState fluid ) {
        if( isFull( state ) ) return false;
        return super.receiveFluid( world, pos, state, fluid );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return STATE_SHAPES[ getIndex( state ) ];
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return STATE_SHAPES[ getIndex( state ) ];
    }
}
