/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.handler;

import modernity.common.Modernity;
import modernity.common.net.SCaveHeightsPacket;
import net.minecraftforge.event.world.ChunkWatchEvent;

/**
 * Sends cave data to players on watching a chunk.
 */
public enum CaveHandler {
    INSTANCE;
//    @SubscribeEvent
    public void onChunkWatch( ChunkWatchEvent.Watch event ) {
        // Share cave data with player
        Modernity.network().sendToPlayer( new SCaveHeightsPacket( event.getWorld().getChunk( event.getPos().x, event.getPos().z ) ), event.getPlayer() );
    }
}
