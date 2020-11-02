package natures.debris.common.handler;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

import natures.debris.core.util.IRegistry;
import natures.debris.common.block.NdBlocks;
import natures.debris.common.item.NdItems;
import natures.debris.common.sound.NdSoundEvents;

public class RegistryHandler {
    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> evt) {
        NdBlocks.registerBlocks(IRegistry.forge(evt.getRegistry()));
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> evt) {
        NdBlocks.registerItems(IRegistry.forge(evt.getRegistry()));
        NdItems.registerItems(IRegistry.forge(evt.getRegistry()));
    }

    @SubscribeEvent
    public void onRegisterSoundEvents(RegistryEvent.Register<SoundEvent> evt) {
        NdSoundEvents.registerSoundEvents(IRegistry.forge(evt.getRegistry()));
    }
}
