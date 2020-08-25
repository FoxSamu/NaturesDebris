/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.blockold.MDPlantBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class FlyFlowerBlock extends SimplePlantBlock {
    public FlyFlowerBlock(Properties properties) {
        super(properties, makeCuboidShape(1, 0, 1, 15, 13, 15));
    }

    @Override
    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state) {
        return state.getBlock() == MDPlantBlocks.FLY_FLOWER_STALK;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }
}
