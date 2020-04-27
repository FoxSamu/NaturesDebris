/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.fluid;

import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

/**
 * Describes a block that can be placed inside modernized water.
 */
public class MurkyWaterloggedBlock extends Block implements IMurkyWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public MurkyWaterloggedBlock( Properties properties ) {
        super( properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( WATERLOGGED );
    }

    @Override
    public IFluidState getFluidState( BlockState state ) {
        return state.get( WATERLOGGED )
               ? MDFluids.MURKY_WATER.getDefaultState()
               : Fluids.EMPTY.getDefaultState();
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        Fluid fluid = world.getFluidState( currentPos ).getFluid();
        world.getPendingFluidTicks().scheduleTick( currentPos, fluid, fluid.getTickRate( world ) );
        return super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        IFluidState fluid = context.getWorld().getFluidState( context.getPos() );
        return getDefaultState().with( WATERLOGGED, fluid.getFluid() == MDFluids.MURKY_WATER );
    }
}
