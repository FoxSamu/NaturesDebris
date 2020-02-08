/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator;

import modernity.api.util.IIntScrambler;
import modernity.api.util.MDDimension;
import modernity.common.biome.MDBiomes;
import modernity.common.block.MDBlocks;
import modernity.common.generator.biome.LayerBiomeProvider;
import modernity.common.generator.biome.LayerBiomeProviderSettings;
import modernity.common.generator.biome.core.BiomeGenerator;
import modernity.common.generator.biome.core.CachingRegionContext;
import modernity.common.generator.biome.layer.*;
import modernity.common.generator.biome.profile.BiomeMutationProfile;
import modernity.common.generator.biome.profile.BiomeProfile;
import modernity.common.generator.biome.profile.DefaultBiomeRarity;
import modernity.common.generator.map.BedrockGenerator;
import modernity.common.generator.map.surface.*;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;
import net.redgalaxy.exc.InstanceOfUtilityClassException;

public final class SurfaceGeneration {
    public static final int BIOME_MIX_RADIUS = 3;
    public static final int BIOME_MIX_DIAMETER = BIOME_MIX_RADIUS * 2 + 1;
    public static final int MAIN_HEIGHT = 72;
    public static final int CAVE_WATER_LEVEL = 16;

    public static ChunkGenerator<?> buildChunkGenerator( IWorld world ) {
        BiomeProvider biomeGen = buildBiomeProvider( world );
        return new MapChunkGenerator(
            world,
            biomeGen,
            new MapGenSettings<SurfaceGenData>()
                .dataSupplier( SurfaceGenData::new )
                .addDecorator( SurfaceGeneration::decorate )
                .addGenerator( new TerrainGenerator( world, biomeGen ) )
                .addGenerator( new SurfaceGenerator( world ) )
                .addGenerator( new CaveGenerator( world ) )
                .addGenerator( new SumestoneGenerator( world ) )
                .addGenerator( new DarkrockGenerator( world ) )
                .addGenerator( new CanyonGenerator( world ) )
                .addGenerator( new BedrockGenerator( world, 0, 4, false, IIntScrambler.lgc( 52839319, 294282 ), MDBlocks.UNBREAKABLE_STONE ) )
                .addGenerator( new CaveDataGenerator( world ) )
        );
    }

    public static BiomeProvider buildBiomeProvider( IWorld world ) {
        return new LayerBiomeProvider(
            new LayerBiomeProviderSettings()
                .setGenerators( buildLayerProcedure( world.getSeed() ) )
                .setBiomes( MDBiomes.getBiomesFor( MDDimension.MURK_SURFACE ) )
        );
    }

    public static BiomeGenerator[] buildLayerProcedure( long seed ) {
        BiomeGenerator[] generators = new BiomeGenerator[ 2 ];

        BiomeProfile profile = buildBiomeProfile();

        CachingRegionContext context = new CachingRegionContext( 25, seed );
        context.generate( new BiomeBaseLayer( profile ), 4538L )
               .transform( new LargeBiomeLayer( profile ) )
               .zoom( 1 )
               .transform( new BiomeMutationLayer( buildLargeMutationProfile() ) )
               .zoom( 1 )
               .transform( new BiomeMutationLayer( buildMutationProfile() ) )
               .zoom( 3 )
               .transform( new BiomeMutationLayer( buildSmallMutationProfile() ) )
               .zoom( 1 )
               .merge(
                   RiverMixLayer.INSTANCE,
                   context.generate( RiverFieldLayer.INSTANCE, 5728L )
                          .zoom( 6 )
                          .transform( RiverLayer.INSTANCE )
                          .smooth()
               )
               .export( generators, 0 )
               .zoomVoronoi()
               .export( generators, 1 );

        return generators;
    }

    public static BiomeProfile buildBiomeProfile() {
        BiomeProfile profile = new BiomeProfile();

        profile.put( MDBiomes.MEADOW, DefaultBiomeRarity.VERY_COMMON, 0.4, 0.3 );
        profile.put( MDBiomes.FLOWER_MEADOW, DefaultBiomeRarity.RELATIVELY_UNCOMMON, 1, 0 );
        profile.put( MDBiomes.LUSH_MEADOW, DefaultBiomeRarity.UNCOMMON, 0, 0.8 );
        profile.put( MDBiomes.FOREST, DefaultBiomeRarity.COMMON, 0.4, 0.2 );
        profile.put( MDBiomes.SWAMP, DefaultBiomeRarity.RELATIVELY_COMMON, 1, 0 );
        profile.put( MDBiomes.WATERLANDS, DefaultBiomeRarity.RELATIVELY_COMMON, 0.9, 0.1 );

        return profile;
    }

    public static BiomeMutationProfile buildLargeMutationProfile() {
        BiomeMutationProfile profile = new BiomeMutationProfile();

        return profile;
    }

    public static BiomeMutationProfile buildMutationProfile() {
        BiomeMutationProfile profile = new BiomeMutationProfile();

        profile.putDefault( MDBiomes.MEADOW, 10 )
               .put( MDBiomes.MEADOW, MDBiomes.HIGH_MEADOW, 7 )
               .put( MDBiomes.MEADOW, MDBiomes.MEADOW_NO_TREES, 5 );

        profile.putDefault( MDBiomes.FLOWER_MEADOW, 10 )
               .put( MDBiomes.FLOWER_MEADOW, MDBiomes.HIGH_FLOWER_MEADOW, 7 )
               .put( MDBiomes.FLOWER_MEADOW, MDBiomes.FLOWER_MEADOW_NO_TREES, 5 );
        return profile;
    }

    public static BiomeMutationProfile buildSmallMutationProfile() {
        BiomeMutationProfile profile = new BiomeMutationProfile();

        return profile;
    }

    public static void decorate( WorldGenRegion region, ChunkGenerator<?> chunkGen ) {
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();
        int x = cx * 16;
        int z = cz * 16;
        BlockPos cornerPos = new BlockPos( x, 0, z );
        Biome biome = region.getChunk( cx, cz ).getBiome( cornerPos.add( 8, 0, 8 ) );
        SharedSeedRandom ssrand = new SharedSeedRandom();
        long seed = ssrand.setDecorationSeed( region.getSeed(), x, z );

        for( GenerationStage.Decoration stage : GenerationStage.Decoration.values() ) {
            try {
                biome.decorate( stage, chunkGen, region, seed, ssrand, cornerPos );
            } catch( Throwable exc ) {
                CrashReport report = CrashReport.makeCrashReport( exc, "Biome decoration" );
                report.makeCategory( "Generation" )
                      .addDetail( "CenterX", cx )
                      .addDetail( "CenterZ", cz )
                      .addDetail( "Step", stage )
                      .addDetail( "Seed", seed )
                      .addDetail( "Biome", biome.getRegistryName() );
                throw new ReportedException( report );
            }
        }
    }

    private SurfaceGeneration() {
        throw new InstanceOfUtilityClassException();
    }
}
