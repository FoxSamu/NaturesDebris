/*
 * Copyright (c) 2018 Red Galaxy Software.
 * Licensed under Apache License 2.0.
 *
 * By  : RgSW
 * Date: 11 - 18 - 2018
 */

package net.rgsw.noise;

import net.rgsw.MathUtil;

public abstract class Noise3D {
    protected int seed;
    protected final double scaleX;
    protected final double scaleY;
    protected final double scaleZ;

    public Noise3D( int seed ) {
        this.seed = seed;
        this.scaleX = 1;
        this.scaleY = 1;
        this.scaleZ = 1;
    }

    public Noise3D( int seed, double scale ) {
        this.seed = seed;
        this.scaleX = scale;
        this.scaleY = scale;
        this.scaleZ = scale;
    }

    public Noise3D( int seed, double scaleX, double scaleY, double scaleZ ) {
        this.seed = seed;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed( int seed ) {
        this.seed = seed;
    }

    public abstract double generate( double x, double y, double z );

    public double generateMultiplied( double x, double y, double z, double mult ) {
        return generate( x, y, z ) * mult;
    }

    public double generateInRange( double x, double y, double z, double min, double max ) {
        return MathUtil.lerp( min, max, ( generate( x, y, z ) + 1 ) / 2 );
    }

    public double generate( int x, int y, int z ) {
        return this.generate( (double) x, (double) y, (double) z );
    }
}
