/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.gui.recipebook;

// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class RecipeTabToggleWidget extends ToggleWidget {
//    private static final int TEX_COORD_X = 153;
//    private static final int TEX_COORD_Y = 2;
//    private static final int TEX_DIFF_X = 35;
//    private static final int TEX_DIFF_Y = 0;
//
//    private final RecipeBookCategories category;
//    private float animationTime;
//
//    public RecipeTabToggleWidget( RecipeBookCategories category ) {
//        super( 0, 0, 35, 27, false );
//        this.category = category;
//
//        initTextureValues( TEX_COORD_X, TEX_COORD_Y, TEX_DIFF_X, TEX_DIFF_Y, RecipeBookGui.TEXTURE );
//    }
//
//    public void startAnimation( Minecraft mc ) {
//        ClientRecipeBook book = mc.player.getRecipeBook();
//
//        List<RecipeList> lists = book.getRecipes( category );
//        if( mc.player.openContainer instanceof RecipeBookContainer ) {
//            lists:
//            for( RecipeList list : lists ) {
//                Iterator<IRecipe<?>> recipes = list.getRecipes( book.isFilteringCraftable( (RecipeBookContainer) mc.player.openContainer ) ).iterator();
//
//                while( true ) {
//                    if( ! recipes.hasNext() ) {
//                        continue lists;
//                    }
//
//                    IRecipe<?> irecipe = recipes.next();
//                    if( book.isNew( irecipe ) ) {
//                        break;
//                    }
//                }
//
//                animationTime = 15;
//                return;
//            }
//
//        }
//    }
//
//    @Override
//    public void renderButton( int p_renderButton_1_, int p_renderButton_2_, float partialTicks ) {
//        if( animationTime > 0 ) {
//            float offset = 1 + 0.1F * (float) Math.sin( animationTime / 15 * (float) Math.PI );
//            GlStateManager.pushMatrix();
//            GlStateManager.translatef( x + 8, y + 12, 0 );
//            GlStateManager.scalef( 1, offset, 1 );
//            GlStateManager.translatef( - ( x + 8 ), - ( y + 12 ), 0 );
//        }
//
//        Minecraft mc = Minecraft.getInstance();
//        mc.getTextureManager().bindTexture( resourceLocation );
//        GlStateManager.disableDepthTest();
//
//        int xTex = xTexStart;
//        int yTex = yTexStart;
//        if( stateTriggered ) {
//            xTex += xDiffTex;
//        }
//
//        if( isHovered() ) {
//            yTex += yDiffTex;
//        }
//
//        int xpos = x;
//        if( stateTriggered ) {
//            xpos -= 2;
//        }
//
//        GlStateManager.color4f( 1, 1, 1, 1 );
//        blit( xpos, y, xTex, yTex, width, height );
//
//        GlStateManager.enableDepthTest();
//
//        RenderHelper.enableGUIStandardItemLighting();
//        GlStateManager.disableLighting();
//        renderIcon( mc.getItemRenderer() );
//        GlStateManager.enableLighting();
//        RenderHelper.disableStandardItemLighting();
//
//        if( animationTime > 0 ) {
//            GlStateManager.popMatrix();
//            animationTime -= partialTicks;
//        }
//
//    }
//
//    private void renderIcon( ItemRenderer renderer ) {
//        List<ItemStack> icons = category.getIcons();
//        int xoff = stateTriggered ? - 2 : 0;
//        if( icons.size() == 1 ) {
//            renderer.renderItemAndEffectIntoGUI( icons.get( 0 ), x + 9 + xoff, y + 5 );
//        } else if( icons.size() == 2 ) {
//            renderer.renderItemAndEffectIntoGUI( icons.get( 0 ), x + 3 + xoff, y + 5 );
//            renderer.renderItemAndEffectIntoGUI( icons.get( 1 ), x + 14 + xoff, y + 5 );
//        }
//
//    }
//
//    public RecipeBookCategories getCategory() {
//        return category;
//    }
//
//    public boolean updateVisibility( ClientRecipeBook book ) {
//        List<RecipeList> lists = book.getRecipes( category );
//        visible = false;
//        for( RecipeList list : lists ) {
//            if( list.isNotEmpty() && list.containsValidRecipes() ) {
//                visible = true;
//                break;
//            }
//        }
//
//        return visible;
//    }
//}