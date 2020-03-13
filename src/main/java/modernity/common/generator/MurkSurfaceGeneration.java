/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.generator;

import modernity.api.util.BlockPredicates;
import modernity.api.util.IIntScrambler;
import modernity.api.util.MDDimension;
import modernity.common.biome.MDBiomes;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.generator.biome.BiomeGenerator;
import modernity.common.generator.biome.LayerBiomeProvider;
import modernity.common.generator.biome.LayerBiomeProviderSettings;
import modernity.common.generator.biome.layer.*;
import modernity.common.generator.biome.profile.BiomeMutationProfile;
import modernity.common.generator.biome.profile.BiomeProfile;
import modernity.common.generator.biome.profile.DefaultBiomeRarity;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.generator.decorate.condition.IsBelowHeight;
import modernity.common.generator.decorate.count.Chance;
import modernity.common.generator.decorate.count.Fixed;
import modernity.common.generator.decorate.decoration.*;
import modernity.common.generator.decorate.decorator.DecorationDecorator;
import modernity.common.generator.decorate.position.BelowHeight;
import modernity.common.generator.decorate.position.BetweenHeight;
import modernity.common.generator.decorate.position.InCave;
import modernity.common.generator.decorate.position.Surface;
import modernity.common.generator.map.BedrockGenerator;
import modernity.common.generator.map.surface.*;
import modernity.common.generator.region.CachingRegionContext;
import modernity.common.generator.region.IRegionRNG;
import modernity.common.generator.region.layer.IEdgeTransformerLayer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.redgalaxy.exc.InstanceOfUtilityClassException;

import static modernity.api.util.BlockPredicates.*;

/**
 * General utility class for generating the Murk Surface dimension.
 */
public final class MurkSurfaceGeneration {
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
                .addDecorator( MurkSurfaceGeneration::decorate )
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
               .zoomFuzzy()
               .transform( new BiomeMutationLayer( buildLargeMutationProfile() ) )
               .zoomFuzzy()
               .transform( new BiomeMutationLayer( buildMutationProfile() ) )
               .zoom()
               .transform( new LushGrasslandEdgeLayer() )
               .transform( new LakeEdgeLayer() )
               .zoom()
               .transform( new BiomeMutationLayer( buildSmallMutationProfile() ) )
               .zoom()
               .transform( new BiomeMutationLayer( buildTinyMutationProfile() ) )
               .zoomFuzzy()
               .smooth()
               .merge(
                   new RiverMixLayer( buildRiverMutationProfile() ),
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

        profile.put( MDBiomes.MEADOW, DefaultBiomeRarity.VERY_COMMON, 0.3, 0.3 );
        profile.put( MDBiomes.FLOWER_MEADOW, DefaultBiomeRarity.RELATIVELY_UNCOMMON, 1, 0 );
        profile.put( MDBiomes.LUSH_GRASSLAND, DefaultBiomeRarity.UNCOMMON, 0.3, 0.8 );
        profile.put( MDBiomes.FOREST, DefaultBiomeRarity.COMMON, 0.3, 0.2 );
        profile.put( MDBiomes.TALL_FOREST, DefaultBiomeRarity.RELATIVELY_RARE, 1, 1 );
        profile.put( MDBiomes.SWAMP, DefaultBiomeRarity.RELATIVELY_COMMON, 1, 0 );
        profile.put( MDBiomes.WETLAND, DefaultBiomeRarity.RELATIVELY_COMMON, 0.8, 0.1 );
        profile.put( MDBiomes.LAKE, DefaultBiomeRarity.RELATIVELY_UNCOMMON, 0.9, 0.3 );
        profile.put( MDBiomes.MOORLAND, DefaultBiomeRarity.RELATIVELY_UNCOMMON, 0.4, 0.5 );
        profile.put( MDBiomes.SHRUBLAND, DefaultBiomeRarity.RELATIVELY_UNCOMMON, 0.4, 0.5 );

        return profile;
    }

    public static BiomeMutationProfile buildLargeMutationProfile() {
        BiomeMutationProfile profile = new BiomeMutationProfile();

        return profile;
    }

    public static BiomeMutationProfile buildRiverMutationProfile() {
        BiomeMutationProfile profile = new BiomeMutationProfile();

        profile.put( MDBiomes.MEADOW, MDBiomes.MEADOW_RIVER, 1 )
               .put( MDBiomes.HIGH_MEADOW, MDBiomes.MEADOW_RIVER, 1 )
               .put( MDBiomes.MEADOW_NO_TREES, MDBiomes.MEADOW_RIVER, 1 );

        profile.put( MDBiomes.FLOWER_MEADOW, MDBiomes.FLOWER_MEADOW_RIVER, 1 )
               .put( MDBiomes.HIGH_FLOWER_MEADOW, MDBiomes.FLOWER_MEADOW_RIVER, 1 )
               .put( MDBiomes.FLOWER_MEADOW_NO_TREES, MDBiomes.FLOWER_MEADOW_RIVER, 1 );

        profile.put( MDBiomes.MOORLAND, MDBiomes.MOORLAND_RIVER, 1 )
               .put( MDBiomes.HIGH_MOORLAND, MDBiomes.MOORLAND_RIVER, 1 )
               .put( MDBiomes.MOORLAND_NO_TREES, MDBiomes.MOORLAND_RIVER, 1 );

        profile.put( MDBiomes.SHRUBLAND, MDBiomes.SHRUBLAND_RIVER, 1 )
               .put( MDBiomes.SHRUBLAND_HEATH, MDBiomes.SHRUBLAND_RIVER, 1 )
               .put( MDBiomes.SHRUBLAND_GRASS, MDBiomes.SHRUBLAND_RIVER, 1 )
               .put( MDBiomes.SHRUBLAND_SAND, MDBiomes.SHRUBLAND_RIVER, 1 )
               .put( MDBiomes.SHRUBLAND_TREES, MDBiomes.SHRUBLAND_RIVER, 1 )
               .put( MDBiomes.SHRUBLAND_REEDS, MDBiomes.SHRUBLAND_RIVER, 1 );

        profile.put( MDBiomes.LUSH_GRASSLAND, MDBiomes.LUSH_GRASSLAND_RIVER, 1 )
               .put( MDBiomes.HIGH_LUSH_GRASSLAND, MDBiomes.LUSH_GRASSLAND_RIVER, 1 )
               .put( MDBiomes.LUSH_GRASSLAND_OPEN, MDBiomes.LUSH_GRASSLAND_RIVER, 1 )
               .put( MDBiomes.HIGH_LUSH_GRASSLAND_OPEN, MDBiomes.LUSH_GRASSLAND_RIVER, 1 )
               .put( MDBiomes.LUSH_GRASSLAND_EDGE, MDBiomes.LUSH_GRASSLAND_RIVER, 1 );

        profile.put( MDBiomes.FOREST, MDBiomes.FOREST_RIVER, 1 )
               .put( MDBiomes.HIGH_FOREST, MDBiomes.FOREST_RIVER, 1 )
               .put( MDBiomes.BUSH_FOREST, MDBiomes.FOREST_RIVER, 1 )
               .put( MDBiomes.HIGH_OPEN_FOREST, MDBiomes.FOREST_RIVER, 1 )
               .put( MDBiomes.OPEN_FOREST, MDBiomes.FOREST_RIVER, 1 );

        profile.put( MDBiomes.TALL_FOREST, MDBiomes.TALL_FOREST_RIVER, 1 )
               .put( MDBiomes.HIGH_TALL_FOREST, MDBiomes.TALL_FOREST_RIVER, 1 )
               .put( MDBiomes.TALL_BUSH_FOREST, MDBiomes.TALL_FOREST_RIVER, 1 )
               .put( MDBiomes.HIGH_OPEN_TALL_FOREST, MDBiomes.TALL_FOREST_RIVER, 1 )
               .put( MDBiomes.OPEN_TALL_FOREST, MDBiomes.TALL_FOREST_RIVER, 1 );

        profile.put( MDBiomes.SWAMP, MDBiomes.SWAMP_RIVER, 1 )
               .put( MDBiomes.SWAMP_HILLS, MDBiomes.SWAMP_RIVER, 1 )
               .put( MDBiomes.SWAMP_LAND, MDBiomes.SWAMP_RIVER, 1 )
               .put( MDBiomes.SWAMP_MARSHES, MDBiomes.SWAMP_RIVER, 1 )
               .put( MDBiomes.SWAMP_WATER, MDBiomes.SWAMP_RIVER, 1 );

        profile.put( MDBiomes.WETLAND, MDBiomes.WETLAND_RIVER, 1 )
               .put( MDBiomes.WETLAND_FOREST, MDBiomes.WETLAND_RIVER, 1 )
               .put( MDBiomes.WETLAND_MARSH, MDBiomes.WETLAND_RIVER, 1 );

        profile.put( MDBiomes.LAKE, MDBiomes.LAKE, 1 )
               .put( MDBiomes.UNDEEP_LAKE, MDBiomes.LAKE, 1 )
               .put( MDBiomes.LAKE_SHORE, MDBiomes.LAKE, 1 )
               .put( MDBiomes.DEEP_LAKE, MDBiomes.DEEP_LAKE, 1 )
               .put( MDBiomes.GRASS_LAKE, MDBiomes.GRASS_LAKE, 1 )
               .put( MDBiomes.DEEP_GRASS_LAKE, MDBiomes.DEEP_GRASS_LAKE, 1 );

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

        profile.putDefault( MDBiomes.LUSH_GRASSLAND, 15 )
               .put( MDBiomes.LUSH_GRASSLAND, MDBiomes.HIGH_LUSH_GRASSLAND, 7 );

        profile.putDefault( MDBiomes.FOREST, 10 )
               .put( MDBiomes.FOREST, MDBiomes.HIGH_FOREST, 7 )
               .put( MDBiomes.FOREST, MDBiomes.BUSH_FOREST, 5 );

        profile.putDefault( MDBiomes.TALL_FOREST, 10 )
               .put( MDBiomes.TALL_FOREST, MDBiomes.HIGH_TALL_FOREST, 7 )
               .put( MDBiomes.TALL_FOREST, MDBiomes.TALL_BUSH_FOREST, 5 );

        profile.putDefault( MDBiomes.SWAMP, 10 )
               .put( MDBiomes.SWAMP, MDBiomes.SWAMP_HILLS, 7 )
               .put( MDBiomes.SWAMP, MDBiomes.SWAMP_MARSHES, 8 )
               .put( MDBiomes.SWAMP, MDBiomes.SWAMP_WATER, 8 )
               .put( MDBiomes.SWAMP, MDBiomes.SWAMP_LAND, 9 );

        profile.putDefault( MDBiomes.WETLAND, 10 )
               .put( MDBiomes.WETLAND, MDBiomes.WETLAND_FOREST, 9 )
               .put( MDBiomes.WETLAND, MDBiomes.WETLAND_MARSH, 8 );

        profile.putDefault( MDBiomes.LAKE, 14 )
               .put( MDBiomes.LAKE, MDBiomes.DEEP_LAKE, 9 )
               .put( MDBiomes.LAKE, MDBiomes.UNDEEP_LAKE, 9 )
               .put( MDBiomes.LAKE, MDBiomes.GRASS_LAKE, 4 )
               .put( MDBiomes.LAKE, MDBiomes.DEEP_GRASS_LAKE, 4 );

        profile.putDefault( MDBiomes.MOORLAND, 10 )
               .put( MDBiomes.MOORLAND, MDBiomes.HIGH_MOORLAND, 7 )
               .put( MDBiomes.MOORLAND, MDBiomes.MOORLAND_NO_TREES, 5 );

        return profile;
    }


    public static BiomeMutationProfile buildSmallMutationProfile() {
        BiomeMutationProfile profile = new BiomeMutationProfile();

        profile.putDefault( MDBiomes.FOREST, 14 )
               .put( MDBiomes.FOREST, MDBiomes.OPEN_FOREST, 3 );

        profile.putDefault( MDBiomes.HIGH_FOREST, 14 )
               .put( MDBiomes.HIGH_FOREST, MDBiomes.HIGH_OPEN_FOREST, 3 );

        profile.putDefault( MDBiomes.SHRUBLAND, 11 )
               .put( MDBiomes.SHRUBLAND, MDBiomes.SHRUBLAND_HEATH, 10 )
               .put( MDBiomes.SHRUBLAND, MDBiomes.SHRUBLAND_GRASS, 11 )
               .put( MDBiomes.SHRUBLAND, MDBiomes.SHRUBLAND_SAND, 8 )
               .put( MDBiomes.SHRUBLAND, MDBiomes.SHRUBLAND_TREES, 9 )
               .put( MDBiomes.SHRUBLAND, MDBiomes.SHRUBLAND_REEDS, 8 );

        return profile;
    }

    public static BiomeMutationProfile buildTinyMutationProfile() {
        BiomeMutationProfile profile = new BiomeMutationProfile();

        profile.putDefault( MDBiomes.LUSH_GRASSLAND, 30 )
               .put( MDBiomes.LUSH_GRASSLAND, MDBiomes.LUSH_GRASSLAND_OPEN, 1 );

        profile.putDefault( MDBiomes.HIGH_LUSH_GRASSLAND, 30 )
               .put( MDBiomes.HIGH_LUSH_GRASSLAND, MDBiomes.HIGH_LUSH_GRASSLAND_OPEN, 1 );

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

    public static void addCaveDeposits( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.MURKY_DIRT.getDefaultState(), 50 ), new BelowHeight( 128 ), new Fixed( 3 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.MURKY_SAND.getDefaultState(), 50 ), new BelowHeight( 128 ), new Chance( 0.4 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.MURKY_COARSE_DIRT.getDefaultState(), 50 ), new BelowHeight( 128 ), new Chance( 0.4 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCKS_OR_LIMESTONE, MDBlocks.REGOLITH.getDefaultState(), 50 ), new BelowHeight( 128 ) ) );

        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_TYPES, MDBlocks.DARKROCK.getDefaultState(), 50 ), new BelowHeight( 128 ), new Fixed( 3 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_TYPES, MDBlocks.LIMESTONE.getDefaultState(), 40 ), new BelowHeight( 128 ) ) );

        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.DARK_SUMESTONE.getDefaultState(), 40 ), new BetweenHeight( 0, 26 ) ) );

        biome.addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.MOLTEN_ROCK, MDBlocks.ROCK, null, null ), new BetweenHeight( 16, 32 ), new Chance( 0.4 ) ) );
        biome.addDecorator( new DecorationDecorator( new LakeDecoration( MDBlocks.CLEAN_WATER, MDBlocks.ROCK, null, null ), new BetweenHeight( 16, 32 ), new Chance( 0.01 ) ) );
    }

    public static void addCaveOres( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_ONLY, MDBlocks.SALT_ORE.getDefaultState(), 15 ), new BetweenHeight( 18, 128 ), new Fixed( 17 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_ONLY, MDBlocks.ALUMINIUM_ORE.getDefaultState(), 9 ), new BetweenHeight( 18, 128 ), new Fixed( 11 ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( ROCK_ONLY, MDBlocks.ANTHRACITE_ORE.getDefaultState(), 15 ), new BetweenHeight( 18, 128 ), new Fixed( 20 ) ) );

        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.FINNERITE_ORE.getDefaultState(), 3 ), new BetweenHeight( 0, 26 ), new Chance( 1 / 3D ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.IVERITE_ORE.getDefaultState(), 3 ), new BetweenHeight( 0, 26 ), new Chance( 1 / 3D ) ) );
        biome.addDecorator( new DecorationDecorator( new MineableDecoration( SUMESTONE, MDBlocks.SAGERITE_ORE.getDefaultState(), 3 ), new BetweenHeight( 0, 26 ), new Chance( 1 / 3D ) ) );
    }

    public static void addCaveSprings( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new SpringDecoration( MDFluids.MURKY_WATER, SpringDecoration.STILL | SpringDecoration.FLOWING ), new InCave(), new Fixed( 10 ) ) );
    }

    public static void addClaySand( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockPredicates.tag( MDBlockTags.SOIL ), MDBlocks.MURKY_SAND.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 12 ), new IsBelowHeight( MurkSurfaceGeneration.MAIN_HEIGHT - 1 ) ) );
        biome.addDecorator( new DecorationDecorator( new DepositDecoration( 4, BlockPredicates.tag( MDBlockTags.SOIL ), MDBlocks.MURKY_CLAY.getDefaultState() ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Chance( 0.25 ), new IsBelowHeight( MurkSurfaceGeneration.MAIN_HEIGHT - 1 ) ) );
    }

    public static void addCavePlants( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 50, 5, MDBlockGenerators.SALT_CRYSTAL ), new InCave(), new Fixed( 2 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.MURINA ), new InCave(), new Fixed( 8 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 120, 6, MDBlockGenerators.CAVE_GRASS ), new InCave(), new Fixed( 7 ) ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.HANGING_MOSS ), new BetweenHeight( 0, 60 ), new Fixed( 3 ) ) );

        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.SEEDLE ), new InCave() ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.BLACK_MUSHROOM ), new InCave() ) );
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 80, 8, MDBlockGenerators.DOTTED_MUSHROOM ), new InCave() ) );

        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 30, 8, MDBlockGenerators.PEBBLES ), new InCave(), new Fixed( 10 ) ) );

        biome.addDecorator( new DecorationDecorator(
            new SelectiveDecoration()
                .add( new GroupedBushDecoration( 3, 3, 0.8, MDBlockGenerators.MOSS ), 3 )
                .add( new GroupedBushDecoration( 3, 3, 0.8, MDBlockGenerators.DEAD_MOSS ), 1 )
                .add( new GroupedBushDecoration( 3, 3, 0.8, MDBlockGenerators.LICHEN ), 2 ),
            new InCave(), new Fixed( 3 )
        ) );

        biome.addDecorator( new DecorationDecorator( new GroupedBushDecoration( 4, 5, 3, MDBlockGenerators.PUDDLE ), new InCave(), new Fixed( 3 ) ) );
    }

    public static void addPebbles( ModernityBiome biome ) {
        biome.addDecorator( new DecorationDecorator( new ClusterBushDecoration( 30, 8, MDBlockGenerators.PEBBLES ), new Surface( Heightmap.Type.OCEAN_FLOOR_WG ), new Fixed( 2 ) ) );
    }

    private static class LushGrasslandEdgeLayer implements IEdgeTransformerLayer, IBiomeLayer {

        private final int[] ids = {
            id( MDBiomes.LUSH_GRASSLAND ),
            id( MDBiomes.LUSH_GRASSLAND_EDGE ),
            id( MDBiomes.LUSH_GRASSLAND_OPEN ),
            id( MDBiomes.HIGH_LUSH_GRASSLAND ),
            id( MDBiomes.HIGH_LUSH_GRASSLAND_OPEN ),
        };

        private boolean isGrassland( int id ) {
            for( int i : ids ) if( i == id ) return true;
            return false;
        }

        @Override
        public boolean isEdge( IRegionRNG rng, int center, int neighbor ) {
            return isGrassland( center ) && ! isGrassland( neighbor );
        }

        @Override
        public int getEdge( IRegionRNG rng, int center, int neighbor ) {
            return ids[ 1 ];
        }
    }

    private static class LakeEdgeLayer implements IEdgeTransformerLayer, IBiomeLayer {

        private final int[] ids = {
            id( MDBiomes.LAKE ),
            id( MDBiomes.LAKE_SHORE ),
            id( MDBiomes.DEEP_LAKE ),
            id( MDBiomes.UNDEEP_LAKE ),
            id( MDBiomes.GRASS_LAKE ),
            id( MDBiomes.DEEP_GRASS_LAKE ),
        };

        private boolean isLake( int id ) {
            for( int i : ids ) if( i == id ) return true;
            return false;
        }

        @Override
        public boolean isEdge( IRegionRNG rng, int center, int neighbor ) {
            return isLake( center ) && ! isLake( neighbor );
        }

        @Override
        public int getEdge( IRegionRNG rng, int center, int neighbor ) {
            return ids[ 1 ];
        }
    }

    private MurkSurfaceGeneration() {
        throw new InstanceOfUtilityClassException();
    }
}
