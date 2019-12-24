/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.terrain.surface;

import net.minecraft.world.gen.GenerationSettings;

/**
 * Chunk generation settings for the Modernity's surface dimension.
 */
public class SurfaceGenSettings extends GenerationSettings {
    public int getBiomeBlendRadius() {
        return 3;
    }

    public int getWaterLevel() {
        return 72;
    }
}
