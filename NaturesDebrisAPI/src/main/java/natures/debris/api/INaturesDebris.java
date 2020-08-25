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

package natures.debris.api;

import natures.debris.api.util.ISidedTickable;
import net.minecraftforge.eventbus.api.IEventBus;

public interface INaturesDebris {
    /**
     * Gets Nature's Debris mod instance. This is {@code natures.debris.common.NaturesDebris} on dedicated server dist
     * and {@code natures.debris.client.NaturesDebrisClient} on the client dist.
     *
     * On the client dist, this instance can be cast to {@link INaturesDebrisClient}, although the {@link
     * INaturesDebrisClient} can be more easily obtained via {@link INaturesDebrisClient#get()}.
     */
    static INaturesDebris get() {
        return APIDelegate.get();
    }
    /**
     * Returns Nature's Debris event bus, to register for Nature's Debris-specific events. Note that this event bus is
     * neither the Forge or the FML event bus.
     */
    static IEventBus eventBus() {
        return APIDelegate.EVENT_BUS;
    }
    /**
     * Returns the version info of the Nature's Debris mod.
     */
    INaturesDebrisInfo info();
    /**
     * Ticks a {@link ISidedTickable}. When not called from the client dist or not on the client thread it invokes only
     * the server tick. When called from the client thread it invokes both server and client tick.
     */
    void tickSided(ISidedTickable sidedTickable);
}
