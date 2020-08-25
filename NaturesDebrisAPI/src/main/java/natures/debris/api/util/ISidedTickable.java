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

package natures.debris.api.util;

import natures.debris.api.INaturesDebris;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Something that can receive tick events on both server and client side (if available). To receive a sided tick, call
 * {@link INaturesDebris#tickSided}.
 */
public interface ISidedTickable {
    /**
     * Tick method that is only called on the client distribution, and only on the client thread.
     */
    @OnlyIn(Dist.CLIENT)
    void clientTick();

    /**
     * Thick method that is only called on the server thread.
     */
    void serverTick();
}
