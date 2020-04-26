/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.gui.recipebook;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import modernity.client.util.MDRecipeBookCategories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.recipebook.GhostRecipe;
import net.minecraft.client.gui.recipebook.IRecipeUpdateListener;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipePlacer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.client.CRecipeInfoPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@OnlyIn( Dist.CLIENT )
public class RecipeBookGui extends net.minecraft.client.gui.recipebook.RecipeBookGui implements IRenderable, IGuiEventListener, IRecipeUpdateListener, IRecipePlacer<Ingredient> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation( "textures/gui/recipe_book.png" );

    private static final int SEARCHBAR_X = 25;
    private static final int SEARCHBAR_Y = 14;
    private static final int SEARCHBAR_W = 80;
    private static final int SEARCHBAR_H = 14;
    private static final int TOGGLE_BUTTON_X = 110;
    private static final int TOGGLE_BUTTON_Y = 12;
    private static final int TOGGLE_BUTTON_W = 26;
    private static final int TOGGLE_BUTTON_H = 16;
    private static final int TOGGLE_BUTTON_TEX_X = 152;
    private static final int TOGGLE_BUTTON_TEX_Y = 41;
    private static final int TOGGLE_BUTTON_DIFF_X = 28;
    private static final int TOGGLE_BUTTON_DIFF_Y = 18;

    private static final int WIDTH = 147;
    private static final int HEIGHT = 166;
    private static final int X_OFFSET = 86;

    protected Minecraft mc;
    protected ClientRecipeBook recipeBook;

    private int xOffset;
    private int screenWidth;
    private int screenHeight;

    protected final GhostRecipe ghostRecipe = new GhostRecipe();

    private final List<RecipeTabToggleWidget> recipeTabs = Lists.newArrayList();
    private RecipeTabToggleWidget currentTab;

    protected ToggleWidget toggleRecipesButton;

    private TextFieldWidget searchBar;
    private String lastSearch = "";
    private boolean searching;

    protected RecipeBookContainer<?> container;

    protected final RecipeBookPage page;

    protected final RecipeItemHelper helper;

    private int changeCount;

    private final IRecipeBookType type;

    public RecipeBookGui( IRecipeBookType type ) {
        this.type = type;
        page = new RecipeBookPage( type );

        helper = new RecipeItemHelper() {
            @Override
            public boolean canCraft( IRecipe<?> recipe, @Nullable IntList packedItemList, int maxAmount ) {
                return type.canCraftRecipe( container, recipe ) && super.canCraft( recipe, packedItemList, maxAmount );
            }

            @Override
            public int getBiggestCraftableStack( IRecipe<?> recipe, int maxAmount, @Nullable IntList packedItemList ) {
                if( ! type.canCraftRecipe( container, recipe ) ) return 0;
                return super.getBiggestCraftableStack( recipe, maxAmount, packedItemList );
            }
        };
    }

    @Override
    public void init( int width, int height, Minecraft mc, boolean tooNarrow, RecipeBookContainer<?> container ) {
        this.mc = mc;
        this.screenWidth = width;
        this.screenHeight = height;
        this.container = container;

        mc.player.openContainer = container;
        recipeBook = mc.player.getRecipeBook();
        changeCount = mc.player.inventory.getTimesChanged();
        if( isVisible() ) {
            setupGui( tooNarrow );
        }

        mc.keyboardListener.enableRepeatEvents( true );
    }

    public void setupGui( boolean tooNarrow ) {
        xOffset = tooNarrow ? 0 : X_OFFSET;

        int x = ( screenWidth - WIDTH ) / 2 - xOffset;
        int y = ( screenHeight - HEIGHT ) / 2;

        helper.clear();
        mc.player.inventory.accountStacks( helper );
        container.func_201771_a( helper );


        String searchQuery = searchBar != null ? searchBar.getText() : "";

        searchBar = new TextFieldWidget(
            mc.fontRenderer,
            x + SEARCHBAR_X, y + SEARCHBAR_Y,
            SEARCHBAR_W, SEARCHBAR_H,
            I18n.format( "itemGroup.search" )
        );

        searchBar.setMaxStringLength( 50 );
        searchBar.setEnableBackgroundDrawing( false );
        searchBar.setVisible( true );
        searchBar.setTextColor( 0xffffff );
        searchBar.setText( searchQuery );

        page.init( mc, x, y );
        page.addListener( this );

        toggleRecipesButton = new ToggleWidget(
            x + TOGGLE_BUTTON_X, y + TOGGLE_BUTTON_Y,
            TOGGLE_BUTTON_W, TOGGLE_BUTTON_H,
            recipeBook.isFilteringCraftable( container )
        );

        initTextureValues();

        recipeTabs.clear();
        for( RecipeBookCategories category : container.getRecipeBookCategories() ) {
            recipeTabs.add( new RecipeTabToggleWidget( category ) );
        }

        if( currentTab != null ) {
            currentTab = recipeTabs.stream().filter( tab -> tab.getCategory() == currentTab.getCategory() ).findFirst().orElse( null );
        }

        if( currentTab == null ) {
            currentTab = recipeTabs.get( 0 );
        }

        currentTab.setStateTriggered( true );

        updateCollections( false );
        updateTabs();
    }

    @Override
    public boolean changeFocus( boolean focused ) {
        return false;
    }


    protected void initTextureValues() {
        int[] uv = type.getToggleButtonTextureCoords();
        toggleRecipesButton.initTextureValues(
            uv[ 0 ], uv[ 1 ],
            uv[ 2 ], uv[ 3 ],
            type.getTexture()
        );
    }

    public void onRemove() {
        searchBar = null;
        currentTab = null;
        mc.keyboardListener.enableRepeatEvents( false );
    }

    public int computeMainScreenX( boolean tooNarrow, int screenWidth, int guiWidth ) {
        int xpos;
        if( isVisible() && ! tooNarrow ) {
            xpos = 177 + ( screenWidth - guiWidth - 200 ) / 2;
        } else {
            xpos = ( screenWidth - guiWidth ) / 2;
        }

        return xpos;
    }

    /**
     * Toggles visibility of the recipe book: shows the recipe book when hidden and hides it when visible.
     */
    @Override
    public void toggleVisibility() {
        setVisible( ! isVisible() );
    }

    /**
     * Returns whether the recipe book is visible
     *
     * @return True if visible, false if hidden
     */
    @Override
    public boolean isVisible() {
        return recipeBook.isGuiOpen();
    }

    /**
     * Sets the visibility of the recipe book gui
     *
     * @param visible True if visible, false if hidden
     */
    @Override
    protected void setVisible( boolean visible ) {
        recipeBook.setGuiOpen( visible );
        if( ! visible ) {
            page.hideOverlay();
        }

        sendUpdateSettings();
    }

    @Override
    public void slotClicked( @Nullable Slot slot ) {
        if( slot != null && slot.slotNumber < container.getSize() ) {
            ghostRecipe.clear();
            if( isVisible() ) {
                updateStackedContents();
            }
        }
    }

    private void updateCollections( boolean gotoPage0 ) {
        List<RecipeList> lists = recipeBook.getRecipes( currentTab.getCategory() );

        lists.forEach( list -> list.canCraft( helper, container.getWidth(), container.getHeight(), recipeBook ) );
        List<RecipeList> filtered = Lists.newArrayList( lists );
        filtered.removeIf( list -> ! list.isNotEmpty() );
        filtered.removeIf( list -> ! list.containsValidRecipes() );
        String query = searchBar.getText();
        if( ! query.isEmpty() ) {
            ObjectSet<RecipeList> queryResult = new ObjectLinkedOpenHashSet<>( mc.getSearchTree( SearchTreeManager.RECIPES ).search( query.toLowerCase( Locale.ROOT ) ) );
            filtered.removeIf( list -> ! queryResult.contains( list ) );
        }

        if( recipeBook.isFilteringCraftable( container ) ) {
            filtered.removeIf( list -> ! list.containsCraftableRecipes() );
        }

        page.updateLists( filtered, gotoPage0 );
    }

    private void updateTabs() {
        int x = ( screenWidth - 147 ) / 2 - xOffset - 30;
        int y = ( screenHeight - 166 ) / 2 + 3;
        int tabY = 0;

        for( RecipeTabToggleWidget tab : recipeTabs ) {
            RecipeBookCategories category = tab.getCategory();
            if( category != RecipeBookCategories.SEARCH && category != RecipeBookCategories.FURNACE_SEARCH && category != MDRecipeBookCategories.CLEANER_SEARCH ) {
                if( tab.updateVisibility( recipeBook ) ) {
                    tab.setPosition( x, y + 27 * tabY++ );
                    tab.startAnimation( mc );
                }
            } else {
                tab.visible = true;
                tab.setPosition( x, y + 27 * tabY++ );
            }
        }
    }

    @Override
    public void tick() {
        if( isVisible() ) {
            if( changeCount != mc.player.inventory.getTimesChanged() ) {
                updateStackedContents();
                changeCount = mc.player.inventory.getTimesChanged();
            }

        }
    }

    private void updateStackedContents() {
        helper.clear();
        mc.player.inventory.accountStacks( helper );
        container.func_201771_a( helper );
        updateCollections( false );
    }

    @Override
    public void render( int mouseX, int mouseZ, float partialTicks ) {
        if( this.isVisible() ) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translatef( 0, 0, 100 );
            this.mc.getTextureManager().bindTexture( TEXTURE );
            GlStateManager.color4f( 1, 1, 1, 1 );
            int x = ( screenWidth - 147 ) / 2 - xOffset;
            int y = ( screenHeight - 166 ) / 2;
            blit( x, y, 1, 1, 147, 166 );
            searchBar.render( mouseX, mouseZ, partialTicks );
            RenderHelper.disableStandardItemLighting();

            for( RecipeTabToggleWidget tab : recipeTabs ) {
                tab.render( mouseX, mouseZ, partialTicks );
            }

            toggleRecipesButton.render( mouseX, mouseZ, partialTicks );
            page.render( x, y, mouseX, mouseZ, partialTicks );
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void renderTooltip( int left, int top, int mouseX, int mouseY ) {
        if( isVisible() ) {
            page.renderTooltip( mouseX, mouseY );
            if( toggleRecipesButton.isHovered() ) {
                String s = craftableAllTranslation();
                if( mc.currentScreen != null ) {
                    mc.currentScreen.renderTooltip( s, mouseX, mouseY );
                }
            }

            renderGhostRecipeTooltip( left, top, mouseX, mouseY );
        }
    }

    protected String craftableAllTranslation() {
        return I18n.format(
            toggleRecipesButton.isStateTriggered()
            ? "gui.recipebook.toggleRecipes.craftable"
            : "gui.recipebook.toggleRecipes.all"
        );
    }

    private void renderGhostRecipeTooltip( int left, int top, int mouseX, int mouseZ ) {
        ItemStack stack = null;

        for( int i = 0; i < ghostRecipe.size(); ++ i ) {
            GhostRecipe.GhostIngredient ingredient = ghostRecipe.get( i );
            int x = ingredient.getX() + left;
            int y = ingredient.getY() + top;
            if( mouseX >= x && mouseZ >= y && mouseX < x + 16 && mouseZ < y + 16 ) {
                stack = ingredient.getItem();
            }
        }

        if( stack != null && mc.currentScreen != null ) {
            mc.currentScreen.renderTooltip( mc.currentScreen.getTooltipFromItem( stack ), mouseX, mouseZ );
        }

    }

    @Override
    public void renderGhostRecipe( int xOff, int yOff, boolean largeResultSlot, float partialTicks ) {
        ghostRecipe.render( mc, xOff, yOff, largeResultSlot, partialTicks );
    }

    @Override
    public boolean mouseClicked( double x, double y, int button ) {
        if( isVisible() && ! mc.player.isSpectator() ) {
            if( page.mouseClicked( x, y, button, ( screenWidth - WIDTH ) / 2 - xOffset, ( screenHeight - HEIGHT ) / 2, WIDTH, HEIGHT ) ) {
                IRecipe<?> recipe = page.getLastClickedRecipe();
                RecipeList list = page.getLastClickedRecipeList();
                if( recipe != null && list != null ) {
                    if( ! list.isCraftable( recipe ) && ghostRecipe.getRecipe() == recipe ) {
                        return false;
                    }

                    ghostRecipe.clear();
                    mc.playerController.func_203413_a( mc.player.openContainer.windowId, recipe, Screen.hasShiftDown() );
                    if( ! isOffsetNextToMainGUI() ) {
                        setVisible( false );
                    }
                }

                return true;
            } else if( searchBar.mouseClicked( x, y, button ) ) {
                return true;
            } else if( toggleRecipesButton.mouseClicked( x, y, button ) ) {
                boolean flag = toggleCraftableFilter();
                toggleRecipesButton.setStateTriggered( flag );
                sendUpdateSettings();
                updateCollections( false );
                return true;
            } else {
                for( RecipeTabToggleWidget tabWidget : recipeTabs ) {
                    if( tabWidget.mouseClicked( x, y, button ) ) {
                        if( currentTab != tabWidget ) {
                            currentTab.setStateTriggered( false );
                            currentTab = tabWidget;
                            currentTab.setStateTriggered( true );
                            updateCollections( true );
                        }

                        return true;
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected boolean toggleCraftableFilter() {
        boolean filteringCraftable = ! recipeBook.isFilteringCraftable();
        recipeBook.setFilteringCraftable( filteringCraftable );
        return filteringCraftable;
    }

    public boolean isClickOutside( double x, double y, int left, int top, int width, int height, int button ) {
        if( ! isVisible() ) {
            return true;
        } else {
            boolean outsideMainGui = x < left || y < top || x >= left + width || y >= top + height;
            boolean insideRecipeBook = left - 147 < x && x < left && top < y && y < top + height;
            return outsideMainGui && ! insideRecipeBook && ! currentTab.isHovered();
        }
    }

    @Override
    public boolean keyPressed( int key, int scanCode, int modifiers ) {
        searching = false;
        if( isVisible() && ! mc.player.isSpectator() ) {
            if( key == 256 && ! isOffsetNextToMainGUI() ) {
                setVisible( false );
                return true;
            } else if( searchBar.keyPressed( key, scanCode, modifiers ) ) {
                updateSearch();
                return true;
            } else if( searchBar.isFocused() && searchBar.getVisible() && key != 256 ) {
                return true;
            } else if( mc.gameSettings.keyBindChat.matchesKey( key, scanCode ) && ! searchBar.isFocused() ) {
                searching = true;
                searchBar.setFocused2( true );
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean keyReleased( int key, int scanCode, int modifiers ) {
        searching = false;
        return false;
    }

    @Override
    public boolean charTyped( char typedChar, int modifiers ) {
        if( searching ) {
            return false;
        } else if( isVisible() && ! mc.player.isSpectator() ) {
            if( searchBar.charTyped( typedChar, modifiers ) ) {
                updateSearch();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isMouseOver( double x, double y ) {
        return false;
    }

    private void updateSearch() {
        String query = searchBar.getText().toLowerCase( Locale.ROOT );
        pirateRecipe( query );
        if( ! query.equals( lastSearch ) ) {
            updateCollections( false );
            lastSearch = query;
        }

    }

    private void pirateRecipe( String text ) {
        if( "excitedze".equals( text ) ) {
            LanguageManager languages = mc.getLanguageManager();
            Language lang = languages.getLanguage( "en_pt" );
            if( languages.getCurrentLanguage().compareTo( lang ) == 0 ) {
                return;
            }

            languages.setCurrentLanguage( lang );
            mc.gameSettings.language = lang.getCode();
            ForgeHooksClient.refreshResources( mc, VanillaResourceType.LANGUAGES );
            mc.fontRenderer.setBidiFlag( languages.isCurrentLanguageBidirectional() );
            mc.gameSettings.saveOptions();
        }

    }

    private boolean isOffsetNextToMainGUI() {
        return xOffset == 86;
    }

    @Override
    public void recipesUpdated() {
        updateTabs();
        if( isVisible() ) {
            updateCollections( false );
        }
    }

    @Override
    public void recipesShown( List<IRecipe<?>> recipes ) {
        for( IRecipe<?> recipe : recipes ) {
            mc.player.removeRecipeHighlight( recipe );
        }
    }

    @Override
    public void setupGhostRecipe( IRecipe<?> recipe, List<Slot> slots ) {
        ItemStack output = recipe.getRecipeOutput();
        ghostRecipe.setRecipe( recipe );
        ghostRecipe.addIngredient( Ingredient.fromStacks( output ), slots.get( 0 ).xPos, slots.get( 0 ).yPos );
        placeRecipe( container.getWidth(), container.getHeight(), container.getOutputSlot(), recipe, recipe.getIngredients().iterator(), 0 );
    }

    @Override
    public void setSlotContents( Iterator<Ingredient> ingredients, int slotIndex, int maxAmount, int y, int x ) {
        Ingredient ingr = ingredients.next();
        if( ! ingr.hasNoMatchingItems() ) {
            Slot slot = container.inventorySlots.get( slotIndex );
            ghostRecipe.addIngredient( ingr, slot.xPos, slot.yPos );
        }

    }

    @Override
    protected void sendUpdateSettings() {
        if( mc.getConnection() != null ) {
            mc.getConnection().sendPacket( new CRecipeInfoPacket(
                recipeBook.isGuiOpen(),
                recipeBook.isFilteringCraftable(),
                recipeBook.isFurnaceGuiOpen(),
                recipeBook.isFurnaceFilteringCraftable(),
                recipeBook.func_216758_e(),
                recipeBook.func_216761_f()
            ) );
        }

    }
}