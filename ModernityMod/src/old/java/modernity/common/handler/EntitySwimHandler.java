/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.handler;

import modernity.generic.event.CheckEntityInWaterEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles oil swimming behaviour
 */
public enum EntitySwimHandler {
    INSTANCE;

    @SubscribeEvent
    public void checkEntityInWater(CheckEntityInWaterEvent event) {
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.abilities.isFlying || player.isSpectator()) return;
        }
    }
}
