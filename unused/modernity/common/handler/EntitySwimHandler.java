/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 23 - 2019
 * Author: rgsw
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
    public void checkEntityInWater( CheckEntityInWaterEvent event ) {
        if( event.getEntity() instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if( player.abilities.isFlying || player.isSpectator() ) return;
        }
    }
}
