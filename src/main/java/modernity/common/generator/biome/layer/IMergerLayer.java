/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.*;

@FunctionalInterface
public interface IMergerLayer extends IBiomeLayer {
    int generate( IRegionRNG rng, IRegion regionA, IRegion regionB, int x, int z );

    default <R extends IRegion> IRegionFactory<R> factory( IRegionContext<R> ctx, long seed, IRegionFactory<R> regionFactoryA, IRegionFactory<R> regionFactoryB ) {
        return () -> {
            IRegionRNG rng = ctx.getRNG( seed );
            R regionA = regionFactoryA.buildRegion();
            R regionB = regionFactoryB.buildRegion();
            return ctx.create(
                (x, z) -> generate( rng.position( x, z ), regionA, regionB, x, z ),
                regionA, regionB
            );
        };
    }
}
