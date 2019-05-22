package modernity.common.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;

public class MDBiomes {

    public static final MeadowBiome MEADOW = new MeadowBiome();

    public static void register( IForgeRegistry<Biome> registry ) {
        registry.register( MEADOW );
    }
}
