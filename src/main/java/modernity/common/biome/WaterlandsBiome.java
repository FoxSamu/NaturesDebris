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

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.ClusterBushFeature;
import modernity.common.world.gen.decorate.feature.GroupedBushFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;
import modernity.common.world.gen.decorate.util.IBlockProvider;
import modernity.common.world.gen.surface.GrassSurfaceGenerator;

public class WaterlandsBiome extends BiomeBase {
    public WaterlandsBiome() {
        super(
                "waterlands", new Builder()
                        .depth( - 0.1F ).scale( 0.07F )
                        .heightDifference( 2 ).baseHeight( 0 ).heightVariation( 0 )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 12, 109, 0 ) )
                        .foliageColor( ColorUtil.rgb( 0, 86, 5 ) )
                        .waterColor( ColorUtil.rgb( 17, 49, 91 ) )
                        .waterFogDensity( 0.01F )
                        .surfaceGenerator( new GrassSurfaceGenerator() )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 4, 8, MDBlocks.REEDS ), AT_SURFACE, new FrequencyConfig( 10 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), AT_SURFACE_WITH_CHANCE, new ChanceConfig( 6 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BLACKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE_WITH_CHANCE, new ChanceConfig( 2 ) ) );
    }
}
