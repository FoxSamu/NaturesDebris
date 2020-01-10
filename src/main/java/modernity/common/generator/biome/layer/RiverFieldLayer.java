/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegionRNG;

public class RiverFieldLayer implements IGeneratorLayer {
    public static final RiverFieldLayer INSTANCE = new RiverFieldLayer();

    protected RiverFieldLayer() {
    }

    @Override
    public int generate( IRegionRNG rng, int x, int z ) {
        return rng.random( 299999 ) + 2;
    }
}