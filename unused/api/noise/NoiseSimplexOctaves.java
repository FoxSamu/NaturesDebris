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

public class NoiseSimplexOctaves extends NoiseGenerator {
    private final NoiseSimplex[] noiseLevels;
    private final int levels;

    public NoiseSimplexOctaves( Random seed, int levelsIn ) {
        this.levels = levelsIn;
        this.noiseLevels = new NoiseSimplex[ levelsIn ];

        for( int i = 0; i < levelsIn; ++ i ) {
            this.noiseLevels[ i ] = new NoiseSimplex( seed );
        }

    }

    public double getValue( double x, double z ) {
        double d0 = 0.0D;
        double d1 = 1.0D;

        for( int i = 0; i < this.levels; ++ i ) {
            d0 += this.noiseLevels[ i ].getValue( x * d1, z * d1 ) / d1;
            d1 /= 2.0D;
        }

        return d0;
    }

    public double[] generateRegion( double startX, double startY, int xSize, int ySize, double xScale, double yScale, double p_202644_11_ ) {
        return this.generateRegion( startX, startY, xSize, ySize, xScale, yScale, p_202644_11_, 0.5D );
    }

    public double[] generateRegion( double startX, double startY, int xSize, int ySize, double xScale, double yScale, double p_202645_11_, double p_202645_13_ ) {
        double[] adouble = new double[ xSize * ySize ];
        double d0 = 1.0D;
        double d1 = 1.0D;

        for( int i = 0; i < this.levels; ++ i ) {
            this.noiseLevels[ i ].add( adouble, startX, startY, xSize, ySize, xScale * d1 * d0, yScale * d1 * d0, 0.55D / d0 );
            d1 *= p_202645_11_;
            d0 *= p_202645_13_;
        }

        return adouble;
    }
}