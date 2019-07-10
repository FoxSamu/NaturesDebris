/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.common.handler;

import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.api.event.AddOverworldStructureEvent;
import modernity.common.net.pkt.SPacketStructure;
import modernity.common.util.ProxyCommon;
import modernity.common.world.gen.structure.CurseRuinStructure;
import modernity.common.world.gen.structure.MDStructures;

public class StructureHandler {
    @SubscribeEvent
    public void onChunkWatch( ChunkWatchEvent.Watch event ) {
        ProxyCommon.network().sendToPlayer( new SPacketStructure( "MDCave", event.getChunk() ), event.getPlayer() );
    }

    @SubscribeEvent
    public void onAddOverworldStructures( AddOverworldStructureEvent event ) {
        event.registerStructure( GenerationStage.Decoration.SURFACE_STRUCTURES, MDStructures.CURSE_RUIN, CurseRuinStructure.Type.PLAINS );
    }
}
