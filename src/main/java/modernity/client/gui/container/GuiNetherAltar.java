/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.gui.container;

import com.mojang.blaze3d.platform.GlStateManager;
import modernity.common.container.NetherAltarContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * The Nether Altar GUI
 */
@OnlyIn( Dist.CLIENT )
public class GuiNetherAltar extends ContainerScreen<NetherAltarContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation( "modernity:textures/gui/container/nether_altar.png" );

    public GuiNetherAltar( NetherAltarContainer container, PlayerInventory playerInv, ITextComponent name ) {
        super( container, playerInv, name );
    }

    @Override
    public void render( int mouseX, int mouseY, float partialTicks ) {
        this.renderBackground();
        super.render( mouseX, mouseY, partialTicks );
        this.renderHoveredToolTip( mouseX, mouseY );
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY ) {
        String s = title.getFormattedText();
        this.font.drawString( s, (float) ( this.xSize / 2 - this.font.getStringWidth( s ) / 2 ), 6.0F, 4210752 );
        this.font.drawString( this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) ( this.ySize - 96 + 2 ), 4210752 );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY ) {
        GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
        this.minecraft.getTextureManager().bindTexture( TEXTURE );
        int i = ( this.width - this.xSize ) / 2;
        int j = ( this.height - this.ySize ) / 2;
        blit( i, j, 0, 0, this.xSize, this.ySize );
    }
}