/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris;

import natures.debris.client.ModernityClientOld;
import natures.debris.data.ModernityData;
import natures.debris.generic.IModernityOld;
import natures.debris.generic.IRunMode;
import natures.debris.generic.MDInfo;
import natures.debris.generic.event.ModernityInitializedEvent;
import natures.debris.server.ModernityServerOld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bootstrap class of the Modernity, which handles setup events and creates the {@link IModernityOld} instance for the
 * side we're running on. This is {@link ModernityClientOld} for the client and {@link ModernityServerOld} for the
 * dedicated server. A special {@link ModernityData} instance is created when running in data generation mode. See
 * {@link #determineMode()} for more information about these run modes.
 */
// @Mod( "modernity" )
public final class ModernityBootstrapOld {
    private static final Logger LOGGER = LogManager.getLogger("Modernity Bootstrap");

    private final IRunMode mode;

    /**
     * Instantiates the Modernity bootstrap. This registers the main listeners and sets up registries.
     */
    public ModernityBootstrapOld() {
        System.setProperty("modernity.test", "models");

        mode = determineMode();
        LOGGER.info("Modernity starting in " + mode + " mode...");

        IModernityOld.EVENT_BUS.start();
        initProxy();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        IModernityOld.get().setupRegistryHandler();
    }

    /**
     * Calls {@link IModernityOld#init()} on our proxy.
     */
    private void setup(FMLCommonSetupEvent event) {
        IModernityOld.get().init();
    }

    /**
     * Calls {@link IModernityOld#postInit()}.
     */
    private void loadComplete(FMLLoadCompleteEvent event) {
        IModernityOld.get().postInit();
    }


    /**
     * Instantiates the proxy using {@link #instantiate()} and initializes it for the specified side. This calls {@link
     * IModernityOld#registerListeners()}, then {@link IModernityOld#preInit()} and casts an event on the forge event
     * bus indicating that the Modernity is initialized.
     */
    private void initProxy() {
        IModernityOld modernity = instantiate();
        if (modernity != null) {
            MinecraftForge.EVENT_BUS.register(modernity);
            FMLJavaModLoadingContext.get().getModEventBus().register(modernity);

            modernity.registerListeners();
            modernity.preInit();

            LOGGER.info("Modernity version {} initialized for {} mode: {}", MDInfo.VERSION, mode, modernity.getClass().getName());

            IModernityOld.EVENT_BUS.post(new ModernityInitializedEvent(mode, modernity));
        } else {
            throw new IllegalStateException("No modernity instance generated... Reflection is broken?");
        }
    }

    /**
     * Creates the {@link IModernityOld} instance, as used in {@link #initProxy()}.
     */
    private IModernityOld instantiate() {
        try {
            IModernityOld inst = (IModernityOld) Class.forName(mode.getClassName()).newInstance();
            IModernityOld.set(inst);
            return inst;
        } catch (Throwable e) {
            throw new IllegalStateException("Unable to instantiate " + mode.getClassName(), e);
        }
    }

    /**
     * Determines in which mode the Modernity should initalize and run. When a test mode is available, the test mode is
     * being used to run. When there is not a test but the used launch handler is {@code fmluserdevdata} or {@code
     * fmldevdata}, the Modernity runs in {@linkplain IRunMode#DATA data} mode. Otherwise the Modernity runs in
     * {@linkplain IRunMode#CLIENT client} mode when <code>{@link FMLEnvironment#dist} == {@link Dist#CLIENT}</code> and
     * in {@linkplain IRunMode#SERVER server} mode when <code>{@link FMLEnvironment#dist} == {@link
     * Dist#DEDICATED_SERVER}</code>.
     */
    private IRunMode determineMode() {
        IRunMode test = ModernityTests.getTest();
        if (test != null) return test;

        String lhn = FMLLoader.launcherHandlerName();
        if (lhn.equals("fmluserdevdata") || lhn.equals("fmldevdata")) {
            return IRunMode.DATA;
        } else {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                return IRunMode.CLIENT;
            } else {
                return IRunMode.SERVER;
            }
        }
    }
}
