/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity;

import modernity.api.event.ModernityProxyReadyEvent;
import modernity.common.handler.RegistryHandler;
import modernity.common.util.ProxyCommon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod( "modernity" )
@SuppressWarnings( "unused" )
public class Modernity {
    private static final Logger LOGGER = LogManager.getLogger( "Modernity" );

    private static ProxyCommon proxy;

    public Modernity() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::clientSetup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::serverSetup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::loadComplete );

        FMLJavaModLoadingContext.get().getModEventBus().register( new RegistryHandler() );
    }

    private void clientSetup( FMLClientSetupEvent event ) {
        try {
            Class cls = Class.forName( "modernity.client.util.ProxyClient" );
            proxy = (ProxyCommon) cls.newInstance();
        } catch( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            throw new IllegalStateException( "Unable to instantiate ProxyClient", e );
        }
        initProxy( LogicalSide.CLIENT );
    }

    private void serverSetup( FMLDedicatedServerSetupEvent event ) {
        try {
            Class cls = Class.forName( "modernity.server.ProxyServer" );
            proxy = (ProxyCommon) cls.newInstance();
        } catch( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            throw new IllegalStateException( "Unable to instantiate ProxyServer", e );
        }
        initProxy( LogicalSide.SERVER );
    }

    private void loadComplete( FMLLoadCompleteEvent event ) {
        proxy.loadComplete();
    }


    private void initProxy( LogicalSide side ) {
        proxy.registerListeners();
        proxy.init();
        MinecraftForge.EVENT_BUS.register( proxy );
        LOGGER.info( "Modernity proxy initialized for side {}: {}", side, proxy );
        MinecraftForge.EVENT_BUS.post( new ModernityProxyReadyEvent( side, proxy ) );
    }
}
