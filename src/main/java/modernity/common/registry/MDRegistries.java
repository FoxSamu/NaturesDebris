package modernity.common.registry;

import modernity.common.Modernity;
import modernity.common.environment.event.EnvironmentEventType;
import modernity.common.event.BlockEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

/**
 * Holder class that holds Modernity registries.
 */
public final class MDRegistries {

    public static final ForgeRegistry<BlockEvent<?>> BLOCK_EVENTS = RegistryManager.ACTIVE.getRegistry( Modernity.res( "block_events" ) );
    public static final ForgeRegistry<EnvironmentEventType> ENVIRONMENT_EVENTS = RegistryManager.ACTIVE.getRegistry( Modernity.res( "environment_events" ) );

    private MDRegistries() {
    }
}
