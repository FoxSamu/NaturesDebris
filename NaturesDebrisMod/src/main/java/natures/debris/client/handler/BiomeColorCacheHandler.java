package natures.debris.client.handler;

import natures.debris.client.util.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public final class BiomeColorCacheHandler {
    private static final List<BiomeColorCache> CACHES = new ArrayList<>();

    private BiomeColorCacheHandler() {
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load load) {
        if (load.getWorld() instanceof ClientWorld) {
            for (BiomeColorCache cache : CACHES) {
                cache.reset();
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load load) {
        if (load.getWorld() instanceof ClientWorld) {
            for (BiomeColorCache cache : CACHES) {
                cache.chunkLoad(load.getChunk().getPos());
            }
        }
    }

    public static void addCache(BiomeColorCache cache) {
        CACHES.add(cache);
    }
}
