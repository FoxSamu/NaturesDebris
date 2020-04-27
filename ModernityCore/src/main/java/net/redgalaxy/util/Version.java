/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package net.redgalaxy.util;

import java.util.Arrays;

public class Version implements Comparable<Version> {
    private final int[] version;
    private final String[] prefixOrder;

    public Version( int[] version, String... prefixOrder ) {
        this.version = version;
        this.prefixOrder = prefixOrder;
    }

    public Version( String versionString, String... prefixOrder ) {
        this.prefixOrder = prefixOrder;

        int minus = versionString.indexOf( '-' );
        int firstVersion = prefixOrder.length;
        if( minus > 0 ) {
            String prefix = versionString.substring( 0, minus );

            for( int i = 0; i < prefixOrder.length; i++ ) {
                if( prefix.equalsIgnoreCase( prefixOrder[ i ] ) ) {
                    firstVersion = i;
                }
            }
        }

        String num = versionString.substring( minus + 1 );

        String[] versions = num.split( "\\." );

        int[] versionNums = new int[ versions.length + 1 ];
        versionNums[ 0 ] = firstVersion;

        for( int i = 0; i < versions.length; i++ ) {
            versionNums[ i + 1 ] = Integer.parseInt( versions[ i ] );
        }

        version = versionNums;
    }

    @Override
    public String toString() {
        int firstVer = version[ 0 ];
        String prefix = "";
        if( firstVer < prefixOrder.length ) {
            prefix = prefixOrder[ firstVer ] + "-";
        }
        StringBuilder builder = new StringBuilder( prefix );
        for( int i = 1; i < version.length; i++ ) {
            builder.append( version[ i ] );
        }
        return builder.toString();
    }

    @Override
    public int compareTo( Version v ) {
        int len = Integer.compare( version.length, v.version.length );
        if( len != 0 ) return len;
        for( int i = 0; i < version.length; i++ ) {
            int ver = version[ i ];
            int otherVer = v.version[ i ];

            int comp = Integer.compare( ver, otherVer );
            if( comp != 0 ) {
                return comp;
            }
        }
        return 0;
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) return true;
        if( ! ( o instanceof Version ) ) return false;
        Version ver = (Version) o;
        return compareTo( ver ) == 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode( version );
    }

    public static boolean compare( String range, Version version ) {
        range = range.replaceAll( "\\s+", "" );
        if( range.startsWith( "[" ) && range.endsWith( "]" ) ) {
            String[] versions = range.substring( 1, range.length() - 1 )
                                     .split( "," );
            for( String verName : versions ) {
                if( compare( verName, version ) ) {
                    return true;
                }
            }

            return false;
        }

        int r = range.indexOf( "--" );
        if( r >= 0 ) {
            String left = range.substring( 0, r );
            String right = range.substring( r + 2 );

            Version lver = new Version( left );
            Version rver = new Version( right );

            return lver.compareTo( version ) <= 0 && rver.compareTo( version ) >= 0;
        }

        if( range.endsWith( "+" ) ) {
            String left = range.substring( 0, range.length() - 1 );
            Version lver = new Version( left );
            return lver.compareTo( version ) <= 0;
        }

        if( range.endsWith( "-" ) ) {
            String left = range.substring( 0, range.length() - 1 );
            Version lver = new Version( left );
            return lver.compareTo( version ) >= 0;
        }

        if( range.startsWith( "<" ) ) {
            String right = range.substring( 1 );
            Version rver = new Version( right );
            return rver.compareTo( version ) > 0;
        }

        if( range.startsWith( ">" ) ) {
            String right = range.substring( 1 );
            Version rver = new Version( right );
            return rver.compareTo( version ) < 0;
        }

        Version ver = new Version( range );
        return ver.compareTo( version ) == 0;
    }
}
