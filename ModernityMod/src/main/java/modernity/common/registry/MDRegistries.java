/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.registry;

import modernity.common.ModernityOld;
import modernity.common.area.core.AreaType;
import modernity.common.environment.event.EnvironmentEventType;
import modernity.common.event.BlockEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

/**
 * Holder class that holds Modernity registries.
 */
public final class MDRegistries {

    public static final ForgeRegistry<BlockEvent<?>> BLOCK_EVENTS = RegistryManager.ACTIVE.getRegistry( ModernityOld.res( "block_events" ) );
    public static final ForgeRegistry<EnvironmentEventType> ENVIRONMENT_EVENTS = RegistryManager.ACTIVE.getRegistry( ModernityOld.res( "environment_events" ) );
    public static final ForgeRegistry<AreaType<?>> AREA_TYPES = RegistryManager.ACTIVE.getRegistry( ModernityOld.res( "area_types" ) );

    private MDRegistries() {
    }
}
