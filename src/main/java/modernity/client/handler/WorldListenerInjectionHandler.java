/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.client.handler;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.client.util.MDClientWorldListener;

public class WorldListenerInjectionHandler {
    @SubscribeEvent
    public void onWorldCreate( WorldEvent.Load event ) {
        if( event.getWorld() instanceof WorldClient ) {
            ( (WorldClient) event.getWorld() ).addEventListener( new MDClientWorldListener( (World) event.getWorld() ) );
        }
    }
}
