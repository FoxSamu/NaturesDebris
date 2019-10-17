package modernity.common.environment.event;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;

/**
 * Holds a type of environment event, to be registered to the {@linkplain IForgeRegistry registry} and to create event
 * instances.
 */
public final class EnvironmentEventType extends ForgeRegistryEntry<EnvironmentEventType> {

    private final Function<EnvironmentEventManager, EnvironmentEvent> factory;

    /**
     * Creates an environment event type.
     * @param factory Function for creating new event instances.
     */
    public EnvironmentEventType( Function<EnvironmentEventManager, EnvironmentEvent> factory ) {
        this.factory = factory;
    }

    /**
     * Creates a new environment event instance for the specified event manager.
     */
    public EnvironmentEvent createEvent( EnvironmentEventManager manager ) {
        return factory.apply( manager );
    }
}
