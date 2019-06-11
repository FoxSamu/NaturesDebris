/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.api.noise;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class NoiseImprovedOctaves extends NoiseGenerator {
    /** Collection of noise generation functions.  Output is combined to produce different octaves of noise. */
    private final NoiseImproved[] generatorCollection;
    private final int octaves;

    public NoiseImprovedOctaves( Random seed, int octavesIn ) {
        this.octaves = octavesIn;
        this.generatorCollection = new NoiseImproved[ octavesIn ];

        for( int i = 0; i < octavesIn; ++ i ) {
            this.generatorCollection[ i ] = new NoiseImproved( seed );
        }

    }

    public double genNoise( double x, double y, double z ) {
        double noise = 0.0D;
        double scale = 1.0D;

        for( int i = 0; i < this.octaves; ++ i ) {
            noise += this.generatorCollection[ i ].genNoise( x * scale, y * scale, z * scale ) / scale;
            scale /= 2.0D;
        }

        return noise;
    }

    public double[] populateNoiseArray( double[] buffer, int xCoord, int yCoord, int zCoord, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale ) {
        double scale = 1.0D;

        for( int i = 0; i < buffer.length; i++ ) buffer[ i ] = 0;

        for( int i = 0; i < this.octaves; ++ i ) {
            double xcoord = xCoord * scale * xScale;
            double ycoord = yCoord * scale * yScale;
            double zcoord = zCoord * scale * zScale;
            long xFloor = MathHelper.lfloor( xcoord );
            long zFloor = MathHelper.lfloor( zcoord );

            // Modulo
            xcoord = xcoord - xFloor;
            zcoord = zcoord - zFloor;
            xFloor = xFloor % 16777216L;
            zFloor = zFloor % 16777216L;
            xcoord = xcoord + xFloor;
            zcoord = zcoord + zFloor;

            this.generatorCollection[ i ].populateNoiseArray( buffer, xcoord, ycoord, zcoord, xSize, ySize, zSize, xScale * scale, yScale * scale, zScale * scale, scale );
            scale /= 2.0D;
        }

        return buffer;
    }

    public double[] populateNoiseArray( double[] buffer, int x, int z, int xSize, int zSize, double xScale, double zScale ) {
        return this.populateNoiseArray( buffer, x, 10, z, xSize, 1, zSize, xScale, 1.0D, zScale );
    }
}