/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.generic.noise;

import java.util.Random;

public class NoiseImproved extends NoiseGenerator {
    /**
     * An int[512], where the first 256 elements are the numbers 0..255, in random shuffled order, and the second half
     * of the array is identical to the first half, apparently for convenience in wrapping lookups.
     *
     * Effectively a shuffled 0..255 that wraps once.
     */
    private final int[] permutations = new int[ 512 ];
    public double xCoord;
    public double yCoord;
    public double zCoord;
    private static final double[] GRAD_X = {
            1.0D, - 1.0D, 1.0D, - 1.0D, 1.0D, - 1.0D, 1.0D, - 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, - 1.0D, 0.0D
    };
    private static final double[] GRAD_Y = {
            1.0D, 1.0D, - 1.0D, - 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, - 1.0D, 1.0D, - 1.0D, 1.0D, - 1.0D, 1.0D, - 1.0D
    };
    private static final double[] GRAD_Z = {
            0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, - 1.0D, - 1.0D, 1.0D, 1.0D, - 1.0D, - 1.0D, 0.0D, 1.0D, 0.0D, - 1.0D
    };
    private static final double[] GRAD_2X = {
            1.0D, - 1.0D, 1.0D, - 1.0D, 1.0D, - 1.0D, 1.0D, - 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, - 1.0D, 0.0D
    };
    private static final double[] GRAD_2Z = {
            0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, - 1.0D, - 1.0D, 1.0D, 1.0D, - 1.0D, - 1.0D, 0.0D, 1.0D, 0.0D, - 1.0D
    };

    public NoiseImproved( Random rand ) {
        this.xCoord = rand.nextDouble() * 256.0D;
        this.yCoord = rand.nextDouble() * 256.0D;
        this.zCoord = rand.nextDouble() * 256.0D;

        // Fill
        int perm = 0;
        while( perm < 256 ) {
            this.permutations[ perm ] = perm++;
        }

        // Shuffle
        for( int index = 0; index < 256; ++ index ) {
            int newIndex = rand.nextInt( 256 - index ) + index;
            int permutation = this.permutations[ index ];
            this.permutations[ index ] = this.permutations[ newIndex ];
            this.permutations[ newIndex ] = permutation;
            this.permutations[ index + 256 ] = this.permutations[ index ];
        }

    }

    public double getNoise( double x, double y, double z ) {
        double absX = x + this.xCoord;
        double absY = y + this.yCoord;
        double absZ = z + this.zCoord;

        // Coords, rounded down to get grid cell corner
        int floorX = (int) absX;
        int floorY = (int) absY;
        int floorZ = (int) absZ;
        if( absX < floorX ) {
            -- floorX;
        }

        if( absY < floorY ) {
            -- floorY;
        }

        if( absZ < floorZ ) {
            -- floorZ;
        }

        int permX = floorX & 255;
        int permY = floorY & 255;
        int permZ = floorZ & 255;


        absX = absX - (double) floorX;
        absY = absY - (double) floorY;
        absZ = absZ - (double) floorZ;


        double smoothX = absX * absX * absX * ( absX * ( absX * 6.0D - 15.0D ) + 10.0D );
        double smoothY = absY * absY * absY * ( absY * ( absY * 6.0D - 15.0D ) + 10.0D );
        double smoothZ = absZ * absZ * absZ * ( absZ * ( absZ * 6.0D - 15.0D ) + 10.0D );

        int permXY = this.permutations[ permX ] + permY;
        int permXYZ = this.permutations[ permXY ] + permZ;
        int permXY1Z = this.permutations[ permXY + 1 ] + permZ;

        int permX1Y = this.permutations[ permX + 1 ] + permY;
        int permX1YZ = this.permutations[ permX1Y ] + permZ;
        int permX1Y1Z = this.permutations[ permX1Y + 1 ] + permZ;
        return lerp(
                smoothZ,
                lerp(
                        smoothY,
                        lerp(
                                smoothX,
                                grad3d( this.permutations[ permXYZ ], absX, absY, absZ ),
                                grad3d( this.permutations[ permX1YZ ], absX - 1.0D, absY, absZ )
                        ),
                        lerp(
                                smoothX,
                                grad3d( this.permutations[ permXY1Z ], absX, absY - 1.0D, absZ ),
                                grad3d( this.permutations[ permX1Y1Z ], absX - 1.0D, absY - 1.0D, absZ )
                        )
                ),
                lerp(
                        smoothY,
                        lerp(
                                smoothX,
                                grad3d( this.permutations[ permXYZ + 1 ], absX, absY, absZ - 1.0D ),
                                grad3d( this.permutations[ permX1YZ + 1 ], absX - 1.0D, absY, absZ - 1.0D )
                        ),
                        lerp(
                                smoothX,
                                grad3d( this.permutations[ permXY1Z + 1 ], absX, absY - 1.0D, absZ - 1.0D ),
                                grad3d( this.permutations[ permX1Y1Z + 1 ], absX - 1.0D, absY - 1.0D, absZ - 1.0D )
                        )
                )
        );
    }

    public static double lerp( double x, double a, double b ) {
        return a + x * ( b - a );
    }

    public static double grad2d( int perm, double x, double z ) {
        int i = perm & 15;
        return GRAD_2X[ i ] * x + GRAD_2Z[ i ] * z;
    }

    public static double grad3d( int perm, double x, double y, double z ) {
        int i = perm & 15;
        return GRAD_X[ i ] * x + GRAD_Y[ i ] * y + GRAD_Z[ i ] * z;
    }

    public double getNoise( double x, double y ) {
        return this.getNoise( x, y, 0.0D );
    }

    public double genNoise( double x, double y, double z ) {
        return this.getNoise( x, y, z );
    }

    /**
     * noiseArray should be xSize*ySize*zSize in size
     */
    public void populateNoiseArray( double[] noiseArray, double xOffset, double yOffset, double zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double noiseScale ) {
        // Check whether this is 2D noise...
        if( ySize == 1 ) {
            int permXY;
            int permXYZ;
            int permX1Y;
            int permX1YZ;

            double noiseX1;
            double noiseX2;

            int iterator = 0;
            double invNoiseScale = 1.0D / noiseScale;

            for( int x = 0; x < xSize; ++ x ) {
                // Absolute coords
                double absX = xOffset + x * xScale + this.xCoord;

                // Fast floor
                int floorX = (int) absX;
                if( absX < floorX ) {
                    -- floorX;
                }

                // Permutation
                int permX = floorX & 255;
                absX = absX - floorX;

                // Smooth
                double smoothX = absX * absX * absX * ( absX * ( absX * 6.0D - 15.0D ) + 10.0D );

                for( int z = 0; z < zSize; ++ z ) {
                    // Absolute coords
                    double absZ = zOffset + (double) z * zScale + this.zCoord;

                    // Fast floor
                    int floorZ = (int) absZ;
                    if( absZ < (double) floorZ ) {
                        -- floorZ;
                    }

                    // Permutation
                    int permZ = floorZ & 255;
                    absZ = absZ - (double) floorZ;
                    // Smooth
                    double smoothZ = absZ * absZ * absZ * ( absZ * ( absZ * 6.0D - 15.0D ) + 10.0D );

                    // Randomize permutation values
                    permXY = this.permutations[ permX ];
                    permXYZ = this.permutations[ permXY ] + permZ;
                    permX1Y = this.permutations[ permX + 1 ];
                    permX1YZ = this.permutations[ permX1Y ] + permZ;

                    noiseX1 = lerp( smoothX, grad2d( this.permutations[ permXYZ ], absX, absZ ), grad3d( this.permutations[ permX1YZ ], absX - 1.0D, 0.0D, absZ ) );
                    noiseX2 = lerp( smoothX, grad3d( this.permutations[ permXYZ + 1 ], absX, 0.0D, absZ - 1.0D ), grad3d( this.permutations[ permX1YZ + 1 ], absX - 1.0D, 0.0D, absZ - 1.0D ) );
                    double noise = lerp( smoothZ, noiseX1, noiseX2 );
                    int index = iterator++;
                    noiseArray[ index ] += noise * invNoiseScale;
                }
            }

        } else {
            int iterator = 0;
            double invNoiseScale = 1.0D / noiseScale;
            int perm = - 1;
            int permXY;
            int permXYZ;
            int permXY1Z;
            int permX1Y;
            int permX1YZ;
            int permX1Y1Z;
            double noiseX1 = 0.0D;
            double noiseX2 = 0.0D;
            double noiseX3 = 0.0D;
            double noiseX4 = 0.0D;

            for( int x = 0; x < xSize; ++ x ) {
                double absX = xOffset + x * xScale + this.xCoord;
                int floorX = (int) absX;
                if( absX < floorX ) {
                    -- floorX;
                }

                int permX = floorX & 255;
                absX = absX - floorX;
                double smoothX = absX * absX * absX * ( absX * ( absX * 6.0D - 15.0D ) + 10.0D );

                for( int z = 0; z < zSize; ++ z ) {
                    double absZ = zOffset + z * zScale + this.zCoord;
                    int floorZ = (int) absZ;
                    if( absZ < floorZ ) {
                        -- floorZ;
                    }

                    int permZ = floorZ & 255;
                    absZ = absZ - floorZ;
                    double smoothZ = absZ * absZ * absZ * ( absZ * ( absZ * 6.0D - 15.0D ) + 10.0D );

                    for( int y = 0; y < ySize; ++ y ) {
                        double absY = yOffset + y * yScale + this.yCoord;
                        int floorY = (int) absY;
                        if( absY < floorY ) {
                            -- floorY;
                        }

                        int permY = floorY & 255;
                        absY = absY - floorY;
                        double smoothY = absY * absY * absY * ( absY * ( absY * 6.0D - 15.0D ) + 10.0D );
                        if( y == 0 || permY != perm ) {
                            perm = permY;

                            permXY = this.permutations[ permX ] + permY;
                            permXYZ = this.permutations[ permXY ] + permZ;
                            permXY1Z = this.permutations[ permXY + 1 ] + permZ;
                            permX1Y = this.permutations[ permX + 1 ] + permY;
                            permX1YZ = this.permutations[ permX1Y ] + permZ;
                            permX1Y1Z = this.permutations[ permX1Y + 1 ] + permZ;

                            noiseX1 = lerp( smoothX, grad3d( this.permutations[ permXYZ ], absX, absY, absZ ), grad3d( this.permutations[ permX1YZ ], absX - 1, absY, absZ ) );
                            noiseX2 = lerp( smoothX, grad3d( this.permutations[ permXY1Z ], absX, absY - 1, absZ ), grad3d( this.permutations[ permX1Y1Z ], absX - 1, absY - 1, absZ ) );
                            noiseX3 = lerp( smoothX, grad3d( this.permutations[ permXYZ + 1 ], absX, absY, absZ - 1 ), grad3d( this.permutations[ permX1YZ + 1 ], absX - 1, absY, absZ - 1 ) );
                            noiseX4 = lerp( smoothX, grad3d( this.permutations[ permXY1Z + 1 ], absX, absY - 1, absZ - 1 ), grad3d( this.permutations[ permX1Y1Z + 1 ], absX - 1, absY - 1, absZ - 1 ) );
                        }

                        double noiseY1 = lerp( smoothY, noiseX1, noiseX2 );
                        double noiseY2 = lerp( smoothY, noiseX3, noiseX4 );
                        double noise = lerp( smoothZ, noiseY1, noiseY2 );
                        int index = iterator++;

                        // Why is this multiplied by invNoiseScale?
                        noiseArray[ index ] += noise * invNoiseScale;
                    }
                }
            }

        }
    }
}