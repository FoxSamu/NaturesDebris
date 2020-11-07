package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

import natures.debris.common.block.plant.Plant;

public class NoFluidLogic implements IFluidLogic {
    public static final NoFluidLogic INSTANCE = new NoFluidLogic();

    @Override
    public Reaction fluidReplace(Plant plant, BlockState state, FluidState fluid) {
        return fluid.isEmpty()
               ? Reaction.FLOOD
               : Reaction.DESTROY;
    }

    @Override
    public Reaction placeInFluid(Plant plant, BlockState state, FluidState fluid) {
        return Reaction.KEEP;
    }

    @Override
    public Reaction growInFluid(Plant plant, BlockState state, FluidState fluid) {
        return Reaction.REMOVE;
    }

    @Override
    public Reaction generateInFluid(Plant plant, BlockState state, FluidState fluid) {
        return Reaction.REMOVE;
    }

    @Override
    public Reaction bucketRemoveFluid(Plant plant, BlockState state) {
        return Reaction.KEEP;
    }

    @Override
    public FluidState getFluidState(Plant plant, BlockState state) {
        return Fluids.EMPTY.getDefaultState();
    }
}
