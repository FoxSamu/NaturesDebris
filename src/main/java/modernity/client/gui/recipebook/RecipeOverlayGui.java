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
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class RecipeOverlayGui extends AbstractGui implements IRenderable, IGuiEventListener {
//    protected static final ResourceLocation TEXTURE = new ResourceLocation( "textures/gui/recipe_book.png" );
//
//    private final List<AbstractRecipeButtonWidget> buttons = Lists.newArrayList();
//
//    private boolean visible;
//    private int x;
//    private int y;
//
//    private Minecraft mc;
//    private RecipeList recipeList;
//    private IRecipe<?> lastRecipeClicked;
//    float time;
//    boolean isFurnace;
//
//    protected final IRecipeBookType type;
//
//    public RecipeOverlayGui( IRecipeBookType type ) {
//        this.type = type;
//    }
//
//    public void show( Minecraft mc, RecipeList recipeList, int x, int y, int parentCenterX, int parentCenterY, float widgetSize ) {
//        this.mc = mc;
//        this.recipeList = recipeList;
//        this.x = x;
//        this.y = y;
//
//        if( mc.player.openContainer instanceof AbstractFurnaceContainer ) {
//            this.isFurnace = true;
//        }
//
//        boolean filteringCraftable = mc.player.getRecipeBook().isFilteringCraftable( (RecipeBookContainer) mc.player.openContainer );
//        List<IRecipe<?>> displayRecipes = recipeList.getDisplayRecipes( true );
//        List<IRecipe<?>> filteredRecipes = filteringCraftable
//                                           ? Collections.emptyList()
//                                           : recipeList.getDisplayRecipes( false );
//
//        int displayCount = displayRecipes.size();
//        int totalCount = displayCount + filteredRecipes.size();
//
//        int cols = totalCount <= 16 ? 4 : 5;
//        int rows = (int) Math.ceil( (float) totalCount / cols );
//
//        float rightX = x + Math.min( totalCount, cols ) * 25;
//        float rightBound = parentCenterX + 50;
//        if( rightX > rightBound ) {
//            this.x = (int) ( x - widgetSize * ( ( rightX - rightBound ) / widgetSize ) );
//        }
//
//        float bottomY = y + rows * 25;
//        float bottomBound = parentCenterY + 50;
//        if( bottomY > bottomBound ) {
//            this.y = (int) ( y - widgetSize * MathHelper.ceil( ( bottomY - bottomBound ) / widgetSize ) );
//        }
//
//        float topBound = parentCenterY - 100;
//        if( y < topBound ) {
//            this.y = (int) ( y - widgetSize * MathHelper.ceil( ( (float) y - topBound ) / widgetSize ) );
//        }
//
//        visible = true;
//        buttons.clear();
//
//        for( int i = 0; i < totalCount; ++ i ) {
//            boolean visible = i < displayCount;
//            IRecipe<?> recipe = visible ? displayRecipes.get( i ) : filteredRecipes.get( i - displayCount );
//            int btnX = x + 4 + 25 * ( i % cols );
//            int btnY = y + 5 + 25 * ( i / cols );
//            buttons.add( type.createRecipeButton( this, btnX, btnY, recipe, visible ) );
//        }
//
//        lastRecipeClicked = null;
//    }
//
//    @Override
//    public boolean changeFocus( boolean focused ) {
//        return false;
//    }
//
//    public RecipeList getRecipeList() {
//        return recipeList;
//    }
//
//    public IRecipe<?> getLastRecipeClicked() {
//        return lastRecipeClicked;
//    }
//
//    @Override
//    public boolean mouseClicked( double x, double y, int button ) {
//        if( button == 0 ) {
//            for( AbstractRecipeButtonWidget widget : buttons ) {
//                if( widget.mouseClicked( x, y, button ) ) {
//                    lastRecipeClicked = widget.recipe;
//                    return true;
//                }
//            }
//
//        }
//        return false;
//    }
//
//    @Override
//    public boolean isMouseOver( double x, double y ) {
//        return false;
//    }
//
//    @Override
//    public void render( int mouseX, int mouseY, float partialTicks ) {
//        if( visible ) {
//            time += partialTicks;
//            RenderHelper.enableGUIStandardItemLighting();
//            GlStateManager.enableBlend();
//            GlStateManager.color4f( 1, 1, 1, 1 );
//            mc.getTextureManager().bindTexture( TEXTURE );
//            GlStateManager.pushMatrix();
//            GlStateManager.translatef( 0, 0, 170 );
//
//            int buttonsInARow = buttons.size() <= 16 ? 4 : 5;
//            int width = Math.min( buttons.size(), buttonsInARow );
//            int height = MathHelper.ceil( (float) buttons.size() / (float) buttonsInARow );
//            renderBackground( width, height, 24, 4, 82, 208 );
//            GlStateManager.disableBlend();
//            RenderHelper.disableStandardItemLighting();
//
//            for( AbstractRecipeButtonWidget widget : this.buttons ) {
//                widget.render( mouseX, mouseY, partialTicks );
//            }
//
//            GlStateManager.popMatrix();
//        }
//    }
//
//    private void renderBackground( int width, int height, int tileSize, int edgeSize, int texX, int texY ) {
//        int w = width * tileSize;
//        int h = height * tileSize;
//
//        blit( x, y, texX, texY, edgeSize, edgeSize );
//        blit( x + edgeSize * 2 + w, y, texX + tileSize + edgeSize, texY, edgeSize, edgeSize );
//        blit( x, y + edgeSize * 2 + h, texX, texY + tileSize + edgeSize, edgeSize, edgeSize );
//        blit( x + edgeSize * 2 + w, y + edgeSize * 2 + h, texX + tileSize + edgeSize, texY + tileSize + edgeSize, edgeSize, edgeSize );
//
//        for( int bx = 0; bx < width; ++ bx ) {
//            blit( x + edgeSize + bx * tileSize, y, texX + edgeSize, texY, tileSize, edgeSize );
//            blit( x + edgeSize + ( bx + 1 ) * tileSize, y, texX + edgeSize, texY, edgeSize, edgeSize );
//
//            for( int by = 0; by < height; ++ by ) {
//                if( bx == 0 ) {
//                    blit( x, y + edgeSize + by * tileSize, texX, texY + edgeSize, edgeSize, tileSize );
//                    blit( x, y + edgeSize + ( by + 1 ) * tileSize, texX, texY + edgeSize, edgeSize, edgeSize );
//                }
//
//                blit( x + edgeSize + bx * tileSize, y + edgeSize + by * tileSize, texX + edgeSize, texY + edgeSize, tileSize, tileSize );
//                blit( x + edgeSize + ( bx + 1 ) * tileSize, y + edgeSize + by * tileSize, texX + edgeSize, texY + edgeSize, edgeSize, tileSize );
//                blit( x + edgeSize + bx * tileSize, y + edgeSize + ( by + 1 ) * tileSize, texX + edgeSize, texY + edgeSize, tileSize, edgeSize );
//                blit( x + edgeSize + ( bx + 1 ) * tileSize - 1, y + edgeSize + ( by + 1 ) * tileSize - 1, texX + edgeSize, texY + edgeSize, edgeSize + 1, edgeSize + 1 );
//                if( bx == width - 1 ) {
//                    blit( x + edgeSize * 2 + width * tileSize, y + edgeSize + by * tileSize, texX + tileSize + edgeSize, texY + edgeSize, edgeSize, tileSize );
//                    blit( x + edgeSize * 2 + width * tileSize, y + edgeSize + ( by + 1 ) * tileSize, texX + tileSize + edgeSize, texY + edgeSize, edgeSize, edgeSize );
//                }
//            }
//
//            blit( x + edgeSize + bx * tileSize, y + edgeSize * 2 + height * tileSize, texX + edgeSize, texY + tileSize + edgeSize, tileSize, edgeSize );
//            blit( x + edgeSize + ( bx + 1 ) * tileSize, y + edgeSize * 2 + height * tileSize, texX + edgeSize, texY + tileSize + edgeSize, edgeSize, edgeSize );
//        }
//
//    }
//
//    public void setVisible( boolean visible ) {
//        this.visible = visible;
//    }
//
//    public boolean isVisible() {
//        return this.visible;
//    }
//
////    @OnlyIn( Dist.CLIENT )
////    public static class FurnaceRecipeButtonWidget extends RecipeButtonWidget {
////        public FurnaceRecipeButtonWidget( RecipeOverlayGui gui, int x, int y, IRecipe<?> recipe, boolean craftable ) {
////            super( gui, x, y, recipe, craftable );
////        }
////
////        @Override
////        protected void placeRecipe( IRecipe<?> recipe ) {
////            ItemStack[] items = recipe.getIngredients().get( 0 ).getMatchingStacks();
////            children.add( new Child( 10, 10, items ) );
////        }
////    }
//
//}