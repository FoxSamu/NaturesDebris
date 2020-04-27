/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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
