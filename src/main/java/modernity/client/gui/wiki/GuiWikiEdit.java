/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiWikiEdit extends GuiScreen {
    protected static final ResourceLocation ITEM_FRAME = new ResourceLocation( "modernity:textures/gui/wiki/item_frame.png" );

    protected final GuiScreen lastScreen;

    protected final List<GuiWikiTextField> textFields = new ArrayList<>();

    protected final GuiWikiTextField<Integer> scaleField;
    protected final GuiWikiTextField<String> nameField;

    protected boolean exporting;
    protected int rootX;
    protected int rootY;


    /*
     * LAYOUT & SETUP
     */

    protected GuiWikiEdit( GuiScreen lastScreen ) {
        this.lastScreen = lastScreen;
        mc = Minecraft.getInstance();

        textFields.add( scaleField = new GuiWikiTextField<>( 0, mc.fontRenderer, width / 2 + 30, 30, 45, 20, "2", GuiWikiTextField.integer( 1, 5 ) ) );
        textFields.add( nameField = new GuiWikiTextField<>( 0, mc.fontRenderer, width / 2 - 75, 30, 100, 20, "untitled", GuiWikiTextField.any() ) );

        nameField.setMaxStringLength( Integer.MAX_VALUE );
        scaleField.setMaxStringLength( 1 );
    }

    @Override
    protected void initGui() {
        labels.clear();
        updateRoot();

        addButton( new GuiWikiButton( 0, width / 2 - 155, height - 40, 150, 20, WikiTranslations.cancel ).setEventHandler( this::cancel ) );
        addButton( new GuiWikiButton( 0, width / 2 + 5, height - 40, 150, 20, WikiTranslations.export ).setEventHandler( this::export ) );

        labels.add( new GuiWikiLabel( WikiTranslations.scale, 0xffffffff, width / 2 + 25, 20, true ) );
        labels.add( new GuiWikiLabel( WikiTranslations.filename, 0xffffffff, width / 2 - 75, 20, true ) );

        initialize();

        scaleField.x = width / 2 + 30;
        nameField.x = width / 2 - 75;

        int w = mc.mainWindow.getWidth();
        int h = mc.mainWindow.getHeight();
        int maxScaleX = w / getContainerWidth() - 1;
        int maxScaleY = h / getContainerHeight() - 1;
        int maxScale = Math.min( maxScaleX, maxScaleY );

        int len = ( maxScale + "" ).length();
        scaleField.setMaxStringLength( len );
        scaleField.setFormat( GuiWikiTextField.integer( 1, maxScale ) );

        children.addAll( textFields );

        super.initGui();
    }

    protected abstract void initialize();

    protected abstract int getContainerWidth();
    protected abstract int getContainerHeight();



    /*
     * ACTIONS
     */

    @Override
    public void tick() {
        for( GuiTextField tf : textFields ) {
            tf.tick();
        }
    }

    protected void reposition() {
    }

    protected void updateRoot() {
        rootX = width / 2 - getContainerWidth() / 2;
        rootY = height / 2 - getContainerHeight() / 2;
        reposition();
    }

    public void cancel() {
        mc.displayGuiScreen( lastScreen );
    }

    private void export() {
        int scale = scaleField.getValue() == null ? 2 : scaleField.getValue();
        export( scale, new File( "./wikiexports/" ), nameField.getValue() );
    }



    /*
     * RENDERING
     */

    @Override
    public void render( int mouseX, int mouseY, float partialTicks ) {
        if( ! exporting ) {
            drawBackground( 0 );
            super.render( mouseX, mouseY, partialTicks );
            renderContainer( mouseX, mouseY, false );

            for( GuiTextField tf : textFields ) {
                tf.drawTextField( mouseX, mouseY, partialTicks );
            }
        }
    }

    public abstract void renderContainer( int mx, int my, boolean export );

    protected void drawItem( ItemStack stack, int x, int y ) {
        GlStateManager.color4f( 1, 1, 1, 1 );
        RenderHelper.enableGUIStandardItemLighting();
        OpenGlHelper.glMultiTexCoord2f( OpenGlHelper.GL_TEXTURE1, 240.0F, 240.0F );
        GlStateManager.enableDepthTest();
        GlStateManager.enableLighting();
        this.itemRender.renderItemAndEffectIntoGUI( this.mc.player, stack, x, y );
        this.itemRender.renderItemOverlayIntoGUI( this.fontRenderer, stack, x, y, null );
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        RenderHelper.disableStandardItemLighting();
    }

    protected void drawFramedItem( ItemStack stack, int x, int y ) {
        mc.textureManager.bindTexture( ITEM_FRAME );
        drawModalRectWithCustomSizedTexture( x, y, 0, 0, 32, 32, 32, 32 );

        if( stack != null ) {
            drawItem( stack, x + 8, y + 8 );
        }
    }


    /*
     * EXPORTING
     */

    public void setupOverlayRendering() {
        GlStateManager.clear( 256 );
        GlStateManager.matrixMode( 5889 );
        GlStateManager.loadIdentity();
        GlStateManager.ortho( 0.0D, mc.mainWindow.getScaledWidth(), mc.mainWindow.getScaledHeight(), 0.0D, 1000.0D, 3000.0D );
        GlStateManager.matrixMode( 5888 );
        GlStateManager.loadIdentity();
        GlStateManager.translatef( 0.0F, 0.0F, - 2000.0F );
    }

    private void export( int scale, File folder, String filename ) {
        double mul = mc.mainWindow.getGuiScaleFactor();
        rootX = 0;
        rootY = 0;
        exporting = true;

        reposition();

        int containerWidth = getContainerWidth();
        int containerHeight = getContainerHeight();

        GlStateManager.pushMatrix();
        GlStateManager.clearColor( 0, 0, 0, 0 );
        GlStateManager.clear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
        GlStateManager.color4f( 1, 1, 1, 1 );
        setupOverlayRendering();
        GlStateManager.scaled( 1F / mul * scale, 1F / mul * scale, 1F / mul * scale );
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableDepthTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE );
        GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
        GlStateManager.disableAlphaTest();
        GlStateManager.depthMask( false );
        float zdepth = itemRender.zLevel;
        itemRender.zLevel = - 50;
        renderContainer( 0, 0, true );
        itemRender.zLevel = zdepth;
        GlStateManager.popMatrix();
        try {
            BufferedImage img = createFlipped( this.readPixels( containerWidth * scale, containerHeight * scale ) );

            File f = new File( folder, filename + ".png" );
            int i = 2;
            while( f.exists() ) {
                f = new File( folder, filename + "_" + i + ".png" );
                i++;
            }
            Files.createParentDirs( f );
            f.createNewFile();
            ImageIO.write( img, "PNG", f );
        } catch( Exception ex ) {
            ex.printStackTrace();
        }

        exporting = false;

        updateRoot();
    }


    private static BufferedImage createFlipped( BufferedImage image ) {
        AffineTransform at = new AffineTransform();
        at.concatenate( AffineTransform.getScaleInstance( 1, - 1 ) );
        at.concatenate( AffineTransform.getTranslateInstance( 0, - image.getHeight() ) );
        return createTransformed( image, at );
    }

    private static BufferedImage createTransformed( BufferedImage image, AffineTransform at ) {
        BufferedImage newImage = new BufferedImage( image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB );
        Graphics2D g = newImage.createGraphics();
        g.transform( at );
        g.drawImage( image, 0, 0, null );
        g.dispose();
        return newImage;
    }

    public BufferedImage readPixels( int width, int height ) {
        GL11.glReadBuffer( GL11.GL_BACK );
        ByteBuffer buf = BufferUtils.createByteBuffer( width * height * 4 );
        GL11.glReadPixels( 0, Minecraft.getInstance().mainWindow.getHeight() - height, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buf );
        BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        int[] pixels = new int[ width * height ];
        buf.asIntBuffer().get( pixels );
        img.setRGB( 0, 0, width, height, pixels, 0, width );
        return img;
    }
}
