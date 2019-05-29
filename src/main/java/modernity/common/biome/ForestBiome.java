package modernity.common.biome;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.BushFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;

public class ForestBiome extends BiomeBase {
    public ForestBiome() {
        super(
                "forest", new Builder()
                        .depth( 0.125F ).scale( 0.07F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 0, 109, 38 ) )
                        .foliageColor( ColorUtil.rgb( 15, 79, 42 ) )
                        .waterColor( ColorUtil.rgb( 35, 49, 142 ) )
                        .waterFogDensity( 0.01F )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.DARKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE_WITH_EXTRA, new AtSurfaceWithExtraConfig( 4, 0.1F, 2 ) ) );
    }
}
