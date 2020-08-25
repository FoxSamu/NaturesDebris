/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.generic.event;

import natures.debris.generic.IModernityOld;
import natures.debris.generic.IRunMode;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired after {@link IModernityOld} is initialized
 *
 * @author RGSW
 */
public class ModernityInitializedEvent extends Event {
    public final IRunMode mode;
    public final IModernityOld modernity;

    public ModernityInitializedEvent(IRunMode mode, IModernityOld modernity) {
        this.mode = mode;
        this.modernity = modernity;
    }
}
