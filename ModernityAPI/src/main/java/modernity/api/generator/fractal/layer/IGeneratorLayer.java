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
public interface IGeneratorLayer {
    int generate(IRegionRNG rng, int x, int z);

    default <R extends IRegion> IRegionFactory<R> factory(IRegionContext<R> ctx, long seed) {
        return () -> {
            IRegionRNG rng = ctx.getRNG(seed);
            return ctx.create((x, z) -> generate(rng.position(x, z), x, z));
        };
    }
}
