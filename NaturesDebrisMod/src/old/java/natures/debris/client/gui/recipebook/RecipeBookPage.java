/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.gui.recipebook;

// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class RecipeBookPage {
//    private static final int LEAST_BUTTON_X = 11;
//    private static final int LEAST_BUTTON_Y = 31;
//
//    private Minecraft mc;
//    private RecipeBook recipeBook;
//
//
//    private final List<RecipeWidget> buttons = Lists.newArrayListWithCapacity( 20 );
//    private RecipeWidget hoveredButton;
//
//    private final RecipeOverlayGui overlay;
//    private final List<IRecipeUpdateListener> listeners = Lists.newArrayList();
//
//    private ToggleWidget forwardButton;
//    private ToggleWidget backButton;
//
//    private int totalPages;
//    private int currentPage;
//
//    private List<RecipeList> recipeLists;
//
//    private IRecipe<?> lastClickedRecipe;
//    private RecipeList lastClickedRecipeList;
//
//    private final IRecipeBookType type;
//
//    public RecipeBookPage( IRecipeBookType type ) {
//        this.type = type;
//        for( int i = 0; i < 20; ++ i ) {
//            buttons.add( new RecipeWidget() );
//        }
//
//        overlay = new RecipeOverlayGui( type );
//    }
//
//    private static final int FORWARD_BUTTON_X = 93;
//    private static final int BACKWARD_BUTTON_X = 38;
//    private static final int NAV_BUTTON_Y = 137;
//    private static final int NAV_BUTTON_WIDTH = 12;
//    private static final int NAV_BUTTON_HEIGHT = 17;
//    private static final int NAV_BUTTON_TEX_X = 1;
//    private static final int NAV_BUTTON_TEX_Y = 208;
//    private static final int NAV_BUTTON_TEX_OFF_X = 13;
//    private static final int NAV_BUTTON_TEX_OFF_Y = 18;
//
//    public void init( Minecraft mc, int guiX, int guiY ) {
//        this.mc = mc;
//
//        recipeBook = mc.player.getRecipeBook();
//
//        for( int i = 0; i < buttons.size(); ++ i ) {
//            buttons.get( i ).setPosition(
//                guiX + LEAST_BUTTON_X + RecipeWidget.SIZE * ( i % 5 ),
//                guiY + LEAST_BUTTON_Y + RecipeWidget.SIZE * ( i / 5 )
//            );
//        }
//
//        forwardButton = new ToggleWidget(
//            guiX + FORWARD_BUTTON_X, guiY + NAV_BUTTON_Y,
//            NAV_BUTTON_WIDTH, NAV_BUTTON_HEIGHT,
//            false
//        );
//        forwardButton.initTextureValues(
//            NAV_BUTTON_TEX_X, NAV_BUTTON_TEX_Y,
//            NAV_BUTTON_TEX_OFF_X, NAV_BUTTON_TEX_OFF_Y,
//            RecipeBookGui.TEXTURE
//        );
//
//        backButton = new ToggleWidget(
//            guiX + BACKWARD_BUTTON_X, guiY + NAV_BUTTON_Y,
//            NAV_BUTTON_WIDTH, NAV_BUTTON_HEIGHT,
//            true
//        );
//        backButton.initTextureValues(
//            NAV_BUTTON_TEX_X, NAV_BUTTON_TEX_Y,
//            NAV_BUTTON_TEX_OFF_X, NAV_BUTTON_TEX_OFF_Y,
//            RecipeBookGui.TEXTURE
//        );
//    }
//
//    public void addListener( RecipeBookGui bookGui ) {
//        listeners.remove( bookGui );
//        listeners.add( bookGui );
//    }
//
//    public void updateLists( List<RecipeList> list, boolean resetPage ) {
//        recipeLists = list;
//        totalPages = (int) Math.ceil( list.size() / 20D );
//        if( totalPages <= currentPage || resetPage ) {
//            currentPage = 0;
//        }
//
//        updateButtonsForPage();
//    }
//
//    private void updateButtonsForPage() {
//        int buttonIndexOffset = 20 * currentPage;
//
//        for( int i = 0; i < buttons.size(); ++ i ) {
//            RecipeWidget button = buttons.get( i );
//
//            if( buttonIndexOffset + i < recipeLists.size() ) {
//                RecipeList list = recipeLists.get( buttonIndexOffset + i );
//                button.update( list, this );
//                button.visible = true;
//            } else {
//                button.visible = false;
//            }
//        }
//
//        updateArrowButtons();
//    }
//
//    private void updateArrowButtons() {
//        forwardButton.visible = totalPages > 1 && currentPage < totalPages - 1;
//        backButton.visible = totalPages > 1 && currentPage > 0;
//    }
//
//    private static final int PAGE_NUMBER_X = 73;
//    private static final int PAGE_NUMBER_Y = 141;
//
//    public void render( int x, int y, int mouseX, int mouseY, float partialTicks ) {
//        if( totalPages > 1 ) {
//            String pageNumber = currentPage + 1 + "/" + totalPages;
//            int width = mc.fontRenderer.getStringWidth( pageNumber );
//
//            mc.fontRenderer.drawString( pageNumber, x - width / 2F + PAGE_NUMBER_X, y + PAGE_NUMBER_Y, 0xffffffff );
//        }
//
//        RenderHelper.disableStandardItemLighting();
//        hoveredButton = null;
//
//        for( RecipeWidget button : buttons ) {
//            button.render( mouseX, mouseY, partialTicks );
//            if( button.visible && button.isHovered() ) {
//                hoveredButton = button;
//            }
//        }
//
//        backButton.render( mouseX, mouseY, partialTicks );
//        forwardButton.render( mouseX, mouseY, partialTicks );
//        overlay.render( mouseX, mouseY, partialTicks );
//    }
//
//    public void renderTooltip( int mouseX, int mouseY ) {
//        if( mc.currentScreen != null && hoveredButton != null && ! overlay.isVisible() ) {
//            mc.currentScreen.renderTooltip( hoveredButton.getToolTipText( mc.currentScreen ), mouseX, mouseY );
//        }
//    }
//
//    @Nullable
//    public IRecipe<?> getLastClickedRecipe() {
//        return lastClickedRecipe;
//    }
//
//    @Nullable
//    public RecipeList getLastClickedRecipeList() {
//        return lastClickedRecipeList;
//    }
//
//    public void hideOverlay() {
//        overlay.setVisible( false );
//    }
//
//    public boolean mouseClicked( double x, double y, int button, int guiX, int guiY, int width, int height ) {
//        lastClickedRecipe = null;
//        lastClickedRecipeList = null;
//        if( overlay.isVisible() ) {
//            if( overlay.mouseClicked( x, y, button ) ) {
//                lastClickedRecipe = overlay.getLastRecipeClicked();
//                lastClickedRecipeList = overlay.getRecipeList();
//            } else {
//                overlay.setVisible( false );
//            }
//
//            return true;
//        } else if( forwardButton.mouseClicked( x, y, button ) ) {
//            ++ currentPage;
//            updateButtonsForPage();
//            return true;
//        } else if( backButton.mouseClicked( x, y, button ) ) {
//            -- currentPage;
//            updateButtonsForPage();
//            return true;
//        } else {
//            for( RecipeWidget recipeButton : this.buttons ) {
//                if( recipeButton.mouseClicked( x, y, button ) ) {
//                    if( button == 0 ) {
//                        lastClickedRecipe = recipeButton.getRecipe();
//                        lastClickedRecipeList = recipeButton.getList();
//                    } else if( button == 1 && ! overlay.isVisible() && ! recipeButton.isOnlyOption() ) {
//                        overlay.show( mc, recipeButton.getList(), recipeButton.x, recipeButton.y, guiX + width / 2, guiY + 13 + height / 2, recipeButton.getWidth() );
//                    }
//
//                    return true;
//                }
//            }
//
//            return false;
//        }
//    }
//
//    public void onRecipesShown( List<IRecipe<?>> recipes ) {
//        for( IRecipeUpdateListener listener : listeners ) {
//            listener.recipesShown( recipes );
//        }
//    }
//
//    public Minecraft minecraft() {
//        return mc;
//    }
//
//    public RecipeBook getBook() {
//        return recipeBook;
//    }
//}
