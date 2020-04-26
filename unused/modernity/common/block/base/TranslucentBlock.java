/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.generic.block.IFluidOverlayBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Describes a translucent block. A translucent block only culls faces of equivalent blocks.
 */
public class TranslucentBlock extends Block implements IFluidOverlayBlock {

    public TranslucentBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean isSideInvisible( BlockState state, BlockState adjacentBlockState, Direction side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
    }

    @Override
    public boolean propagatesSkylightDown( BlockState state, IBlockReader reader, BlockPos pos ) {
        return true;
    }

}
