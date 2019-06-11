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
import cpw.mods.modlauncher.api.ITransformingClassLoaderBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.LibraryFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

import static net.minecraftforge.fml.loading.LogMarkers.*;

public class FMLUserdevClientLaunchProvider extends FMLUserdevLaunchProvider implements ILaunchHandlerService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Dist getDist() {
        return Dist.CLIENT;
    }

    @Override
    public String name() {
        return "fmluserdevclient";
    }

    @Override
    public Callable<Void> launchService( String[] arguments, ITransformingClassLoader launchClassLoader ) {
        return () -> {
            LOGGER.debug( CORE, "Launching minecraft in {} with arguments {}", launchClassLoader, arguments );
            beforeStart( launchClassLoader );
            launchClassLoader.addTargetPackageFilter( getPackagePredicate() );
            Class.forName( "net.minecraft.client.main.Main", true, launchClassLoader.getInstance() ).getMethod( "main", String[].class ).invoke( null, (Object) arguments );
            return null;
        };
    }

    @Override
    public void configureTransformationClassLoader( final ITransformingClassLoaderBuilder builder ) {
        super.configureTransformationClassLoader( builder );
        builder.addTransformationPath( LibraryFinder.findJarPathFor( "com/mojang/realmsclient/RealmsVersion.class", "realms" ) );
    }
}
