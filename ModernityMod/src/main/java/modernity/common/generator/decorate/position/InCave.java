/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.position;

import modernity.common.util.CaveUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class InCave implements IDecorPosition {

    private final int offset;

    public InCave( int offset ) {
        this.offset = offset;
    }

    public InCave() {
        this( 0 );
    }

    @Override
    public BlockPos findPosition( IWorld world, int cx, int cz, Random rand ) {
        int x = rand.nextInt( 16 ) + cx * 16;
        int z = rand.nextInt( 16 ) + cz * 16;

        int h = CaveUtil.caveHeight( x, z, world ) + offset;
        int y = rand.nextInt( h + 1 );

        return new BlockPos( x, y, z );
    }
}
