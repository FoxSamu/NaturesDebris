/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.generic.util;

import net.redgalaxy.exc.InstanceOfUtilityClassException;

public final class Scrambler {
    private Scrambler() {
        throw new InstanceOfUtilityClassException();
    }

    public static long xorshift( long value, int a, int b, int c ) {
        value ^= value >> a;
        value ^= value << b;
        value ^= value >> c;
        return value;
    }

    public static long xorshift( long value, int... s ) {
        boolean dir = true;
        for( int a : s ) {
            value ^= ( dir = ! dir )
                     ? value << a
                     : value >> a;
        }
        return value;
    }
}
