/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.handler;

import modernity.common.area.core.ServerWorldAreaManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum WorldAreaHandler {
    INSTANCE;

    @SubscribeEvent
    public void tickWorld(TickEvent.WorldTickEvent event) {
        ServerWorldAreaManager.get(event.world).ifPresent(ServerWorldAreaManager::tick);
    }

    @SubscribeEvent
    public void worldSave(WorldEvent.Save event) {
        ServerWorldAreaManager.get((World) event.getWorld()).ifPresent(ServerWorldAreaManager::saveAll);
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        ServerWorldAreaManager.get((World) event.getWorld()).ifPresent(ServerWorldAreaManager::init);
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event) {
        ServerWorldAreaManager.get((World) event.getWorld()).ifPresent(ServerWorldAreaManager::saveAll);
    }

    @SubscribeEvent
    public void playerJoin(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof PlayerEntity) {
            ServerWorldAreaManager.get(event.getWorld()).ifPresent(ServerWorldAreaManager::playerJoin);
        }
    }
}
