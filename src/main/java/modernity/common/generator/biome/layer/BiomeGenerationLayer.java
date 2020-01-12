/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.api.util.MDDimension;
import modernity.common.biome.MDBiomes;
import modernity.common.generator.biome.core.IRegionRNG;

public class BiomeGenerationLayer implements IGeneratorLayer {

    private final MDBiomes.GenProfile profile;

    public BiomeGenerationLayer( MDDimension dimension ) {
        profile = MDBiomes.createGenProfile( dimension );
    }

    @Override
    public int generate( IRegionRNG rng, int x, int z ) {
        int rand = rng.random( profile.totalWeight );
        int wg = 0;
        int biomeID = - 1;
        for( int i = 0; i < profile.biomeIDs.length; i++ ) {
            wg += profile.weights[ i ];
            if( wg > rand ) {
                biomeID = profile.biomeIDs[ i ];
                break;
            }
        }
        return biomeID;
    }
}