/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegionRNG;
import modernity.common.generator.biome.core.IRegion;

@FunctionalInterface
public interface IFilterTransformerLayer extends ITransformerLayer {
    @Override
    default int generate( IRegionRNG rng, IRegion region, int x, int z ) {
        return generate( rng, region.getValue( x, z ) );
    }

    int generate( IRegionRNG rng, int value );
}
