/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.event;

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

    public CheckEntityInWaterEvent( boolean inWater, Entity e ) {
        super( e );
        this.inWater = inWater;
    }

    public boolean isInWater() {
        return inWater;
    }

    public void setInWater( boolean inWater ) {
        this.inWater = inWater;
    }
}
