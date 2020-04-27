/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;

public class ProceduralChunkGenerator extends ChunkGenerator<ProceduralGenSettings<?>> {
    public ProceduralChunkGenerator( IWorld world, BiomeProvider biomeGen, ProceduralGenSettings<?> settings ) {
        super( world, biomeGen, settings );
        settings.setGenerator( this );
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
    public void func_225550_a_( BiomeManager p_225550_1_, IChunk chunk, GenerationStage.Carving carvingSettings ) {
    }

    @Override
    public void decorate( WorldGenRegion region ) {
        getSettings().decorate( region );
    }

    @Override
    public void func_225551_a_( WorldGenRegion p_225551_1_, IChunk p_225551_2_ ) {
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
