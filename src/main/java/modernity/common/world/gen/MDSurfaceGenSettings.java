/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen;

import net.minecraft.world.gen.GenerationSettings;

/**
 * Chunk generation settings for the Modernity's surface dimension.
 */
public class MDSurfaceGenSettings extends GenerationSettings {
    public int getBiomeBlendRadius() {
        return 3;
    }

    public int getWaterLevel() {
        return 72;
    }
}
