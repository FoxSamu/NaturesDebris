package modernity.common.biome;

import modernity.api.util.ColorUtil;

public class ForestBiome extends BiomeBase {
    public ForestBiome() {
        super(
                "forest", new Builder()
                        .baseHeight( 70F )
                        .heightVariation( 8F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 0, 109, 38 ) )
                        .foliageColor( ColorUtil.rgb( 15, 79, 42 ) )
                        .waterColor( ColorUtil.rgb( 47, 98, 181 ) )
                        .waterFogDensity( 0.01F )
        );
    }
}
