/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.biome.layer;

import modernity.api.generator.fractal.IRegionRNG;
import modernity.api.generator.fractal.layer.IFilterTransformerLayer;
import natures.debris.common.generator.biome.profile.BiomeMutationProfile;
import natures.debris.common.generator.biome.profile.BiomeProfile;

public class BiomeMutationLayer implements IFilterTransformerLayer, IBiomeLayer {
    private final BiomeMutationProfile profile;

    public BiomeMutationLayer(BiomeMutationProfile profile) {
        this.profile = profile;
    }

    @Override
    public int generate(IRegionRNG rng, int value) {
        BiomeProfile mutations = profile.getProfile(value);
        if (mutations == null) return value;
        return mutations.random(rng.random(mutations.getTotalWeight())).getBiomeID();
    }
}
