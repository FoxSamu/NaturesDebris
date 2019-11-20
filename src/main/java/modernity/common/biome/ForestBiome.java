/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.api.util.BlockPredicates;
import modernity.api.util.IBlockProvider;
import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.world.gen.feature.*;
import modernity.common.world.gen.surface.HumusSurfaceGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.LakeChanceConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * The 'Forest' or 'modernity:forest' biome.
 */
public class ForestBiome extends ModernityBiome {
    protected ForestBiome() {
        super(
            new Builder()
                .baseHeight( 4 ).heightVariation( 6 ).heightDifference( 3 )
                .surfaceGen( new HumusSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        );

        addFeature( GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature( MDFeatures.LAKE, new LakeFeature.Config( MDBlocks.MODERNIZED_WATER, null, null, MDBlocks.DARK_GRASS_BLOCK ), Placement.WATER_LAKE, new LakeChanceConfig( 5 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, MDBlocks.NETTLES ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, MDBlocks.MINT_PLANT ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 1 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MELION, MDBlocks.ORANGE_MELION, MDBlocks.INDIGO_MELION, MDBlocks.YELLOW_MELION, MDBlocks.MAGENTA_MELION, MDBlocks.RED_MELION, MDBlocks.WHITE_MELION ) ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 1 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REEDS ), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REDWOLD ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 6 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.INVER_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.BLACKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 6 ) ) );
        addFeature( GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockPredicates.TRUE, MDBlocks.ROCK.getDefaultState() ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 8 ) ) );
        addFeature( GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockPredicates.TRUE, MDBlocks.DARKROCK.getDefaultState() ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 16 ) ) );
    }
}
