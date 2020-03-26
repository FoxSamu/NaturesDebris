/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 05 - 2020
 * Author: rgsw
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
