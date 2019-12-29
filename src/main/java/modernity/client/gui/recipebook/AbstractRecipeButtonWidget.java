/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.client.gui.recipebook;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipePlacer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractRecipeButtonWidget extends Widget implements IRecipePlacer<Ingredient> {
    protected final IRecipe<?> recipe;
    protected final boolean craftable;
    protected final List<Child> children = Lists.newArrayList();
    protected RecipeOverlayGui gui;
    protected Minecraft mc = Minecraft.getInstance();
    protected final IRecipeBookType type;

    public AbstractRecipeButtonWidget( RecipeOverlayGui gui, int x, int y, IRecipe<?> recipe, boolean craftable ) {
        super( x, y, 24, 24, "" );
        this.recipe = recipe;
        this.craftable = craftable;
        this.gui = gui;
        this.type = gui.type;
        placeRecipe( recipe );
    }

    protected abstract void placeRecipe( IRecipe<?> recipe );

    @Override
    public void setSlotContents( Iterator<Ingredient> ingredients, int slot, int maxAmount, int y, int x ) {
    }

    @Override
    public void renderButton( int mouseX, int mouseY, float partialTicks ) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableAlphaTest();
        mc.getTextureManager().bindTexture( RecipeOverlayGui.TEXTURE );
        int texX = 152;
        if( ! craftable ) {
            texX += 26;
        }

        int texY = gui.isFurnace ? 130 : 78;
        if( isHovered() ) {
            texY += 26;
        }

        blit( x, y, texX, texY, width, height );

        for( Child child : children ) {
            GlStateManager.pushMatrix();
            int childX = (int) ( ( x + child.x ) / 0.42 - 3 );
            int childY = (int) ( ( y + child.y ) / 0.42 - 3 );
            GlStateManager.scaled( 0.42, 0.42, 1 );
            GlStateManager.enableLighting();
            mc.getItemRenderer().renderItemAndEffectIntoGUI( child.items[ MathHelper.floor( gui.time / 30 ) % child.items.length ], childX, childY );
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }

        GlStateManager.disableAlphaTest();
        RenderHelper.disableStandardItemLighting();
    }

    @OnlyIn( Dist.CLIENT )
    public static class Child {
        public final ItemStack[] items;
        public final int x;
        public final int y;

        public Child( int x, int y, ItemStack[] items ) {
            this.x = x;
            this.y = y;
            this.items = items;
        }
    }
}