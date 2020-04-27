/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.profile.BiomeMutationProfile;
import modernity.common.generator.biome.profile.BiomeProfile;
import modernity.common.generator.region.IRegionRNG;
import modernity.common.generator.region.layer.IFilterTransformerLayer;

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
