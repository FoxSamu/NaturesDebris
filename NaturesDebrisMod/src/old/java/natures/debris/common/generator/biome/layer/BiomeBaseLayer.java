/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.biome.layer;

import modernity.api.generator.fractal.IRegionRNG;
import modernity.api.generator.fractal.layer.IGeneratorLayer;
import natures.debris.common.generator.biome.profile.BiomeProfile;

public class BiomeBaseLayer implements IGeneratorLayer, IBiomeLayer {
    private final BiomeProfile profile;

    public BiomeBaseLayer(BiomeProfile profile) {
        this.profile = profile;
    }

    @Override
    public int generate(IRegionRNG rng, int x, int z) {
        BiomeProfile.Entry entry = profile.random(rng.random(profile.getTotalWeight()));
        return entry.getBiomeID();
    }
}
