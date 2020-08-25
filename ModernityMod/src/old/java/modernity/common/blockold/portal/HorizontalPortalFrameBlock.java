/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.portal;

import modernity.common.blockold.MDBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

import javax.annotation.Nullable;

public class HorizontalPortalFrameBlock extends AbstractPortalFrameBlock {
    public static final EnumProperty<Direction.Axis> DIRECTION = MDBlockStateProperties.HORIZONTAL_AXIS;

    public HorizontalPortalFrameBlock(Properties props) {
        super(props);
        setDefaultState(getDefaultState().with(DIRECTION, Direction.Axis.X));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing().getAxis());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        Direction.Axis axis = state.get(DIRECTION);
        if(direction != Rotation.CLOCKWISE_180) {
            axis = axis == Direction.Axis.Z ? Direction.Axis.X : Direction.Axis.Z;
        }
        return state.with(DIRECTION, axis);
    }
}
