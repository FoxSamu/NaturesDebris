/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.item.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import modernity.common.block.MDBlocks;
import modernity.common.block.base.BlockFluid;
import modernity.common.fluid.MDFluidTags;
import modernity.common.fluid.MDFluids;
import modernity.common.util.MDEvents;

public class ItemCurseCrystal extends ItemBase {
    public ItemCurseCrystal( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick( World world, EntityPlayer player, EnumHand hand ) {

        ItemStack stack = player.getHeldItem( hand );
        if( world.isRemote ) return new ActionResult<>( EnumActionResult.PASS, stack );

        RayTraceResult rtr = rayTrace( world, player, true );
        if( rtr == null ) return new ActionResult<>( EnumActionResult.PASS, stack );

        BlockPos pos = rtr.getBlockPos();
        IBlockState state = world.getBlockState( pos );
        IFluidState fstate = world.getFluidState( pos );
        Block block = state.getBlock();

        if( fstate.isTagged( MDFluidTags.PORTALIZABLE ) ) {
            IFluidState newState = convertToPortal( fstate );
            if( newState == null ) return new ActionResult<>( EnumActionResult.FAIL, stack );

            if( block instanceof ILiquidContainer ) {
                ILiquidContainer lc = (ILiquidContainer) block;
                if( lc.canContainFluid( world, pos, state, newState.getFluid() ) ) {
                    lc.receiveFluid( world, pos, state, newState );
                    doPortalizeEffect( world, pos );
                    return new ActionResult<>( EnumActionResult.SUCCESS, stack );
                }
            }
            if( block instanceof BlockFluid || block instanceof BlockFlowingFluid ) {
                world.setBlockState( pos, newState.getBlockState(), 11 );
                doPortalizeEffect( world, pos );
                return new ActionResult<>( EnumActionResult.SUCCESS, stack );
            }
        }

        if( block instanceof BlockCauldron ) {
            if( state.get( BlockCauldron.LEVEL ) == 3 ) {
                world.setBlockState( pos, MDBlocks.PORTAL_CAULDRON.getDefaultState(), 11 );
                doPortalizeEffect( world, pos );
                return new ActionResult<>( EnumActionResult.SUCCESS, stack );
            }
        }

        return new ActionResult<>( EnumActionResult.PASS, stack );
    }

    private void doPortalizeEffect( World world, BlockPos pos ) {
        world.playEvent( MDEvents.SUMMON_PORTAL, pos, 0 );
    }

    private IFluidState convertToPortal( IFluidState state ) {
        if( state.isSource() ) {
            return MDFluids.PORTAL.getDefaultState();
        }
        return null;
    }
}
