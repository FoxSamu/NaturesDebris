/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public abstract class SnowyDirtlikeBlock extends DirtlikeBlock {
    public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;

    public SnowyDirtlikeBlock( Properties properties ) {
        super( properties );

        setDefaultState( stateContainer.getBaseState().with( SNOWY, false ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( SNOWY );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( facing == Direction.UP && ( facingState.getBlock() == Blocks.SNOW || facingState.getBlock() == Blocks.SNOW_BLOCK ) ) {
            return state.with( SNOWY, true );
        } else {
            return state.with( SNOWY, false );
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext ctx ) {
        BlockState state = getDefaultState();
        BlockState facingState = ctx.getWorld().getBlockState( ctx.getPos().up() );
        if( facingState.getBlock() == Blocks.SNOW || facingState.getBlock() == Blocks.SNOW_BLOCK ) {
            state = state.with( SNOWY, true );
        }
        return state;
    }
}
