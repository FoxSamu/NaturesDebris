/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 15 - 2019
 * Author: rgsw
 */

package modernity.api.util;

import net.redgalaxy.exc.InstanceOfUtilityClassException;

import java.util.Random;

public final class Randomizer {
    private static final ThreadLocal<Random> RNG = ThreadLocal.withInitial( Random::new );

    private Randomizer() {
        throw new InstanceOfUtilityClassException();
    }

    private static Random rng() {
        return RNG.get();
    }

    public static int randomInt() {
        return rng().nextInt();
    }

    public static int randomInt( int bound ) {
        return rng().nextInt( bound );
    }

    public static int randomInt( int min, int max ) {
        return rng().nextInt( max - min ) + min;
    }

    public static int randomSignedInt() {
        return rng().nextInt() | ( rng().nextInt() & 1 ) << 31;
    }

    public static long randomLong() {
        return rng().nextLong();
    }

    public static long randomLong( long bound ) {
        return rng().nextLong() % bound;
    }

    public static long randomLong( long min, long max ) {
        return randomLong( max - min ) + min;
    }

    public static long randomSignedLong( long min, long max ) {
        return rng().nextLong() | ( rng().nextLong() & 1 ) << 63;
    }

    public static double randomDouble() {
        return rng().nextDouble();
    }

    public static double randomDouble( double bound ) {
        return rng().nextDouble() * bound;
    }

    public static double randomDouble( double min, double max ) {
        return randomDouble( max - min ) + min;
    }

    public static float randomFloat() {
        return rng().nextFloat();
    }

    public static float randomFloat( float bound ) {
        return rng().nextFloat() * bound;
    }

    public static float randomFloat( float min, float max ) {
        return randomFloat( max - min ) + min;
    }

    public static boolean randomBoolean() {
        return rng().nextBoolean();
    }

    public static boolean randomBoolean( double chance ) {
        return rng().nextDouble() < chance;
    }

    public static boolean randomBoolean( float chance ) {
        return rng().nextFloat() < chance;
    }

    public static boolean randomBoolean( int chance, int ratio ) {
        return rng().nextInt( ratio ) < chance;
    }

    public static boolean randomBoolean( long chance, long ratio ) {
        return randomLong( ratio ) < chance;
    }
}
