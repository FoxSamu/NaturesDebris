/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.count;

import net.minecraft.world.IWorld;

import java.util.Random;

public class RegionCount implements IDecorCount {
    private final int iterations;
    private final int regionSizeX;
    private final int regionSizeZ;
    private final long seed;
    private final IDecorCount countPerChunk;

    public RegionCount(int iterations, int regionSizeX, int regionSizeZ, long seed, IDecorCount countPerChunk) {
        this.iterations = iterations;
        this.regionSizeX = regionSizeX;
        this.regionSizeZ = regionSizeZ;
        this.seed = seed;
        this.countPerChunk = countPerChunk;
    }

    public RegionCount(int iterations, int regionSize, long seed, IDecorCount countPerChunk) {
        this(iterations, regionSize, regionSize, seed, countPerChunk);
    }

    public RegionCount(int regionSize, long seed, IDecorCount countPerChunk) {
        this(1, regionSize, regionSize, seed, countPerChunk);
    }

    public RegionCount(int regionSize, long seed) {
        this(1, regionSize, regionSize, seed, new One());
    }

    @Override
    public int count(IWorld world, int cx, int cz, Random rand) {
        int rx = Math.floorDiv(cx, regionSizeX);
        int rz = Math.floorDiv(cz, regionSizeZ);

        int lx = cx - rx;
        int lz = cz - rz;

        long seed = world.getSeed() * 28117L ^ rx * 41207245L + rz * 5182371849L ^ this.seed << 17;
        Random r = new Random(seed);

        int count = 0;

        for (int i = 0; i < iterations; i++) {
            int mx = r.nextInt(regionSizeX);
            int mz = r.nextInt(regionSizeZ);

            if (lx == mx && lz == mz) {
                count += countPerChunk.count(world, cx, cz, rand);
            }
        }
        return count;
    }
}
