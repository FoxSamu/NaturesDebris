/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.misc;

import modernity.common.blockold.MDBlockStateProperties;
import modernity.common.blockold.base.AxisBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlackboneBlock extends AxisBlock {
    private static final BooleanProperty NATURAL = MDBlockStateProperties.NATURAL;

    public BlackboneBlock(Properties properties) {
        super(properties);

        setDefaultState(stateContainer.getBaseState().with(NATURAL, true));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(NATURAL);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return Optional.ofNullable(super.getStateForPlacement(context))
                       .orElseGet(this::getDefaultState)
                       .with(NATURAL, false);
    }
}
