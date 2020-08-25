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

package modernity.api;

import modernity.api.util.ISidedTickable;
import net.minecraftforge.eventbus.api.IEventBus;

public interface IModernity {
    /**
     * Gets the Modernity mod instance. This is {@code modernity.common.Modernity} on dedicated server dist and {@code
     * modernity.client.ModernityClient} on the client dist.
     *
     * On the client dist, this instance can be cast to {@link IModernityClient}, although the {@link IModernityClient}
     * can be more easily obtained via {@link IModernityClient#get()}.
     */
    static IModernity get() {
        return ModernityHolder.get();
    }
    /**
     * Returns the Modernity event bus, to register for Modernity-specific events. Note that this event bus is neither
     * the Forge or the FML event bus.
     */
    static IEventBus eventBus() {
        return ModernityHolder.EVENT_BUS;
    }
    /**
     * Returns the version info of the the Modernity mod.
     */
    IModernityInfo info();
    /**
     * Returns the current loading lifecycle phase of the Modernity.
     */
    LifecyclePhase getLifecyclePhase();
    /**
     * Ticks a {@link ISidedTickable}. When not called from the client dist or not on the client thread it invokes only
     * the server tick. When called from the client thread it invokes both server and client tick.
     */
    void tickSided(ISidedTickable sidedTickable);
}
