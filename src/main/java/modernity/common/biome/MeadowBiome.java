package modernity.common.biome;

import modernity.api.util.ColorUtil;

public class MeadowBiome extends BiomeBase {
    public MeadowBiome() {
        super(
                "meadow", new BiomeBase.Builder()
                        .baseHeight( 70F )
                        .heightVariation( 8F )
                        .fogColor( ColorUtil.rgb( 0, 0, 21 ) )
                        .fogDensity( 0.01F )
                        .grassColor( ColorUtil.rgb( 0, 109, 38 ) )
                        .foliageColor( ColorUtil.rgb( 32, 86, 49 ) )
                        .waterColor( ColorUtil.rgb( 21, 91, 165 ) )
                        .waterFogDensity( 0.01F )
        );
    }
}
