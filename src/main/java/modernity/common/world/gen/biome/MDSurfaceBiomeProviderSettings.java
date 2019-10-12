package modernity.common.world.gen.biome;

import modernity.common.world.gen.MDSurfaceGenSettings;
import net.minecraft.world.storage.WorldInfo;

public class MDSurfaceBiomeProviderSettings {
    private final WorldInfo worldInfo;
    private final MDSurfaceGenSettings settings;

    public MDSurfaceBiomeProviderSettings( WorldInfo worldInfo, MDSurfaceGenSettings settings ) {
        this.worldInfo = worldInfo;
        this.settings = settings;
    }

    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    public MDSurfaceGenSettings getSettings() {
        return settings;
    }
}