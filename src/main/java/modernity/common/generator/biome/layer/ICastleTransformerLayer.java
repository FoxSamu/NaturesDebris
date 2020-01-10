/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegion;
import modernity.common.generator.biome.core.IRegionRNG;

@FunctionalInterface
public interface ICastleTransformerLayer extends ITransformerLayer {
    @Override
    default int generate( IRegionRNG rng, IRegion region, int x, int z ) {
        return generate(
            rng,
            region.getValue( x, z ),
            region.getValue( x - 1, z ),
            region.getValue( x + 1, z ),
            region.getValue( x, z - 1 ),
            region.getValue( x, z + 1 )
        );
    }

    int generate( IRegionRNG rng, int center, int negX, int posX, int negZ, int posZ );
}
