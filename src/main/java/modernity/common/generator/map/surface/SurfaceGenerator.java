/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.map.surface;

import modernity.api.util.IIntScrambler;
import modernity.common.block.MDBlocks;
import modernity.common.generator.map.BedrockGenerator;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;

/**
 * Chunk generator that combines all chunk generation of the Modernity's surface dimension.
 */
public class SurfaceGenerator extends ChunkGenerator<GenerationSettings> {
    private final ThreadLocal<SurfaceGenData> dataLocal = ThreadLocal.withInitial( SurfaceGenData::new );

    private final TerrainGenerator terrain;
    private final SurfaceBuilder surface;
    private final CaveGenerator caves;
    private final DarkrockGenerator darkrock;
    private final CanyonGenerator canyons;
    private final BedrockGenerator bedrock;
    private final CaveDataGenerator caveData;

    public SurfaceGenerator( IWorld world, BiomeProvider biomeProvider ) {
        super( world, biomeProvider, new GenerationSettings() );

        terrain = new TerrainGenerator( world, biomeProvider );
        surface = new SurfaceBuilder( world );
        caves = new CaveGenerator( world );
        darkrock = new DarkrockGenerator( world );
        canyons = new CanyonGenerator( world );
        bedrock = new BedrockGenerator( world, 0, 4, false, IIntScrambler.lgc( 52839319, 294282 ), MDBlocks.UNBREAKABLE_STONE );
        caveData = new CaveDataGenerator( world );
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
        SurfaceGenData data = dataLocal.get();
        data.init( this );

        WorldGenRegion region = (WorldGenRegion) world;

        terrain.generate( region, data );
        bedrock.generate( region, data );
        surface.generate( region, data );
        caves.generate( region, data );
        canyons.generate( region, data );
        darkrock.generate( region, data );
        caveData.generate( region, data );
    }

    @Override
    public void carve( IChunk chunk, GenerationStage.Carving carvingSettings ) {
    }

    @Override
    public void decorate( WorldGenRegion region ) {
//        int cx = region.getMainChunkX();
//        int cz = region.getMainChunkZ();
//        int x = cx * 16;
//        int z = cz * 16;
//        BlockPos cornerPos = new BlockPos( x, 0, z );
//        Biome biome = region.getChunk( cx, cz ).getBiome( cornerPos.add( 8, 0, 8 ) );
//        SharedSeedRandom ssrand = new SharedSeedRandom();
//        long seed = ssrand.setDecorationSeed( region.getSeed(), x, z );
//
//        for( GenerationStage.Decoration stage : GenerationStage.Decoration.values() ) {
//            try {
//                biome.decorate( stage, this, region, seed, ssrand, cornerPos );
//            } catch( Throwable exc ) {
//                CrashReport report = CrashReport.makeCrashReport( exc, "Biome decoration" );
//                report.makeCategory( "Generation" )
//                           .addDetail( "CenterX", cx )
//                           .addDetail( "CenterZ", cz )
//                           .addDetail( "Step", stage )
//                           .addDetail( "Seed", seed )
//                           .addDetail( "Biome", biome.getRegistryName() );
//                throw new ReportedException( report );
//            }
//        }
    }

    public int getHeight( int x, int z, Heightmap.Type type ) {
        return world.getHeight( type, x, z );
    }

    @Override
    public int func_222529_a( int x, int z, Heightmap.Type type ) {
        return getHeight( x, z, type );
    }

    public static class Factory implements IChunkGeneratorFactory<GenerationSettings, SurfaceGenerator> {

        @Override
        public SurfaceGenerator create( World world, BiomeProvider biomeProvider, GenerationSettings settings ) {
            return new SurfaceGenerator( world, biomeProvider );
        }
    }
}
