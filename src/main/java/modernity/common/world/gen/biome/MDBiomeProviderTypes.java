package modernity.common.world.gen.biome;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDBiomeProviderTypes {
    private static final RegistryHandler<BiomeProviderType<?, ?>> ENTRIES = new RegistryHandler<>( "modernity" );

    private static <T extends BiomeProviderType<?, ?>> T register( String id, T type ) {
        return ENTRIES.register( id, type );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<BiomeProviderType<?, ?>> token = new TypeToken<BiomeProviderType<?, ?>>() {
        };
        handler.addHandler( (Class<BiomeProviderType<?, ?>>) token.getRawType(), ENTRIES );
    }

    private MDBiomeProviderTypes() {
    }
}
