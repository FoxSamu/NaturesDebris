/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.container;

import com.google.common.collect.Lists;
import modernity.client.util.MDRecipeBookCategories;
import modernity.common.container.inventory.CleaningInventory;
import modernity.common.container.inventory.ICleaningInventory;
import modernity.common.container.inventory.TrackingCleaningInventory;
import modernity.common.container.slot.CleanerBucketSlot;
import modernity.common.container.slot.CleanerFuelSlot;
import modernity.common.container.slot.CleanerResultSlot;
import modernity.common.recipes.CleaningRecipePlacer;
import modernity.common.recipes.MDRecipeTypes;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.IIntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class CleanerContainer extends RecipeBookContainer<ICleaningInventory> {

    private final ICleaningInventory cleanerInv;
    private final World world;

    private final IIntArray data;

    public CleanerContainer( int windowID, PlayerInventory playerInv ) {
        this( windowID, new TrackingCleaningInventory( 4 ), playerInv, null );
    }

    public CleanerContainer( int windowID, ICleaningInventory cleanerInv, PlayerInventory playerInv, IIntArray data ) {
        super( MDContainerTypes.CLEANER, windowID );
        if( cleanerInv instanceof TrackingCleaningInventory ) {
            data = ( (TrackingCleaningInventory) cleanerInv ).getArray();
        }

        this.cleanerInv = cleanerInv;
        this.data = data;


        this.world = playerInv.player.world;

        // Bucket slot
        addSlot( new CleanerBucketSlot( cleanerInv, 0, 28, 17 ) );

        // Item slot
        addSlot( new Slot( cleanerInv, 1, 62, 17 ) );

        // Fuel slot
        addSlot( new CleanerFuelSlot( this, cleanerInv, 2, 62, 53 ) );

        // Result slot
        addSlot( new CleanerResultSlot( playerInv.player, cleanerInv, 3, 113, 35 ) );

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

        trackIntArray( data );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void func_217056_a( boolean all, IRecipe<?> rec, ServerPlayerEntity player ) {
        new CleaningRecipePlacer<>( this ).place( player, (IRecipe<ICleaningInventory>) rec, all );
    }

    @Override
    public boolean canInteractWith( PlayerEntity player ) {
        return true;
    }

    @OnlyIn( Dist.CLIENT )
    public int getCookProgressionScaled() {
        int time = data.get( 2 );
        int total = data.get( 3 );
        return total != 0 && time != 0 ? time * 24 / total : 0;
    }

    @OnlyIn( Dist.CLIENT )
    public int getBurnProgressionScaled() {
        int total = data.get( 1 );
        if( total == 0 ) {
            total = 200;
        }

        return data.get( 0 ) * 13 / total;
    }

    @OnlyIn( Dist.CLIENT )
    public int getFluidAmountScaled() {
        int fluidAmount = data.get( 4 );
        int fluidUseTotal = data.get( 5 );
        int cookTime = data.get( 2 );
        int cookTimeTotal = data.get( 3 );

        int amount = fluidAmount;
        if( cookTimeTotal != 0 && cookTime != 0 ) {
            amount = fluidAmount - cookTime * fluidUseTotal / cookTimeTotal;
        }

        return Math.max( 0, amount * 52 / 300 );
    }

    @OnlyIn( Dist.CLIENT )
    @Nullable
    public Fluid getFluidToRender() {
        int amount = data.get( 4 );
        if( amount == 0 ) return null;
        int fluidID = data.get( 6 );
        if( fluidID == - 1 ) {
            return null;
        }
        return ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getValue( fluidID );
    }

    protected boolean isValidInput( ItemStack stack ) {
        return world.getRecipeManager().getRecipe( MDRecipeTypes.CLEANING, new CleaningInventory( cleanerInv.getFluid(), cleanerInv.getFluidAmount(), stack ), world ).isPresent();
    }

    private boolean mergeItemStack( ItemStack toTransfer, int index ) {
        return mergeItemStack( toTransfer, index, index + 1, false );
    }

    public static final int BUCKET = 0;
    public static final int INPUT = 1;
    public static final int FUEL = 2;
    public static final int RESULT = 3;

    private static final int INV_START = 4;
    private static final int HOTBAR_START = INV_START + 27;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    @Override
    public ItemStack transferStackInSlot( PlayerEntity player, int index ) {
        ItemStack out = ItemStack.EMPTY;
        Slot slot = inventorySlots.get( index );
        if( slot != null && slot.getHasStack() ) {

            ItemStack toTransfer = slot.getStack();
            out = toTransfer.copy();
            if( index == RESULT ) { // Output slot
                if( ! mergeItemStack( toTransfer, INV_START, HOTBAR_END, true ) ) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange( toTransfer, out );
            } else if( index != FUEL && index != BUCKET && index != INPUT ) { // Player inventory slot
                if( CleanerFuelSlot.isFuel( toTransfer ) ) { // Transfer to fuel slot
                    if( ! mergeItemStack( toTransfer, FUEL ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( CleanerBucketSlot.isBucket( toTransfer ) ) { // Transfer to bucket slot
                    if( ! mergeItemStack( toTransfer, BUCKET ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( isValidInput( toTransfer ) ) { // Transfer to input slot
                    if( ! mergeItemStack( toTransfer, INPUT ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( index < HOTBAR_START ) { // Transfer to hotbar
                    if( ! mergeItemStack( toTransfer, HOTBAR_START, HOTBAR_END, false ) ) {
                        return ItemStack.EMPTY;
                    }
                } else if( index < HOTBAR_END ) { // Transfer to inventory
                    if( ! mergeItemStack( toTransfer, INV_START, HOTBAR_START, false ) ) {
                        return ItemStack.EMPTY;
                    }
                }
            } else { // Input, fuel or bucket slot
                if( mergeItemStack( toTransfer, INV_START, HOTBAR_END, false ) ) {
                    return ItemStack.EMPTY;
                }
            }

            if( toTransfer.isEmpty() ) {
                slot.putStack( ItemStack.EMPTY );
            } else {
                slot.onSlotChanged();
            }

            if( toTransfer.getCount() == out.getCount() ) {
                return ItemStack.EMPTY;
            }

            slot.onTake( player, toTransfer );
        }

        return out;
    }

    public void drain() {
        cleanerInv.drain();
    }

    @Override
    public void func_201771_a( RecipeItemHelper rih ) {
        if( cleanerInv instanceof IRecipeHelperPopulator ) {
            ( (IRecipeHelperPopulator) cleanerInv ).fillStackedContents( rih );
        }
    }

    @Override
    public void clear() {
        cleanerInv.clear();
    }

    @Override
    public boolean matches( IRecipe<? super ICleaningInventory> recipe ) {
        return recipe.matches( cleanerInv, world );
    }

    @Override
    public int getOutputSlot() {
        return RESULT;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public List<RecipeBookCategories> getRecipeBookCategories() {
        return Lists.newArrayList( MDRecipeBookCategories.CLEANER_SEARCH, MDRecipeBookCategories.CLEANER_BLOCKS );
    }
}
