/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors;

// TODO Re-evaluate
//public class ColorResourceLocation extends ResourceLocation {
//    private final String color;
//
//    protected ColorResourceLocation( String[] pathItems ) {
//        super( pathItems );
//        color = pathItems[ 2 ].toLowerCase( Locale.ROOT );
//    }
//
//    public ColorResourceLocation( String path ) {
//        this( parsePathString( path ) );
//    }
//
//    public ColorResourceLocation( ResourceLocation location, String color ) {
//        this( new String[] { location.getNamespace(), location.getPath(), color } );
//    }
//
//    public ColorResourceLocation( String location, String color ) {
//        this( parsePathString( location + '#' + color ) );
//    }
//
//    protected static String[] parsePathString( String path ) {
//        String[] out = { null, path, "" };
//        int hash = path.indexOf( '#' );
//
//        String p = path;
//        if( hash >= 0 ) {
//            out[ 2 ] = path.substring( hash + 1 );
//            if( hash > 1 ) {
//                p = path.substring( 0, hash );
//            }
//        }
//
//        System.arraycopy( ResourceLocation.decompose( p, ':' ), 0, out, 0, 2 );
//        return out;
//    }
//
//    public String getColor() {
//        return this.color;
//    }
//
//    public boolean equals( Object obj ) {
//        if( this == obj ) {
//            return true;
//        } else if( obj instanceof ColorResourceLocation && super.equals( obj ) ) {
//            ColorResourceLocation crl = (ColorResourceLocation) obj;
//            return color.equals( crl.color );
//        } else {
//            return false;
//        }
//    }
//
//    public int hashCode() {
//        return 31 * super.hashCode() + color.hashCode();
//    }
//
//    public String toString() {
//        return super.toString() + '#' + color;
//    }
//}