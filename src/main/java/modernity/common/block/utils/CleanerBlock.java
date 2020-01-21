/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.block.utils;

import modernity.common.tileentity.CleanerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CleanerBlock extends ContainerBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public CleanerBlock( Properties properties ) {
        super( properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( LIT );
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity( IBlockReader world ) {
        return new CleanerTileEntity();
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        if( ! world.isRemote ) {
            interactWith( world, pos, player );
        }

        return true;
    }

    protected void interactWith( World world, BlockPos pos, PlayerEntity player ) {
        TileEntity te = world.getTileEntity( pos );
        if( te instanceof INamedContainerProvider ) {
            player.openContainer( (INamedContainerProvider) te );
        }
    }
}
