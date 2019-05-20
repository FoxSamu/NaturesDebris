package modernity.common.util;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegistryHandler {
    @SubscribeEvent
    public static void onBlocksRegistry( final RegistryEvent.Register<Block> blockRegistryEvent ) {
        System.out.println( "HELLO from Register Block" );
    }
}
