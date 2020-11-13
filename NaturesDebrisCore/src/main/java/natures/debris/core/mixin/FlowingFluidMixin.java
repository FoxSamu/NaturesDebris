package natures.debris.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import natures.debris.core.NaturesDebrisCore;
import natures.debris.core.event.FluidStateChangeEvent;

@Mixin(FlowingFluid.class)
public class FlowingFluidMixin {
    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/fluid/FluidState;getBlockState()Lnet/minecraft/block/BlockState;"
        )
    )
    private BlockState fluidGetBlockStateProxy(FluidState fluid, World world, BlockPos pos, FluidState fluid1) {
        FluidStateChangeEvent event = new FluidStateChangeEvent(fluid.getBlockState(), world, pos, fluid);
        NaturesDebrisCore.CORE_EVENT_BUS.post(event);
        return event.getState();
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;"
        )
    )
    private BlockState blockGetDefaultStateProxy(Block block, World world, BlockPos pos, FluidState fluid) {
        FluidStateChangeEvent event = new FluidStateChangeEvent(block.getDefaultState(), world, pos, Fluids.EMPTY.getDefaultState());
        NaturesDebrisCore.CORE_EVENT_BUS.post(event);
        return event.getState();
    }
}
