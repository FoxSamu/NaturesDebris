/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.fluid;

import modernity.common.block.MDBlockStateProperties;
import modernity.generic.block.IModernityBucketPickupHandler;
import modernity.generic.block.fluid.IModernityBucketTakeable;
import modernity.generic.block.fluid.IVanillaBucketTakeable;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

/**
 * Handles waterlogging in both Modernized Water and Vanilla Water
 */
public interface IWaterloggedBlock extends IBucketPickupHandler, IModernityBucketPickupHandler, ILiquidContainer {
    EnumProperty<WaterlogType> WATERLOGGED = MDBlockStateProperties.WATERLOGGED;

    @Override
    default boolean canContainFluid( IBlockReader world, BlockPos pos, BlockState state, Fluid fluid ) {
        return state.get( WATERLOGGED ).canContain( fluid );
    }

    @Override
    default boolean receiveFluid( IWorld world, BlockPos pos, BlockState state, IFluidState fstate ) {
        if( state.get( WATERLOGGED ).canContain( fstate.getFluid() ) ) {
            if( ! world.isRemote() ) {
                world.setBlockState( pos, state.with( WATERLOGGED, WaterlogType.getType( fstate ) ), 3 );
                world.getPendingFluidTicks().scheduleTick( pos, fstate.getFluid(), fstate.getFluid().getTickRate( world ) );
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    default Fluid pickupFluid( IWorld world, BlockPos pos, BlockState state ) {
        WaterlogType type = state.get( WATERLOGGED );
        if( ! type.isEmpty() && type.getFluidState().getFluid() instanceof IVanillaBucketTakeable ) {
            world.setBlockState( pos, state.with( WATERLOGGED, WaterlogType.NONE ), 3 );
            return type.getFluidState().getFluid();
        } else {
            return Fluids.EMPTY;
        }
    }

    @Override
    default Fluid pickupFluidModernity( IWorld world, BlockPos pos, BlockState state ) {
        WaterlogType type = state.get( WATERLOGGED );
        if( ! type.isEmpty() && type.getFluidState().getFluid() instanceof IModernityBucketTakeable ) {
            world.setBlockState( pos, state.with( WATERLOGGED, WaterlogType.NONE ), 3 );
            return type.getFluidState().getFluid();
        } else {
            return Fluids.EMPTY;
        }
    }
}
