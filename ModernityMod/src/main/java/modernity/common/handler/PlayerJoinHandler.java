/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.handler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Sends seed packet to player when it joins a world.
 */
public class PlayerJoinHandler {
    @SubscribeEvent
    public void onPlayerJoin( PlayerEvent.PlayerLoggedInEvent event ) {
        if( event.getPlayer() instanceof ServerPlayerEntity ) { // TODO Send seed
//            Modernity.network().sendToPlayer( new SSeedPacket( event.getPlayer().world.getSeed() ), (ServerPlayerEntity) event.getPlayer() );
        }
    }
}
