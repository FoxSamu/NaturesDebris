/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modernity.common.fluid.OilFluid;
import modernity.common.fluid.RegularFluid;

public class BlockOilFluid extends BlockFluid {
    private static final String SRG_EntityLivingBase_inWater = "field_70171_ac";

    public static final BooleanProperty BURNING = OilFluid.BURNING;

    public BlockOilFluid( String id, RegularFluid fluid, Properties builder ) {
        super( id, fluid, builder );
        setDefaultState( getStateContainer().getBaseState().with( BURNING, false ) );
    }

    @Override
    protected void fillFluidStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillFluidStateContainer( builder );
        builder.add( BURNING );
    }

    @Override
    public IFluidState getFluidState( IBlockState state ) {
        return super.getFluidState( state ).with( BURNING, state.get( BURNING ) );
    }

    @Override
    public void onEntityCollision( IBlockState state, World world, BlockPos pos, Entity entity ) {
        entity.onGround = false;
        if( entity.isBurning() ) {
            world.setBlockState( pos, state.with( BURNING, true ) );
        }
    }
}
