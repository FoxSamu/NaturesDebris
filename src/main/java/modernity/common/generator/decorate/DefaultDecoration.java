/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate;

import modernity.common.biome.ModernityBiome;
import modernity.common.generator.SurfaceGeneration;

public final class DefaultDecoration {
    private DefaultDecoration() {
    }

    @Deprecated
    public static void setupDefaultDecoration( ModernityBiome biome ) {
        SurfaceGeneration.addCaveDeposits( biome );
        SurfaceGeneration.addCaveOres( biome );
        SurfaceGeneration.addCavePlants( biome );
        SurfaceGeneration.addCaveSprings( biome );
        SurfaceGeneration.addClaySand( biome );
        SurfaceGeneration.addPebbles( biome );
    }
}
