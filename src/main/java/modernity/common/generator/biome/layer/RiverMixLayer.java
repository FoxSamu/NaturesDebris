/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.biome.MDBiomes;
import modernity.common.generator.biome.core.IRegionRNG;

public class RiverMixLayer implements IFilterMergerLayer {
    public static final RiverMixLayer INSTANCE = new RiverMixLayer();

    protected RiverMixLayer() {
    }

    private final int river = biomeID( MDBiomes.RIVER );

    @Override
    public int generate( IRegionRNG rng, int a, int b ) {
        if( b > 0 ) {
            return river;
        } else {
            return a;
        }
    }
}
