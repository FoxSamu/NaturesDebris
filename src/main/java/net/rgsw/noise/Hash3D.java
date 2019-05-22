/*
 * Copyright (c) 2018 Red Galaxy Software.
 * Licensed under Apache License 2.0.
 *
 * By  : RgSW
 * Date: 1 - 15 - 2019
 */

package net.rgsw.noise;

public class Hash3D {
    private final int seed;

    public Hash3D( int seed ) {
        this.seed = seed;
    }

    public int getSeed() {
        return this.seed;
    }

    public int getInt( int x, int y, int z ) {
        int i = Hash.hash3Dint( this.seed, x, y, z );
        return i < 0 ? i - Integer.MIN_VALUE : i;
    }

    public int getInt( int x, int y, int z, int bound ) {
        return this.getInt( x, y, z ) % bound;
    }

    public double getDouble( int x, int y, int z ) {
        return ( Hash.hash3D( this.seed, x, y, z ) + 1 ) / 2D;
    }

    public double getDouble( int x, int y, int z, double bound ) {
        return this.getDouble( x, y, z ) * bound;
    }

    public boolean getBoolean( int x, int y, int z ) {
        return ( this.getInt( x, y, z ) & 1 ) == 0;
    }

    public long getLong( int x, int y, int z ) {
        long i0 = Hash.hash3Dint( this.seed, x, y, z );
        long i1 = Hash.hash3Dint( ~ this.seed, x, y, z );
        long i = i0 << 32 & i1;
        return i < 0 ? i - Long.MIN_VALUE : i;
    }

    public long getLong( int x, int y, int z, long bound ) {
        return this.getLong( x, y, z ) % bound;
    }

    public float getFloat( int x, int y, int z ) {
        return (float) this.getDouble( x, y, z );
    }

    public float getFloat( int x, int y, int z, double bound ) {
        return (float) this.getDouble( x, y, z, bound );
    }
}
