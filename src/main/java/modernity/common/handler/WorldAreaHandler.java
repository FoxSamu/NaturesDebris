/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.handler;

import modernity.common.area.core.ServerWorldAreaManager;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum WorldAreaHandler {
    INSTANCE;

    @SubscribeEvent
    public void tickWorld( TickEvent.WorldTickEvent event ) {
        ServerWorldAreaManager.get( event.world ).ifPresent( ServerWorldAreaManager::tick );
    }

    @SubscribeEvent
    public void worldSave( WorldEvent.Save event ) {
        ServerWorldAreaManager.get( (World) event.getWorld() ).ifPresent( ServerWorldAreaManager::saveAll );
    }

    @SubscribeEvent
    public void worldLoad( WorldEvent.Load event ) {
        ServerWorldAreaManager.get( (World) event.getWorld() ).ifPresent( ServerWorldAreaManager::init );
    }

    @SubscribeEvent
    public void worldUnload( WorldEvent.Unload event ) {
        ServerWorldAreaManager.get( (World) event.getWorld() ).ifPresent( ServerWorldAreaManager::saveAll );
    }
}
