/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.api.util;

@FunctionalInterface
public interface IIntScrambler {
    IIntScrambler IDENTITY = seed -> seed;

    int scramble( int seed );

    static IIntScrambler lgc( int add, int mul ) {
        return seed -> seed * mul + add;
    }
}
