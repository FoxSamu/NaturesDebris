/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.world.gen.terrain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.CompositeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.BasePlacement;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;

import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.world.gen.ModernityGenSettings;
import modernity.common.world.gen.decorate.feature.DepositFeature;
import modernity.common.world.gen.decorate.feature.FluidFallFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;
import modernity.common.world.gen.decorate.placement.AtSurfaceBelowHeight;
import modernity.common.world.gen.decorate.placement.MDPlacements;
import modernity.common.world.gen.util.BlockPredicates;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModernityTerrainDecorator {

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;
    private final IChunkGenerator chunkGen;

    private final ThreadLocal<double[]> noiseBuffer = new ThreadLocal<>();

    private final Map<GenerationStage.Decoration, List<CompositeFeature<?, ?>>> features = Maps.newEnumMap( GenerationStage.Decoration.class );

    public ModernityTerrainDecorator( World world, BiomeProvider provider, IChunkGenerator chunkgen, ModernityGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );
        this.chunkGen = chunkgen;

        for( GenerationStage.Decoration stage : GenerationStage.Decoration.values() ) {
            this.features.put( stage, Lists.newArrayList() );
        }

        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( Feature.MINABLE, new MinableConfig( BlockPredicates.ROCK_TYPES, MDBlocks.DARK_DIRT.getDefaultState(), 50 ), MDPlacements.IN_CAVE_WITH_FREQUENCY, new FrequencyConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( Feature.MINABLE, new MinableConfig( BlockPredicates.ROCK_TYPES, MDBlocks.DARKROCK.getDefaultState(), 50 ), MDPlacements.IN_CAVE_WITH_FREQUENCY, new FrequencyConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( Feature.MINABLE, new MinableConfig( BlockPredicates.ROCK_TYPES, MDBlocks.LIGHTROCK.getDefaultState(), 30 ), MDPlacements.IN_CAVE_WITH_CHANCE, new ChanceConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( Feature.MINABLE, new MinableConfig( BlockPredicates.ROCK_TYPES, MDBlocks.REDROCK.getDefaultState(), 40 ), MDPlacements.IN_CAVE_WITH_FREQUENCY, new FrequencyConfig( 1 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature( MDFeatures.FLUID_FALL, new FluidFallFeature.Config( MDFluids.MODERNIZED_WATER, FluidFallFeature.STILL | FluidFallFeature.FLOWING ), MDPlacements.IN_CAVE_WITH_FREQUENCY, new FrequencyConfig( 30 ) ) );
        addFeature( GenerationStage.Decoration.RAW_GENERATION, createCompositeFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, IBlockState::isFullCube, MDBlocks.DARK_SAND.getDefaultState() ), MDPlacements.AT_SURFACE_BELOW_HEIGHT, new AtSurfaceBelowHeight.FrequencyConfig( settings.getWaterLevel() - 1, 12 ) ) );
        addFeature( GenerationStage.Decoration.RAW_GENERATION, createCompositeFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, IBlockState::isFullCube, MDBlocks.DARK_CLAY.getDefaultState() ), MDPlacements.AT_SURFACE_BELOW_HEIGHT, new AtSurfaceBelowHeight.ChanceConfig( settings.getWaterLevel() - 1, 2 ) ) );
    }

    public void decorate( WorldGenRegion region ) {
        BlockFalling.fallInstantly = true;
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();
        int x = cx * 16;
        int z = cz * 16;
        BlockPos cornerPos = new BlockPos( x, 0, z );

        Biome biome = region.getChunk( cx, cz ).getBiomes()[ 7 * 16 + 7 ];
        SharedSeedRandom ssrand = new SharedSeedRandom();
        long seed = ssrand.setDecorationSeed( region.getSeed(), x, z );

        for( GenerationStage.Decoration stage : GenerationStage.Decoration.values() ) {
            biome.decorate( stage, chunkGen, region, seed, ssrand, cornerPos );

            for( CompositeFeature<?, ?> feature : features.get( stage ) ) {
                feature.place( region, chunkGen, ssrand, cornerPos, IFeatureConfig.NO_FEATURE_CONFIG );
            }
        }

        BlockFalling.fallInstantly = false;
    }

    public void addFeature( GenerationStage.Decoration decorationStage, CompositeFeature<?, ?> feature ) {
        this.features.get( decorationStage ).add( feature );
    }

    protected static <F extends IFeatureConfig, D extends IPlacementConfig> CompositeFeature<F, D> createCompositeFeature( Feature<F> feature, F config, BasePlacement<D> basePlacement, D placementConfig ) {
        return new CompositeFeature<>( feature, config, basePlacement, placementConfig );
    }
}
