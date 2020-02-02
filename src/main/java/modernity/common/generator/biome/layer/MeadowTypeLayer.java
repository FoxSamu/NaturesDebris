/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.biome.MDBiomes;
import modernity.common.generator.biome.core.IRegionRNG;

public class MeadowTypeLayer implements IFilterTransformerLayer {

    private final int meadowID = biomeID( MDBiomes.MEADOW );
    private final int flowerMeadowID = biomeID( MDBiomes.FLOWER_MEADOW );
    private final int meadowNoTreesID = biomeID( MDBiomes.MEADOW_NO_TREES );

    @Override
    public int generate( IRegionRNG rng, int value ) {
        if( value == meadowID ) {
            int rand = rng.random( 4 );
            if( rand == 0 ) return flowerMeadowID;
            if( rand == 1 ) return meadowNoTreesID;
            else return meadowID;
        }
        return value;
    }
}
