/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.biome.layer;

import modernity.api.generator.fractal.IRegionRNG;
import modernity.api.generator.fractal.layer.IGeneratorLayer;
import natures.debris.common.biome.MDBiomes;
import natures.debris.generic.util.MDDimension;

public class BiomeGenerationLayer implements IGeneratorLayer, IBiomeLayer {

    private final MDBiomes.GenProfile profile;

    public BiomeGenerationLayer(MDDimension dimension) {
        profile = MDBiomes.createGenProfile(dimension);
    }

    @Override
    public int generate(IRegionRNG rng, int x, int z) {
        int rand = rng.random(profile.totalWeight);
        int wg = 0;
        int biomeID = -1;
        for (int i = 0; i < profile.biomeIDs.length; i++) {
            wg += profile.weights[i];
            if (wg > rand) {
                biomeID = profile.biomeIDs[i];
                break;
            }
        }
        return biomeID;
    }
}
