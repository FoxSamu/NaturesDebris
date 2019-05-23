package modernity.common.biome;

import modernity.api.util.ColorUtil;

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
    }
}
