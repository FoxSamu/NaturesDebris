package modernity.common.handler;

import modernity.common.area.core.ServerWorldAreaManager;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum WorldAreaHandler {
    INSTANCE;

    @SubscribeEvent
    public void tickWorld( TickEvent.WorldTickEvent event ) {
        ServerWorldAreaManager.get( event.world ).ifPresent( ServerWorldAreaManager::tick );
    }

    @SubscribeEvent
    public void chunkLoad( ChunkEvent.Load event ) {
        ServerWorldAreaManager.get( (World) event.getWorld() ).ifPresent( m -> m.loadChunk( event.getChunk().getPos() ) );
    }

    @SubscribeEvent
    public void chunkUnload( ChunkEvent.Unload event ) {
        ServerWorldAreaManager.get( (World) event.getWorld() ).ifPresent( m -> m.unloadChunk( event.getChunk().getPos() ) );
    }

    @SubscribeEvent
    public void worldSave( WorldEvent.Save event ) {
        ServerWorldAreaManager.get( (World) event.getWorld() ).ifPresent( ServerWorldAreaManager::saveAll );
    }
}
