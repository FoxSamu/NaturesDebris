/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.block.base;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import modernity.api.util.EWaterlogType;
import modernity.common.fluid.MDFluids;
import modernity.common.item.block.ItemBranch;

public class BlockBranch extends BlockBase implements IBucketPickupHandler, ILiquidContainer {
    public static final BooleanProperty NORTH = BooleanProperty.create( "north" );
    public static final BooleanProperty EAST = BooleanProperty.create( "east" );
    public static final BooleanProperty SOUTH = BooleanProperty.create( "south" );
    public static final BooleanProperty WEST = BooleanProperty.create( "west" );
    public static final BooleanProperty UP = BooleanProperty.create( "up" );
    public static final BooleanProperty DOWN = BooleanProperty.create( "down" );
    public static final EnumProperty<EnumFacing> ROOT = EnumProperty.create( "root", EnumFacing.class );
    public static final EnumProperty<EWaterlogType> WATERLOGGED = EnumProperty.create( "waterlogged", EWaterlogType.class );

    private final VoxelShape upShape;
    private final VoxelShape downShape;
    private final VoxelShape northShape;
    private final VoxelShape eastShape;
    private final VoxelShape southShape;
    private final VoxelShape westShape;
    private final VoxelShape innerShape;

    private final Object2ObjectOpenHashMap<IBlockState, VoxelShape> shapeCache = new Object2ObjectOpenHashMap<>();

    public BlockBranch( String id, double thickness, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );

        double nh = 0.5 - thickness / 2;
        double ph = 0.5 + thickness / 2;

        innerShape = VoxelShapes.create( nh, nh, nh, ph, ph, ph );
        downShape = VoxelShapes.create( nh, 0, nh, ph, nh, ph );
        upShape = VoxelShapes.create( nh, ph, nh, ph, 1, ph );
        northShape = VoxelShapes.create( nh, nh, 0, ph, ph, nh );
        southShape = VoxelShapes.create( nh, nh, ph, ph, ph, 1 );
        westShape = VoxelShapes.create( 0, nh, nh, nh, ph, ph );
        eastShape = VoxelShapes.create( ph, nh, nh, 1, ph, ph );

        setDefaultState(
                stateContainer.getBaseState()
                              .with( ROOT, EnumFacing.UP )
                              .with( NORTH, false )
                              .with( EAST, false )
                              .with( SOUTH, false )
                              .with( WEST, false )
                              .with( UP, false )
                              .with( DOWN, false )
                              .with( WATERLOGGED, EWaterlogType.NONE )
        );
    }

    public BlockBranch( String id, double thickness, Properties properties ) {
        super( id, properties );

        double nh = 0.5 - thickness / 2;
        double ph = 0.5 + thickness / 2;

        innerShape = VoxelShapes.create( nh, nh, nh, ph, ph, ph );
        downShape = VoxelShapes.create( nh, 0, nh, ph, nh, ph );
        upShape = VoxelShapes.create( nh, ph, nh, ph, 1, ph );
        northShape = VoxelShapes.create( nh, nh, 0, ph, ph, nh );
        southShape = VoxelShapes.create( nh, nh, ph, ph, ph, 1 );
        westShape = VoxelShapes.create( 0, nh, nh, nh, ph, ph );
        eastShape = VoxelShapes.create( ph, nh, nh, 1, ph, ph );

        setDefaultState(
                stateContainer.getBaseState()
                              .with( ROOT, EnumFacing.UP )
                              .with( NORTH, false )
                              .with( EAST, false )
                              .with( SOUTH, false )
                              .with( WEST, false )
                              .with( UP, false )
                              .with( DOWN, true )
                              .with( WATERLOGGED, EWaterlogType.NONE )
        );
    }

    @Override
    public IFluidState getFluidState( IBlockState state ) {
        return state.get( WATERLOGGED ).getFluidState();
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( UP, DOWN, NORTH, EAST, SOUTH, WEST, ROOT, WATERLOGGED );
    }

//    @Override
//    @SuppressWarnings( "deprecation" )
//    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
//        if( isEquivalent( facingState ) ) {
//            EnumFacing root = state.get( ROOT );
//
//            if( facing.getOpposite() == root ) {
//                return state;
//            }
//
//            return state.with( facingProperty( facing ), true );
//        }
//        return state;
//    }

    public boolean isEquivalent( IBlockState state ) {
        return state.getBlock() == this;
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    @Override
    public IBlockState rotate( IBlockState state, IWorld world, BlockPos pos, Rotation direction ) {
        EnumFacing facing = direction.rotate( state.get( ROOT ) );
        EnumFacing north = direction.rotate( EnumFacing.NORTH );
        EnumFacing east = direction.rotate( EnumFacing.EAST );
        EnumFacing south = direction.rotate( EnumFacing.SOUTH );
        EnumFacing west = direction.rotate( EnumFacing.WEST );
        return state.with( ROOT, facing )
                    .with( facingProperty( north ), state.get( NORTH ) )
                    .with( facingProperty( east ), state.get( EAST ) )
                    .with( facingProperty( south ), state.get( SOUTH ) )
                    .with( facingProperty( west ), state.get( WEST ) );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public IBlockState mirror( IBlockState state, Mirror mirror ) {
        EnumFacing facing = mirror.mirror( state.get( ROOT ) );
        EnumFacing north = mirror.mirror( EnumFacing.NORTH );
        EnumFacing east = mirror.mirror( EnumFacing.EAST );
        EnumFacing south = mirror.mirror( EnumFacing.SOUTH );
        EnumFacing west = mirror.mirror( EnumFacing.WEST );
        return state.with( ROOT, facing )
                    .with( facingProperty( north ), state.get( NORTH ) )
                    .with( facingProperty( east ), state.get( EAST ) )
                    .with( facingProperty( south ), state.get( SOUTH ) )
                    .with( facingProperty( west ), state.get( WEST ) );
    }

//    @Nullable
//    @Override
//    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
//        return getDefaultState().with( ROOT, context.getFace() ).with( facingProperty( context.getFace().getOpposite() ), true );
//    }

    private VoxelShape computeShape( IBlockState state ) {
        VoxelShape shape = innerShape;
        if( state.get( UP ) ) {
            shape = VoxelShapes.combineAndSimplify( shape, upShape, IBooleanFunction.OR );
        }
        if( state.get( DOWN ) ) {
            shape = VoxelShapes.combineAndSimplify( shape, downShape, IBooleanFunction.OR );
        }
        if( state.get( EAST ) ) {
            shape = VoxelShapes.combineAndSimplify( shape, eastShape, IBooleanFunction.OR );
        }
        if( state.get( WEST ) ) {
            shape = VoxelShapes.combineAndSimplify( shape, westShape, IBooleanFunction.OR );
        }
        if( state.get( NORTH ) ) {
            shape = VoxelShapes.combineAndSimplify( shape, northShape, IBooleanFunction.OR );
        }
        if( state.get( SOUTH ) ) {
            shape = VoxelShapes.combineAndSimplify( shape, southShape, IBooleanFunction.OR );
        }
        return shape;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        return shapeCache.computeIfAbsent( state, this::computeShape );
    }

    public boolean place( EnumFacing root, World world, BlockPos pos, IBlockState state, boolean connect ) {
        state = state.with( ROOT, root ).with( facingProperty( root.getOpposite() ), true );

        IFluidState fluid = world.getFluidState( pos );
        state = state.with( WATERLOGGED, EWaterlogType.getType( fluid ) );

        BlockPos sourcePos = pos.offset( root, - 1 );
        IBlockState source = world.getBlockState( sourcePos );
        if( source.getBlock() instanceof BlockBranch && this.isEquivalent( source ) ) {
            source = source.with( facingProperty( root ), true );
            world.setBlockState( sourcePos, source, 11 );
        }

        if( connect ) {
            for( EnumFacing facing : EnumFacing.values() ) {
                BlockPos adjPos = pos.offset( facing );
                IBlockState adj = world.getBlockState( adjPos );
                if( adj.getBlock() instanceof BlockBranch && this.isEquivalent( adj ) ) {
                    adj = adj.with( facingProperty( facing.getOpposite() ), true );
                    world.setBlockState( adjPos, adj, 11 );

                    state = state.with( facingProperty( facing ), true );
                }
            }
        }

        return world.setBlockState( pos, state, 11 );
    }

    public static IBlockState withFluid( IBlockState state, IWorld world, BlockPos pos ) {
        IFluidState fluid = world.getFluidState( pos );
        return state.with( WATERLOGGED, EWaterlogType.getType( fluid ) );
    }

    @Override
    public Item createBlockItem() {
        return new ItemBranch( this, itemProps ).setRegistryName( getRegistryName() );
    }

    @Override
    public boolean isSolid( IBlockState state ) {
        return false;
    }

    public static BooleanProperty facingProperty( EnumFacing facing ) {
        switch( facing ) {
            default:
            case UP:
                return UP;
            case DOWN:
                return DOWN;
            case NORTH:
                return NORTH;
            case EAST:
                return EAST;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
        }
    }

    @Override
    public boolean canContainFluid( IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid ) {
        return fluid == MDFluids.MODERNIZED_WATER || fluid == Fluids.WATER;
    }

    @Override
    public boolean receiveFluid( IWorld world, BlockPos pos, IBlockState state, IFluidState fluidState ) {
        if( state.get( WATERLOGGED ) == EWaterlogType.NONE ) {
            if( fluidState.getFluid() == Fluids.WATER ) {
                if( ! world.isRemote() ) {
                    world.setBlockState( pos, state.with( WATERLOGGED, EWaterlogType.WATER ), 3 );
                    world.getPendingFluidTicks().scheduleTick( pos, Fluids.WATER, Fluids.WATER.getTickRate( world ) );
                }
            } else if( fluidState.getFluid() == MDFluids.MODERNIZED_WATER ) {
                if( ! world.isRemote() ) {
                    world.setBlockState( pos, state.with( WATERLOGGED, EWaterlogType.MODERNIZED_WATER ), 3 );
                    world.getPendingFluidTicks().scheduleTick( pos, MDFluids.MODERNIZED_WATER, MDFluids.MODERNIZED_WATER.getTickRate( world ) );
                }
            } else {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, IBlockState state ) {
        if( state.get( WATERLOGGED ) == EWaterlogType.WATER ) {
            world.setBlockState( pos, state.with( WATERLOGGED, EWaterlogType.NONE ), 3 );
            return Fluids.WATER;
        }
        return Fluids.EMPTY;
    }

    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( state.get( WATERLOGGED ) == EWaterlogType.WATER ) {
            world.getPendingFluidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickRate( world ) );
        }
        if( state.get( WATERLOGGED ) == EWaterlogType.MODERNIZED_WATER ) {
            world.getPendingFluidTicks().scheduleTick( currentPos, MDFluids.MODERNIZED_WATER, MDFluids.MODERNIZED_WATER.getTickRate( world ) );
        }
        return state;
    }

    public IBlockState copy( IBlockState other ) {
        return getDefaultState()
                .with( UP, other.get( UP ) )
                .with( DOWN, other.get( DOWN ) )
                .with( NORTH, other.get( NORTH ) )
                .with( EAST, other.get( EAST ) )
                .with( SOUTH, other.get( SOUTH ) )
                .with( WEST, other.get( WEST ) )
                .with( ROOT, other.get( ROOT ) )
                .with( WATERLOGGED, other.get( WATERLOGGED ) );
    }
}
