package modernity.common.biome;

import modernity.api.util.IBlockProvider;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.feature.ClusterBushFeature;
import modernity.common.world.gen.feature.GroupedBushFeature;
import modernity.common.world.gen.feature.LakeFeature;
import modernity.common.world.gen.feature.MDFeatures;
import modernity.common.world.gen.surface.GrassSurfaceGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.LakeChanceConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * The 'Meadow' or 'modernity:meadow' biome.
 */
public class MeadowBiome extends ModernityBiome {
    protected MeadowBiome() {
        super(
            new Builder()
                .baseHeight( 2 ).heightVariation( 3 ).heightDifference( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
        );

        addFeature( GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature( MDFeatures.LAKE, new LakeFeature.Config( MDBlocks.MODERNIZED_WATER, null, null, MDBlocks.DARK_GRASS_BLOCK ), Placement.WATER_LAKE, new LakeChanceConfig( 5 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REEDS ), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig( 3 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 120, 6, MDBlocks.DARK_TALLGRASS ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 12 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 120, 6, MDBlocks.NETTLES ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 4 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MELION, MDBlocks.ORANGE_MELION, MDBlocks.INDIGO_MELION, MDBlocks.YELLOW_MELION, MDBlocks.MAGENTA_MELION, MDBlocks.RED_MELION, MDBlocks.WHITE_MELION ) ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 1 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.BLACKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 30 ) ) );
    }

}
