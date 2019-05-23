package modernity.common.biome;

import modernity.api.util.ColorUtil;

public class WaterlandsBiome extends BiomeBase {
    public WaterlandsBiome() {
        super(
                "waterlands", new Builder()
                        .baseHeight( 63F )
                        .heightVariation( 3F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 12, 109, 0 ) )
                        .foliageColor( ColorUtil.rgb( 0, 86, 5 ) )
                        .waterColor( ColorUtil.rgb( 23, 81, 80 ) )
                        .waterFogDensity( 0.01F )
        );
    }
}
