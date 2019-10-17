package modernity.common.environment.event;

import modernity.common.environment.event.impl.RandomEnvironmentEvent;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Function;

/**
 * Holder class for the Modernity's environment event types.
 */
@ObjectHolder( "modernity" )
public final class MDEnvEvents {
    private static final RegistryHandler<EnvironmentEventType> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final EnvironmentEventType RANDOM = register( "random", RandomEnvironmentEvent::new );

    private static EnvironmentEventType register( String id, Function<EnvironmentEventManager, EnvironmentEvent> factory ) {
        return ENTRIES.register( id, new EnvironmentEventType( factory ) );
    }

    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( EnvironmentEventType.class, ENTRIES );
    }

    private MDEnvEvents() {
    }
}
