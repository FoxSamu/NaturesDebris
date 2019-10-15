package modernity.common.world.gen.terrain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import modernity.api.util.BlockPredicates;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.world.gen.MDSurfaceGenSettings;
import modernity.common.world.gen.feature.*;
import modernity.common.world.gen.placement.AtSurfaceBelowHeight;
import modernity.common.world.gen.placement.MDPlacements;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Decorates the Modernity's surface dimension.
 */
public class MDSurfaceDecorator {

    private final IWorld world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;
    private final ChunkGenerator<?> chunkGen;

    private final Map<GenerationStage.Decoration, List<ConfiguredFeature<?>>> features = Maps.newEnumMap( GenerationStage.Decoration.class );

    public MDSurfaceDecorator( IWorld world, BiomeProvider provider, ChunkGenerator<?> chunkgen, MDSurfaceGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );
        this.chunkGen = chunkgen;

        for( GenerationStage.Decoration stage : GenerationStage.Decoration.values() ) {
            this.features.put( stage, Lists.newArrayList() );
        }

        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockPredicates.ROCK_TYPES, MDBlocks.DARK_DIRT.getDefaultState(), 50 ), MDPlacements.COUNT_CAVE, new FrequencyConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockPredicates.ROCK_TYPES, MDBlocks.DARKROCK.getDefaultState(), 50 ), MDPlacements.COUNT_CAVE, new FrequencyConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockPredicates.ROCK_TYPES, MDBlocks.LIGHTROCK.getDefaultState(), 30 ), MDPlacements.CHANCE_CAVE, new ChanceConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockPredicates.ROCK_TYPES, MDBlocks.REDROCK.getDefaultState(), 40 ), MDPlacements.COUNT_CAVE, new FrequencyConfig( 1 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockPredicates.ROCK_TYPES, MDBlocks.LIMESTONE.getDefaultState(), 40 ), MDPlacements.COUNT_CAVE, new FrequencyConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockMatcher.forBlock( MDBlocks.ROCK ), MDBlocks.SALT_ORE.getDefaultState(), 15 ), Placement.COUNT_BIASED_RANGE, new CountRangeConfig( 17, 4, 4, 128 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockMatcher.forBlock( MDBlocks.ROCK ), MDBlocks.ALUMINIUM_ORE.getDefaultState(), 9 ), Placement.COUNT_BIASED_RANGE, new CountRangeConfig( 11, 4, 4, 128 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature( MDFeatures.MINABLE, new MinableFeature.Config( BlockMatcher.forBlock( MDBlocks.ROCK ), MDBlocks.ANTHRACITE_ORE.getDefaultState(), 15 ), Placement.COUNT_BIASED_RANGE, new CountRangeConfig( 20, 4, 4, 128 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature( MDFeatures.FLUID_FALL, new FluidFallFeature.Config( MDFluids.MODERNIZED_WATER, FluidFallFeature.STILL | FluidFallFeature.FLOWING ), MDPlacements.COUNT_CAVE, new FrequencyConfig( 10 ) ) );
        addFeature( GenerationStage.Decoration.RAW_GENERATION, createDecoratedFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockState::isSolid, MDBlocks.DARK_SAND.getDefaultState() ), MDPlacements.LIMITED_HEIGHTMAP, new AtSurfaceBelowHeight.FrequencyConfig( settings.getWaterLevel() - 1, 24 ) ) );
        addFeature( GenerationStage.Decoration.RAW_GENERATION, createDecoratedFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockState::isSolid, MDBlocks.DARK_CLAY.getDefaultState() ), MDPlacements.LIMITED_HEIGHTMAP, new AtSurfaceBelowHeight.ChanceConfig( settings.getWaterLevel() - 1, 4 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 50, 5, MDBlocks.SALT_CRYSTAL ), MDPlacements.LIMITED_HEIGHTMAP, new AtSurfaceBelowHeight.ChanceConfig( settings.getWaterLevel() - 1, 35 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 50, 5, MDBlocks.SALT_CRYSTAL ), MDPlacements.COUNT_CAVE, new FrequencyConfig( 4 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 80, 8, MDBlocks.MURINA ), MDPlacements.COUNT_CAVE, new FrequencyConfig( 7 ) ) );
    }


    /**
     * Decorates the specified world region.
     */
    public void decorate( WorldGenRegion region ) {
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

            for( ConfiguredFeature<?> feature : features.get( stage ) ) {
                feature.place( region, chunkGen, ssrand, cornerPos );
            }
        }
    }


    /**
     * Adds a biome-independent feature to the world generator.
     */
    public void addFeature( GenerationStage.Decoration decorationStage, ConfiguredFeature<?> feature ) {
        this.features.get( decorationStage ).add( feature );
    }

    /**
     * Creates a {@link ConfiguredFeature}.
     * @see Biome#createDecoratedFeature(Feature, IFeatureConfig, Placement, IPlacementConfig)
     */
    protected static <F extends IFeatureConfig, D extends IPlacementConfig> ConfiguredFeature<?> createDecoratedFeature( Feature<F> feature, F featureConfig, Placement<D> placement, D placementConfig ) {
        return Biome.createDecoratedFeature( feature, featureConfig, placement, placementConfig );
    }
}
