/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.base;

import modernity.generic.block.IFluidOverlayBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Describes a glass block. This is a block that culls sides only when the adjacent block is the same block as this
 * block.
 */
public class TransparentBlock extends Block implements IFluidOverlayBlock {
    public TransparentBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean propagatesSkylightDown( BlockState state, IBlockReader reader, BlockPos pos ) {
        return true;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean isSideInvisible( BlockState state, BlockState adjacentBlockState, Direction side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
    }
}