package modernity.common.biome;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.BushFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;

public class MeadowBiome extends BiomeBase {
    public MeadowBiome() {
        super(
                "meadow", new Builder()
                        .depth( 0.125F ).scale( 0.05F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 0, 109, 38 ) )
                        .foliageColor( ColorUtil.rgb( 32, 86, 49 ) )
                        .waterColor( ColorUtil.rgb( 1, 13, 150 ) )
                        .waterFogDensity( 0.01F )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 120, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 12 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BLACKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE_WITH_CHANCE, new ChanceConfig( 30 ) ) );
    }
}
