package modernity.common.world.gen.biome;

import modernity.common.world.gen.MDSurfaceGenSettings;
import net.minecraft.world.storage.WorldInfo;

/**
 * Settings for the {@link MDSurfaceBiomeProvider}. Currently, only the {@link WorldInfo} is used.
 */
public class MDSurfaceBiomeProviderSettings {
    private final WorldInfo worldInfo;
    private final MDSurfaceGenSettings settings;

    public MDSurfaceBiomeProviderSettings( WorldInfo worldInfo, MDSurfaceGenSettings settings ) {
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
     * Returns the {@link MDSurfaceGenSettings} to feed to the {@link MDSurfaceBiomeProvider}.
     */
    public MDSurfaceGenSettings getSettings() {
        return settings;
    }
}