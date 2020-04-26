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

public class FixedHeight implements IDecorPosition {

    private final int y;

    public FixedHeight( int y ) {
        this.y = y;
    }

    @Override
    public BlockPos findPosition( IWorld world, int cx, int cz, Random rand ) {
        int x = rand.nextInt( 16 ) + cx * 16;
        int z = rand.nextInt( 16 ) + cz * 16;

        return new BlockPos( x, y, z );
    }
}
