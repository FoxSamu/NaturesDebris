/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.api.noise;

import java.util.Random;

public class NoiseSimplex {
    private static final int[][] grad3 = {
            { 1, 1, 0 }, { - 1, 1, 0 }, { 1, - 1, 0 }, { - 1, - 1, 0 }, { 1, 0, 1 }, { - 1, 0, 1 }, { 1, 0, - 1 },
            { - 1, 0, - 1 }, { 0, 1, 1 }, { 0, - 1, 1 }, { 0, 1, - 1 }, { 0, - 1, - 1 }
    };
    public static final double SQRT_3 = Math.sqrt( 3.0D );
    private final int[] permutations = new int[ 512 ];
    public double xOffset;
    public double yOffset;
    public double zOffset;
    private static final double F = 0.5D * ( SQRT_3 - 1.0D );
    private static final double G = ( 3.0D - SQRT_3 ) / 6.0D;

    public NoiseSimplex( Random rand ) {
        this.xOffset = rand.nextDouble() * 256.0D;
        this.yOffset = rand.nextDouble() * 256.0D;
        this.zOffset = rand.nextDouble() * 256.0D;

        int perm = 0;
        while( perm < 256 ) {
            this.permutations[ perm ] = perm++;
        }

        for( int index = 0; index < 256; ++ index ) {
            int newIndex = rand.nextInt( 256 - index ) + index;
            int permutation = this.permutations[ index ];
            this.permutations[ index ] = this.permutations[ newIndex ];
            this.permutations[ newIndex ] = permutation;
            this.permutations[ index + 256 ] = this.permutations[ index ];
        }

    }

    private static int fastFloor( double value ) {
        return value > 0.0D ? (int) value : (int) value - 1;
    }

    private static double dot( int[] u, double vX, double vY ) {
        return (double) u[ 0 ] * vX + (double) u[ 1 ] * vY;
    }

    public double getValue( double x, double y ) {
        double skew = ( x + y ) * F;
        int floorX = fastFloor( x + skew );
        int floorY = fastFloor( y + skew );

        double unskew = (double) ( floorX + floorY ) * G;
        double ufloorX = (double) floorX - unskew;
        double ufloorY = (double) floorY - unskew;

        double locX = x - ufloorX;
        double locY = y - ufloorY;

        int cornerX;
        int cornerY;
        if( locX > locY ) {
            cornerX = 1;
            cornerY = 0;
        } else {
            cornerX = 0;
            cornerY = 1;
        }

        double invLocX = locX - (double) cornerX + G;
        double invLocY = locY - (double) cornerY + G;

        double radiusX = locX - 1.0D + 2.0D * G;
        double radiusY = locY - 1.0D + 2.0D * G;

        int permX = floorX & 255;
        int permY = floorY & 255;

        int gradIndex1 = this.permutations[ permX + this.permutations[ permY ] ] % 12;
        int gradIndex2 = this.permutations[ permX + cornerX + this.permutations[ permY + cornerY ] ] % 12;
        int gradIndex3 = this.permutations[ permX + 1 + this.permutations[ permY + 1 ] ] % 12;

        double dist1 = 0.5 - locX * locX - locY * locY;
        double noise1;
        if( dist1 < 0 ) {
            noise1 = 0;
        } else {
            dist1 = dist1 * dist1;
            noise1 = dist1 * dist1 * dot( grad3[ gradIndex1 ], locX, locY );
        }

        double dist2 = 0.5 - invLocX * invLocX - invLocY * invLocY;
        double noise2;
        if( dist2 < 0 ) {
            noise2 = 0;
        } else {
            dist2 = dist2 * dist2;
            noise2 = dist2 * dist2 * dot( grad3[ gradIndex2 ], invLocX, invLocY );
        }

        double dist3 = 0.5 - radiusX * radiusX - radiusY * radiusY;
        double noise3;
        if( dist3 < 0 ) {
            noise3 = 0;
        } else {
            dist3 = dist3 * dist3;
            noise3 = dist3 * dist3 * dot( grad3[ gradIndex3 ], radiusX, radiusY );
        }

        // Why multiplying by 70?
        return 70 * ( noise1 + noise2 + noise3 );
    }

    public void add( double[] targetArray, double p_151606_2_, double p_151606_4_, int p_151606_6_, int xSize, double ySize, double p_151606_10_, double p_151606_12_ ) {
        int i = 0;

        for( int j = 0; j < xSize; ++ j ) {
            double d0 = ( p_151606_4_ + (double) j ) * p_151606_10_ + this.yOffset;

            for( int k = 0; k < p_151606_6_; ++ k ) {
                double d1 = ( p_151606_2_ + (double) k ) * ySize + this.xOffset;
                double d5 = ( d1 + d0 ) * F;
                int l = fastFloor( d1 + d5 );
                int i1 = fastFloor( d0 + d5 );
                double d6 = (double) ( l + i1 ) * G;
                double d7 = (double) l - d6;
                double d8 = (double) i1 - d6;
                double d9 = d1 - d7;
                double d10 = d0 - d8;
                int j1;
                int k1;
                if( d9 > d10 ) {
                    j1 = 1;
                    k1 = 0;
                } else {
                    j1 = 0;
                    k1 = 1;
                }

                double d11 = d9 - (double) j1 + G;
                double d12 = d10 - (double) k1 + G;
                double d13 = d9 - 1.0D + 2.0D * G;
                double d14 = d10 - 1.0D + 2.0D * G;
                int l1 = l & 255;
                int i2 = i1 & 255;
                int j2 = this.permutations[ l1 + this.permutations[ i2 ] ] % 12;
                int k2 = this.permutations[ l1 + j1 + this.permutations[ i2 + k1 ] ] % 12;
                int l2 = this.permutations[ l1 + 1 + this.permutations[ i2 + 1 ] ] % 12;
                double d15 = 0.5D - d9 * d9 - d10 * d10;
                double d2;
                if( d15 < 0.0D ) {
                    d2 = 0.0D;
                } else {
                    d15 = d15 * d15;
                    d2 = d15 * d15 * dot( grad3[ j2 ], d9, d10 );
                }

                double d16 = 0.5D - d11 * d11 - d12 * d12;
                double d3;
                if( d16 < 0.0D ) {
                    d3 = 0.0D;
                } else {
                    d16 = d16 * d16;
                    d3 = d16 * d16 * dot( grad3[ k2 ], d11, d12 );
                }

                double d17 = 0.5D - d13 * d13 - d14 * d14;
                double d4;
                if( d17 < 0.0D ) {
                    d4 = 0.0D;
                } else {
                    d17 = d17 * d17;
                    d4 = d17 * d17 * dot( grad3[ l2 ], d13, d14 );
                }

                int i3 = i++;
                targetArray[ i3 ] += 70.0D * ( d2 + d3 + d4 ) * p_151606_12_;
            }
        }

    }
}