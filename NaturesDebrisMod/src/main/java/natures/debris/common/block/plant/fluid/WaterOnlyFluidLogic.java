package natures.debris.common.block.plant.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

public class WaterOnlyFluidLogic implements IFluidLogic {
    public static final WaterOnlyFluidLogic INSTANCE = new WaterOnlyFluidLogic();

    @Override
    public FluidReaction fluidReplace(BlockState state, FluidState fluid) {
        return fluid.getFluid() == Fluids.WATER ? FluidReaction.FLOOD : FluidReaction.KEEP;
    }

    @Override
    public FluidReaction placeInFluid(BlockState state, FluidState fluid) {
        return fluid.getFluid() == Fluids.WATER ? FluidReaction.FLOOD : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction growInFluid(BlockState state, FluidState fluid) {
        return fluid.getFluid() == Fluids.WATER ? FluidReaction.FLOOD : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction generateInFluid(BlockState state, FluidState fluid) {
        return fluid.getFluid() == Fluids.WATER ? FluidReaction.FLOOD : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction bucketRemoveFluid(BlockState state) {
        return FluidReaction.DESTROY;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getDefaultState();
    }

    @Override
    public boolean isWet(BlockState state) {
        return true;
    }
}
