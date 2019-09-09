/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStripableLog extends BlockLog {
    private final BlockLog result;

    public BlockStripableLog( String id, BlockLog result, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        this.result = result;
    }

    public BlockStripableLog( String id, BlockLog result, Properties properties ) {
        super( id, properties );
        this.result = result;
    }

    @Override
    public boolean onBlockActivated( IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ ) {
        if( player.getHeldItem( hand ).getItem() instanceof ItemAxe ) {
            world.setBlockState( pos, result.getDefaultState().with( AXIS, state.get( AXIS ) ) );
            world.playSound( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1, 1, false );
            return true;
        }
        return false;
    }
}
