/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.generic.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;

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
