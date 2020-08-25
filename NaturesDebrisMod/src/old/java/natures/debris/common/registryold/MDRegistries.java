/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.registryold;

import natures.debris.common.ModernityOld;
import natures.debris.common.area.core.AreaType;
import natures.debris.common.environment.event.EnvironmentEventType;
import natures.debris.common.event.BlockEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

/**
 * Holder class that holds Modernity registries.
 */
public final class MDRegistries {

    public static final ForgeRegistry<BlockEvent<?>> BLOCK_EVENTS = RegistryManager.ACTIVE.getRegistry(ModernityOld.res("block_events"));
    public static final ForgeRegistry<EnvironmentEventType> ENVIRONMENT_EVENTS = RegistryManager.ACTIVE.getRegistry(ModernityOld.res("environment_events"));
    public static final ForgeRegistry<AreaType<?>> AREA_TYPES = RegistryManager.ACTIVE.getRegistry(ModernityOld.res("area_types"));

    private MDRegistries() {
    }
}
