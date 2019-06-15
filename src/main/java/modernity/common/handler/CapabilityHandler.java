/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.common.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.common.capability.chunk.ChunkCapabilityProvider;

public class CapabilityHandler {
    @SubscribeEvent
    public void onAttachChunkCaps( AttachCapabilitiesEvent<Chunk> event ) {
        event.addCapability( new ResourceLocation( "modernity:chunk_data" ), new ChunkCapabilityProvider( event.getObject() ) );
    }
}
