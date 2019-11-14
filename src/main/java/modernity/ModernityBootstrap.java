/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity;

import modernity.api.event.ModernityReadyEvent;
import modernity.client.ModernityClient;
import modernity.common.Modernity;
import modernity.common.registry.RegistryEventHandler;
import modernity.server.ModernityServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bootstrap class of the Modernity, which handles setup events and creates the {@link Modernity} instance for the side
 * we're running on ({@link ModernityClient} for the client and {@link ModernityServer} for the dedicated server).
 */
@Mod( "modernity" )
@SuppressWarnings( "unused" )
public class ModernityBootstrap {
    private static final Logger LOGGER = LogManager.getLogger( "ModernityBootstrap" );

    private static Modernity proxy;

    public ModernityBootstrap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::loadComplete );
        FMLJavaModLoadingContext.get().getModEventBus().register( RegistryEventHandler.INSTANCE );
        MinecraftForge.EVENT_BUS.register( RegistryEventHandler.INSTANCE );
        if( FMLEnvironment.dist == Dist.CLIENT ) {
            clientSetup();
        } else {
            serverSetup();
        }
    }

    /**
     * Calls {@link Modernity#init()} on our proxy.
     */
    private void setup( FMLCommonSetupEvent event ) {
        proxy.init();
    }

    /**
     * Creates {@link ModernityClient}. Uses reflection because it may not be available while Java still checks the use
     * of it.
     */
    private void clientSetup() {
        try {
            Class cls = Class.forName( "modernity.client.ModernityClient" );
            proxy = (Modernity) cls.newInstance();
        } catch( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            throw new IllegalStateException( "Unable to instantiate ModernityClient", e );
        }
        initProxy( LogicalSide.CLIENT );
    }

    /**
     * Creates {@link ModernityServer}. Uses reflection because it may not be available while Java still checks the use
     * of it.
     */
    private void serverSetup() {
        try {
            Class cls = Class.forName( "modernity.server.ModernityServer" );
            proxy = (Modernity) cls.newInstance();
        } catch( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            throw new IllegalStateException( "Unable to instantiate ModernityServer", e );
        }
        initProxy( LogicalSide.SERVER );
    }

    /**
     * Calls {@link Modernity#postInit()}.
     */
    private void loadComplete( FMLLoadCompleteEvent event ) {
        proxy.postInit();
    }


    /**
     * Initializes the proxy for the specified side. This calls {@link Modernity#registerListeners()}, then {@link
     * Modernity#preInit()} and casts an event on the forge event bus indicating that the Modernity is initialized.
     */
    private void initProxy( LogicalSide side ) {
        MinecraftForge.EVENT_BUS.register( proxy );
        proxy.registerListeners();
        proxy.preInit();
        LOGGER.info( "Modernity version {} initialized for side {}: {}", MDInfo.VERSION, side, proxy );
        MinecraftForge.EVENT_BUS.post( new ModernityReadyEvent( side, proxy ) );
    }
}
