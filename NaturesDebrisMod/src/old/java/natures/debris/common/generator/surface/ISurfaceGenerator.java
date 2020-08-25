/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.surface;

import natures.debris.common.biome.ModernityBiome;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.rgsw.noise.INoise3D;

import java.util.Random;

/**
 * Default surface generator interface.
 */
@FunctionalInterface
public interface ISurfaceGenerator {
    default SurfaceBuilderConfig createBuilderConfig() {
        return SurfaceBuilder.AIR_CONFIG;
    }

    default void init(Random rand) {
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
     */
    void buildSurface(IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos mpos);
}
