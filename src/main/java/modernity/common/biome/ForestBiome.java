package modernity.common.biome;

import modernity.api.util.ColorUtil;

public class ForestBiome extends BiomeBase {
    public ForestBiome() {
        super(
                "forest", new Builder()
                        .baseHeight( 64F )
                        .heightVariation( 18F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 0, 109, 38 ) )
                        .foliageColor( ColorUtil.rgb( 15, 79, 42 ) )
                        .waterColor( ColorUtil.rgb( 35, 49, 142 ) )
                        .waterFogDensity( 0.01F )
        );
    }
}
