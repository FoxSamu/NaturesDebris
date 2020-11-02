/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import natures.debris.api.LifecyclePhase;
import natures.debris.api.plugin.ILifecycleListener;
import natures.debris.core.plugin.PluginManager;
import natures.debris.common.NaturesDebris;
import natures.debris.client.NaturesDebrisClient;

@Mod("ndebris")
public class Bootstrap {
    private static final Logger LOGGER = LogManager.getLogger("Nature's Debris Bootstrap");

    public static final NaturesDebris INSTANCE = DistExecutor.safeRunForDist(
        () -> NaturesDebrisClient::new,
        () -> NaturesDebris::new
    );

    private static LifecyclePhase lifecyclePhase = LifecyclePhase.UNLOADED;

    public Bootstrap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);

        PluginManager.loadPlugins();

        construct();
        LOGGER.info("Nature's Debris was initialized:");
        LOGGER.info("- Dist:    {}", FMLEnvironment.dist);
        LOGGER.info("- Version: {}", NdInfo.VERSION);
    }

    /**
     * Calls {@link NaturesDebris#construct()} and hints all {@link ILifecycleListener} plugins.
     */
    private void construct() {
        lifecyclePhase = LifecyclePhase.CONSTRUCTING;
        INSTANCE.construct();
        PluginManager.getLifecycleListeners().forEach(listener -> listener.ndebrisConstruct(INSTANCE));
    }

    /**
     * Calls {@link NaturesDebris#setup()} and hints all {@link ILifecycleListener} plugins.
     */
    private void setup(FMLCommonSetupEvent event) {
        lifecyclePhase = LifecyclePhase.SETTING_UP;
        INSTANCE.setup();
        PluginManager.getLifecycleListeners().forEach(listener -> listener.ndebrisSetup(INSTANCE));
    }

    /**
     * Calls {@link NaturesDebris#complete()} and hints all {@link ILifecycleListener} plugins.
     */
    private void loadComplete(FMLLoadCompleteEvent event) {
        lifecyclePhase = LifecyclePhase.COMPLETING;
        INSTANCE.complete();
        PluginManager.getLifecycleListeners().forEach(listener -> listener.ndebrisLoaded(INSTANCE));
        lifecyclePhase = LifecyclePhase.LOADED;
    }

    public static LifecyclePhase getLifecyclePhase() {
        return lifecyclePhase;
    }
}
