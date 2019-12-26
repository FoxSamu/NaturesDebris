/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modernity;

import modernity.api.event.ModernityReadyEvent;
import modernity.client.ModernityClient;
import modernity.common.Modernity;
import modernity.common.registry.RegistryEventHandler;
import modernity.server.ModernityServer;
import modul.Modul;
import modul.core.MListFile;
import modul.core.ModulCore;
import modul.root.ModulRoot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.redgalaxy.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bootstrap class of the Modernity, which handles setup events and creates the {@link Modernity} instance for the side
 * we're running on ({@link ModernityClient} for the client and {@link ModernityServer} for the dedicated server).
 */
@Mod( "modernity" )
public class ModernityBootstrap implements ModulRoot {
    private static final Logger LOGGER = LogManager.getLogger( "ModernityBootstrap" );

    public ModernityBootstrap() {
        ModulCore.start( this );
        MDModules.load();
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::setup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::loadComplete );
        FMLJavaModLoadingContext.get().getModEventBus().register( RegistryEventHandler.INSTANCE );
        MinecraftForge.EVENT_BUS.register( RegistryEventHandler.INSTANCE );
    }

    @Override
    public void onStart( Modul modul ) {
        initProxy( FMLEnvironment.dist == Dist.CLIENT ? LogicalSide.CLIENT : LogicalSide.SERVER );
    }

    /**
     * Calls {@link Modernity#init()} on our proxy.
     */
    private void setup( FMLCommonSetupEvent event ) {
        Modernity.get().init();
    }

    /**
     * Calls {@link Modernity#postInit()}.
     */
    private void loadComplete( FMLLoadCompleteEvent event ) {
        Modernity.get().postInit();
    }


    /**
     * Initializes the proxy for the specified side. This calls {@link Modernity#registerListeners()}, then {@link
     * Modernity#preInit()} and casts an event on the forge event bus indicating that the Modernity is initialized.
     */
    private void initProxy( LogicalSide side ) {
        MinecraftForge.EVENT_BUS.register( Modernity.get() );
        Modernity.get().registerListeners();
        Modernity.get().preInit();
        LOGGER.info( "Modernity version {} initialized for side {}: {}", MDInfo.VERSION, side, Modernity.get() );
        MinecraftForge.EVENT_BUS.post( new ModernityReadyEvent( side, Modernity.get() ) );
    }

    @Override
    public MListFile.Context buildCoreContext( MListFile.Context ctx ) {
        Version modernity = new Version( MDInfo.VERSION, "INDEV" );
        Version minecraft = new Version( "1.14.4" );
        Version forge = new Version( ForgeVersion.getVersion() );
        LogicalSide side = FMLEnvironment.dist == Dist.CLIENT ? LogicalSide.CLIENT : LogicalSide.SERVER;

        return ctx.withCondition( MListFile.prefix( "DIST", str -> str.equalsIgnoreCase( side.name() ) ) )
                  .withCondition( MListFile.prefix( "VERSION_MODERNITY", str -> Version.compare( str, modernity ) ) )
                  .withCondition( MListFile.prefix( "VERSION_MINECRAFT", str -> Version.compare( str, minecraft ) ) )
                  .withCondition( MListFile.prefix( "VERSION_FORGE", str -> Version.compare( str, forge ) ) );
    }

    @Override
    public Object instantiate( Class<?> cls ) {
        try {
            return cls.newInstance();
        } catch( InstantiationException | IllegalAccessException e ) {
            throw new IllegalStateException( "Unable to instantiate " + cls, e );
        }
    }
}
