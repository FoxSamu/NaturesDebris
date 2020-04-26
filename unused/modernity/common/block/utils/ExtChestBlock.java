/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.block.utils;

import net.minecraft.block.Block;

// TODO Re-evaluate
public class ExtChestBlock extends Block { // extends ChestBlock implements IWaterloggedBlock, ICustomBlockItem {
//    public static final EnumProperty<WaterlogType> WATERLOGGED = MDBlockStateProperties.WATERLOGGED;
//    private final StateContainer<Block, BlockState> chestStateContainer;

    public ExtChestBlock( Properties properties ) {
        super( properties );

//        StateContainer.Builder<Block, BlockState> builder = new StateContainer.Builder<>( this );
//        builder.add( FACING, TYPE, WATERLOGGED );
//        chestStateContainer = builder.create( BlockState::new );
//
//        setDefaultState( chestStateContainer.getBaseState()
//                                            .with( FACING, Direction.NORTH )
//                                            .with( TYPE, ChestType.SINGLE )
//                                            .with( WATERLOGGED, WaterlogType.NONE ) );
    }

//    @Override
//    public StateContainer<Block, BlockState> getStateContainer() {
//        return chestStateContainer;
//    }
//
//    @Nullable
//    @Override
//    public TileEntity createNewTileEntity( IBlockReader world ) {
//        return new TexturedChestTileEntity();
//    }
//
//    @Override
//    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adj ) {
//        if( ! state.get( WATERLOGGED ).isEmpty() ) {
//            IFluidState f = world.getFluidState( pos );
//            world.getPendingFluidTicks().scheduleTick( pos, f.getFluid(), f.getFluid().getTickRate( world ) );
//        }
//
//        if( adjState.getBlock() == this && dir.getAxis().isHorizontal() ) {
//            ChestType type = adjState.get( TYPE );
//            if( state.get( TYPE ) == ChestType.SINGLE && type != ChestType.SINGLE && state.get( FACING ) == adjState.get( FACING ) && getDirectionToAttached( adjState ) == dir.getOpposite() ) {
//                return state.with( TYPE, type.opposite() );
//            }
//        } else if( getDirectionToAttached( state ) == dir ) {
//            return state.with( TYPE, ChestType.SINGLE );
//        }
//
//        return state;
//    }
//
//    @Override
//    public BlockState getStateForPlacement( BlockItemUseContext context ) {
//        ChestType type = ChestType.SINGLE;
//        Direction chestDir = context.getPlacementHorizontalFacing().getOpposite();
//
//        IFluidState fstate = context.getWorld().getFluidState( context.getPos() );
//
//        boolean sneaking = context.isPlacerSneaking();
//        Direction face = context.getFace();
//
//        if( face.getAxis().isHorizontal() && sneaking ) {
//            Direction dir = getDirectionToAttach( context, face.getOpposite() );
//            if( dir != null && dir.getAxis() != face.getAxis() ) {
//                chestDir = dir;
//                type = dir.rotateYCCW() == face.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
//            }
//        }
//
//        if( type == ChestType.SINGLE && ! sneaking ) {
//            if( chestDir == getDirectionToAttach( context, chestDir.rotateY() ) ) {
//                type = ChestType.LEFT;
//            } else if( chestDir == getDirectionToAttach( context, chestDir.rotateYCCW() ) ) {
//                type = ChestType.RIGHT;
//            }
//        }
//
//        return getDefaultState().with( FACING, chestDir )
//                                .with( TYPE, type )
//                                .with( WATERLOGGED, WaterlogType.getType( fstate ) );
//    }
//
//    @Override
//    public IFluidState getFluidState( BlockState state ) {
//        return state.get( WATERLOGGED ).getFluidState();
//    }
//
//    /**
//     * Returns facing pointing to a chest to form a double chest with, null otherwise
//     */
//    @Nullable
//    private Direction getDirectionToAttach( BlockItemUseContext ctx, Direction dir ) {
//        BlockState state = ctx.getWorld().getBlockState( ctx.getPos().offset( dir ) );
//        return state.getBlock() == this && state.get( TYPE ) == ChestType.SINGLE ? state.get( FACING ) : null;
//    }
//
//    @Override
//    public boolean canContainFluid( IBlockReader world, BlockPos pos, BlockState state, Fluid fluid ) {
//        return state.get( WATERLOGGED ).canContain( fluid );
//    }
//
//    @Override
//    public boolean receiveFluid( IWorld world, BlockPos pos, BlockState state, IFluidState fstate ) {
//        if( state.get( WATERLOGGED ).canContain( fstate.getFluid() ) ) {
//            if( ! world.isRemote() ) {
//                world.setBlockState( pos, state.with( WATERLOGGED, WaterlogType.getType( fstate ) ), 3 );
//                world.getPendingFluidTicks().scheduleTick( pos, fstate.getFluid(), fstate.getFluid().getTickRate( world ) );
//            }
//
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public Fluid pickupFluid( IWorld world, BlockPos pos, BlockState state ) {
//        WaterlogType type = state.get( WATERLOGGED );
//        if( ! type.isEmpty() && type.getFluidState().getFluid() instanceof IVanillaBucketTakeable ) {
//            world.setBlockState( pos, state.with( WATERLOGGED, WaterlogType.NONE ), 3 );
//            return type.getFluidState().getFluid();
//        } else {
//            return Fluids.EMPTY;
//        }
//    }
//
//    @Override
//    public Fluid pickupFluidModernity( IWorld world, BlockPos pos, BlockState state ) {
//        WaterlogType type = state.get( WATERLOGGED );
//        if( ! type.isEmpty() && type.getFluidState().getFluid() instanceof IModernityBucketTakeable ) {
//            world.setBlockState( pos, state.with( WATERLOGGED, WaterlogType.NONE ), 3 );
//            return type.getFluidState().getFluid();
//        } else {
//            return Fluids.EMPTY;
//        }
//    }
//
//    @Override
//    public Item createBlockItem( Item.Properties properties ) {
//        return new BlockItem( this, properties.setTEISR( () -> getChestItemRenderer( this ) ) );
//    }
//
//    @OnlyIn( Dist.CLIENT )
//    private static Callable<ItemStackTileEntityRenderer> getChestItemRenderer( Block block ) {
//        return () -> new TexturedChestItemRenderer( block );
//    }
}
