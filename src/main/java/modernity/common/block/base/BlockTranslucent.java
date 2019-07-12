/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.client.particle.SaltParticle;
import modernity.common.block.MDBlocks;
import modernity.common.item.MDItems;

import java.util.Random;

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
        @OnlyIn( Dist.CLIENT )
        public void animateTick( IBlockState state, World world, BlockPos pos, Random rand ) {
            if( rand.nextInt( 5 ) == 0 ) {
                if( world.getBlockState( pos.down() ).getMaterial().blocksMovement() ) return;

                double x = rand.nextDouble() + pos.getX();
                double y = - 0.05 + pos.getY();
                double z = rand.nextDouble() + pos.getZ();

                Minecraft.getInstance().particles.addEffect( new SaltParticle( world, x, y, z, 0, 0, 0 ) );
            }
        }

        @Override
        public void getDrops( IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune ) {
            int nuggetAmount = world.rand.nextInt( Math.max( 6 - fortune, 1 ) ) == 0 ? world.rand.nextInt( 3 ) : 0;
            int dustAmount = world.rand.nextInt( fortune * 2 + 1 ) + 2;
            int crystalAmount = world.rand.nextInt( 80 ) < fortune + 1 ? 1 : 0;
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

        @Override
        protected ItemStack getSilkTouchDrop( IBlockState state ) {
            return new ItemStack( this );
        }
    }
}
