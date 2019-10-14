package modernity.common.registry;

import modernity.common.Modernity;
import modernity.common.event.BlockEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

/**
 * Holder class that holds Modernity registries.
 */
public final class MDRegistries {

    public static final ForgeRegistry<BlockEvent<?>> BLOCK_EVENTS = RegistryManager.ACTIVE.getRegistry( Modernity.res( "block_events" ) );

    private MDRegistries() {
    }
}
