/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
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