/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.item.base;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import modernity.api.block.fluid.ICustomBucketSound;
import modernity.api.block.fluid.ICustomVaporize;
import modernity.common.fluid.RegularFluid;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ItemBucketBase extends ItemBase {

    protected final Fluid containing;
    protected final Item empty;
    protected final Function<Fluid, Item> fluidToItem;

    public ItemBucketBase( String id, Fluid fluid, Properties properties ) {
        this( id, fluid, Items.BUCKET, Fluid::getFilledBucket, properties );
    }

    public ItemBucketBase( String id, Fluid fluid, Item empty, Function<Fluid, Item> fluidToItem, Properties properties ) {
        super( id, properties );
        this.containing = fluid;
        this.empty = empty;
        this.fluidToItem = fluidToItem;
    }

    public ActionResult<ItemStack> onItemRightClick( World world, EntityPlayer player, EnumHand hand ) {
        ItemStack held = player.getHeldItem( hand );
        RayTraceResult rtr = rayTrace( world, player, containing == Fluids.EMPTY );

        ActionResult<ItemStack> hooked = ForgeEventFactory.onBucketUse( player, world, held, rtr );
        if( hooked != null ) return hooked;

        if( rtr == null ) {
            return new ActionResult<>( EnumActionResult.PASS, held );
        } else if( rtr.type == RayTraceResult.Type.BLOCK ) {
            BlockPos pos = rtr.getBlockPos();
            if( world.isBlockModifiable( player, pos ) && player.canPlayerEdit( pos, rtr.sideHit, held ) ) {
                if( containing == Fluids.EMPTY ) {
                    // Bucket is empty: try to pick up fluid

                    IBlockState state = world.getBlockState( pos );

                    if( state.getBlock() instanceof IBucketPickupHandler ) {
                        Fluid fluid = ( (IBucketPickupHandler) state.getBlock() ).pickupFluid( world, pos, state );

                        if( fluid != Fluids.EMPTY ) {
                            player.addStat( StatList.ITEM_USED.get( this ) );

                            Item filledItem = fluidToItem.apply( fluid );
                            if( filledItem != null ) {
                                ItemStack filled = fillBucket( held, player, filledItem );
                                if( ! world.isRemote ) {
                                    CriteriaTriggers.FILLED_BUCKET.trigger( (EntityPlayerMP) player, new ItemStack( filledItem ) );
                                }
                                playFillSound( player, world, pos, fluid );

                                return new ActionResult<>( EnumActionResult.SUCCESS, filled );
                            } else {
                                world.setBlockState( pos, state );
                            }
                        }
                    }

                    return new ActionResult<>( EnumActionResult.FAIL, held );
                } else {
                    // Bucket is filled: try to place fluid

                    IBlockState state = world.getBlockState( pos );
                    BlockPos placePos = getPlacementPosition( state, pos, rtr );
                    if( tryPlaceContainedLiquid( player, world, placePos, rtr, hand ) ) {
                        onLiquidPlaced( world, held, placePos );
                        if( player instanceof EntityPlayerMP ) {
                            CriteriaTriggers.PLACED_BLOCK.trigger( (EntityPlayerMP) player, placePos, held );
                        }

                        player.addStat( StatList.ITEM_USED.get( this ) );
                        return new ActionResult<>( EnumActionResult.SUCCESS, emptyBucket( held, player ) );
                    } else {
                        return new ActionResult<>( EnumActionResult.FAIL, held );
                    }
                }
            } else {
                return new ActionResult<>( EnumActionResult.FAIL, held );
            }
        } else {
            return new ActionResult<>( EnumActionResult.PASS, held );
        }
    }

    protected BlockPos getPlacementPosition( IBlockState state, BlockPos pos, RayTraceResult rtr ) {
        return state.getBlock() instanceof ILiquidContainer ? pos : rtr.getBlockPos().offset( rtr.sideHit );
    }

    protected ItemStack emptyBucket( ItemStack bucket, EntityPlayer player ) {
        return ! player.abilities.isCreativeMode ? new ItemStack( empty ) : bucket;
    }

    public void onLiquidPlaced( World world, ItemStack bucket, BlockPos pos ) {
    }

    private ItemStack fillBucket( ItemStack emptyBuckets, EntityPlayer player, Item fullBucket ) {
        if( player.abilities.isCreativeMode ) {
            return emptyBuckets;
        } else {
            emptyBuckets.shrink( 1 );
            if( emptyBuckets.isEmpty() ) {
                return new ItemStack( fullBucket );
            } else {
                if( ! player.inventory.addItemStackToInventory( new ItemStack( fullBucket ) ) ) {
                    // Inventory is full
                    player.dropItem( new ItemStack( fullBucket ), false );
                }

                return emptyBuckets;
            }
        }
    }

    protected IFluidState getPlacedFluidState() {
        if( containing instanceof FlowingFluid ) return ( (FlowingFluid) containing ).getStillFluidState( false );
        if( containing instanceof RegularFluid ) return ( (RegularFluid) containing ).getStillFluidState( false );
        return containing.getDefaultState();
    }

    protected void vaporize( @Nullable EntityPlayer player, BlockPos pos, World world ) {
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        world.playSound( player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + ( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.8F );

        for( int i = 0; i < 8; ++ i ) {
            world.addParticle( Particles.LARGE_SMOKE, posX + Math.random(), posY + Math.random(), posZ + Math.random(), 0, 0, 0 );
        }
    }

    public boolean tryPlaceContainedLiquid( @Nullable EntityPlayer player, World world, BlockPos pos, @Nullable RayTraceResult rtr, EnumHand hand ) {
        IBlockState state = world.getBlockState( pos );
        Material mat = state.getMaterial();
        boolean nonSolid = ! mat.isSolid();
        boolean replaceable = mat.isReplaceable();

        if( world.isAirBlock( pos ) || nonSolid || replaceable || state.getBlock() instanceof ILiquidContainer && ( (ILiquidContainer) state.getBlock() ).canContainFluid( world, pos, state, this.containing ) ) {
            if( containing instanceof ICustomVaporize && ( (ICustomVaporize) containing ).doesVaporize( world, pos ) ) {
                // Custom vaporize behaviour
                vaporize( player, pos, world );
            } else if( world.dimension.doesWaterVaporize() && containing.isIn( FluidTags.WATER ) && ! ( containing instanceof ICustomVaporize ) ) {
                // This is vaporize-dimension (usually nether), and there is no custom behaviour, so we may vaporize
                vaporize( player, pos, world );
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

    protected void playEmptySound( @Nullable EntityPlayer player, IWorld world, BlockPos pos ) {
        SoundEvent evt;
        if( containing instanceof ICustomBucketSound ) {
            evt = ( (ICustomBucketSound) containing ).getPlaceSound( world, pos );
        } else {
            evt = containing.isIn( FluidTags.LAVA ) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        }
        world.playSound( player, pos, evt, SoundCategory.BLOCKS, 1, 1 );
    }

    protected void playFillSound( @Nullable EntityPlayer player, IWorld world, BlockPos pos, Fluid fluid ) {
        SoundEvent evt;
        if( fluid instanceof ICustomBucketSound ) {
            evt = ( (ICustomBucketSound) containing ).getPickupSound( world, pos );
        } else {
            evt = fluid.isIn( FluidTags.LAVA ) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL;
        }
        world.playSound( player, pos, evt, SoundCategory.BLOCKS, 1, 1 );
    }
}
