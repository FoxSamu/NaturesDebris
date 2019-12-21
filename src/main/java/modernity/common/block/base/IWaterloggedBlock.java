/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 21 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.block.IModernityBucketPickupHandler;
import modernity.api.block.fluid.IModernityBucketTakeable;
import modernity.api.block.fluid.IVanillaBucketTakeable;
import modernity.api.util.EWaterlogType;
import modernity.common.block.MDBlockStateProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

/**
 * Handles waterlogging in both Modernized Water and Vanilla Water
 */
public interface IWaterloggedBlock extends IBucketPickupHandler, IModernityBucketPickupHandler, ILiquidContainer {
    @Override
    default boolean canContainFluid( IBlockReader world, BlockPos pos, BlockState state, Fluid fluid ) {
        return state.get( MDBlockStateProperties.WATERLOGGED ).canContain( fluid );
    }

    @Override
    default boolean receiveFluid( IWorld world, BlockPos pos, BlockState state, IFluidState fstate ) {
        if( state.get( MDBlockStateProperties.WATERLOGGED ).canContain( fstate.getFluid() ) ) {
            if( ! world.isRemote() ) {
                world.setBlockState( pos, state.with( MDBlockStateProperties.WATERLOGGED, EWaterlogType.getType( fstate ) ), 3 );
                world.getPendingFluidTicks().scheduleTick( pos, fstate.getFluid(), fstate.getFluid().getTickRate( world ) );
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    default Fluid pickupFluid( IWorld world, BlockPos pos, BlockState state ) {
        EWaterlogType type = state.get( MDBlockStateProperties.WATERLOGGED );
        if( ! type.isEmpty() && type.getFluidState().getFluid() instanceof IVanillaBucketTakeable ) {
            world.setBlockState( pos, state.with( MDBlockStateProperties.WATERLOGGED, EWaterlogType.NONE ), 3 );
            return type.getFluidState().getFluid();
        } else {
            return Fluids.EMPTY;
        }
    }

    @Override
    default Fluid pickupFluidModernity( IWorld world, BlockPos pos, BlockState state ) {
        EWaterlogType type = state.get( MDBlockStateProperties.WATERLOGGED );
        if( ! type.isEmpty() && type.getFluidState().getFluid() instanceof IModernityBucketTakeable ) {
            world.setBlockState( pos, state.with( MDBlockStateProperties.WATERLOGGED, EWaterlogType.NONE ), 3 );
            return type.getFluidState().getFluid();
        } else {
            return Fluids.EMPTY;
        }
    }
}
