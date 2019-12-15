/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 15 - 2019
 * Author: rgsw
 */

package modernity.common.area.core;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.redgalaxy.MathUtil;

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
