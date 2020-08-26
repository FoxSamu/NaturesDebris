package natures.debris.common.handler;

import natures.debris.common.block.NdBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegistryHandler {
    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> evt) {
        NdBlocks.register(evt.getRegistry());
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> evt) {
        NdBlocks.registerItems(evt.getRegistry());
    }
}
