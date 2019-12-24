/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.biome;

import modernity.common.generator.terrain.surface.SurfaceGenSettings;
import net.minecraft.world.storage.WorldInfo;

/**
 * Settings for the {@link MDSurfaceBiomeProvider}. Currently, only the {@link WorldInfo} is used.
 */
public class MDSurfaceBiomeProviderSettings {
    private final WorldInfo worldInfo;
    private final SurfaceGenSettings settings;

    public MDSurfaceBiomeProviderSettings( WorldInfo worldInfo, SurfaceGenSettings settings ) {
        this.worldInfo = worldInfo;
        this.settings = settings;
    }

    /**
     * Returns the {@link WorldInfo} to feed to the {@link MDSurfaceBiomeProvider}.
     */
    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    /**
     * Returns the {@link SurfaceGenSettings} to feed to the {@link MDSurfaceBiomeProvider}.
     */
    public SurfaceGenSettings getSettings() {
        return settings;
    }
}