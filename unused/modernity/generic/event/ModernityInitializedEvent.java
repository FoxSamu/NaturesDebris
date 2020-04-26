/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.generic.event;

import modernity.generic.IModernityOld;
import modernity.generic.IRunMode;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired after {@link IModernityOld} is initialized
 *
 * @author RGSW
 */
public class ModernityInitializedEvent extends Event {
    public final IRunMode mode;
    public final IModernityOld modernity;

    public ModernityInitializedEvent( IRunMode mode, IModernityOld modernity ) {
        this.mode = mode;
        this.modernity = modernity;
    }
}
