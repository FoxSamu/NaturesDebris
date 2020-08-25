/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved.
 * This file is part of the Modernity Plugin API and may be used,
 * included and distributed within other projects without further
 * permission, unless the copyright holder is not the original
 * author or the owner had forbidden the user to use this file.
 * Other terms and conditions still apply.
 *
 * For a full license, see LICENSE.txt.
 */

package modernity.api.util.math;

import modernity.api.util.exc.InstanceOfUtilityClassException;

public final class MathUtil {

    private MathUtil() {
        throw new InstanceOfUtilityClassException();
    }

    /**
     * Interpolates value between 0 and 1 to value between a and b
     *
     * @param x Value between 0 and 1
     * @see MathUtil#unlerp(double, double, double)
     */
    public static double lerp(double a, double b, double x) {
        return a + x * (b - a);
    }

    /**
     * Interpolates value between 0 and 1 to value between a and b
     *
     * @param x Value between 0 and 1
     * @see MathUtil#unlerp(float, float, float)
     */
    public static float lerp(float a, float b, float x) {
        return a + x * (b - a);
    }

    /**
     * Interpolates value between a and b to value between 0 and 1
     *
     * @param x Value between a and b
     * @see MathUtil#lerp(double, double, double)
     */
    public static double unlerp(double a, double b, double x) {
        return (x - a) / (b - a);
    }

    /**
     * Interpolates value between a and b to value between 0 and 1
     *
     * @param x Value between a and b
     * @see MathUtil#lerp(float, float, float)
     */
    public static float unlerp(float a, float b, float x) {
        return (x - a) / (b - a);
    }

    /**
     * Interpolates value between a and b to value between p and q
     *
     * @param x Value between a and b
     * @see MathUtil#lerp(double, double, double)
     * @see MathUtil#unlerp(double, double, double)
     */
    public static double relerp(double a, double b, double p, double q, double x) {
        return p + (x - a) / (b - a) * (q - p);
    }

    /**
     * Interpolates value between a and b to value between p and q
     *
     * @param x Value between a and b
     * @see MathUtil#lerp(float, float, float)
     * @see MathUtil#unlerp(float, float, float)
     */
    public static float relerp(float a, float b, float p, float q, float x) {
        return p + (x - a) / (b - a) * (q - p);
    }

    /**
     * Applies Ken Perlin's smoothing function to a double value between 0 and 1.
     *
     * @param t The linear value (0 - 1)
     * @return The smoothed value (0 - 1)
     */
    public static double smooth(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * Clamps the value <code>t</code> to the specified lower and upper bounds. This returns:
     * <ul>
     * <li><code>min</code> when <code>t < min</code></li>
     * <li><code>max</code> when <code>t > max && t >= min</code></li>
     * <li><code>t</code> when <code>t >= min && t <= max</code></li>
     * </ul>
     *
     * @param t   The value to clamp
     * @param min The lower bound
     * @param max The upper bound
     */
    public static int clamp(int t, int min, int max) {
        return t < min ? min : Math.min(t, max);
    }

    /**
     * Clamps the value <code>t</code> to the specified lower and upper bounds. This returns:
     * <ul>
     * <li><code>min</code> when <code>t < min</code></li>
     * <li><code>max</code> when <code>t > max && t >= min</code></li>
     * <li><code>t</code> when <code>t >= min && t <= max</code></li>
     * </ul>
     *
     * @param t   The value to clamp
     * @param min The lower bound
     * @param max The upper bound
     */
    public static double clamp(double t, double min, double max) {
        return t < min ? min : Math.min(t, max);
    }

    /**
     * Clamps the value <code>t</code> to the specified lower and upper bounds. This returns:
     * <ul>
     * <li><code>min</code> when <code>t < min</code></li>
     * <li><code>max</code> when <code>t > max && t >= min</code></li>
     * <li><code>t</code> when <code>t >= min && t <= max</code></li>
     * </ul>
     *
     * @param t   The value to clamp
     * @param min The lower bound
     * @param max The upper bound
     */
    public static float clamp(float t, float min, float max) {
        return t < min ? min : Math.min(t, max);
    }

    /**
     * Interpolates between a and b and clamps the value to a and b
     *
     * @param x The interpolator
     * @see #lerp(double, double, double)
     * @see #clamp(double, double, double)
     */
    public static double clampLerp(double a, double b, double x) {
        return lerp(a, b, clamp(x, 0, 1));
    }

    /**
     * Interpolates between a and b and clamps the value to a and b
     *
     * @param x The interpolator
     * @see #lerp(float, float, float)
     * @see #clamp(float, float, float)
     */
    public static float clampLerp(float a, float b, float x) {
        return lerp(a, b, clamp(x, 0, 1));
    }


    public static double positiveModulo(double d, double mod) {
        return d % mod + (d < 0 ? mod : 0);
    }

    public static float positiveModulo(float f, float mod) {
        return f % mod + (f < 0 ? mod : 0);
    }

    public static int positiveModulo(int i, int mod) {
        return i % mod + (i < 0 ? mod : 0);
    }

    public static long positiveModulo(long l, long mod) {
        return l % mod + (l < 0 ? mod : 0);
    }

    public static double sum(double... values) {
        double total = 0;
        for(double d : values) total += d;
        return total;
    }

    public static double avg(double... values) {
        double total = 0;
        for(double d : values) total += d;
        return total / values.length;
    }

    public static double min(double... values) {
        double min = Double.POSITIVE_INFINITY;
        for(double d : values) min = Math.min(min, d);
        return min;
    }

    public static double max(double... values) {
        double max = Double.NEGATIVE_INFINITY;
        for(double d : values) max = Math.max(max, d);
        return max;
    }

    public static float sum(float... values) {
        float total = 0;
        for(float d : values) total += d;
        return total;
    }

    public static float avg(float... values) {
        float total = 0;
        for(float d : values) total += d;
        return total / values.length;
    }

    public static float min(float... values) {
        float min = Float.POSITIVE_INFINITY;
        for(float d : values) min = Math.min(min, d);
        return min;
    }

    public static float max(float... values) {
        float max = Float.NEGATIVE_INFINITY;
        for(float d : values) max = Math.max(max, d);
        return max;
    }

    public static int sum(int... values) {
        int total = 0;
        for(int d : values) total += d;
        return total;
    }

    public static int min(int... values) {
        int min = Integer.MAX_VALUE;
        for(int d : values) min = Math.min(min, d);
        return min;
    }

    public static int max(int... values) {
        int max = Integer.MIN_VALUE;
        for(int d : values) max = Math.max(max, d);
        return max;
    }

    public static long sum(long... values) {
        long total = 0;
        for(long d : values) total += d;
        return total;
    }

    public static long min(long... values) {
        long min = Long.MAX_VALUE;
        for(long d : values) min = Math.min(min, d);
        return min;
    }

    public static long max(long... values) {
        long max = Long.MIN_VALUE;
        for(long d : values) max = Math.max(max, d);
        return max;
    }
}
