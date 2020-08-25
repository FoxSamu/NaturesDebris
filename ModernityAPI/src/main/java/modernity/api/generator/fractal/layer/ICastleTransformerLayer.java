/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.api.generator.fractal.layer;

import modernity.api.generator.fractal.IRegion;
import modernity.api.generator.fractal.IRegionRNG;

@FunctionalInterface
public interface ICastleTransformerLayer extends ITransformerLayer {
    @Override
    default int generate(IRegionRNG rng, IRegion region, int x, int z) {
        return generate(
            rng,
            region.getValue(x, z),
            region.getValue(x - 1, z),
            region.getValue(x + 1, z),
            region.getValue(x, z - 1),
            region.getValue(x, z + 1)
        );
    }

    int generate(IRegionRNG rng, int center, int negX, int posX, int negZ, int posZ);
}
