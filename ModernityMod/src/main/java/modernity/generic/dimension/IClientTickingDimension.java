/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.dimension;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* // TODO Doc comment
 * Dimensions implementing this interface receive client side ticks from {@link ModernityClient}.
 */
@FunctionalInterface
public interface IClientTickingDimension {
    @OnlyIn( Dist.CLIENT )
    void tickClient();
}
