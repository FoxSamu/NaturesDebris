/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 10 - 2020
 * Author: rgsw
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
