/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.container;

import com.google.common.collect.Lists;
import modernity.common.block.base.WorkbenchBlock;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Optional;

public class WorkbenchContainer extends RecipeBookContainer<CraftingInventory> {
    private final CraftingInventory craftingInv = new CraftingInventory( this, 3, 3 );
    private final CraftResultInventory resultInv = new CraftResultInventory();
    private final IWorldPosCallable sidedCallable;
    private final PlayerEntity player;

    public WorkbenchContainer( int windowID, PlayerInventory playerInv ) {
        this( windowID, playerInv, IWorldPosCallable.DUMMY );
    }

    public WorkbenchContainer( int windowID, PlayerInventory playerInv, IWorldPosCallable slotChangeHandler ) {
        super( MDContainerTypes.WORKBENCH, windowID );
        this.sidedCallable = slotChangeHandler;
        this.player = playerInv.player;

        // Result
        addSlot( new CraftingResultSlot( playerInv.player, craftingInv, resultInv, 0, 124, 35 ) );

        // Crafting Grid
        for( int y = 0; y < 3; ++ y ) {
            for( int x = 0; x < 3; ++ x ) {
                addSlot( new Slot( craftingInv, x + y * 3, 30 + x * 18, 17 + y * 18 ) );
            }
        }

        // Player Inventory
        for( int y = 0; y < 3; ++ y ) {
            for( int x = 0; x < 9; ++ x ) {
                addSlot( new Slot( playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18 ) );
            }
        }

        // Player Hotbar
        for( int x = 0; x < 9; ++ x ) {
            addSlot( new Slot( playerInv, x, 8 + x * 18, 142 ) );
        }

    }

    protected static void sendPacket( int windowID, World world, PlayerEntity player, CraftingInventory craftingInv, CraftResultInventory resultInv ) {
        if( ! world.isRemote ) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ItemStack stack = ItemStack.EMPTY;

            Optional<ICraftingRecipe> optRecipe = world.getServer().getRecipeManager().getRecipe( IRecipeType.CRAFTING, craftingInv, world );
            if( optRecipe.isPresent() ) {
                ICraftingRecipe recipe = optRecipe.get();
                if( resultInv.canUseRecipe( world, serverPlayer, recipe ) ) {
                    stack = recipe.getCraftingResult( craftingInv );
                }
            }

            resultInv.setInventorySlotContents( 0, stack );
            serverPlayer.connection.sendPacket( new SSetSlotPacket( windowID, 0, stack ) );
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged( IInventory inventoryIn ) {
        sidedCallable.consume( ( world, pos ) -> sendPacket( windowId, world, player, craftingInv, resultInv ) );
    }

    @Override // fillStackedContents
    public void func_201771_a( RecipeItemHelper helper ) {
        craftingInv.fillStackedContents( helper );
    }

    @Override
    public void clear() {
        craftingInv.clear();
        resultInv.clear();
    }

    @Override
    public boolean matches( IRecipe<? super CraftingInventory> recipe ) {
        return recipe.matches( craftingInv, player.world );
    }

    @Override
    public void onContainerClosed( PlayerEntity player ) {
        super.onContainerClosed( player );
        sidedCallable.consume( ( world, pos ) -> clearContainer( player, world, craftingInv ) );
    }

    @Override
    public boolean canInteractWith( PlayerEntity player ) {
        return isWithinUsableDistance( sidedCallable, player );
    }

    private static boolean isWithinUsableDistance( IWorldPosCallable worldPos, PlayerEntity player ) {
        return worldPos.applyOrElse(
            ( world, pos ) ->
                world.getBlockState( pos ).getBlock() instanceof WorkbenchBlock
                    && player.getDistanceSq( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 ) <= 64,
            true
        );
    }

    @Override
    public ItemStack transferStackInSlot( PlayerEntity player, int index ) {
        ItemStack stack = ItemStack.EMPTY;

        Slot slot = inventorySlots.get( index );

        if( slot != null && slot.getHasStack() ) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if( index == 0 ) {
                sidedCallable.consume( ( world, pos ) -> slotStack.getItem().onCreated( slotStack, world, player ) );
                if( ! mergeItemStack( slotStack, 10, 46, true ) ) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange( slotStack, stack );
            } else if( index >= 10 && index < 37 ) {
                if( ! mergeItemStack( slotStack, 37, 46, false ) ) {
                    return ItemStack.EMPTY;
                }
            } else if( index >= 37 && index < 46 ) {
                if( ! mergeItemStack( slotStack, 10, 37, false ) ) {
                    return ItemStack.EMPTY;
                }
            } else if( ! mergeItemStack( slotStack, 10, 46, false ) ) {
                return ItemStack.EMPTY;
            }

            if( slotStack.isEmpty() ) {
                slot.putStack( ItemStack.EMPTY );
            } else {
                slot.onSlotChanged();
            }

            if( slotStack.getCount() == stack.getCount() ) {
                return ItemStack.EMPTY;
            }

            ItemStack takenStack = slot.onTake( player, slotStack );
            if( index == 0 ) {
                player.dropItem( takenStack, false );
            }
        }

        return stack;
    }

    @Override
    public boolean canMergeSlot( ItemStack stack, Slot slotIn ) {
        return slotIn.inventory != resultInv && super.canMergeSlot( stack, slotIn );
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getWidth() {
        return craftingInv.getWidth();
    }

    @Override
    public int getHeight() {
        return craftingInv.getHeight();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int getSize() {
        return 10;
    }

    @Override
    public List<RecipeBookCategories> getRecipeBookCategories() {
        return Lists.newArrayList( RecipeBookCategories.SEARCH, RecipeBookCategories.EQUIPMENT, RecipeBookCategories.BUILDING_BLOCKS, RecipeBookCategories.MISC, RecipeBookCategories.REDSTONE );
    }
}
