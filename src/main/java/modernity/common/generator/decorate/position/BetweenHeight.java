/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.position;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class BetweenHeight implements IDecorPosition {

    private final int min;
    private final int max;

    public BetweenHeight( int min, int max ) {
        this.min = min;
        this.max = max;
    }

    @Override
    public BlockPos findPosition( IWorld world, int cx, int cz, Random rand ) {
        int x = rand.nextInt( 16 ) + cx * 16;
        int z = rand.nextInt( 16 ) + cz * 16;
        int y = rand.nextInt( max - min + 1 ) + min;

        return new BlockPos( x, y, z );
    }
}
