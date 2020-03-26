/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.region.layer;

import modernity.common.generator.region.IRegion;
import modernity.common.generator.region.IRegionContext;
import modernity.common.generator.region.IRegionFactory;
import modernity.common.generator.region.IRegionRNG;

@FunctionalInterface
public interface IMergerLayer {
    int generate( IRegionRNG rng, IRegion regionA, IRegion regionB, int x, int z );

    default <R extends IRegion> IRegionFactory<R> factory( IRegionContext<R> ctx, long seed, IRegionFactory<R> regionFactoryA, IRegionFactory<R> regionFactoryB ) {
        return () -> {
            IRegionRNG rng = ctx.getRNG( seed );
            R regionA = regionFactoryA.buildRegion();
            R regionB = regionFactoryB.buildRegion();
            return ctx.create(
                ( x, z ) -> generate( rng.position( x, z ), regionA, regionB, x, z ),
                regionA, regionB
            );
        };
    }
}
