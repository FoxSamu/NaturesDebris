/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;

public class MapChunkGenerator extends ChunkGenerator<MapGenSettings<?>> {
    public MapChunkGenerator( IWorld world, BiomeProvider biomeGen, MapGenSettings<?> settings ) {
        super( world, biomeGen, settings );
        settings.setGenerator( this );
    }

    @Override
    public void generateSurface( IChunk chunk ) {
    }

    @Override
    public int getGroundHeight() {
        return 72;
    }

    @Override
    public void makeBase( IWorld world, IChunk chunk ) {
        WorldGenRegion region = (WorldGenRegion) world;
        getSettings().makeBase( region );
    }

    @Override
    public void carve( IChunk chunk, GenerationStage.Carving carvingSettings ) {
    }

    @Override
    public void decorate( WorldGenRegion region ) {
        getSettings().decorate( region );
    }

    public int getHeight( int x, int z, Heightmap.Type type ) {
        return world.getHeight( type, x, z );
    }

    /**
     * @deprecated Use {@link #getHeight}...
     */
    @Override
    @Deprecated
    public int func_222529_a( int x, int z, Heightmap.Type type ) {
        return getHeight( x, z, type );
    }
}
