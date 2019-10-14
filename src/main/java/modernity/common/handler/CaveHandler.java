/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.common.handler;

import modernity.common.Modernity;
import modernity.common.net.SCaveHeightsPacket;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Sends cave data to players on watching a chunk.
 */
public class CaveHandler {
    @SubscribeEvent
    public void onChunkWatch( ChunkWatchEvent.Watch event ) {
        // Share cave data with player
        Modernity.network().sendToPlayer( new SCaveHeightsPacket( event.getWorld().getChunk( event.getPos().x, event.getPos().z ) ), event.getPlayer() );
    }
}
