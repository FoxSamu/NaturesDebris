/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.biome.MDBiomes;
import modernity.common.generator.region.layer.IFilterMergerLayer;
import modernity.common.generator.region.IRegionRNG;

public class RiverMixLayer implements IFilterMergerLayer, IBiomeLayer {
    public static final RiverMixLayer INSTANCE = new RiverMixLayer();

    protected RiverMixLayer() {
    }

    private final int river = id( MDBiomes.RIVER );

    @Override
    public int generate( IRegionRNG rng, int a, int b ) {
        if( b > 0 ) {
            return river;
        } else {
            return a;
        }
    }
}
