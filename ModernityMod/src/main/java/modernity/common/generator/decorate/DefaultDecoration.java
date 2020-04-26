/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate;

import modernity.common.biome.ModernityBiome;
import modernity.common.generator.MurkSurfaceGeneration;

public final class DefaultDecoration {
    private DefaultDecoration() {
    }

    @Deprecated
    public static void setupDefaultDecoration( ModernityBiome biome ) {
        MurkSurfaceGeneration.addCaveDeposits( biome );
        MurkSurfaceGeneration.addCaveOres( biome );
        MurkSurfaceGeneration.addCavePlants( biome );
        MurkSurfaceGeneration.addCaveSprings( biome );
        MurkSurfaceGeneration.addClaySand( biome );
        MurkSurfaceGeneration.addPebbles( biome );
    }
}
