/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul.core;

import modul.Modul;
import modul.root.ModulRoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class ModulCore {
    private static final Logger LOGGER = LogManager.getLogger( "Modul" );
    private static ModulRoot root;
    private static Modul modul;

    private ModulCore() {
    }

    private static List<Class<?>> classesToInstantiate( MListFile.Context ctx ) throws Exception {
        ArrayList<Class<?>> classes = new ArrayList<>();

        Enumeration<URL> resources = ModulCore.class.getClassLoader().getResources( "META-INF/modul/modul.mlist" );

        while( resources.hasMoreElements() ) {
            URL resource = resources.nextElement();

            InputStream stream = resource.openStream();

            MListFile file = MListFile.load( ctx, stream );

            for( String str : file ) {
                try {
                    classes.add( Class.forName( str ) );
                } catch( ClassNotFoundException exc ) {
                    LOGGER.warn( "Class not found: '{}' (file: '{}')", str, resource.toExternalForm() );
                }
            }
        }

        LOGGER.info( "Modul is going to load {} plugins", classes.size() );

        return classes;
    }

    public static MListFile.Context context() {
        return root.buildCoreContext( MListFile.context()
                                               .withCondition( MListFile.not( "NOT" ) ) );
    }

    public static void start( ModulRoot r ) {
        root = r;
        try {
            List<Class<?>> classes = classesToInstantiate( context() );
            modul = new Modul( root, classes );
        } catch( Exception e ) {
            throw new IllegalStateException( "Can't instantiate Modul!", e );
        }

        modul.startUp();
    }

    public static Modul modul() {
        return modul;
    }
}
