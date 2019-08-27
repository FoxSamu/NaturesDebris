/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 27 - 2019
 */

package modernity.common.biome;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.LakeChanceConfig;

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.*;
import modernity.common.world.gen.decorate.util.IBlockProvider;
import modernity.common.world.gen.surface.HumusSurfaceGenerator;
import modernity.common.world.gen.util.BlockPredicates;

public class ForestBiome extends BiomeBase {
    public ForestBiome() {
        super(
                "forest", new Builder()
                        .depth( 0.125F ).scale( 0.07F )
                        .heightDifference( 3 ).baseHeight( 4 ).heightVariation( 6 )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 0, 109, 58 ) )
                        .foliageColor( ColorUtil.rgb( 27, 91, 54 ) )
                        .waterColor( ColorUtil.rgb( 45, 61, 163 ) )
                        .waterFogDensity( 0.01F )
                        .surfaceGenerator( new HumusSurfaceGenerator() )
        );

        addFeature( GenerationStage.Decoration.LOCAL_MODIFICATIONS, createCompositeFeature( MDFeatures.LAKE, new LakeFeature.Config( MDBlocks.MODERNIZED_WATER, null, null, MDBlocks.DARK_GRASS ), LAKE_WATER, new LakeChanceConfig( 5 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), AT_SURFACE, new FrequencyConfig( 1 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MELION, MDBlocks.ORANGE_MELION, MDBlocks.INDIGO_MELION, MDBlocks.YELLOW_MELION, MDBlocks.MAGENTA_MELION, MDBlocks.RED_MELION, MDBlocks.WHITE_MELION ) ), AT_SURFACE, new FrequencyConfig( 1 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REEDS ), TOP_SURFACE_WITH_CHANCE, new ChanceConfig( 3 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.INVER_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BLACKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE, new FrequencyConfig( 6 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockPredicates.TRUE, MDBlocks.ROCK.getDefaultState() ), AT_SURFACE_WITH_CHANCE, new ChanceConfig( 8 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockPredicates.TRUE, MDBlocks.DARKROCK.getDefaultState() ), AT_SURFACE_WITH_CHANCE, new ChanceConfig( 16 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockPredicates.TRUE, MDBlocks.LIGHTROCK.getDefaultState() ), AT_SURFACE_WITH_CHANCE, new ChanceConfig( 128 ) ) );
        addFeature( GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature( MDFeatures.DEPOSIT, new DepositFeature.Config( 4, BlockPredicates.TRUE, MDBlocks.REDROCK.getDefaultState() ), AT_SURFACE_WITH_CHANCE, new ChanceConfig( 48 ) ) );
    }
}
