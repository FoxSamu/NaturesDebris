/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity;

import modernity.api.IModernity;
import modernity.api.IRunMode;
import modernity.api.MDInfo;
import modernity.api.event.ModernityInitializedEvent;
import modernity.client.ModernityClient;
import modernity.data.ModernityData;
import modernity.server.ModernityServer;
import modernity.tests.ModernityTests;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bootstrap class of the Modernity, which handles setup events and creates the {@link IModernity} instance for the side
 * we're running on. This is {@link ModernityClient} for the client and {@link ModernityServer} for the dedicated
 * server. A special {@link ModernityData} instance is created when running in data generation mode. See {@link
 * #determineMode()} for more information about these run modes.
 */
@Mod( "modernity" )
public final class ModernityBootstrap {
    private static final Logger LOGGER = LogManager.getLogger( "Modernity Bootstrap" );

    private final IRunMode mode;

    /**
     * Instantiates the Modernity bootstrap. This registers the main listeners and sets up registries.
     */
    public ModernityBootstrap() {
        System.setProperty( "modernity.test", "models" );

        mode = determineMode();
        LOGGER.info( "Modernity starting in " + mode + " mode..." );

        IModernity.EVENT_BUS.start();
        initProxy();

        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::loadComplete );
        IModernity.get().setupRegistryHandler();
    }

    /**
     * Calls {@link IModernity#init()} on our proxy.
     */
    private void setup( FMLCommonSetupEvent event ) {
        IModernity.get().init();
    }

    /**
     * Calls {@link IModernity#postInit()}.
     */
    private void loadComplete( FMLLoadCompleteEvent event ) {
        IModernity.get().postInit();
    }


    /**
     * Instantiates the proxy using {@link #instantiate()} and initializes it for the specified side. This calls {@link
     * IModernity#registerListeners()}, then {@link IModernity#preInit()} and casts an event on the forge event bus
     * indicating that the Modernity is initialized.
     */
    private void initProxy() {
        IModernity modernity = instantiate();
        if( modernity != null ) {
            MinecraftForge.EVENT_BUS.register( modernity );
            FMLJavaModLoadingContext.get().getModEventBus().register( modernity );

            modernity.registerListeners();
            modernity.preInit();

            LOGGER.info( "Modernity version {} initialized for {} mode: {}", MDInfo.VERSION, mode, modernity.getClass().getName() );

            IModernity.EVENT_BUS.post( new ModernityInitializedEvent( mode, modernity ) );
        } else {
            throw new IllegalStateException( "No modernity instance generated... Reflection is broken?" );
        }
    }

    /**
     * Creates the {@link IModernity} instance, as used in {@link #initProxy()}.
     */
    private IModernity instantiate() {
        try {
            IModernity inst = (IModernity) Class.forName( mode.getClassName() ).newInstance();
            IModernity.set( inst );
            return inst;
        } catch( Throwable e ) {
            throw new IllegalStateException( "Unable to instantiate " + mode.getClassName(), e );
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
        if( test != null ) return test;

        String lhn = FMLLoader.launcherHandlerName();
        if( lhn.equals( "fmluserdevdata" ) || lhn.equals( "fmldevdata" ) ) {
            return IRunMode.DATA;
        } else {
            if( FMLEnvironment.dist == Dist.CLIENT ) {
                return IRunMode.CLIENT;
            } else {
                return IRunMode.SERVER;
            }
        }
    }
}
