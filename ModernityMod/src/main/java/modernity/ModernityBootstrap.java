/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity;

import modernity.api.plugin.ILifecycleListener;
import modernity.client.ModernityClient;
import modernity.common.Modernity;
import modernity.plugin.PluginManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod( "modernity" )
public class ModernityBootstrap {
    private static final Logger LOGGER = LogManager.getLogger( "Modernity Bootstrap" );

    public static final Modernity MODERNITY = DistExecutor.runForDist(
        () -> ModernityClient::new,
        () -> Modernity::new
    );

    public ModernityBootstrap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);

        PluginManager.loadPlugins();

        construct();
        LOGGER.info("The Modernity was initialized:");
        LOGGER.info("- Dist:    {}", FMLEnvironment.dist);
        LOGGER.info("- Version: {}", MDInfo.VERSION);
    }

    /**
     * Calls {@link Modernity#construct()} and hints all {@link ILifecycleListener} plugins.
     */
    private void construct() {
        MODERNITY.construct();
        PluginManager.getLifecycleListeners().forEach( listener -> listener.modernityConstruct( MODERNITY ) );
    }

    /**
     * Calls {@link Modernity#setup()} and hints all {@link ILifecycleListener} plugins.
     */
    private void setup( FMLCommonSetupEvent event ) {
        MODERNITY.setup();
        PluginManager.getLifecycleListeners().forEach( listener -> listener.modernitySetup( MODERNITY ) );
    }

    /**
     * Calls {@link Modernity#complete()} and hints all {@link ILifecycleListener} plugins.
     */
    private void loadComplete( FMLLoadCompleteEvent event ) {
        MODERNITY.complete();
        PluginManager.getLifecycleListeners().forEach( listener -> listener.modernityLoaded( MODERNITY ) );
    }
}
