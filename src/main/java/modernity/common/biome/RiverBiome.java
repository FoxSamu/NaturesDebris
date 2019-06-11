/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.biome;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;

import modernity.api.util.ColorUtil;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.decorate.feature.BushFeature;
import modernity.common.world.gen.decorate.feature.MDFeatures;
import modernity.common.world.gen.decorate.util.IBlockProvider;
import modernity.common.world.gen.surface.GrassSurfaceGenerator;

public class RiverBiome extends BiomeBase {
    public RiverBiome() {
        super(
                "river", new Builder()
                        .depth( - 0.1F ).scale( 0.07F )
                        .heightDifference( 2 ).baseHeight( 58 ).heightVariation( 0 )
                        .blendWeight( 2 )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 4, 122, 59 ) )
                        .foliageColor( ColorUtil.rgb( 27, 107, 51 ) )
                        .waterColor( ColorUtil.rgb( 26, 43, 155 ) )
                        .waterFogDensity( 0.01F )
                        .surfaceGenerator( new GrassSurfaceGenerator() )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 100, 6, MDBlocks.DARK_TALLGRASS ), AT_SURFACE, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature( MDFeatures.BUSH, new BushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), AT_SURFACE_WITH_CHANCE, new ChanceConfig( 6 ) ) );
    }
}
