/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.common.tileentity.WorkbenchTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings( "deprecation" )
public class WorkbenchBlock extends HorizontalFacingBlock {

    public WorkbenchBlock( Properties properties ) {
        super( properties );
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new WorkbenchTileEntity();
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        player.openContainer( state.getContainer( world, pos ) );
        player.addStat( Stats.INTERACT_WITH_CRAFTING_TABLE );
        return true;
    }

    @Override
    public INamedContainerProvider getContainer( BlockState state, World world, BlockPos pos ) {
        TileEntity te = world.getTileEntity( pos );
        if( te instanceof WorkbenchTileEntity ) {
            return (INamedContainerProvider) te;
        }
        return null;
    }

    @Override
    public void onReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving ) {
        if( state.getBlock() != newState.getBlock() ) {
            TileEntity te = world.getTileEntity( pos );
            if( te instanceof IInventory ) {
                InventoryHelper.dropInventoryItems( world, pos, (IInventory) te );
                world.updateComparatorOutputLevel( pos, this );
            }

            super.onReplaced( state, world, pos, newState, isMoving );
        }
    }
}
