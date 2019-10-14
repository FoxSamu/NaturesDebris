package modernity.common.event;

import com.google.common.reflect.TypeToken;
import modernity.common.event.impl.LeavesDecayBlockEvent;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity block events.
 */
@ObjectHolder( "modernity" )
public final class MDBlockEvents {
    private static final RegistryHandler<BlockEvent<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final LeavesDecayBlockEvent LEAVES_DECAY = register( "leaves_decay", new LeavesDecayBlockEvent() );

    private static <T extends BlockEvent<?>> T register( String id, T event ) {
        return ENTRIES.register( id, event );
    }

    /**
     * Adds the registry handler to the {@link RegistryEventHandler}. Must be called internally only.
     */
    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<BlockEvent<?>> token = new TypeToken<BlockEvent<?>>() {
        };
        handler.addHandler( (Class<BlockEvent<?>>) token.getRawType(), ENTRIES );
    }

    private MDBlockEvents() {
    }
}
