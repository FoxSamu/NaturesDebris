/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs.generator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FileCopy {
    private final File file;
    private final File to;
    private final HashMap<String, String> props = new HashMap<>();

    public FileCopy( String file, String to ) {
        this.file = new File( Generator.ROOT, file );
        this.to = new File( Generator.ROOT, to );
    }

    public FileCopy property( String name, String value ) {
        props.put( name, value );
        return this;
    }

    public void doCopy() {
        System.out.println( "Copying " + file + " to " + to );
        if( ! to.exists() ) {
            to.getParentFile().mkdirs();
        }

        try {
            FileReader reader = new FileReader( file );

            StringBuffer builder = new StringBuffer();

            System.out.println( " - Reading..." );
            int read;
            while( ( read = reader.read() ) >= 0 ) {
                builder.append( (char) read );
            }

            for( String str : props.keySet() ) {
                System.out.println( " - Property: '" + str + "'..." );
                String var = "$[" + str + "]";
                int len = var.length();

                int i;
                while( ( i = builder.indexOf( var ) ) >= 0 ) {
                    builder.replace( i, i + len, props.get( str ) );
                }
            }
            System.out.println( " - Writing..." );

            FileWriter writer = new FileWriter( to );
            writer.append( builder );
            writer.close();

        } catch( IOException exc ) {
            throw new RuntimeException( exc );
        }
        System.out.println( " - Done!" );
    }
}
