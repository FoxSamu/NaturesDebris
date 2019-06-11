/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package net.rgsw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class CopyFiles {
    private static String from;
    private static String to;

    public static void main( String[] args ) throws IOException {
        if( args.length < 2 ) {
            throw new RuntimeException( "Args: <from> <to> [folder]..." );
        }


        from = args[ 0 ];
        to = args[ 1 ];
//        System.out.println( from );
//        System.out.println( to );
//        System.out.println( transform( new File( from, "abc" ) ) );

        ArrayList<String> folders = new ArrayList<>( Arrays.asList( args ).subList( 2, args.length ) );
        if( folders.isEmpty() ) {
            copyFolder( new File( from ), new File( to ) );
            return;
        }

        for( String str : folders ) {
            copy( str );
        }
    }

    public static void copy( String name ) throws IOException {
        File fromFile = new File( from, name );
        File toFile = new File( to, name );

        if( fromFile.isFile() ) {
            copyFile( fromFile, toFile );
        } else {
            copyFolder( fromFile, toFile );
        }
    }

    public static File transform( File fromFile ) {
        String f = fromFile.toString();
        if( f.startsWith( from ) ) {
            f = f.replace( from, to );
        }
        return new File( f );
    }

    public static void copyFolder( File from, File to ) throws IOException {
        if( from.exists() && from.isDirectory() ) {
            File[] files = from.listFiles();
            assert files != null;
            for( File f : files ) {
                if( f.isFile() ) {
                    copyFile( f, transform( f ) );
                } else {
                    copyFolder( f, transform( f ) );
                }
            }
        }
    }

    public static void copyFile( File from, File to ) throws IOException {
        if( from.exists() && ! from.isDirectory() ) {
            if( ! to.exists() ) {
                to.getParentFile().mkdirs();
                to.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream( to );
            FileInputStream fis = new FileInputStream( from );
            int i;

            while( ( i = fis.read() ) >= 0 ) {
                fos.write( i );
            }
            System.out.printf( "Copied %s to %s\n", from, to );
        }
    }
}
