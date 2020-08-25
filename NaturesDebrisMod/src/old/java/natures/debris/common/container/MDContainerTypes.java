/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.container;

import com.google.common.reflect.TypeToken;
import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.common.registryold.RegistryHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Holder class for Modernity container types
 */
//@ObjectHolder( "modernity" )
public final class MDContainerTypes {
    private static final RegistryHandler<ContainerType<?>> ENTRIES = new RegistryHandler<>("natures/debris");

    public static final ContainerType<WorkbenchContainer> WORKBENCH = register("workbench", new ContainerType<>(WorkbenchContainer::new));

    private MDContainerTypes() {
    }

    private static <T extends Container> ContainerType<T> register(String id, ContainerType<T> type, String... aliases) {
        ENTRIES.register(id, type, aliases);
        return type;
    }

    /**
     * Registers the registry handler to the {@link RegistryEventHandler}. Should be called only from {@link
     * RegistryEventHandler}.
     */
    @SuppressWarnings("unchecked")
    public static void setup(RegistryEventHandler handler) {
        TypeToken<ContainerType<?>> token = new TypeToken<ContainerType<?>>(ContainerType.class) {
        };
        handler.addHandler((Class<ContainerType<?>>) token.getRawType(), ENTRIES);
    }

    /**
     * Registers the screen factories to the {@link ScreenManager}.
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerScreens() { // TODO Re-evaluate
//        ScreenManager.registerFactory( WORKBENCH, WorkbenchScreen::new );
    }
}
