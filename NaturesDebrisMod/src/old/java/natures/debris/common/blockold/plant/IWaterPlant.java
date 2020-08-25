/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.fluidold.MDFluids;
import natures.debris.generic.block.IModernityBucketPickupHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public interface IWaterPlant extends IModernityBucketPickupHandler, ILiquidContainer {

    @Override
    default boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    default boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, IFluidState fstate) {
        return false;
    }

    @Override
    default Fluid pickupFluidModernity(IWorld world, BlockPos pos, BlockState state) {
        if (state.get(BlockStateProperties.WATERLOGGED)) {
            world.destroyBlock(pos, true);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            return MDFluids.MURKY_WATER;
        } else {
            return Fluids.EMPTY;
        }
    }
}
