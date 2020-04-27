/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modul.module;

import modul.core.MListFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings( "unchecked" )
public class ModuleContext {
    private static final Logger LOGGER = LogManager.getLogger( "Modul" );

    private final List<ModuleType<?, ?>> moduleTypes = new ArrayList<>();
    private final String name;

    private final Supplier<MListFile.Context> contextSupplier;

    private boolean loaded;

    public ModuleContext( String contextName ) {
        this.name = contextName;
        contextSupplier = MListFile::context;
    }

    public ModuleContext( String contextName, Supplier<MListFile.Context> ctxBuilder ) {
        this.name = contextName;
        contextSupplier = ctxBuilder;
    }

    public void load() {
        if( loaded ) return;
        moduleTypes.clear();

        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources( "META-INF/modul/" + name + ".mlist" );

            while( resources.hasMoreElements() ) {
                URL resource = resources.nextElement();
                InputStream stream = resource.openStream();

                try {
                    MListFile mlist = MListFile.load( contextSupplier.get(), stream );

                    for( String str : mlist ) {
                        ModuleType<?, ?> type = ModuleType.getByName( str );
                        if( type == null ) {
                            LOGGER.warn( "Unknown module '{}' (file: '{}')", str, resource.toExternalForm() );
                        } else {
                            moduleTypes.add( type );
                        }
                    }
                } catch( IOException exc ) {
                    LOGGER.error( "Unable to load resource: " + resource.toExternalForm(), exc );
                } catch( Exception exc ) {
                    LOGGER.error( "Malformed MList format: {} (file: '{}')", exc.getMessage(), resource.toExternalForm() );
                }
            }

            LOGGER.info( "Loaded {} modules for context '{}'", moduleTypes.size(), name );
        } catch( IOException exception ) {
            LOGGER.error( "Could not load context '" + name + "' because of IO problems", exception );
        }

        loaded = true;
    }

    public <T> Iterable<ModuleType<T, ?>> getModuleTypes() {
        if( ! loaded ) load();
        return moduleTypes.stream()
                          .map( moduleType -> (ModuleType<T, ?>) moduleType )
                          .collect( Collectors.toList() );
    }
}
