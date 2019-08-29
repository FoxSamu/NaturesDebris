/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 29 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockGlass extends BlockBase {
    public BlockGlass( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockGlass( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    public int quantityDropped( IBlockState state, Random random ) {
        return 0;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean propagatesSkylightDown( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return true;
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    @Override
    public boolean canSilkHarvest( IBlockState state, IWorldReader world, BlockPos pos, EntityPlayer player ) {
        return true;
    }

    @Override
    protected ItemStack getSilkTouchDrop( IBlockState state ) {
        return new ItemStack( asItem() );
    }

    @OnlyIn( Dist.CLIENT )
    public boolean isSideInvisible( IBlockState state, IBlockState adjacentBlockState, EnumFacing side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
    }
}
