/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package net.rgsw.noise;

public class Hash {

    private final static int X_PRIME = 1619;
    private final static int Y_PRIME = 31337;
    private final static int Z_PRIME = 6971;
    private final static int W_PRIME = 1013;

    public static double hash1D( int seed, int x ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        hash ^= X_PRIME * x;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return (double) hash / Integer.MAX_VALUE;
    }

    public static double hash2D( int seed, int x, int y ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        if( y < 0 ) y = Integer.MAX_VALUE + y;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return (double) hash / Integer.MAX_VALUE;
    }

    public static double hash3D( int seed, int x, int y, int z ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        if( y < 0 ) y = Integer.MAX_VALUE + y;
        if( z < 0 ) z = Integer.MAX_VALUE + z;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;
        hash ^= Z_PRIME * z;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return (double) hash / Integer.MAX_VALUE;
    }

    public static double hash4D( int seed, int x, int y, int z, int w ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        if( y < 0 ) y = Integer.MAX_VALUE + y;
        if( z < 0 ) z = Integer.MAX_VALUE + z;
        if( w < 0 ) w = Integer.MAX_VALUE + w;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;
        hash ^= Z_PRIME * z;
        hash ^= W_PRIME * w;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return (double) hash / Integer.MAX_VALUE;
    }

    public static int hash1Dint( int seed, int x ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        hash ^= X_PRIME * x;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return hash;
    }

    public static int hash2Dint( int seed, int x, int y ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        if( y < 0 ) y = Integer.MAX_VALUE + y;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return hash;
    }

    public static int hash3Dint( int seed, int x, int y, int z ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        if( y < 0 ) y = Integer.MAX_VALUE + y;
        if( z < 0 ) z = Integer.MAX_VALUE + z;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;
        hash ^= Z_PRIME * z;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return hash;
    }

    public static int hash4Dint( int seed, int x, int y, int z, int w ) {
        int hash = seed;
        if( x < 0 ) x = Integer.MAX_VALUE + x;
        if( y < 0 ) y = Integer.MAX_VALUE + y;
        if( z < 0 ) z = Integer.MAX_VALUE + z;
        if( w < 0 ) w = Integer.MAX_VALUE + w;
        hash ^= X_PRIME * x;
        hash ^= Y_PRIME * y;
        hash ^= Z_PRIME * z;
        hash ^= W_PRIME * w;

        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;

        return hash;
    }
}
