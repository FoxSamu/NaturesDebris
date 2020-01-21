/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.client.gui.recipebook;

import com.mojang.blaze3d.platform.GlStateManager;
import modernity.api.block.fluid.IAluminiumBucketTakeable;
import modernity.common.container.CleanerContainer;
import modernity.common.fluid.MDFluidTags;
import modernity.common.recipes.CleaningRecipe;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class CleanerBookGui extends RecipeBookGui {
    private Iterator<Item> fuelItemsItr;
    private Set<Item> fuelItems;
    private Slot fuelSlot;
    private Item currFuelItem;

    private Iterator<Map.Entry<Item, Fluid>> bucketItemsItr;
    private Map<Item, Fluid> bucketItems;
    private Slot bucketSlot;
    private Item currBucketItem;

    private float time;

    public CleanerBookGui( IRecipeBookType type ) {
        super( type );
    }

    @Override
    protected boolean toggleCraftableFilter() {
        boolean filtering = ! recipeBook.isFurnaceFilteringCraftable();
        recipeBook.setFurnaceFilteringCraftable( filtering );
        return filtering;
    }

    @Override
    public boolean isVisible() {
        return recipeBook.isFurnaceGuiOpen();
    }

    @Override
    protected void setVisible( boolean open ) {
        recipeBook.setFurnaceGuiOpen( open );
        if( ! open ) {
            recipeBookPage.setInvisible();
        }

        sendUpdateSettings();
    }



    @Override
    protected String craftableAllTranslation() {
        return I18n.format( toggleRecipesButton.isStateTriggered()
                            ? "gui.recipebook.toggleRecipes.smeltable"
                            : "gui.recipebook.toggleRecipes.all" );
    }

    @Override
    public void slotClicked( @Nullable Slot slot ) {
        super.slotClicked( slot );
        if( slot != null && slot.slotNumber < container.getSize() ) {
            fuelSlot = null;
        }
    }

    @Override
    public void setupGhostRecipe( IRecipe<?> rec, List<Slot> slots ) {
        CleaningRecipe recipe = (CleaningRecipe) rec;
        ItemStack result = rec.getRecipeOutput();

        ghostRecipe.setRecipe( rec );
        ghostRecipe.addIngredient( Ingredient.fromStacks( result ), slots.get( CleanerContainer.RESULT ).xPos, slots.get( CleanerContainer.RESULT ).yPos );

        NonNullList<Ingredient> ingrs = rec.getIngredients();

        fuelSlot = slots.get( CleanerContainer.FUEL );
        if( fuelItems == null ) {
            fuelItems = getFuelItems();
        }

        fuelItemsItr = fuelItems.iterator();
        currFuelItem = null;

        bucketSlot = slots.get( CleanerContainer.BUCKET );
        if( bucketItems == null ) {
            bucketItems = getBucketItems();
        }

        bucketItemsItr = bucketItems.entrySet()
                                    .stream()
                                    .filter( item -> recipe.getRequiredFluid().test( item.getValue() ) )
                                    .iterator();
        currBucketItem = null;

        Iterator<Ingredient> ingrIterator = ingrs.iterator();

        for( int slotI = CleanerContainer.INPUT; slotI != CleanerContainer.RESULT; slotI = CleanerContainer.RESULT ) {
            if( ! ingrIterator.hasNext() ) {
                return;
            }

            Ingredient ingr = ingrIterator.next();
            if( ! ingr.hasNoMatchingItems() ) {
                Slot slot = slots.get( slotI );
                ghostRecipe.addIngredient( ingr, slot.xPos, slot.yPos );
            }
        }

    }

    protected Set<Item> getFuelItems() {
        HashSet<Item> items = new HashSet<>();
        for( Item item : ForgeRegistries.ITEMS ) {
            if( ForgeHooks.getBurnTime( item.getDefaultInstance() ) > 0 ) {
                items.add( item );
            }
        }
        return items;
    }

    protected Map<Item, Fluid> getBucketItems() {
        HashMap<Item, Fluid> items = new HashMap<>();

        for( Fluid fluid : ForgeRegistries.FLUIDS ) {
            if( ! fluid.isIn( MDFluidTags.CLEANING ) ) continue;

            if( fluid instanceof IAluminiumBucketTakeable ) {
                items.put( ( (IAluminiumBucketTakeable) fluid ).getFilledAluminiumBucket(), fluid );
            }
            if( fluid.getFilledBucket() != Items.BUCKET ) {
                items.put( fluid.getFilledBucket(), fluid );
            }
        }

        return items;
    }

    @Override
    public void renderGhostRecipe( int xoff, int yoff, boolean largeResultSlot, float partialTicks ) {
        super.renderGhostRecipe( xoff, yoff, largeResultSlot, partialTicks );
        if( fuelSlot != null ) {
            if( ! Screen.hasControlDown() ) {
                time += partialTicks;
            }

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();


            int x = fuelSlot.xPos + xoff;
            int y = fuelSlot.yPos + yoff;
            AbstractGui.fill( x, y, x + 16, y + 16, 0x30ff0000 );
            mc.getItemRenderer().renderItemAndEffectIntoGUI( mc.player, findSuitableFuelItem().getDefaultInstance(), x, y );
            GlStateManager.depthFunc( 516 );
            AbstractGui.fill( x, y, x + 16, y + 16, 0x30ffffff );
            GlStateManager.depthFunc( 515 );


            x = bucketSlot.xPos + xoff;
            y = bucketSlot.yPos + yoff;
            AbstractGui.fill( x, y, x + 16, y + 16, 0x30ff0000 );
            mc.getItemRenderer().renderItemAndEffectIntoGUI( mc.player, findSuitableBucketItem( (CleaningRecipe) ghostRecipe.getRecipe() ).getDefaultInstance(), x, y );
            GlStateManager.depthFunc( 516 );
            AbstractGui.fill( x, y, x + 16, y + 16, 0x30ffffff );
            GlStateManager.depthFunc( 515 );


            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();
        }
    }

    private Item findSuitableFuelItem() {
        if( currFuelItem == null || time > 30 ) {
            time = 0;
            if( fuelItemsItr == null || ! fuelItemsItr.hasNext() ) {
                if( fuelItems == null ) {
                    fuelItems = getFuelItems();
                }

                fuelItemsItr = fuelItems.iterator();
            }

            currFuelItem = fuelItemsItr.next();
        }

        return currFuelItem;
    }

    private Item findSuitableBucketItem( CleaningRecipe rec ) {
        if( currBucketItem == null || time > 30 ) {
            time = 0;


            if( bucketItemsItr == null || ! bucketItemsItr.hasNext() ) {
                if( bucketItems == null ) {
                    bucketItems = getBucketItems();
                }

                bucketItemsItr = bucketItems.entrySet()
                                            .stream()
                                            .filter( item -> rec.getRequiredFluid().test( item.getValue() ) )
                                            .iterator();
            }

            currBucketItem = bucketItemsItr.hasNext() ? bucketItemsItr.next().getKey() : Items.AIR;
        }

        return currBucketItem;
    }
}
