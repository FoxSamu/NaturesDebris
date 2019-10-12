package modernity.common.world.gen.structure;

import com.google.common.reflect.TypeToken;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDStructures {
    private static final RegistryHandler<Feature<?>> STRUCTURES = new RegistryHandler<>( "modernity" );

    public static final CaveStructure CAVE = register( "cave", new CaveStructure() );

    private static <T extends Structure<?>> T register( String id, T structure, String... aliases ) {
        return STRUCTURES.register( id, structure, aliases );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<Feature<?>> token = new TypeToken<Feature<?>>() {
        };
        handler.addHandler( (Class<Feature<?>>) token.getRawType(), STRUCTURES );
    }

    private MDStructures() {
    }
}
