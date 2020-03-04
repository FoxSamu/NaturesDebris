/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.region.layer.IFilterTransformerLayer;
import modernity.common.generator.region.IRegionRNG;
import modernity.common.generator.biome.profile.BiomeMutationProfile;
import modernity.common.generator.biome.profile.BiomeProfile;

public class BiomeMutationLayer implements IFilterTransformerLayer, IBiomeLayer {
    private final BiomeMutationProfile profile;

    public BiomeMutationLayer( BiomeMutationProfile profile ) {
        this.profile = profile;
    }

    @Override
    public int generate( IRegionRNG rng, int value ) {
        BiomeProfile mutations = profile.getProfile( value );
        if( mutations == null ) return value;
        return mutations.random( rng.random( mutations.getTotalWeight() ) ).getBiomeID();
    }
}
