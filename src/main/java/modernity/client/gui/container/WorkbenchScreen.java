/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.gui.container;

import com.mojang.blaze3d.platform.GlStateManager;
import modernity.client.gui.recipebook.RecipeBookGui;
import modernity.client.gui.recipebook.WorkbenchBookType;
import modernity.common.container.WorkbenchContainer;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class WorkbenchScreen extends ContainerScreen<WorkbenchContainer> implements IRecipeShownListener {
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation( "textures/gui/container/crafting_table.png" );
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation( "textures/gui/recipe_button.png" );
    private final RecipeBookGui recipeBookGui = new RecipeBookGui( new WorkbenchBookType() );
    private boolean tooNarrow;

    public WorkbenchScreen( WorkbenchContainer container, PlayerInventory playerInv, ITextComponent title ) {
        super( container, playerInv, title );
    }

    @Override
    protected void init() {
        super.init();
        tooNarrow = width < 379;

        assert minecraft != null;

        recipeBookGui.init( width, height, minecraft, tooNarrow, container );
        guiLeft = recipeBookGui.computeMainScreenX( tooNarrow, width, xSize );
        children.add( recipeBookGui );
        setFocused( recipeBookGui );
        addButton( new ImageButton( guiLeft + 5, height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, btn -> {
            recipeBookGui.setupGui( tooNarrow );
            recipeBookGui.toggleVisibility();
            guiLeft = recipeBookGui.computeMainScreenX( tooNarrow, width, xSize );
            ( (ImageButton) btn ).setPosition( guiLeft + 5, height / 2 - 49 );
        } ) );
    }

    @Override
    public void tick() {
        super.tick();
        recipeBookGui.tick();
    }

    @Override
    public void render( int mx, int my, float partialTicks ) {
        this.renderBackground();
        if( recipeBookGui.isVisible() && tooNarrow ) {
            drawGuiContainerBackgroundLayer( partialTicks, mx, my );
            recipeBookGui.render( mx, my, partialTicks );
        } else {
            recipeBookGui.render( mx, my, partialTicks );
            super.render( mx, my, partialTicks );
            recipeBookGui.renderGhostRecipe( guiLeft, guiTop, true, partialTicks );
        }

        renderHoveredToolTip( mx, my );
        recipeBookGui.renderTooltip( guiLeft, guiTop, mx, my );
        func_212932_b( recipeBookGui );
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY ) {
        font.drawString( title.getFormattedText(), 28, 6, 0x404040 );
        font.drawString( playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96 + 2, 0x404040 );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY ) {
        GlStateManager.color4f( 1, 1, 1, 1 );
        minecraft.getTextureManager().bindTexture( CRAFTING_TABLE_GUI_TEXTURES );
        int x = guiLeft;
        int y = ( height - ySize ) / 2;
        this.blit( x, y, 0, 0, xSize, ySize );
    }

    @Override
    protected boolean isPointInRegion( int minX, int minY, int maxX, int maxY, double x, double y ) {
        return ( ! tooNarrow || ! recipeBookGui.isVisible() ) && super.isPointInRegion( minX, minY, maxX, maxY, x, y );
    }

    @Override
    public boolean mouseClicked( double x, double y, int button ) {
        if( recipeBookGui.mouseClicked( x, y, button ) ) {
            return true;
        } else {
            return tooNarrow && recipeBookGui.isVisible() || super.mouseClicked( x, y, button );
        }
    }

    @Override
    protected boolean hasClickedOutside( double x, double y, int guiX, int guiZ, int button ) {
        boolean outside = x < (double) guiX || y < (double) guiZ || x >= (double) ( guiX + this.xSize ) || y >= (double) ( guiZ + this.ySize );
        return recipeBookGui.isClickOutside( x, y, this.guiLeft, this.guiTop, this.xSize, this.ySize, button ) && outside;
    }

    @Override
    protected void handleMouseClick( Slot slot, int slotID, int mouseButton, ClickType type ) {
        super.handleMouseClick( slot, slotID, mouseButton, type );
        recipeBookGui.slotClicked( slot );
    }

    @Override
    public void recipesUpdated() {
        recipeBookGui.recipesUpdated();
    }

    @Override
    public void removed() {
        recipeBookGui.onRemove();
        super.removed();
    }

    @Override
    public RecipeBookGui getRecipeGui() {
        return recipeBookGui;
    }
}