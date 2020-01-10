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
public interface IGeneratorLayer extends IBiomeLayer {
    int generate( IRegionRNG rng, int x, int z );

    default <R extends IRegion> IRegionFactory<R> factory( IRegionContext<R> ctx, long seed ) {
        return () -> {
            IRegionRNG rng = ctx.getRNG( seed );
            return ctx.create( (x, z) -> generate( rng.position( x, z ), x, z ) );
        };
    }
}
