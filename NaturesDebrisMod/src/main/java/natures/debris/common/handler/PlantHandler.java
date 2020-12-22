package natures.debris.common.handler;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import natures.debris.core.event.FluidStateChangeEvent;
import natures.debris.common.block.plantold.PlantBlock;

public class PlantHandler {
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        ItemStack item = event.getItemStack();
        BlockState state = world.getBlockState(pos);


    }

    @SubscribeEvent
    public void onFluidStateChange(FluidStateChangeEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        FluidState fluid = event.getFluid();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof PlantBlock) {
            PlantBlock plant = (PlantBlock) state.getBlock();
            BlockState out = plant.getFluidLogic().withFluidState(state, fluid);
            event.setState(out);
        }
    }
}
