package modernity.common.biome;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.BushFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;

public class SwampBiome extends BiomeBase {
    public SwampBiome() {
        super(
                "swamp", new Builder()
                        .depth( - 0.2F ).scale( 0.1F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 12, 57, 18 ) )
                        .foliageColor( ColorUtil.rgb( 0, 57, 25 ) )
                        .waterColor( ColorUtil.rgb( 57, 68, 60 ) )
                        .waterFogDensity( 0.03F )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.DARKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE_WITH_CHANCE, new ChanceConfig( 2 ) ) );
    }
}
