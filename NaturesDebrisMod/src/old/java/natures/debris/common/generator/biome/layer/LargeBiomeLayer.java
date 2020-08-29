/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.biome.layer;

import modernity.api.generator.fractal.IRegion;
import modernity.api.generator.fractal.IRegionRNG;
import modernity.api.generator.fractal.layer.ITransformerLayer;
import natures.debris.common.generator.biome.profile.BiomeProfile;

public class LargeBiomeLayer implements ITransformerLayer, IBiomeLayer {
    private final BiomeProfile profile;

    public LargeBiomeLayer(BiomeProfile profile) {
        this.profile = profile;
    }

    @Override
    public int generate(IRegionRNG rng, IRegion region, int x, int z) {
        int xval = region.getValue(x - 1, z);
        int zval = region.getValue(x, z - 1);
        int xzval = region.getValue(x - 1, z - 1);

        int out = region.getValue(x, z);

        rng.setPosition(x - 1, z);
        if (isLarge(rng, xval)) {
            rng.setPosition(x, z);
            out = applyDominance(rng, out, xval);
        }

        rng.setPosition(x, z - 1);
        if (isLarge(rng, zval)) {
            rng.setPosition(x, z);
            rng.random(5);
            out = applyDominance(rng, out, zval);
        }

        rng.setPosition(x - 1, z - 1);
        if (isLarge(rng, xzval)) {
            rng.setPosition(x, z);
            rng.random(5);
            rng.random(5);
            out = applyDominance(rng, out, xzval);
        }

        return out;
    }

    private int applyDominance(IRegionRNG rng, int biome1, int biome2) {
        double dominance1 = profile.getEntry(biome1).getDominance();
        double dominance2 = profile.getEntry(biome2).getDominance();

        if (dominance1 == dominance2) {
            return rng.randomBool() ? biome1 : biome2;
        } else {
            return dominance1 > dominance2 ? biome1 : biome2;
        }
    }

    private boolean isLarge(IRegionRNG rng, int biome) {
        double chance = profile.getEntry(biome).getLargeChance();
        return rng.randomDouble() < chance;
    }
}