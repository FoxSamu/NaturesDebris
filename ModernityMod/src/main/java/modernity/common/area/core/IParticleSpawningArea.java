/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.area.core;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.redgalaxy.util.MathUtil;

import java.util.Random;

@FunctionalInterface
public interface IParticleSpawningArea {
    @OnlyIn( Dist.CLIENT )
    void particleTick( Random rand );

    default double spawningFallofFunction( double distance ) {
        if( distance == 0 ) return 1;
        double r = falloffRange();
        return MathUtil.clamp( ( r - distance ) / r, 0, 1 );
    }

    default double falloffRange() {
        return 20;
    }
}
