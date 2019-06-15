/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.common.handler;

import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.common.net.SPacketStructure;

public class StructureSyncHandler {
    @SubscribeEvent
    public void onChunkWatch( ChunkWatchEvent.Watch event ) {
        event.getPlayer().connection.sendPacket( new SPacketStructure( "MDCave", event.getChunk() ) );
    }
}
