/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant;

import modernity.common.blockold.MDBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class SimpleWaterPlantBlock extends SimplePlantBlock implements IWaterPlant {

    @Deprecated
    public SimpleWaterPlantBlock(Properties properties, VoxelShape shape) {
        super(properties, shape);
    }

    public SimpleWaterPlantBlock(Properties properties, Type type) {
        super(properties, type);
    }

    @Override
    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.SOIL);
    }
}
