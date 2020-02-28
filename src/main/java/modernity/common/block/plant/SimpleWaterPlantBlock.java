/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class SimpleWaterPlantBlock extends SimplePlantBlock implements IWaterPlant {

    @Deprecated
    public SimpleWaterPlantBlock( Properties properties, VoxelShape shape ) {
        super( properties, shape );
    }

    public SimpleWaterPlantBlock( Properties properties, Type type ) {
        super( properties, type );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.SOIL );
    }
}
