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
import modernity.common.generator.region.layer.IFilterMergerLayer;

public class RiverMixLayer implements IFilterMergerLayer, IBiomeLayer {
    private final BiomeMutationProfile mutations;

    public RiverMixLayer( BiomeMutationProfile mutations ) {
        this.mutations = mutations;
    }

    @Override
    public int generate( IRegionRNG rng, int original, int riverData ) {
        if( riverData > 0 ) {
            BiomeProfile profile = mutations.getProfile( original );
            if( profile == null ) return original;
            return profile.random( rng.random( profile.getTotalWeight() ) ).getBiomeID();
        } else {
            return original;
        }
    }
}
