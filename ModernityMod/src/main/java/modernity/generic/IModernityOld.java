/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic;

import modernity.generic.data.IDataService;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;

public interface IModernityOld {
    IEventBus EVENT_BUS = BusBuilder.builder().startShutdown().build();

    static IModernityOld get() {
        return ModernityHolder.instance;
    }

    static void set( IModernityOld instance ) {
        if( ModernityHolder.instance != null ) {
            throw new IllegalStateException( "Modernity already initialized!" );
        }
        ModernityHolder.instance = instance;
    }

    default void registerListeners() {
    }

    default void setupRegistryHandler() {
    }

    default void preInit() {
    }

    default void init() {
    }

    default void postInit() {
    }

    default IDataService getDataService() {
        return IDataService.NONE;
    }
}
