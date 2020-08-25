/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.util;

import net.minecraft.util.BitArray;

public class HeightMap {
    private final BitArray heights = new BitArray(9, 256);

    public void setHeight(int x, int z, int height) {
        x %= 16;
        if(x < 0) x += 16;
        z %= 16;
        if(z < 0) z += 16;
        heights.setAt(x * 16 + z, height);
    }

    public int getHeight(int x, int z) {
        x %= 16;
        if(x < 0) x += 16;
        z %= 16;
        if(z < 0) z += 16;
        return heights.getAt(x * 16 + z);
    }

    public long[] getLongArray() {
        return heights.getBackingLongArray();
    }

    public void applyLongArray(long[] longs) {
        System.arraycopy(longs, 0, heights.getBackingLongArray(), 0, longs.length);
    }
}
