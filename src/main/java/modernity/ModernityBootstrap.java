/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity;

import modernity.api.event.ModernityReadyEvent;
import modernity.common.Modernity;
import modernity.common.registry.RegistryEventHandler;
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

@Mod( "modernity" )
@SuppressWarnings( "unused" )
public class ModernityBootstrap {
    private static final Logger LOGGER = LogManager.getLogger( "ModernityBootstrap" );

    private static Modernity proxy;

    public ModernityBootstrap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::loadComplete );
        FMLJavaModLoadingContext.get().getModEventBus().register( RegistryEventHandler.INSTANCE );
        if( FMLEnvironment.dist == Dist.CLIENT ) {
            clientSetup();
        } else {
            serverSetup();
        }
    }

    private void setup( FMLCommonSetupEvent event ) {
        proxy.init();
    }

    private void clientSetup() {
        try {
            Class cls = Class.forName( "modernity.client.ModernityClient" );
            proxy = (Modernity) cls.newInstance();
        } catch( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            throw new IllegalStateException( "Unable to instantiate ModernityClient", e );
        }
        initProxy( LogicalSide.CLIENT );
    }

    private void serverSetup() {
        try {
            Class cls = Class.forName( "modernity.server.ModernityServer" );
            proxy = (Modernity) cls.newInstance();
        } catch( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            throw new IllegalStateException( "Unable to instantiate ModernityServer", e );
        }
        initProxy( LogicalSide.SERVER );
    }

    private void loadComplete( FMLLoadCompleteEvent event ) {
        proxy.postInit();
    }


    private void initProxy( LogicalSide side ) {
        proxy.registerListeners();
        proxy.preInit();
        MinecraftForge.EVENT_BUS.register( proxy );
        LOGGER.info( "Modernity version {} initialized for side {}: {}", MDInfo.VERSION, side, proxy );
        MinecraftForge.EVENT_BUS.post( new ModernityReadyEvent( side, proxy ) );
    }
}
