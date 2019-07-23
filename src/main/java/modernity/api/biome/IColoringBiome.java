/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.api.biome;

import net.minecraft.util.math.BlockPos;

public interface IColoringBiome {
    int getMDWaterColor( BlockPos pos );
    int getGrassColor( BlockPos pos );
    int getFoliageColor( BlockPos pos );
    int getFogColor( BlockPos pos );

    float getFogDensity();
    float getWaterFogDensity();
}
