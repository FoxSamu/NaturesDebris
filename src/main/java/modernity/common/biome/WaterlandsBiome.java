package modernity.common.biome;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.BushFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;

public class WaterlandsBiome extends BiomeBase {
    public WaterlandsBiome() {
        super(
                "waterlands", new Builder()
                        .baseHeight( 62F )
                        .heightVariation( 8F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 12, 109, 0 ) )
                        .foliageColor( ColorUtil.rgb( 0, 86, 5 ) )
                        .waterColor( ColorUtil.rgb( 17, 49, 91 ) )
                        .waterFogDensity( 0.01F )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.DARKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE_WITH_CHANCE, new ChanceConfig( 2 ) ) );
    }
}
