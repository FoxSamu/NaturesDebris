/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package net.minecraftforge.userdev;

import net.minecraftforge.fml.loading.LibraryFinder;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.loading.LogMarkers.*;

public class ClasspathLocator extends AbstractJarFileLocator {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String MODS_TOML = "META-INF/mods.toml";
    private List<Path> modCoords;

    @Override
    public List<ModFile> scanMods() {
        return modCoords.stream().
                map( mc -> new ModFile( mc, this ) ).
                                peek( f -> modJars.compute( f, ( mf, fs ) -> createFileSystem( mf ) ) ).
                                collect( Collectors.toList() );
    }

    @Override
    public String name() {
        return "userdev classpath";
    }

    @Override
    public void initArguments( Map<String, ?> arguments ) {
        try {
            modCoords = new ArrayList<>();
            final Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources( MODS_TOML );
            while( resources.hasMoreElements() ) {
                URL url = resources.nextElement();
                Path path = LibraryFinder.findJarPathFor( MODS_TOML, "classpath_mod", url );
                if( Files.isDirectory( path ) )
                    continue;

                this.modCoords.add( path );
            }
        } catch( IOException e ) {
            LOGGER.fatal( CORE, "Error trying to find resources", e );
            throw new RuntimeException( "wha?", e );
        }
    }
}
