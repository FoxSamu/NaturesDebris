/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.api.generator.fractal.layer;

import modernity.api.generator.fractal.IRegion;
import modernity.api.generator.fractal.IRegionContext;
import modernity.api.generator.fractal.IRegionFactory;
import modernity.api.generator.fractal.IRegionRNG;

@FunctionalInterface
public interface ITransformerLayer {
    int generate(IRegionRNG rng, IRegion region, int x, int z);

    default <R extends IRegion> IRegionFactory<R> factory(IRegionContext<R> ctx, long seed, IRegionFactory<R> regionFactory) {
        return () -> {
            IRegionRNG rng = ctx.getRNG(seed);
            R region = regionFactory.buildRegion();
            return ctx.create((x, z) -> generate(rng.position(x, z), region, x, z), region);
        };
    }
}
