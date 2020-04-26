/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class MurkyGrassPathBlock extends Block {

    private static final VoxelShape SHAPE = makeCuboidShape( 0, 0, 0, 16, 15, 16 );

    public MurkyGrassPathBlock( Properties properties ) {
        super( properties );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return SHAPE;
    }
}
