/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 24 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.EWaterlogType;
import modernity.common.block.MDBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

/**
 * A block that can be placed in both vanilla water as modernized water.
 */
public class WaterloggedBlock extends Block implements IWaterloggedBlock {
    public static final EnumProperty<EWaterlogType> WATERLOGGED = MDBlockStateProperties.WATERLOGGED;

    public WaterloggedBlock( Properties properties ) {
        super( properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( WATERLOGGED );
    }

    @Override
    public IFluidState getFluidState( BlockState state ) {
        return state.get( WATERLOGGED ).getFluidState();
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
        return getDefaultState().with( WATERLOGGED, EWaterlogType.getType( fluid ) );
    }
}
