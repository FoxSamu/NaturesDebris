/*
 * Copyright (c) 2018 Red Galaxy Software.
 * Licensed under Apache License 2.0.
 *
 * By  : RgSW
 * Date: 1 - 24 - 2019
 */

package net.rgsw.noise;

public class FractalOpenSimplex3D extends Noise3D {

    private final OpenSimplex3D[] noiseOctaves;

    public FractalOpenSimplex3D( int seed, int octaves ) {
        super( seed );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new OpenSimplex3D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new OpenSimplex3D( seed );
        }
    }

    public FractalOpenSimplex3D( int seed, double scale, int octaves ) {
        super( seed, scale );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new OpenSimplex3D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new OpenSimplex3D( seed );
        }
    }

    public FractalOpenSimplex3D( int seed, double scaleX, double scaleY, double scaleZ, int octaves ) {
        super( seed, scaleX, scaleY, scaleZ );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new OpenSimplex3D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new OpenSimplex3D( seed );
        }
    }

    @Override
    public double generate( double x, double y, double z ) {
        x /= this.scaleX;
        y /= this.scaleY;
        z /= this.scaleZ;

        double t = 0;
        double d = 1;
        double n = 0;

        for( OpenSimplex3D noise : this.noiseOctaves ) {
            t += 1 / d;
            n += noise.generate( x * d, y * d, z * d ) / d;
            d *= 2;
        }
        return n / t;
    }

    public void setSeed( int seed ) {
        this.seed = seed;
        for( OpenSimplex3D noise : this.noiseOctaves ) {
            noise.setSeed( this.seed );
        }
    }
}
