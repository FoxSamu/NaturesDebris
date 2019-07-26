/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import modernity.common.settings.ServerSettings;
import modernity.common.settings.SynchronizingServerSettings;
import modernity.common.util.ProxyCommon;

public class SettingsShareHandler {
    @SubscribeEvent
    public void onPlayerJoin( PlayerEvent.PlayerLoggedInEvent event ) {
        ServerSettings settings = ProxyCommon.get().getServerSettings();
        if( settings instanceof SynchronizingServerSettings && event.getPlayer() instanceof EntityPlayerMP ) {
            ( (SynchronizingServerSettings) settings ).shareWith( (EntityPlayerMP) event.getPlayer() );
        }
    }
}
