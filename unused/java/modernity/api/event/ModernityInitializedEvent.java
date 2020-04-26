/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.generic.event;

import modernity.generic.IModernity;
import modernity.generic.RunMode;
import modernity.common.Modernity;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired after {@link Modernity} is initialized
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
