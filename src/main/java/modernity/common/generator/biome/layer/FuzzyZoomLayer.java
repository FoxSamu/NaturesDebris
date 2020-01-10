/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegionRNG;

public class FuzzyZoomLayer extends ZoomLayer {
    public static final FuzzyZoomLayer INSTANCE = new FuzzyZoomLayer();

    protected FuzzyZoomLayer() {
    }

    @Override
    protected int pickRandom( IRegionRNG rng, int v00, int v01, int v10, int v11 ) {
        return rng.pickRandom( v00, v01, v10, v11 );
    }
}
