/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 2 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class BlockHorizontalFacing extends BlockBase {
    public static final EnumProperty<EnumFacing> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockHorizontalFacing( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        init();
    }

    public BlockHorizontalFacing( String id, Properties properties ) {
        super( id, properties );
        init();
    }

    private void init() {
        setDefaultState( stateContainer.getBaseState().with( FACING, EnumFacing.NORTH ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( FACING );
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        return getDefaultState().with( FACING, context.getPlacementHorizontalFacing().getOpposite() );
    }
}
