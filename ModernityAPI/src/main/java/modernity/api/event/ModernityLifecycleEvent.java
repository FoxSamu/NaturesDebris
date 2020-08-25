/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved.
 * This file is part of the Modernity Plugin API and may be used,
 * included and distributed within other projects without further
 * permission, unless the copyright holder is not the original
 * author or the owner had forbidden the user to use this file.
 * Other terms and conditions still apply.
 *
 * For a full license, see LICENSE.txt.
 */

package modernity.api.event;

import modernity.api.IModernity;
import modernity.api.LifecyclePhase;
import net.minecraftforge.eventbus.api.Event;

public class ModernityLifecycleEvent extends Event {
    private final LifecyclePhase phase;
    private final IModernity modernity;

    public ModernityLifecycleEvent(LifecyclePhase phase, IModernity modernity) {
        this.phase = phase;
        this.modernity = modernity;
    }

    public LifecyclePhase getLifecyclePhase() {
        return phase;
    }

    public IModernity getModernity() {
        return modernity;
    }
}
