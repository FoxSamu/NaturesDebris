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
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

public class BelowSurface implements IDecorPosition {

    private final Heightmap.Type type;
    private final int offset;

    public BelowSurface( Heightmap.Type type, int offset ) {
        this.type = type;
        this.offset = offset;
    }

    public BelowSurface( Heightmap.Type type ) {
        this( type, 0 );
    }

    @Override
    public BlockPos findPosition( IWorld world, int cx, int cz, Random rand ) {
        int x = rand.nextInt( 16 ) + cx * 16;
        int z = rand.nextInt( 16 ) + cz * 16;

        int h = world.getHeight( type, x, z ) + offset;
        int y = rand.nextInt( h + 1 );

        return new BlockPos( x, y, z );
    }
}
