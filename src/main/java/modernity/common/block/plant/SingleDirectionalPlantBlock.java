/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public abstract class SingleDirectionalPlantBlock extends DirectionalPlantBlock {
    public SingleDirectionalPlantBlock( Properties properties, Direction growDir ) {
        super( properties, growDir );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) ) {
            world.setBlockState( pos, computeStateForPos( world, pos, getDefaultState() ), 2 );
            return true;
        }
        return false;
    }
}
