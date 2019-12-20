/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.block.IFluidOverlayBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Describes a glass block. This is a block that culls sides only when the adjacent block is the same block as this
 * block, and with a {@link BlockRenderLayer#CUTOUT}.
 */
public class GlassBlock extends Block implements IFluidOverlayBlock {
    public GlassBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
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

    public static class Translucent extends GlassBlock {

        public Translucent( Properties properties ) {
            super( properties );
        }

        @Override
        public BlockRenderLayer getRenderLayer() {
            return BlockRenderLayer.TRANSLUCENT;
        }
    }
}