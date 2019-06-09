package modernity.common.biome;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.BushFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;
import modernity.common.world.gen.decorate.util.IBlockProvider;
import modernity.common.world.gen.surface.SwampSurfaceGenerator;

public class SwampBiome extends BiomeBase {
    public SwampBiome() {
        super(
                "swamp", new Builder()
                        .depth( - 0.2F ).scale( 0.1F )
                        .heightDifference( 1 ).baseHeight( 63 ).heightVariation( 4 )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 12, 57, 18 ) )
                        .foliageColor( ColorUtil.rgb( 0, 57, 25 ) )
                        .waterColor( ColorUtil.rgb( 57, 68, 60 ) )
                        .waterFogDensity( 0.03F )
                        .surfaceGenerator( new SwampSurfaceGenerator() )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), AT_SURFACE_WITH_CHANCE, new ChanceConfig( 6 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BLACKWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, AT_SURFACE_WITH_CHANCE, new ChanceConfig( 2 ) ) );
    }
}
