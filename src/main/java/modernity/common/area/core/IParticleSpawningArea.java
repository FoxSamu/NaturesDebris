/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
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
