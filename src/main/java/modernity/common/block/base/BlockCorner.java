/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 24 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.EWaterlogType;
import modernity.api.util.MDVoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.util.EnumFacing.*;

public class BlockCorner extends BlockWaterlogged {
    public static final EnumProperty<Type> TYPE = EnumProperty.create( "type", Type.class );

    public static final BooleanProperty CORNER_000 = BooleanProperty.create( "c000" );
    public static final BooleanProperty CORNER_001 = BooleanProperty.create( "c001" );
    public static final BooleanProperty CORNER_010 = BooleanProperty.create( "c010" );
    public static final BooleanProperty CORNER_011 = BooleanProperty.create( "c011" );
    public static final BooleanProperty CORNER_100 = BooleanProperty.create( "c100" );
    public static final BooleanProperty CORNER_101 = BooleanProperty.create( "c101" );
    public static final BooleanProperty CORNER_110 = BooleanProperty.create( "c110" );
    public static final BooleanProperty CORNER_111 = BooleanProperty.create( "c111" );

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

    public BlockCorner( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );

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

    public BlockCorner( String id, Properties properties ) {
        super( id, properties );

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
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
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

    public boolean isSolidSide( EnumFacing facing, IBlockState state ) {
        for( BooleanProperty corner : FACE_PROPERTIES[ facing.ordinal() ] ) {
            if( ! state.get( corner ) ) return false;
        }
        return true;
    }

    public boolean isFull( IBlockState state ) {
        for( BooleanProperty corner : CORNERS ) {
            if( ! state.get( corner ) ) return false;
        }
        return true;
    }

    public boolean isEmpty( IBlockState state ) {
        for( BooleanProperty corner : CORNERS ) {
            if( state.get( corner ) ) return false;
        }
        return true;
    }

    public boolean hasCorner( int corner, IBlockState state ) {
        return state.get( CORNERS[ corner ] );
    }

    public int getIndex( IBlockState state ) {
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

    public int getQuantity( IBlockState state ) {
        int quantity = 0;
        for( BooleanProperty corner : CORNERS ) {
            if( state.get( corner ) ) quantity++;
        }
        return quantity;
    }

    @Deprecated
    @OnlyIn( Dist.CLIENT )
    @Override
    public int getPackedLightmapCoords( IBlockState state, IWorldReader source, BlockPos pos ) {
        int i = source.getCombinedLight( pos, state.getLightValue( source, pos ) );
        if( i == 0 && ( isSolidSide( UP, state ) || isSolidSide( DOWN, state ) ) ) {
            pos = pos.down();
            state = source.getBlockState( pos );
            return source.getCombinedLight( pos, state.getLightValue( source, pos ) );
        } else {
            return i;
        }
    }

    @Override
    public int getOpacity( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return reader.getMaxLightLevel();
    }

    @Override
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        state = super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        if( isEmpty( state ) ) return state.with( CORNER_000, true ); // Fix empty block
        return state;
    }

    private int getCornerFromUseContext( BlockItemUseContext ctx ) {
        int corner = 0;

        EnumFacing face = ctx.getFace();
        if( ! ctx.replacingClickedOnBlock() ) face = face.getOpposite();

        double hitX = ctx.getHitX(), hitY = ctx.getHitY(), hitZ = ctx.getHitZ();

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
    public IBlockState getStateForPlacement( BlockItemUseContext ctx ) {
        IFluidState fluid = ctx.getWorld().getFluidState( ctx.getPos() );
        IBlockState state = ctx.getWorld().getBlockState( ctx.getPos() );
        int corner = getCornerFromUseContext( ctx );
        if( state.getBlock() == this ) {
            if( hasCorner( corner, state ) ) return null;
            else {
                // Add corner
                state = state.with( CORNERS[ corner ], true );

                // Remove waterlogging on full blocks
                if( isFull( state ) ) state = state.with( WATERLOGGED, EWaterlogType.NONE );
                return state;
            }
        }
        return getDefaultState().with( CORNERS[ corner ], true ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
    }

    public boolean isReplaceable( IBlockState state, BlockItemUseContext ctx ) {
        ItemStack stack = ctx.getItem();
        if( ! isFull( state ) && stack.getItem() == asItem() ) {
            int corner = getCornerFromUseContext( ctx );
            return ! hasCorner( corner, state );
        } else {
            return false;
        }
    }

    @Override
    public boolean propagatesSkylightDown( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return false;
    }

    @Override
    public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        return STATE_SHAPES[ getIndex( state ) ];
    }

    @Override
    public int quantityDropped( IBlockState state, Random rand ) {
        return getQuantity( state );
    }

    public boolean isFullCube( IBlockState state ) {
        return isFull( state );
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing facing ) {
        return isSolidSide( facing, state ) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, IBlockState state ) {
        if( isFull( state ) ) return Fluids.EMPTY;
        return super.pickupFluid( world, pos, state );
    }

    @Override
    public boolean canContainFluid( IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid ) {
        if( isFull( state ) ) return false;
        return super.canContainFluid( world, pos, state, fluid );
    }

    @Override
    public boolean receiveFluid( IWorld world, BlockPos pos, IBlockState state, IFluidState fluid ) {
        if( isFull( state ) ) return false;
        return super.receiveFluid( world, pos, state, fluid );
    }

    public enum Type implements IStringSerializable {
        DOWN( "down", MDVoxelShapes.create16( 0, 0, 0, 16, 8, 16 ), EnumFacing.DOWN ),
        UP( "up", MDVoxelShapes.create16( 0, 8, 0, 16, 16, 16 ), EnumFacing.UP ),
        NORTH( "north", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 8 ), EnumFacing.NORTH ),
        SOUTH( "south", MDVoxelShapes.create16( 0, 0, 8, 16, 16, 16 ), EnumFacing.SOUTH ),
        WEST( "west", MDVoxelShapes.create16( 0, 0, 0, 8, 16, 16 ), EnumFacing.EAST ),
        EAST( "east", MDVoxelShapes.create16( 8, 0, 0, 16, 16, 16 ), EnumFacing.WEST ),
        DOUBLE( "double", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 16 ), null );

        private final String name;
        private final VoxelShape shape;
        private final EnumFacing facing;

        Type( String name, VoxelShape shape, EnumFacing facing ) {
            this.name = name;
            this.shape = shape;
            this.facing = facing;
        }

        @Override
        public String getName() {
            return name;
        }

        public VoxelShape getShape() {
            return shape;
        }

        public EnumFacing getFacing() {
            return facing;
        }

        public static Type forFacing( EnumFacing facing ) {
            if( facing == null ) {
                return DOUBLE;
            }
            switch( facing ) {
                case UP: return UP;
                case DOWN: return DOWN;
                case EAST: return EAST;
                case WEST: return WEST;
                case NORTH: return NORTH;
                case SOUTH: return SOUTH;
                default: return DOUBLE;
            }
        }
    }
}
