package natures.debris.core.event;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidStateChangeEvent extends Event {
    private BlockState state;
    private final World world;
    private final BlockPos pos;
    private final FluidState fluid;

    public FluidStateChangeEvent(BlockState state, World world, BlockPos pos, FluidState fluid) {
        this.state = state;
        this.world = world;
        this.pos = pos;
        this.fluid = fluid;
    }

    public BlockState getState() {
        return state;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public FluidState getFluid() {
        return fluid;
    }

    public void setState(BlockState state) {
        this.state = state;
    }
}
