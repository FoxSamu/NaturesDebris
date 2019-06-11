/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package net.rgsw.noise;

public class Hash2D {
    private final int seed;

    public Hash2D( int seed ) {
        this.seed = seed;
    }

    public int getSeed() {
        return this.seed;
    }

    public int getInt( int x, int y ) {
        int i = Hash.hash2Dint( this.seed, x, y );
        return i == Integer.MIN_VALUE ? 0 : i < 0 ? i + Integer.MAX_VALUE : i;
    }

    public int getInt( int x, int y, int bound ) {
        return this.getInt( x, y ) % bound;
    }

    public double getDouble( int x, int y ) {
        return ( Hash.hash2D( this.seed, x, y ) + 1 ) / 2D;
    }

    public double getDouble( int x, int y, double bound ) {
        return this.getDouble( x, y ) * bound;
    }

    public boolean getBoolean( int x, int y ) {
        return ( this.getInt( x, y ) & 1 ) == 0;
    }

    public long getLong( int x, int y ) {
        long i0 = Hash.hash2Dint( this.seed, x, y );
        long i1 = Hash.hash2Dint( ~ this.seed, x, y );
        long i = i0 << 32 & i1;
        return i < 0 ? i - Long.MIN_VALUE : i;
    }

    public long getLong( int x, int y, long bound ) {
        return this.getLong( x, y ) % bound;
    }

    public float getFloat( int x, int y ) {
        return (float) this.getDouble( x, y );
    }

    public float getFloat( int x, int y, double bound ) {
        return (float) this.getDouble( x, y, bound );
    }
}
