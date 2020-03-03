/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate.position;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class FossilPosition implements IDecorPosition {
    @Override
    public BlockPos findPosition( IWorld world, int cx, int cz, Random rand ) {
        return new BlockPos( cx * 16 + 7 + rand.nextInt( 2 ), 30 + rand.nextInt( 15 ), cz * 16 + 7 + rand.nextInt( 2 ) );
    }
}
