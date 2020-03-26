/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.rgsw.noise.FractalPerlin3D;

import java.util.Random;

/**
 * Surface builder that wraps {@link ISurfaceGenerator}s.
 */
public class WrapperSurfaceBuilder extends SurfaceBuilder<WrapperSurfaceBuilderConfig> {
    public WrapperSurfaceBuilder() {
        super( dynamic -> new WrapperSurfaceBuilderConfig( null ) );
    }

    @Override
    public void buildSurface( Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, WrapperSurfaceBuilderConfig config ) {
        ISurfaceGenerator gen = config.getSurfaceGen();
        FractalPerlin3D surfaceNoise = new FractalPerlin3D( (int) seed, 6.348456, 0.52, 6.348456, 6 );

        gen.init( new Random( seed ) );
        gen.buildSurface( chunk, chunk.getPos().x, chunk.getPos().z, x, z, random, (ModernityBiome) biome, surfaceNoise, new MovingBlockPos() );
    }
}
