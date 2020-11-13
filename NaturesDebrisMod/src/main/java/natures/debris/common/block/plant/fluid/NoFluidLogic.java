package natures.debris.common.block.plant.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

public class NoFluidLogic implements IFluidLogic {
    public static final NoFluidLogic INSTANCE = new NoFluidLogic();

    @Override
    public FluidReaction fluidReplace(BlockState state, FluidState fluid) {
        return fluid.isEmpty()
               ? FluidReaction.FLOOD
               : FluidReaction.DESTROY;
    }

    @Override
    public FluidReaction placeInFluid(BlockState state, FluidState fluid) {
        return FluidReaction.KEEP;
    }

    @Override
    public FluidReaction growInFluid(BlockState state, FluidState fluid) {
        return fluid.isEmpty() ? FluidReaction.KEEP : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction generateInFluid(BlockState state, FluidState fluid) {
        return fluid.isEmpty() ? FluidReaction.KEEP : FluidReaction.REMOVE;
    }

    @Override
    public FluidReaction bucketRemoveFluid(BlockState state) {
        return FluidReaction.KEEP;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    public boolean isWet(BlockState state) {
        return false;
    }
}
