/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.position;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class FossilPosition implements IDecorPosition {
    @Override
    public BlockPos findPosition( IWorld world, int cx, int cz, Random rand ) {
        return new BlockPos( cx * 16 + 7 + rand.nextInt( 2 ), 20 + rand.nextInt( 25 ), cz * 16 + 7 + rand.nextInt( 2 ) );
    }
}
