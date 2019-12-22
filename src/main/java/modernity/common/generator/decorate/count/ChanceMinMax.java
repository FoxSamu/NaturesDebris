/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.count;

import net.minecraft.world.IWorld;

import java.util.Random;

public class ChanceMinMax implements IDecorCount {
    private final double chance;
    private final int min;
    private final int max;

    public ChanceMinMax( double chance, int min, int max ) {
        this.chance = chance;
        this.min = min;
        this.max = max;
    }

    @Override
    public int count( IWorld world, int cx, int cz, Random rand ) {
        return rand.nextDouble() < chance ? rand.nextInt( max - min + 1 ) + min : 0;
    }
}
