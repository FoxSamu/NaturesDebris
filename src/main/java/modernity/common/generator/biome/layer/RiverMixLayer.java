/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 08 - 2020
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
    private final int lake = id( MDBiomes.LAKE );
    private final int deepLake = id( MDBiomes.DEEP_LAKE );
    private final int undeepLake = id( MDBiomes.UNDEEP_LAKE );
    private final int grassLake = id( MDBiomes.GRASS_LAKE );
    private final int deepGrassLake = id( MDBiomes.DEEP_GRASS_LAKE );

    @Override
    public int generate( IRegionRNG rng, int original, int riverData ) {
        if( original == lake ) return original;
        if( original == deepLake ) return original;
        if( original == undeepLake ) return original;
        if( original == grassLake ) return original;
        if( original == deepGrassLake ) return original;

        if( riverData > 0 ) {
            return river;
        } else {
            return original;
        }
    }
}
