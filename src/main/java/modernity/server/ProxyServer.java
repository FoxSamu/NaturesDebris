/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.server;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import modernity.common.settings.DedicatedServerSettings;
import modernity.common.util.ProxyCommon;

// Currently, the only thing we use a server proxy for are the dedicated server settings, as they are only needed on the
// dedicated server... When doing this on the common proxy, the dedicated server settings will load on the client and we
// don't want that. So special server case because eww...
public class ProxyServer extends ProxyCommon {
    private static final Logger LOGGER = LogManager.getLogger();
    private final DedicatedServerSettings serverSettings = new DedicatedServerSettings( DedicatedServerSettings.DEFAULT_LOCATION );

    @Override
    public void init() {
        super.init();
//        serverSettings.load( false );
//        Thread t = new Thread( () -> serverSettings.save( false ) );
//        t.setUncaughtExceptionHandler( new DefaultUncaughtExceptionHandler( LOGGER ) );
//        Runtime.getRuntime().addShutdownHook( t );
    }

    @Override
    public void serverAboutToStart( FMLServerAboutToStartEvent event ) {
        super.serverAboutToStart( event );
        serverSettings.load( false );
    }

    @Override
    public void serverStop( FMLServerStoppedEvent event ) {
        super.serverStop( event );
        serverSettings.save( false );
    }

    public DedicatedServer getDedicatedServer() {
        return (DedicatedServer) getServer();
    }

    public static ProxyServer get() {
        return (ProxyServer) ProxyCommon.get();
    }

    @Override
    public DedicatedServerSettings getServerSettings() {
        return serverSettings;
    }
}
