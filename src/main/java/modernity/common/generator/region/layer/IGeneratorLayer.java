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
public interface IGeneratorLayer {
    int generate( IRegionRNG rng, int x, int z );

    default <R extends IRegion> IRegionFactory<R> factory( IRegionContext<R> ctx, long seed ) {
        return () -> {
            IRegionRNG rng = ctx.getRNG( seed );
            return ctx.create( ( x, z ) -> generate( rng.position( x, z ), x, z ) );
        };
    }
}