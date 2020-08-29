package natures.debris.common.handler;

import natures.debris.common.block.NdBlocks;
import natures.debris.common.item.NdItems;
import natures.debris.common.sound.NdSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
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
        NdItems.register(evt.getRegistry());
    }

    @SubscribeEvent
    public void onRegisterSoundEvents(RegistryEvent.Register<SoundEvent> evt) {
        NdSoundEvents.register(evt.getRegistry());
    }
}
