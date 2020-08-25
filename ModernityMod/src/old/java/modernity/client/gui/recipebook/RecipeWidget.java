/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.gui.recipebook;

// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class RecipeWidget extends Widget {
//    protected static final int TEXTURE_U = 29;
//    protected static final int TEXTURE_V = 206;
//    protected static final int SIZE = 25;
//
//    private static final ResourceLocation TEXTURE = new ResourceLocation( "textures/gui/recipe_book.png" );
//
//    private RecipeBookContainer<?> container;
//    private RecipeBook book;
//    private RecipeList list;
//    private float time;
//    private float animationTime;
//    private int currentIndex;
//
//    public RecipeWidget() {
//        super( 0, 0, SIZE, SIZE, "" );
//    }
//
//    public void update( RecipeList list, RecipeBookPage page ) {
//        this.list = list;
//
//        container = (RecipeBookContainer) page.minecraft().player.openContainer;
//        book = page.getBook();
//
//        List<IRecipe<?>> recipes = list.getRecipes( book.isFilteringCraftable( container ) );
//
//        for( IRecipe<?> recipe : recipes ) {
//            if( book.isNew( recipe ) ) {
//                page.onRecipesShown( recipes );
//                animationTime = 15;
//                break;
//            }
//        }
//
//    }
//
//    public RecipeList getList() {
//        return list;
//    }
//
//    public void setPosition( int x, int y ) {
//        this.x = x;
//        this.y = y;
//    }
//
//    @Override
//    public void renderButton( int mx, int my, float partialTicks ) {
//        if( ! Screen.hasControlDown() ) {
//            time += partialTicks;
//        }
//
//        RenderHelper.enableGUIStandardItemLighting();
//
//        Minecraft mc = Minecraft.getInstance();
//        mc.getTextureManager().bindTexture( TEXTURE );
//
//        GlStateManager.disableLighting();
//        int u = TEXTURE_U;
//        if( ! list.containsCraftableRecipes() ) {
//            u += SIZE;
//        }
//
//        int v = TEXTURE_V;
//        if( list.getRecipes( book.isFilteringCraftable( container ) ).size() > 1 ) {
//            v += SIZE;
//        }
//
//        boolean animating = animationTime > 0;
//        if( animating ) {
//            float scale = 1 + 0.1F * (float) Math.sin( animationTime / 15 * Math.PI );
//
//            GlStateManager.pushMatrix();
//            GlStateManager.translatef( x + 8, y + 12, 0 );
//            GlStateManager.scalef( scale, scale, 1 );
//            GlStateManager.translatef( - ( x + 8 ), - ( y + 12 ), 0.0F );
//            animationTime -= partialTicks;
//        }
//
//        blit( x, y, u, v, width, height );
//
//        List<IRecipe<?>> recipes = getOrderedRecipes();
//        currentIndex = MathHelper.floor( time / 30 ) % recipes.size();
//
//        ItemStack item = recipes.get( currentIndex ).getRecipeOutput();
//
//        int stackOffset = 4;
//        if( list.hasSingleResultItem() && getOrderedRecipes().size() > 1 ) {
//            mc.getItemRenderer().renderItemAndEffectIntoGUI( item, x + stackOffset + 1, y + stackOffset + 1 );
//            -- stackOffset;
//        }
//
//        mc.getItemRenderer().renderItemAndEffectIntoGUI( item, x + stackOffset, y + stackOffset );
//        if( animating ) {
//            GlStateManager.popMatrix();
//        }
//
//        GlStateManager.enableLighting();
//        RenderHelper.disableStandardItemLighting();
//    }
//
//    private List<IRecipe<?>> getOrderedRecipes() {
//        List<IRecipe<?>> recipes = list.getDisplayRecipes( true );
//        if( ! book.isFilteringCraftable( container ) ) {
//            recipes.addAll( list.getDisplayRecipes( false ) );
//        }
//
//        return recipes;
//    }
//
//    public boolean isOnlyOption() {
//        return getOrderedRecipes().size() == 1;
//    }
//
//    public IRecipe<?> getRecipe() {
//        List<IRecipe<?>> ordered = getOrderedRecipes();
//        return ordered.get( currentIndex );
//    }
//
//    public List<String> getToolTipText( Screen screen ) {
//        ItemStack stack = getOrderedRecipes().get( currentIndex ).getRecipeOutput();
//        List<String> tooltipText = screen.getTooltipFromItem( stack );
//        if( list.getRecipes( book.isFilteringCraftable( container ) ).size() > 1 ) {
//            tooltipText.add( I18n.format( "gui.recipebook.moreRecipes" ) );
//        }
//
//        return tooltipText;
//    }
//
//    @Override
//    public int getWidth() {
//        return SIZE;
//    }
//
//    @Override
//    public int getHeight() {
//        return SIZE;
//    }
//
//    @Override
//    protected boolean isValidClickButton( int btn ) {
//        return btn == GLFW.GLFW_MOUSE_BUTTON_LEFT || btn == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
//    }
//}