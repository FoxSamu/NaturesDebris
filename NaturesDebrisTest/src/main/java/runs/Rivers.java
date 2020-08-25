/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package runs;

import java.util.Random;

public final class Rivers {

    private static final int W = 50;
    private static final int H = 50;

    private Rivers() {
    }

    public static void main(String[] args) {
        Random rand = new Random();

        int[] riverInit = new int[W * H];
        int[] river = new int[W * H];

        for (int x = 0; x < W; x++) {
            for (int z = 0; z < H; z++) {
                int rx = x >> 2;
                int rz = z >> 2;
                rand.setSeed(rx * 35173512431312938L + rz * 3471856723L);
                rand.nextInt();
                int r = rand.nextInt(299999999) + 2;
                riverInit[index(x, z)] = r;
            }
        }

        for (int x = 1; x < W - 1; x++) {
            for (int z = 1; z < H - 1; z++) {
                int north = riverInit[index(x, z - 1)];
                int south = riverInit[index(x, z + 1)];
                int west = riverInit[index(x - 1, z)];
                int east = riverInit[index(x + 1, z)];
                int center = riverInit[index(x, z)];

                int centerFilter = riverFilter(center);
                boolean isSame = centerFilter == riverFilter(west)
                                     && centerFilter == riverFilter(north)
                                     && centerFilter == riverFilter(east)
                                     && centerFilter == riverFilter(south);
                int out = isSame ? 0 : 1;
                river[index(x, z)] = out;
            }
        }

        for (int z = 1; z < W - 1; z++) {
            for (int x = 1; x < H - 1; x++) {
                System.out.print(river[index(x, z)] == 1 ? "* " : "  ");
            }
            System.out.println();
        }
    }

    private static int index(int x, int z) {
        return x * H + z;
    }

    private static int riverFilter(int value) {
        return value >= 2 ? 2 + (value & 1) : value;
    }
}
