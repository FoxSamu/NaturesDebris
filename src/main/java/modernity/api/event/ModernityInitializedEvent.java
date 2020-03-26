/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.api.event;

import modernity.api.IModernity;
import modernity.api.RunMode;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired after {@link IModernity} is initialized
 *
 * @author RGSW
 */
public class ModernityInitializedEvent extends Event {
    public final RunMode mode;
    public final IModernity modernity;

    public ModernityInitializedEvent( RunMode mode, IModernity modernity ) {
        this.mode = mode;
        this.modernity = modernity;
    }
}
