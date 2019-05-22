package modernity.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.common.block.MDBlocks;
import modernity.common.item.MDItems;

public class RegistryHandler {

    @SubscribeEvent
    public void registerBlocks( RegistryEvent.Register<Block> event ) {
        MDBlocks.register( event.getRegistry() );
    }

    @SubscribeEvent
    public void registerItems( RegistryEvent.Register<Item> event ) {
        MDBlocks.registerItems( event.getRegistry() );
        MDItems.register( event.getRegistry() );
    }

}
