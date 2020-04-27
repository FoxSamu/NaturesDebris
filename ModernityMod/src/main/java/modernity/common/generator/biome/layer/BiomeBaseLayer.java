/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.profile.BiomeProfile;
import modernity.common.generator.region.IRegionRNG;
import modernity.common.generator.region.layer.IGeneratorLayer;

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
