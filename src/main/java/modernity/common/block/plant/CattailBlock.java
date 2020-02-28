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
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class CattailBlock extends DoublePlantBlock {
    private static final VoxelShape SHAPE = makeCuboidShape( 1, 0, 1, 15, 16, 15 );

    public CattailBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.SOIL ) && isBlockSideSustainable( state, world, pos, Direction.UP );
    }

    @Override
    public VoxelShape getShape( BlockState state ) {
        return SHAPE;
    }
}
