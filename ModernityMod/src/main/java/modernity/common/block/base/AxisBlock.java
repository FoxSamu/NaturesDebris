/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/**
 * Describes a block that can be oriented along x, y and z axes, but does not have a specific facing.
 */
public class AxisBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;


    public AxisBlock( Properties properties ) {
        super( properties );

        setDefaultState( stateContainer.getBaseState().with( AXIS, Direction.Axis.Y ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( AXIS );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return getDefaultState().with( AXIS, context.getFace().getAxis() );
    }
}
