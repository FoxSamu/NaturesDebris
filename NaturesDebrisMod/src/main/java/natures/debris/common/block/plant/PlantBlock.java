package natures.debris.common.block.plant;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import net.shadew.util.contract.Validate;

import natures.debris.core.util.OptionalUtil;
import natures.debris.core.util.reflect.MethodAccessor;
import natures.debris.common.block.plant.logic.IFluidLogic;

@SuppressWarnings("deprecation")
public class PlantBlock extends Block implements ILiquidContainer, IBucketPickupHandler {
    private static final String BEFORE_REPLACE_FLUID_SRG = "func_205580_a";
    private static final MethodAccessor<FlowingFluid, Void> BEFORE_REPLACE_FLUID_ACCESS = new MethodAccessor<>(
        FlowingFluid.class, BEFORE_REPLACE_FLUID_SRG,
        IWorld.class, BlockPos.class, BlockState.class
    );

    private final StateContainer<Block, BlockState> stateContainer;
    protected final Plant plant;
    protected final IFluidLogic fluidLogic;

    public PlantBlock(Plant plant) {
        super(plant.buildBlockProperties());
        this.plant = plant;
        this.fluidLogic = plant.getFluidLogic();

        stateContainer = plant.buildStateContainer(this);
        setDefaultState(plant.withDefaults(stateContainer.getBaseState()));
    }

    @Override
    public StateContainer<Block, BlockState> getStateContainer() {
        return stateContainer;
    }

    public Plant getPlant() {
        return plant;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return plant.getLuminance(state, world, pos);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos) {
        plant.tickFluid(state, world, pos);

        state = super.updatePostPlacement(state, dir, adjState, world, pos, adjPos);
        if (plant.canRemain(state, world, pos)) {
            state = plant.updateState(state, world, pos);
        } else {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return plant.canGenerateAt(state, world, pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getPos();
        BlockState state = plant.placementState(getDefaultState(), world, pos, ctx);

        if (!plant.canPlace(state, world, pos))
            return null;

        FluidState fluid = world.getFluidState(pos);
        IFluidLogic.Reaction reaction = fluidLogic.placeInFluid(plant, state, fluid);
        switch (OptionalUtil.orElse(reaction, IFluidLogic.Reaction.KEEP)) {
            case KEEP:
                return state;
            case DESTROY:
            case REMOVE:
                return null;
            case FLOOD:
                return fluidLogic.withFluidState(plant, state, fluid);
        }

        return Validate.illegalState();
    }


    @Override
    public Fluid pickupFluid(IWorld world, BlockPos pos, BlockState state) {
        FluidState fstate = world.getFluidState(pos);
        if (fstate.isEmpty())
            return Fluids.EMPTY;
        if (!fstate.isSource())
            return Fluids.EMPTY;

        IFluidLogic.Reaction reaction = fluidLogic.bucketRemoveFluid(plant, state);
        switch (OptionalUtil.orElse(reaction, IFluidLogic.Reaction.KEEP)) {
            case KEEP:
                return Fluids.EMPTY;

            case DESTROY:
                world.destroyBlock(pos, true);
            case REMOVE:
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

                break;

            case FLOOD:
                BlockState newState = fluidLogic.withFluidState(plant, state, Fluids.EMPTY.getDefaultState());
                world.setBlockState(pos, newState, 11);
        }
        return fstate.getFluid();
    }

    @Override
    public boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
        IFluidLogic.Reaction reaction = fluidLogic.fluidReplace(plant, state, fluid.getDefaultState());
        return reaction != IFluidLogic.Reaction.DESTROY;
    }

    @Override
    public boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, FluidState fstate) {
        Fluid fluid = fstate.getFluid();
        IFluidLogic.Reaction reaction = fluidLogic.fluidReplace(plant, state, fstate);
        switch (OptionalUtil.orElse(reaction, IFluidLogic.Reaction.KEEP)) {
            case KEEP:
                return false;
            case DESTROY:
                if (fluid instanceof FlowingFluid) {
                    FlowingFluid ffluid = (FlowingFluid) fluid;
                    BEFORE_REPLACE_FLUID_ACCESS.call(ffluid, world, pos, state);
                } else {
                    world.destroyBlock(pos, true);
                }
            case REMOVE:
                world.setBlockState(pos, fstate.getBlockState(), 3);
                break;
            case FLOOD:
                BlockState newState = fluidLogic.withFluidState(plant, state, fstate);
                world.setBlockState(pos, newState, 3);
        }
        return true;
    }
}
