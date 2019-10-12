/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.block.base;

import modernity.common.fluid.OilFluid;
import modernity.common.fluid.RegularFluid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OilFluidBlock extends RegularFluidBlock {

    public static final BooleanProperty BURNING = OilFluid.BURNING;

    public OilFluidBlock( RegularFluid fluid, Properties builder ) {
        super( fluid, builder );
        setDefaultState( getStateContainer().getBaseState().with( BURNING, false ) );
    }

    @Override
    protected void fillFluidStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillFluidStateContainer( builder );
        builder.add( BURNING );
    }

    @Override
    public IFluidState getFluidState( BlockState state ) {
        return super.getFluidState( state ).with( BURNING, state.get( BURNING ) );
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        entity.onGround = false;
        if( entity.isBurning() ) {
            world.setBlockState( pos, state.with( BURNING, true ) );
        }
    }
}
