/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modernity.common.item.MDItems;

public class BlockTranslucent extends BlockBase {

    public BlockTranslucent( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockTranslucent( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isSideInvisible( IBlockState state, IBlockState adjacentBlockState, EnumFacing side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
    }

    public static class Salt extends BlockTranslucent {

        public Salt( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Salt( String id, Properties properties ) {
            super( id, properties );
        }

        @Override
        public void getDrops( IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune ) {
            int nuggetAmount = world.rand.nextInt( Math.max( 6 - fortune, 1 ) ) == 0 ? world.rand.nextInt( 3 ) : 0;
            int dustAmount = world.rand.nextInt( fortune + 1 ) + 1;
            // TODO: Drop crystals
            if( nuggetAmount > 0 ) {
                drops.add( new ItemStack( MDItems.SALT_NUGGET, nuggetAmount ) );
            }
            if( dustAmount > 0 ) {
                drops.add( new ItemStack( MDItems.SALT_DUST, dustAmount ) );
            }
        }

        @Override
        protected ItemStack getSilkTouchDrop( IBlockState state ) {
            return new ItemStack( this );
        }
    }
}
