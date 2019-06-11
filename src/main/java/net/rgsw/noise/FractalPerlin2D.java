/*
 * Copyright (c) 2019 RGSW.
 * This file belongs to a separate library, made for the Modernity.
 * Licensed under the Apache Licence v2.0. Do not redistribute.
 *
 * Date: 6 - 11 - 2019
 */

package net.rgsw.noise;

public class FractalPerlin2D extends Noise2D {

    private final Perlin2D[] noiseOctaves;

    public FractalPerlin2D( int seed, int octaves ) {
        super( seed );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin2D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin2D( seed );
        }
    }

    public FractalPerlin2D( int seed, double scale, int octaves ) {
        super( seed, scale );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin2D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin2D( seed );
        }
    }

    public FractalPerlin2D( int seed, double scaleX, double scaleY, int octaves ) {
        super( seed, scaleX, scaleY );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin2D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin2D( seed );
        }
    }

    @Override
    public double generate( double x, double y ) {
        x /= this.scaleX;
        y /= this.scaleY;

        double t = 0;
        double d = 1;
        double n = 0;

        for( Perlin2D noise : this.noiseOctaves ) {
            t += 1 / d;
            n += noise.generate( x * d, y * d ) / d;
            d *= 2;
        }
        return n / t;
    }
}
