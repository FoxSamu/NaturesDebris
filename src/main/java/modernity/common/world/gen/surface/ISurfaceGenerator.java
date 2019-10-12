package modernity.common.world.gen.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationSettings;
import net.rgsw.noise.INoise3D;

import java.util.Random;

@FunctionalInterface
public interface ISurfaceGenerator<T extends GenerationSettings> {
    default void init( Random rand, T settings ) {
    }

    void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos mpos, T settings );
}
