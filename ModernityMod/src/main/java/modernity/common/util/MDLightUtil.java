/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public final class MDLightUtil {
    private MDLightUtil() {
    }

    public static int getEffectiveOpacity( IBlockReader world, BlockState state, BlockPos pos, BlockState upState, BlockPos upPos, Direction direction, int opacity ) {
        boolean blocksLight = state.isSolid() && state.isTransparent();
        boolean upBlocksLight = upState.isSolid() && upState.isTransparent();
        if( ! blocksLight && ! upBlocksLight ) {
            return opacity;
        } else {
            VoxelShape shape = blocksLight ? state.getRenderShape( world, pos ) : VoxelShapes.empty();
            VoxelShape upShape = upBlocksLight ? upState.getRenderShape( world, upPos ) : VoxelShapes.empty();
            return VoxelShapes.doAdjacentCubeSidesFillSquare( shape, upShape, direction ) ? 16 : opacity;
        }
    }
}
