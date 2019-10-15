package modernity.common.world.gen.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationSettings;
import net.rgsw.noise.INoise3D;

import java.util.Random;

/**
 * Default surface generator interface.
 *
 * @param <T> The chunk generator settings.
 */
@FunctionalInterface
public interface ISurfaceGenerator<T extends GenerationSettings> {
    default void init( Random rand, T settings ) {
    }

    /**
     * Generates the surface for the specified biome.
     *
     * @param chunk        The chunk to generate in.
     * @param cx           The chunk x.
     * @param cz           The chunk z.
     * @param x            The block x to generate at.
     * @param z            The block z to generate at.
     * @param rand         A random number generator.
     * @param biome        The biome to generate the surface of.
     * @param surfaceNoise A noise generator that generates the surface depths.
     * @param mpos         A {@link MovingBlockPos} to reuse.
     * @param settings     The chunk generator settings of the chunk generator that generates the surface.
     */
    void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos mpos, T settings );
}
