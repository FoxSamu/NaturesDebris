/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.handler;

import modernity.common.Modernity;
import modernity.common.net.SSeedPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Sends seed packet to player when it joins a world.
 */
public class PlayerJoinHandler {
    @SubscribeEvent
    public void onPlayerJoin( PlayerEvent.PlayerLoggedInEvent event ) {
        if( event.getPlayer() instanceof ServerPlayerEntity ) {
            Modernity.network().sendToPlayer( new SSeedPacket( event.getPlayer().world.getSeed() ), (ServerPlayerEntity) event.getPlayer() );
        }
    }
}
