/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.profile;

import it.unimi.dsi.fastutil.ints.IntHash;

public final class BiomeHashStrategy implements IntHash.Strategy {
    public static final BiomeHashStrategy INSTANCE = new BiomeHashStrategy();

    private BiomeHashStrategy() {
    }

    @Override
    public int hashCode( int i ) {
        return i;
    }

    @Override
    public boolean equals( int a, int b ) {
        return a == b;
    }
}
