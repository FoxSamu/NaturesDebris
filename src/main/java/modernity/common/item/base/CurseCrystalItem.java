/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.item.base;

import net.minecraft.item.Item;

/**
 * Represents the curse crystal item.
 */
public class CurseCrystalItem extends Item {
    public CurseCrystalItem( Properties properties ) {
        super( properties );
    }

//    @Override
//    public ActionResult<ItemStack> onItemRightClick( World world, PlayerEntity player, Hand hand ) {
//
//        ItemStack stack = player.getHeldItem( hand );
//        if( world.isRemote ) return new ActionResult<>( ActionResultType.PASS, stack );
//
//        RayTraceResult rtr = rayTrace( world, player, RayTraceContext.FluidMode.SOURCE_ONLY );
//        if( rtr.getType() == RayTraceResult.Type.MISS || !( rtr instanceof BlockRayTraceResult) ) return new ActionResult<>( ActionResultType.PASS, stack );
//
//        BlockPos pos = ( (BlockRayTraceResult) rtr ).getPos();
//        BlockState state = world.getBlockState( pos );
//        IFluidState fstate = world.getFluidState( pos );
//        Block block = state.getBlock();
//
//        if( fstate.isTagged( MDFluidTags.PORTALIZABLE ) ) {
//            IFluidState newState = convertToPortal( fstate );
//            if( newState == null ) return new ActionResult<>( ActionResultType.FAIL, stack );
//
//            if( block instanceof ILiquidContainer ) {
//                ILiquidContainer lc = (ILiquidContainer) block;
//                if( lc.canContainFluid( world, pos, state, newState.getFluid() ) ) {
//                    lc.receiveFluid( world, pos, state, newState );
//                    doPortalizeEffect( world, pos );
//                    return new ActionResult<>( ActionResultType.SUCCESS, stack );
//                }
//            }
//            if( block instanceof RegularFluidBlock || block instanceof FlowingFluidBlock ) {
//                world.setBlockState( pos, newState.getBlockState(), 11 );
//                doPortalizeEffect( world, pos );
//                return new ActionResult<>( ActionResultType.SUCCESS, stack );
//            }
//        }
//
//        if( block instanceof CauldronBlock ) {
//            if( state.get( CauldronBlock.LEVEL ) == 3 ) {
//                world.setBlockState( pos, MDBlocks.PORTAL_CAULDRON.getDefaultState(), 11 );
//                doPortalizeEffect( world, pos );
//                return new ActionResult<>( ActionResultType.SUCCESS, stack );
//            }
//        }
//
//        return new ActionResult<>( ActionResultType.PASS, stack );
//    }
//
//    private void doPortalizeEffect( World world, BlockPos pos ) {
//        world.playEvent( MDEvents.SUMMON_PORTAL, pos, 0 );
//    }
//
//    private IFluidState convertToPortal( IFluidState state ) {
//        if( state.isSource() ) {
//            return MDFluids.PORTAL.getDefaultState();
//        }
//        return null;
//    }
}
