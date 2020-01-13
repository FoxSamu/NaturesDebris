/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class HangingPlantBlock extends TallDirectionalPlantBlock {

    private final VoxelShape shape;

    public HangingPlantBlock( Properties properties, VoxelShape shape ) {
        super( properties, Direction.DOWN );
        this.shape = shape;
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.func_224755_d( world, pos, Direction.DOWN );
    }
}
