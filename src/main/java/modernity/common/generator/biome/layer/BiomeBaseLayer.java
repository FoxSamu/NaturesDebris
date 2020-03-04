/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.region.layer.IGeneratorLayer;
import modernity.common.generator.region.IRegionRNG;
import modernity.common.generator.biome.profile.BiomeProfile;

public class BiomeBaseLayer implements IGeneratorLayer, IBiomeLayer {
    private final BiomeProfile profile;

    public BiomeBaseLayer( BiomeProfile profile ) {
        this.profile = profile;
    }

    @Override
    public int generate( IRegionRNG rng, int x, int z ) {
        BiomeProfile.Entry entry = profile.random( rng.random( profile.getTotalWeight() ) );
        return entry.getBiomeID();
    }
}
