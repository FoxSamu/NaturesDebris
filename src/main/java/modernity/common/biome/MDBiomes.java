package modernity.common.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;

public class MDBiomes {

    public static final MeadowBiome MEADOW = new MeadowBiome();
    public static final ForestBiome FOREST = new ForestBiome();
    public static final WaterlandsBiome WATERLANDS = new WaterlandsBiome();

    public static void register( IForgeRegistry<Biome> registry ) {
        registry.registerAll( MEADOW, FOREST, WATERLANDS );
    }
}
