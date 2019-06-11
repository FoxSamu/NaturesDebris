/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package net.minecraftforge.userdev;

import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import net.minecraftforge.api.distmarker.Dist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

import static net.minecraftforge.fml.loading.LogMarkers.*;

public class FMLUserdevServerLaunchProvider extends FMLUserdevLaunchProvider implements ILaunchHandlerService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Dist getDist() {
        return Dist.DEDICATED_SERVER;
    }


    @Override
    public String name() {
        return "fmluserdevserver";
    }

    @Override
    public Callable<Void> launchService( String[] arguments, ITransformingClassLoader launchClassLoader ) {
        return () -> {
            LOGGER.debug( CORE, "Launching minecraft in {} with arguments {}", launchClassLoader, arguments );
            beforeStart( launchClassLoader );
            launchClassLoader.addTargetPackageFilter( getPackagePredicate() );
            Thread.currentThread().setContextClassLoader( launchClassLoader.getInstance() );
            Class.forName( "net.minecraft.server.MinecraftServer", true, launchClassLoader.getInstance() ).getMethod( "main", String[].class ).invoke( null, (Object) arguments );
            return null;
        };
    }
}
