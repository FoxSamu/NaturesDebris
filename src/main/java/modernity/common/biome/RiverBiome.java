package modernity.common.biome;

import modernity.api.util.IBlockProvider;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.feature.ClusterBushFeature;
import modernity.common.world.gen.feature.GroupedBushFeature;
import modernity.common.world.gen.feature.MDFeatures;
import modernity.common.world.gen.surface.GrassSurfaceGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * The 'River' or 'modernity:river' biome.
 */
public class RiverBiome extends ModernityBiome {
    protected RiverBiome() {
        super(
            new Builder()
                .baseHeight( - 6 ).heightVariation( 0 ).heightDifference( 2 )
                .blendWeight( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REEDS ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 1 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 6 ) ) );
    }

}
