/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.portal;

import natures.debris.common.blockold.MDBlockStateProperties;
import natures.debris.generic.util.BlockUpdates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public abstract class AbstractPortalFrameBlock extends Block {
    public static final BooleanProperty ACTIVE = MDBlockStateProperties.ACTIVE;

    public AbstractPortalFrameBlock(Properties props) {
        super(props);

        setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
    }

    public static void setActive(IWorld world, BlockPos pos, BlockState state, boolean active) {
        if (state.getBlock() instanceof AbstractPortalFrameBlock) {
            world.setBlockState(pos, state.with(ACTIVE, active), BlockUpdates.CAUSE_UPDATE | BlockUpdates.NOTIFY_CLIENTS);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(ACTIVE) ? 4 : 0;
    }
}
