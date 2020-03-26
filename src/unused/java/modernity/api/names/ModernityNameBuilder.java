/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 24 - 2020
 * Author: rgsw
 */

package modernity.api.names;

import java.util.Arrays;

import static modernity.api.names.ModernityNameCodes.*;

public class ModernityNameBuilder implements IModernityName {
    private int length;
    private int[] codes = new int[ 10 ];

    public ModernityNameBuilder() {
    }

    public ModernityNameBuilder( int... codes ) {
        append( codes );
    }

    public ModernityNameBuilder( String str ) {
        append( str );
    }

    private void ensureSpace() {
        if( length > codes.length ) {
            int[] newCodes = new int[ length + 10 ];
            System.arraycopy( codes, 0, newCodes, 0, codes.length );
            codes = newCodes;
        }
    }

    private void shiftRight( int from, int count ) {
        length += count;
        ensureSpace();
        System.arraycopy( codes, from, codes, from + count, codes.length - count - from );
    }

    private void shiftLeft( int from, int count ) {
        length -= count;
        System.arraycopy( codes, from + count, codes, from, codes.length - count - from );
    }

    private void put( int index, int... newCodes ) {
        System.arraycopy( newCodes, 0, codes, index, newCodes.length );
    }

    public ModernityNameBuilder append( int... codes ) {
        int index = length;
        length += codes.length;
        ensureSpace();
        put( index, codes );
        return this;
    }

    public ModernityNameBuilder prepend( int... codes ) {
        shiftRight( 0, codes.length );
        put( 0, codes );
        return this;
    }

    public ModernityNameBuilder insert( int index, int... codes ) {
        shiftRight( index, codes.length );
        put( index, codes );
        return this;
    }

    public ModernityNameBuilder overwrite( int index, int... codes ) {
        length = Math.max( length, index + codes.length );
        ensureSpace();
        put( index, codes );
        return this;
    }

    public ModernityNameBuilder remove( int index, int length ) {
        shiftLeft( index, length );
        return this;
    }

    public ModernityNameBuilder append( String str ) {
        append( ModernityName.evaluate( str ) );
        return this;
    }

    public ModernityNameBuilder prepend( String str ) {
        prepend( ModernityName.evaluate( str ) );
        return this;
    }

    public ModernityNameBuilder insert( int index, String str ) {
        insert( index, ModernityName.evaluate( str ) );
        return this;
    }

    public ModernityNameBuilder overwrite( int index, String str ) {
        overwrite( index, ModernityName.evaluate( str ) );
        return this;
    }

    public ModernityNameBuilder append( ModernityName str ) {
        append( str.getCodes() );
        return this;
    }

    public ModernityNameBuilder prepend( ModernityName str ) {
        prepend( str.getCodes() );
        return this;
    }

    public ModernityNameBuilder insert( int index, ModernityName str ) {
        insert( index, str.getCodes() );
        return this;
    }

    public ModernityNameBuilder overwrite( int index, ModernityName str ) {
        overwrite( index, str.getCodes() );
        return this;
    }

    public ModernityNameBuilder setLength( int length ) {
        this.length = length;
        ensureSpace();
        return this;
    }

    public ModernityNameBuilder setCodeAt( int index, int code ) {
        if( index < 0 || index >= length ) throw new IndexOutOfBoundsException( index + "" );
        codes[ index ] = code;
        return this;
    }

    @Override
    public int[] getCodes() {
        return Arrays.copyOf( codes, length );
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        int j = 0;
        for( int i : codes ) {
            j++;
            builder.append( getAsciiCode( i ) );
            if( j == length ) return builder.toString();
        }
        return builder.toString();
    }

    public String toUnicodeString() {
        StringBuilder builder = new StringBuilder();
        int j = 0;
        for( int i : codes ) {
            j++;
            builder.append( getUnicodeCode( i ) );
            if( j == length ) return builder.toString();
        }
        return builder.toString();
    }

    public ModernityName build() {
        return new ModernityName( codes, 0, length );
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public int codeAt( int index ) {
        if( index < 0 || index >= length ) throw new IndexOutOfBoundsException( index + "" );
        return codes[ index ];
    }

    @Override
    public IModernityName subname( int start, int end ) {
        if( start < 0 || start > length ) throw new IndexOutOfBoundsException( start + "" );
        if( end < 0 || end > length ) throw new IndexOutOfBoundsException( start + "" );
        if( start > end ) throw new IllegalArgumentException( "start > end" );
        return new ModernityName( codes, start, end - start );
    }
}
