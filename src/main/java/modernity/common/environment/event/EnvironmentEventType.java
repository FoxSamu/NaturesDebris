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
    private EnvironmentEvent dummy;

    /**
     * Creates an environment event type.
     *
     * @param factory Function for creating new event instances. It MUST handle a {@code null} environment manager,
     *                which is used to create a dummy.
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

    /**
     * Returns a dummy instance, apart from any manager instance (it's referenced manager is null). Currently only used
     * for building commands.
     */
    @SuppressWarnings( "unchecked" )
    public <T extends EnvironmentEvent> T getDummy() {
        if( dummy == null ) {
            dummy = factory.apply( null );
            if( dummy == null ) {
                throw new NullPointerException( "Environment event factory returns null" );
            }
        }
        return (T) dummy;
    }
}
