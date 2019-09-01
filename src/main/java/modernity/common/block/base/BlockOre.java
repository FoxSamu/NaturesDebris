/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modernity.common.block.MDBlocks;
import modernity.common.item.MDItems;

public class BlockOre extends BlockBase {

    public BlockOre( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockOre( String id, Properties properties ) {
        super( id, properties );
    }

    public static class Salt extends BlockOre {
        public Salt( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Salt( String id, Properties properties ) {
            super( id, properties );
        }

        @Override
        public void getDrops( IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune ) {
            int nuggetAmount = world.rand.nextInt( Math.max( 6 - fortune, 1 ) * 2 ) == 0 ? world.rand.nextInt( 2 ) : 0;
            int dustAmount = world.rand.nextInt( fortune + 1 ) + 1;
            int crystalAmount = world.rand.nextInt( 100 ) < fortune + 1 ? 1 : 0;
            if( nuggetAmount > 0 ) {
                drops.add( new ItemStack( MDItems.SALT_NUGGET, nuggetAmount ) );
            }
            if( dustAmount > 0 ) {
                drops.add( new ItemStack( MDItems.SALT_DUST, dustAmount ) );
            }
            if( crystalAmount > 0 ) {
                drops.add( new ItemStack( MDBlocks.SALT_CRYSTAL, crystalAmount ) );
            }
        }
    }

    public static class Anthracite extends BlockOre {
        public Anthracite( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Anthracite( String id, Properties properties ) {
            super( id, properties );
        }

        @Override
        public void getDrops( IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune ) {
            drops.add( new ItemStack( MDItems.ANTHRACITE, world.rand.nextInt( fortune + 1 ) ) );
        }
    }
}
