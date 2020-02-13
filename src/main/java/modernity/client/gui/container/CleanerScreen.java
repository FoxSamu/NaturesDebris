/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 13 - 2020
 * Author: rgsw
 */

package modernity.client.gui.container;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.api.util.ColorUtil;
import modernity.client.gui.recipebook.CleanerBookGui;
import modernity.client.gui.recipebook.CleanerBookType;
import modernity.client.gui.recipebook.RecipeBookGui;
import modernity.common.container.CleanerContainer;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;

@OnlyIn( Dist.CLIENT )
public class CleanerScreen extends ContainerScreen<CleanerContainer> implements IRecipeShownListener {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation( "modernity:textures/gui/container/cleaner.png" );
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation( "textures/gui/recipe_button.png" );

    private static final HashMap<Fluid, TextureAtlasSprite> TEXTURE_CACHE = new HashMap<>();
    private static final HashMap<Fluid, Integer> COLOR_CACHE = new HashMap<>();

    private final RecipeBookGui recipeBookGui = new CleanerBookGui( new CleanerBookType() );
    private boolean tooNarrow;

    public CleanerScreen( CleanerContainer container, PlayerInventory playerInv, ITextComponent title ) {
        super( container, playerInv, title );
    }

    private ImageButton drainButton;
    private List<String> tooltip;

    @Override
    protected void init() {
        super.init();

        String drainTranslation = I18n.format( "container.modernity.cleaner.drain" );
        String drainDescTranslation = I18n.format( "container.modernity.cleaner.drain.desc" );
        tooltip = ImmutableList.of( drainTranslation, TextFormatting.GRAY + drainDescTranslation + TextFormatting.RESET );

        tooNarrow = width < 379;

        assert minecraft != null;

        recipeBookGui.init( width, height, minecraft, tooNarrow, container );
        guiLeft = recipeBookGui.computeMainScreenX( tooNarrow, width, xSize );
        children.add( recipeBookGui );
        func_212928_a( recipeBookGui );
        addButton( new ImageButton( guiLeft + 5, height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, btn -> {
            recipeBookGui.setupGui( tooNarrow );
            recipeBookGui.toggleVisibility();
            guiLeft = recipeBookGui.computeMainScreenX( tooNarrow, width, xSize );
            ( (ImageButton) btn ).setPosition( guiLeft + 5, height / 2 - 49 );
            drainButton.setPosition( 28 + guiLeft, 54 + guiTop );
        } ) );

        addButton( drainButton = new ImageButton( 28 + guiLeft, 54 + guiTop, 16, 16, 184, 31, 16, GUI_TEXTURE, btn -> drain() ) {
            @Override
            public void render( int mx, int my, float pt ) {
                if( canDrain() ) super.render( mx, my, pt );
            }
        } );
    }

    @Override
    public void tick() {
        super.tick();
        recipeBookGui.tick();
    }

    private void drain() {
        if( ! canDrain() ) return;
        assert minecraft != null;
    }

    private boolean canDrain() {
        return container.getFluidToRender() != null;
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

        if( drainButton != null && drainButton.isMouseOver( mx, my ) && ! ( recipeBookGui.isVisible() && tooNarrow ) ) {
            renderTooltip( tooltip, mx, my );
        } else {
            renderHoveredToolTip( mx, my );
        }
        recipeBookGui.renderTooltip( guiLeft, guiTop, mx, my );
        func_212932_b( recipeBookGui );
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
    protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY ) {
        font.drawString( title.getFormattedText(), 28, 6, 0x404040 );
        font.drawString( playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96 + 2, 0x404040 );

        assert minecraft != null;

        minecraft.getTextureManager().bindTexture( AtlasTexture.LOCATION_BLOCKS_TEXTURE );

        int fluidAmount = container.getFluidAmountScaled();
        Fluid fluid = container.getFluidToRender();

        if( fluid != null && fluidAmount > 0 ) {
            GlStateManager.enableBlend();

            TextureAtlasSprite sprite;
            if( TEXTURE_CACHE.containsKey( fluid ) ) {
                sprite = TEXTURE_CACHE.get( fluid );
            } else {
                ResourceLocation loc;
                if( fluid instanceof ICustomRenderFluid ) {
                    loc = ( (ICustomRenderFluid) fluid ).getStill();
                } else {
                    loc = fluid.getAttributes().getStillTexture();
                }
                sprite = minecraft.getTextureMap().getSprite( loc );
                TEXTURE_CACHE.put( fluid, sprite );
            }

            int color;
            if( COLOR_CACHE.containsKey( fluid ) ) {
                color = COLOR_CACHE.get( fluid );
            } else {
                if( fluid instanceof ICustomRenderFluid ) {
                    color = ( (ICustomRenderFluid) fluid ).getDefaultColor();
                } else {
                    color = fluid.getAttributes().getColor();
                }
                COLOR_CACHE.put( fluid, color );
            }

            for( int i = 0; i < fluidAmount; ) {
                int h = Math.min( fluidAmount - i, 16 );

                int y = i + h;
                int v = 16 - h;

                blitSprite( 47, 69 - y, 4, v, 8, h, sprite, color );

                i += h;
            }
        }

        minecraft.getTextureManager().bindTexture( GUI_TEXTURE );
        blit( 47, 17, 176, 31, 8, 52 );

        if( ! canDrain() ) {
            blit( 28, 54, 184, 63, 16, 16 );
        }

        int burnTime = container.getBurnProgressionScaled() + 1;
        blit( 62, 50 - burnTime, 176, 15 - burnTime, 14, burnTime );

        int cookProg = container.getCookProgressionScaled();
        blit( 81, 34, 176, 14, cookProg + 1, 16 );
    }

    protected static void blitColored( int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, float r, float g, float b ) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        buff.begin( 7, DefaultVertexFormats.POSITION_TEX_COLOR );
        buff.pos( x0, y1, z ).tex( u0, v1 ).color( r, g, b, 1 ).endVertex();
        buff.pos( x1, y1, z ).tex( u1, v1 ).color( r, g, b, 1 ).endVertex();
        buff.pos( x1, y0, z ).tex( u1, v0 ).color( r, g, b, 1 ).endVertex();
        buff.pos( x0, y0, z ).tex( u0, v0 ).color( r, g, b, 1 ).endVertex();
        tess.draw();
    }

    protected void blitSprite( int x, int y, int u, int v, int w, int h, TextureAtlasSprite sprite, int color ) {
        int u1 = u + w;
        int v1 = v + h;
        blitColored(
            x, x + w, y, y + h, blitOffset,
            sprite.getInterpolatedU( u ), sprite.getInterpolatedU( u1 ),
            sprite.getInterpolatedV( v ), sprite.getInterpolatedV( v1 ),
            ColorUtil.redf( color ), ColorUtil.greenf( color ), ColorUtil.bluef( color )
        );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY ) {
        GlStateManager.color4f( 1, 1, 1, 1 );
        assert minecraft != null;
        minecraft.getTextureManager().bindTexture( GUI_TEXTURE );
        int x = guiLeft;
        int y = ( height - ySize ) / 2;

        blit( x, y, 0, 0, xSize, ySize );
    }

    @Override
    public void recipesUpdated() {
        recipeBookGui.recipesUpdated();
    }

    @Override
    public RecipeBookGui func_194310_f() {
        return recipeBookGui;
    }
}