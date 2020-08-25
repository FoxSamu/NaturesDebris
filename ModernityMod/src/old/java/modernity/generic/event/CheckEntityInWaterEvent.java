/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fired by hook to manually decide whether an entity is in water or not...
 *
 * @author RGSW
 */
@Cancelable
public class CheckEntityInWaterEvent extends EntityEvent {

    private boolean inWater;

    public CheckEntityInWaterEvent(boolean inWater, Entity e) {
        super(e);
        this.inWater = inWater;
    }

    public boolean isInWater() {
        return inWater;
    }

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }
}
