/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.surface;

import modernity.api.util.EcoBlockPos;
import modernity.common.biome.BiomeBase;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.IChunkGenSettings;
import net.rgsw.noise.FractalOpenSimplex3D;

import java.util.Random;

public interface ISurfaceGenerator <S extends IChunkGenSettings> {
    default void init( Random rand, S settings ) {}
    void generateSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, BiomeBase biome, FractalOpenSimplex3D surfaceNoise, EcoBlockPos rpos, S settings );
}
