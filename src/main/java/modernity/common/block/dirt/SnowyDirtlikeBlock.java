/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.common.block.dirt.logic.DirtLogic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class SnowyDirtlikeBlock extends DirtlikeBlock implements ISnowyDirtlikeBlock {

    public SnowyDirtlikeBlock( DirtLogic logic, Properties properties ) {
        super( logic, properties );

        setDefaultState( stateContainer.getBaseState().with( SNOWY, false ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( SNOWY );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( facing == Direction.UP ) {
            return ISnowyDirtlikeBlock.makeSnowy( world, currentPos, state );
        }
        return state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext ctx ) {
        return ISnowyDirtlikeBlock.makeSnowy( ctx.getWorld(), ctx.getPos(), getDefaultState() );
    }
}
