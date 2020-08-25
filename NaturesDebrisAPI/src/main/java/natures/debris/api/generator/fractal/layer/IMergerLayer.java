/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.api.generator.fractal.layer;

import natures.debris.api.generator.fractal.IRegion;
import natures.debris.api.generator.fractal.IRegionContext;
import natures.debris.api.generator.fractal.IRegionFactory;
import natures.debris.api.generator.fractal.IRegionRNG;

@FunctionalInterface
public interface IMergerLayer {
    int generate(IRegionRNG rng, IRegion regionA, IRegion regionB, int x, int z);

    default <R extends IRegion> IRegionFactory<R> factory(IRegionContext<R> ctx, long seed, IRegionFactory<R> regionFactoryA, IRegionFactory<R> regionFactoryB) {
        return () -> {
            IRegionRNG rng = ctx.getRNG(seed);
            R regionA = regionFactoryA.buildRegion();
            R regionB = regionFactoryB.buildRegion();
            return ctx.create(
                (x, z) -> generate(rng.position(x, z), regionA, regionB, x, z),
                regionA, regionB
            );
        };
    }
}
