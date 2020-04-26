/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 24 - 2020
 * Author: rgsw
 */

package modernity.generic.names;

import java.util.Arrays;

import static modernity.generic.names.ModernityNameCodes.*;

public class ModernityName implements IModernityName {
    private final int[] codes;
    private final int length;

    public ModernityName( int[] codes ) {
        this.codes = Arrays.copyOf( codes, codes.length );
        this.length = codes.length;
    }

    public ModernityName( int[] codes, int start, int length ) {
        this.codes = new int[ length ];
        System.arraycopy( codes, start, this.codes, 0, length );
        this.length = length;
    }

    public ModernityName( String string ) {
        this( evaluate( string ) );
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for( int i : codes ) {
            builder.append( getAsciiCode( i ) );
        }
        return builder.toString();
    }

    public String toUnicodeString() {
        StringBuilder builder = new StringBuilder();
        for( int i : codes ) {
            builder.append( getUnicodeCode( i ) );
        }
        return builder.toString();
    }

    public String toPhonemeString() {
        StringBuilder builder = new StringBuilder();
        boolean space = false;
        for( int i : codes ) {
            if( space ) builder.append( " " );
            space = true;
            builder.append( getAsciiCode( i ).toUpperCase() );
        }
        return builder.toString();
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
    public int[] getCodes() {
        return Arrays.copyOf( codes, length );
    }

    public ModernityName subname( int start ) {
        if( start < 0 || start > length ) throw new IndexOutOfBoundsException( start + "" );
        return new ModernityName( codes, start, length - start );
    }

    @Override
    public ModernityName subname( int start, int end ) {
        if( start < 0 || start > length ) throw new IndexOutOfBoundsException( start + "" );
        if( end < 0 || end > length ) throw new IndexOutOfBoundsException( start + "" );
        if( start > end ) throw new IllegalArgumentException( "start > end" );
        return new ModernityName( codes, start, end - start );
    }

    public static int[] evaluate( String string ) {
        int length = 0;
        int[] codes = new int[ string.length() ];

        int sl = string.length();
        for( int i = 0; i < sl; ) {
            char A = string.charAt( i++ );
            char a = Character.toLowerCase( A );
            char b = i == sl ? 0 : Character.toLowerCase( string.charAt( i ) );

            if( a == ' ' ) continue;

            int capital = Character.isUpperCase( A ) ? CAPTIAL : 0;

            if( b != 0 ) {
                int combi = combination( a, b );
                if( combi != INVALID ) {
                    codes[ length ] = combi + capital;
                    length++;
                    i++;
                    continue;
                }
            }

            int single = single( a );
            if( single != INVALID ) {
                codes[ length ] = single + capital;
                length++;
                continue;
            }

            throw new IllegalArgumentException( "Can't evaluate, invalid character: '" + a + "'" );
        }

        return Arrays.copyOf( codes, length );
    }
}
