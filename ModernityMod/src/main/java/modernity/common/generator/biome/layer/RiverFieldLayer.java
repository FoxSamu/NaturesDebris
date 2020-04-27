/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.region.IRegionRNG;
import modernity.common.generator.region.layer.IGeneratorLayer;

public class RiverFieldLayer implements IGeneratorLayer, IBiomeLayer {
    public static final RiverFieldLayer INSTANCE = new RiverFieldLayer();

    protected RiverFieldLayer() {
    }

    @Override
    public int generate( IRegionRNG rng, int x, int z ) {
        return rng.random( 299999 ) + 2;
    }
}