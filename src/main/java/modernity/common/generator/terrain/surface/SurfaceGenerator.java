/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.terrain.surface;

import modernity.api.util.IntArrays;
import modernity.common.generator.map.DarkrockGenerator;
import modernity.common.generator.structure.MDStructures;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;

/**
 * Chunk generator that combines all chunk generation of the Modernity's surface dimension.
 */
public class SurfaceGenerator extends ChunkGenerator<SurfaceGenSettings> {
    private final SurfaceTerrainGenerator terrain;
    private final SurfaceSurfaceBuilder surface;
    private final SurfaceCaveGenerator cave;
    private final SurfaceDecorator decorator;

    private final DarkrockGenerator darkrockGenerator;
    private final SurfaceCanyonGenerator canyonGenerator;

    public SurfaceGenerator( IWorld world, BiomeProvider biomeProvider, SurfaceGenSettings settings ) {
        super( world, biomeProvider, settings );

        terrain = new SurfaceTerrainGenerator( world, biomeProvider, settings );
        surface = new SurfaceSurfaceBuilder( world, biomeProvider, settings );
        cave = new SurfaceCaveGenerator( world, biomeProvider, settings );
        decorator = new SurfaceDecorator( world, biomeProvider, this, settings );
        darkrockGenerator = new DarkrockGenerator( world );
        canyonGenerator = new SurfaceCanyonGenerator( world );
    }

    public SurfaceGenerator( IWorld world, BiomeProvider provider ) {
        this( world, provider, new SurfaceGenSettings() );
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

        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;

        terrain.generateTerrain( chunk );
        int[] hm = surface.buildSurface( chunk );
        cave.generateCaves( chunk, hm );
        canyonGenerator.generate( region, hm );

        IntArrays.add( hm, - 8 );

        MDStructures.CAVE.addCaves( this, chunk, cx, cz, hm, world.getSeed() );

        darkrockGenerator.generate( region );
    }

    @Override
    public void carve( IChunk chunk, GenerationStage.Carving carvingSettings ) {
    }

    @Override
    public void decorate( WorldGenRegion region ) {
        decorator.decorate( region );
    }

    public int getHeight( int x, int z, Heightmap.Type type ) {
        return world.getHeight( type, x, z );
    }

    @Override
    public int func_222529_a( int x, int z, Heightmap.Type type ) {
        return getHeight( x, z, type );
    }

    public static class Factory implements IChunkGeneratorFactory<SurfaceGenSettings, SurfaceGenerator> {

        @Override
        public SurfaceGenerator create( World world, BiomeProvider biomeProvider, SurfaceGenSettings settings ) {
            return new SurfaceGenerator( world, biomeProvider, settings );
        }
    }
}
