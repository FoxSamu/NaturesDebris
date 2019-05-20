package modernity.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.common.item.MDItems;

public class RegistryHandler {

    @SubscribeEvent
    public void registerBlocks( RegistryEvent.Register<Block> event ) {

    }

    @SubscribeEvent
    public void registerItems( RegistryEvent.Register<Item> event ) {
        MDItems.register( event.getRegistry() );
    }

}
