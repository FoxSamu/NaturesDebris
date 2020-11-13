package natures.debris.common.block.plant.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

public class WaterFlowThroughFluidLogic implements IFluidLogic {
    public static final WaterFlowThroughFluidLogic INSTANCE = new WaterFlowThroughFluidLogic();

    @Override
    public FluidReaction fluidReplace(BlockState state, FluidState fluid) {
        return fluid.isEmpty() || fluid.getFluid() == Fluids.WATER || fluid.getFluid() == Fluids.FLOWING_WATER
               ? FluidReaction.FLOOD : FluidReaction.KEEP;
    }

    @Override
    public FluidReaction placeInFluid(BlockState state, FluidState fluid) {
        return fluid.isEmpty() || fluid.getFluid() == Fluids.WATER || fluid.getFluid() == Fluids.FLOWING_WATER
               ? FluidReaction.FLOOD : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction growInFluid(BlockState state, FluidState fluid) {
        return fluid.isEmpty() || fluid.getFluid() == Fluids.WATER || fluid.getFluid() == Fluids.FLOWING_WATER
               ? FluidReaction.FLOOD : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction generateInFluid(BlockState state, FluidState fluid) {
        return fluid.isEmpty() || fluid.getFluid() == Fluids.WATER || fluid.getFluid() == Fluids.FLOWING_WATER
               ? FluidReaction.FLOOD : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction bucketRemoveFluid(BlockState state) {
        return FluidReaction.FLOOD;
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(FlowingFluidBlock.LEVEL) > 8
               ? Fluids.EMPTY.getDefaultState()
               : Blocks.WATER.getFluidState(state);
    }

    @Override
    public BlockState withFluidState(BlockState state, FluidState fluid) {
        return state.with(FlowingFluidBlock.LEVEL, getLevelFromState(fluid));
    }

    @Override
    public boolean isWet(BlockState state) {
        return state.get(FlowingFluidBlock.LEVEL) <= 8;
    }

    protected static int getLevelFromState(FluidState state) {
        if (state.isEmpty())
            return 9;
        if (state.isSource())
            return 0;
        return state.get(FlowingFluid.FALLING)
               ? 8
               : 8 - Math.min(state.getLevel(), 8);
    }
}
