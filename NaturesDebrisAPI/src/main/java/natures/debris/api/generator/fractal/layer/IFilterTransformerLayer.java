/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.api.generator.fractal.layer;

import natures.debris.api.generator.fractal.IRegion;
import natures.debris.api.generator.fractal.IRegionRNG;

@FunctionalInterface
public interface IFilterTransformerLayer extends ITransformerLayer {
    @Override
    default int generate(IRegionRNG rng, IRegion region, int x, int z) {
        return generate(rng, region.getValue(x, z));
    }

    int generate(IRegionRNG rng, int value);
}
