/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate;

import modernity.common.biome.ModernityBiome;
import modernity.common.generator.MurkSurfaceGeneration;

public final class DefaultDecoration {
    private DefaultDecoration() {
    }

    @Deprecated
    public static void setupDefaultDecoration(ModernityBiome biome) {
        MurkSurfaceGeneration.addCaveDeposits(biome);
        MurkSurfaceGeneration.addCaveOres(biome);
        MurkSurfaceGeneration.addCavePlants(biome);
        MurkSurfaceGeneration.addCaveSprings(biome);
        MurkSurfaceGeneration.addClaySand(biome);
        MurkSurfaceGeneration.addPebbles(biome);
    }
}
