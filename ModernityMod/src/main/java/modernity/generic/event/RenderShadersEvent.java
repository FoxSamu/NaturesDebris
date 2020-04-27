/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.event;

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
