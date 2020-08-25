/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public interface ISolidBlock {
    static boolean isSolid(IBlockReader world, BlockPos pos, Direction dir) {
        BlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof ISolidBlock) {
            return ((ISolidBlock) state.getBlock()).isSideSolid(world, pos, state, dir);
        } else {
            return state.isSolidSide(world, pos, dir);
        }
    }
    default boolean isSideSolid(IBlockReader world, BlockPos pos, BlockState state, Direction dir) {
        return true;
    }
}
