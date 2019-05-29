/*
 * Copyright (c) 2018 Red Galaxy Software.
 * Licensed under Apache License 2.0.
 *
 * By  : RgSW
 * Date: 1 - 24 - 2019
 */

package net.rgsw.noise;

public class InverseFractalPerlin3D extends Noise3D {

    private final Perlin3D[] noiseOctaves;

    public InverseFractalPerlin3D( int seed, int octaves ) {
        super( seed );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin3D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin3D( seed );
        }
    }

    public InverseFractalPerlin3D( int seed, double scale, int octaves ) {
        super( seed, scale );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin3D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin3D( seed );
        }
    }

    public InverseFractalPerlin3D( int seed, double scaleX, double scaleY, double scaleZ, int octaves ) {
        super( seed, scaleX, scaleY, scaleZ );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin3D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin3D( seed );
        }
    }

    @Override
    public double generate( double x, double y, double z ) {
        x /= this.scaleX;
        y /= this.scaleY;
        z /= this.scaleZ;

        double d = 1;
        double n = 0;

        for( Perlin3D noise : this.noiseOctaves ) {
            n += noise.generate( x * d, y * d, z * d ) / d;
            d /= 2; // This division is a multiplication in non-inverse: that's the only difference
        }
        return n;
    }

    public void setSeed( int seed ) {
        this.seed = seed;
        for( Perlin3D perlin : this.noiseOctaves ) {
            perlin.setSeed( this.seed );
        }
    }
}
