/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.api.generator.fractal;

import modernity.api.util.math.ILongScrambler;

/**
 * A basic implementation of {@link IRegionRNG}.
 */
public class RegionRNG implements IRegionRNG {

    private static final ILongScrambler SCRAMBLER = ILongScrambler.xorshift(13, -17, 5, -23, 19, -27, 7, -19);

    /**
     * The global seed. This seed is based on the world seed and is the same for all instances used in the same world.
     * This value does not change after initialization.
     */
    private long globalSeed;

    /**
     * The local seed. This seed is based on the {@linkplain #globalSeed global seed} and a seed for this instance. This
     * value does not change after initialization.
     */
    private long localSeed;

    /**
     * The position-local seed. This seed is scrambled and used to generate random values. The inital value is based on
     * the {@linkplain #localSeed local seed} and an XZ coordinate pair. This seed can be reinitialized using {@link
     * #setPosition}.
     */
    private long currentSeed;

    public RegionRNG(long worldSeed, long seed) {
        seed = SCRAMBLER.scramble(seed);
        initWorldSeed(worldSeed);
        initLocalSeed(seed);
    }

    /**
     * Simple LGC scramble operation.
     */
    private static long scramble(long value) {
        return value * 6364136223846793005L + 1442695040888963407L;
    }

    private void initWorldSeed(long seed) {
        globalSeed = seed;
        globalSeed *= scramble(globalSeed);
        globalSeed += seed;
        globalSeed *= scramble(globalSeed);
        globalSeed += seed;
        globalSeed *= scramble(globalSeed);
        globalSeed += seed;
    }

    private void initLocalSeed(long seed) {
        localSeed = seed;
        localSeed *= scramble(localSeed);
        localSeed += globalSeed;
        localSeed *= scramble(localSeed);
        localSeed += globalSeed;
        localSeed *= scramble(localSeed);
        localSeed += globalSeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(long x, long z) {
        currentSeed = localSeed;
        currentSeed *= scramble(currentSeed);
        currentSeed += x;
        currentSeed *= scramble(currentSeed);
        currentSeed += z;
        currentSeed *= scramble(currentSeed);
        currentSeed += x;
        currentSeed *= scramble(currentSeed);
        currentSeed += z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int random(int bound) {
        int rand = (int) ((currentSeed >> 24) % bound);
        if(rand < 0) rand += bound;

        currentSeed *= scramble(currentSeed);
        currentSeed += localSeed;
        return rand;
    }
}
