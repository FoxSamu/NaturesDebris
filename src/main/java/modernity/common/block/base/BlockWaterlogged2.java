/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import modernity.common.fluid.MDFluids;

import javax.annotation.Nullable;

public class BlockWaterlogged2 extends BlockBase implements ILiquidContainer, IBucketPickupHandler {
    public static final BooleanProperty WATERLOGGED = BooleanProperty.create( "waterlogged" );

    public BlockWaterlogged2( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockWaterlogged2( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    public boolean canContainFluid( IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid ) {
        return fluid == MDFluids.MODERNIZED_WATER;
    }

    @Override
    public boolean receiveFluid( IWorld world, BlockPos pos, IBlockState state, IFluidState fluidState ) {
        if( ! state.get( WATERLOGGED ) ) {
            if( fluidState.getFluid() == MDFluids.MODERNIZED_WATER ) {
                if( ! world.isRemote() ) {
                    world.setBlockState( pos, state.with( WATERLOGGED, true ), 3 );
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
        if( state.get( WATERLOGGED ) ) {
            world.setBlockState( pos, state.with( WATERLOGGED, false ), 3 );
            return Fluids.WATER;
        }
        return Fluids.EMPTY;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( WATERLOGGED );
    }

    @Override
    public IFluidState getFluidState( IBlockState state ) {
        if( state.get( WATERLOGGED ) ) {
            return MDFluids.MODERNIZED_WATER.getDefaultState();
        }
        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        Fluid fluid = world.getFluidState( currentPos ).getFluid();
        world.getPendingFluidTicks().scheduleTick( currentPos, fluid, fluid.getTickRate( world ) );
        return super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        IFluidState fluid = context.getWorld().getFluidState( context.getPos() );
        return getDefaultState().with( WATERLOGGED, fluid.getFluid() == MDFluids.MODERNIZED_WATER );
    }
}
