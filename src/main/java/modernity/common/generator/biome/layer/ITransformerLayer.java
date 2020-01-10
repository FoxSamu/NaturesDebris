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
public interface ITransformerLayer extends IBiomeLayer {
    int generate( IRegionRNG rng, IRegion region, int x, int z );

    default <R extends IRegion> IRegionFactory<R> factory( IRegionContext<R> ctx, long seed, IRegionFactory<R> regionFactory ) {
        return () -> {
            IRegionRNG rng = ctx.getRNG( seed );
            R region = regionFactory.buildRegion();
            return ctx.create( (x, z) -> generate( rng.position( x, z ), region, x, z ), region );
        };
    }
}
