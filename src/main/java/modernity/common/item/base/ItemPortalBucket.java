/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.item.base;

import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ItemPortalBucket extends ItemBucketBase {
    public ItemPortalBucket( String id, Properties properties ) {
        super( id, MDFluids.PORTAL, properties );
    }

    public ItemPortalBucket( String id, Item empty, Function<Fluid, Item> fluidToItem, Properties properties ) {
        super( id, MDFluids.PORTAL, empty, fluidToItem, properties );
    }

    protected BlockPos getPlacementPosition( IBlockState state, BlockPos pos, RayTraceResult rtr ) {
        return state.getBlock() instanceof ILiquidContainer || state.getBlock() == Blocks.CAULDRON && state.get( BlockCauldron.LEVEL ) == 0 ? pos : rtr.getBlockPos().offset( rtr.sideHit );
    }


    public boolean tryPlaceContainedLiquid( @Nullable EntityPlayer player, World world, BlockPos pos, @Nullable RayTraceResult rtr, EnumHand hand ) {
        IBlockState state = world.getBlockState( pos );
        Material mat = state.getMaterial();
        boolean nonSolid = ! mat.isSolid();
        boolean replaceable = mat.isReplaceable();

        boolean cauldron = state.getBlock() == Blocks.CAULDRON && state.get( BlockCauldron.LEVEL ) == 0;

        if( world.isAirBlock( pos ) || nonSolid || replaceable || cauldron || state.getBlock() instanceof ILiquidContainer && ( (ILiquidContainer) state.getBlock() ).canContainFluid( world, pos, state, containing ) ) {
            if( player != null && cauldron ) {
                if( ! player.abilities.isCreativeMode ) {
                    player.setHeldItem( hand, new ItemStack( Items.BUCKET ) );
                }

                player.addStat( StatList.FILL_CAULDRON );
                world.setBlockState( pos, MDBlocks.PORTAL_CAULDRON.getDefaultState(), 11 );
                world.playSound( null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1 );
            } else if( state.getBlock() instanceof ILiquidContainer ) {
                // Place in block
                if( ( (ILiquidContainer) state.getBlock() ).receiveFluid( world, pos, state, getPlacedFluidState() ) ) {
                    playEmptySound( player, world, pos );
                }
            } else {
                // Just place in air
                if( ! world.isRemote && ( nonSolid || replaceable ) && ! mat.isLiquid() ) {
                    // Remove replaceable blocks
                    world.destroyBlock( pos, true );
                }

                playEmptySound( player, world, pos );
                world.setBlockState( pos, containing.getDefaultState().getBlockState(), 11 );
            }

            return true;
        } else {
            // Try to place on side, if we weren't doing that already
            return rtr != null && tryPlaceContainedLiquid( player, world, rtr.getBlockPos().offset( rtr.sideHit ), null, hand );
        }
    }
}
