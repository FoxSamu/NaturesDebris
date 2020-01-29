/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class FlyFlowerBlock extends SimplePlantBlock {
    public FlyFlowerBlock( Properties properties ) {
        super( properties, makeCuboidShape( 1, 0, 1, 15, 13, 15 ) );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.getBlock() == MDBlocks.FLY_FLOWER_STALK;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }
}
