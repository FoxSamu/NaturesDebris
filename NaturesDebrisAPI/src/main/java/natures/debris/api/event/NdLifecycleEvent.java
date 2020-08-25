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

package natures.debris.api.event;

import natures.debris.api.INaturesDebris;
import natures.debris.api.LifecyclePhase;
import net.minecraftforge.eventbus.api.Event;

public class NdLifecycleEvent extends Event {
    private final LifecyclePhase phase;
    private final INaturesDebris naturesDebris;

    public NdLifecycleEvent(LifecyclePhase phase, INaturesDebris naturesDebris) {
        this.phase = phase;
        this.naturesDebris = naturesDebris;
    }

    public LifecyclePhase getLifecyclePhase() {
        return phase;
    }

    public INaturesDebris getNaturesDebris() {
        return naturesDebris;
    }
}
