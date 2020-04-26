/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity;

import modernity.generic.IModernity;
import modernity.generic.MDInfo;
import modernity.generic.RunMode;
import modernity.generic.event.ModernityInitializedEvent;
import modernity.client.ModernityClient;
import modernity.common.loot.MDLootTables;
import modernity.common.registry.RegistryEventHandler;
import modernity.data.ModernityData;
import modernity.data.ModernityDataGenerator;
import modernity.server.ModernityServer;
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
 * server. A special {@link ModernityData} instance is created when running in data generation mode.
 */
@Mod( "modernity" )
public class ModernityBootstrap {
    private static final Logger LOGGER = LogManager.getLogger( "ModernityBootstrap" );

    public static final RunMode MODE;

    public ModernityBootstrap() {
        if( MODE == RunMode.DATA ) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener( ModernityDataGenerator::gather );
            MDLootTables.register();
        }
        LOGGER.info( "Modernity starting in " + MODE + " mode..." );

        IModernity.EVENT_BUS.start();
        initProxy();

        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::loadComplete );
        FMLJavaModLoadingContext.get().getModEventBus().register( RegistryEventHandler.INSTANCE );
        MinecraftForge.EVENT_BUS.register( RegistryEventHandler.INSTANCE );
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
     * Initializes the proxy for the specified side. This calls {@link IModernity#registerListeners()}, then {@link
     * IModernity#preInit()} and casts an event on the forge event bus indicating that the Modernity is initialized.
     */
    private void initProxy() {
        IModernity modernity = instantiate();
        if( modernity != null ) {
            MinecraftForge.EVENT_BUS.register( modernity );
            FMLJavaModLoadingContext.get().getModEventBus().register( modernity );

            modernity.registerListeners();
            modernity.preInit();

            LOGGER.info( "Modernity version {} initialized for {} mode: {}", MDInfo.VERSION, MODE, modernity.getClass().getName() );

            IModernity.EVENT_BUS.post( new ModernityInitializedEvent( MODE, modernity ) );
        } else {
            throw new IllegalStateException( "No modernity instance generated... Reflection is broken?" );
        }
    }

    private IModernity instantiate() {
        try {
            IModernity inst = (IModernity) Class.forName( MODE.getClassName() ).newInstance();
            IModernity.set( inst );
            return inst;
        } catch( Throwable e ) {
            throw new IllegalStateException( "Unable to instantiate " + MODE.getClassName(), e );
        }
    }

    static {
        String lhn = FMLLoader.launcherHandlerName();
        if( lhn.equals( "fmluserdevdata" ) || lhn.equals( "fmldevdata" ) ) {
            MODE = RunMode.DATA;
        } else {
            if( FMLEnvironment.dist == Dist.CLIENT ) {
                MODE = RunMode.CLIENT;
            } else {
                MODE = RunMode.SERVER;
            }
        }
    }
}
