/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import modernity.common.container.NetherAltarContainer;

public class GuiNetherAltar extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation( "modernity:textures/gui/container/nether_altar.png" );
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInv;
    /** The inventory contained within the corresponding Dispenser. */
    public IInventory altarInv;

    public GuiNetherAltar( InventoryPlayer playerInv, IInventory altarInv ) {
        super( new NetherAltarContainer( playerInv, altarInv ) );
        this.playerInv = playerInv;
        this.altarInv = altarInv;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void render( int mouseX, int mouseY, float partialTicks ) {
        this.drawDefaultBackground();
        super.render( mouseX, mouseY, partialTicks );
        this.renderHoveredToolTip( mouseX, mouseY );
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY ) {
        String s = this.altarInv.getDisplayName().getFormattedText();
        this.fontRenderer.drawString( s, (float) ( this.xSize / 2 - this.fontRenderer.getStringWidth( s ) / 2 ), 6.0F, 4210752 );
        this.fontRenderer.drawString( this.playerInv.getDisplayName().getFormattedText(), 8.0F, (float) ( this.ySize - 96 + 2 ), 4210752 );
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY ) {
        GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
        this.mc.getTextureManager().bindTexture( TEXTURE );
        int i = ( this.width - this.xSize ) / 2;
        int j = ( this.height - this.ySize ) / 2;
        this.drawTexturedModalRect( i, j, 0, 0, this.xSize, this.ySize );
    }
}