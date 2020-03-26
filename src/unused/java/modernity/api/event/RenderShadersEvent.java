/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 23 - 2019
 * Author: rgsw
 */

package modernity.api.event;

import net.minecraftforge.eventbus.api.Event;

public class RenderShadersEvent extends Event {
    private final float partialTicks;

    public RenderShadersEvent( float partialTicks ) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
