/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package net.rgsw;

public class MathUtil {

    /**
     * Interpolates value between 0 and 1 to value between a and b
     * @param a
     * @param b
     * @param x Value between 0 and 1
     * @see MathUtil#invLerp(double, double, double)
     */
    public static double lerp( double a, double b, double x ) {
        return a + x * ( b - a );
    }

    /**
     * Interpolates value between a and b to value between 0 and 1
     * @param a
     * @param b
     * @param x Value between a and b
     * @see MathUtil#lerp(double, double, double)
     */
    public static double invLerp( double a, double b, double x ) {
        return ( x - a ) / ( b - a );
    }

    /**
     * Applies Ken Perlin's smoothing function to a double value between 0 and 1.
     * @param t The linear value (0 - 1)
     * @return The smoothed value (0 - 1)
     */
    public static double smooth( double t ) {
        return t * t * t * ( t * ( t * 6 - 15 ) + 10 );
    }

    public static double clamp( double t, double min, double max ) {
        return t < min ? min : t > max ? max : t;
    }

}
