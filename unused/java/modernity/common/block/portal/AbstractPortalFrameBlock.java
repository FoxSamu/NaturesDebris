/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.portal;

import modernity.generic.util.BlockUpdates;
import modernity.common.block.MDBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;

public abstract class AbstractPortalFrameBlock extends Block {
    public static final BooleanProperty ACTIVE = MDBlockStateProperties.ACTIVE;

    public AbstractPortalFrameBlock( Properties props ) {
        super( props );

        setDefaultState( stateContainer.getBaseState().with( ACTIVE, false ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( ACTIVE );
    }

    public static void setActive( IWorld world, BlockPos pos, BlockState state, boolean active ) {
        if( state.getBlock() instanceof AbstractPortalFrameBlock ) {
            world.setBlockState( pos, state.with( ACTIVE, active ), BlockUpdates.CAUSE_UPDATE | BlockUpdates.NOTIFY_CLIENTS );
        }
    }

    @Override
    public int getLightValue( BlockState state, IEnviromentBlockReader world, BlockPos pos ) {
        return state.get( ACTIVE ) ? 4 : 0;
    }
}
