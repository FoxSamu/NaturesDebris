package modernity.common.event;

import com.google.common.reflect.TypeToken;
import modernity.common.event.impl.LeavesDecayBlockEvent;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDBlockEvents {
    private static final RegistryHandler<BlockEvent<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final LeavesDecayBlockEvent LEAVES_DECAY = register( "leaves_decay", new LeavesDecayBlockEvent() );

    private static <T extends BlockEvent<?>> T register( String id, T event ) {
        return ENTRIES.register( id, event );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<BlockEvent<?>> token = new TypeToken<BlockEvent<?>>() {
        };
        handler.addHandler( (Class<BlockEvent<?>>) token.getRawType(), ENTRIES );
    }

    private MDBlockEvents() {
    }
}
