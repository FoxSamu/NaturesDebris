/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.region.layer;

import modernity.common.generator.region.IRegion;
import modernity.common.generator.region.IRegionContext;
import modernity.common.generator.region.IRegionFactory;
import modernity.common.generator.region.IRegionRNG;

@FunctionalInterface
public interface ITransformerLayer {
    int generate( IRegionRNG rng, IRegion region, int x, int z );

    default <R extends IRegion> IRegionFactory<R> factory( IRegionContext<R> ctx, long seed, IRegionFactory<R> regionFactory ) {
        return () -> {
            IRegionRNG rng = ctx.getRNG( seed );
            R region = regionFactory.buildRegion();
            return ctx.create( ( x, z ) -> generate( rng.position( x, z ), region, x, z ), region );
        };
    }
}
