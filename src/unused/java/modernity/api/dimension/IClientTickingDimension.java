/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.dimension;

import modernity.client.ModernityClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Dimensions implementing this interface receive client side ticks from {@link ModernityClient}.
 */
@FunctionalInterface
public interface IClientTickingDimension {
    @OnlyIn( Dist.CLIENT )
    void tickClient();
}
