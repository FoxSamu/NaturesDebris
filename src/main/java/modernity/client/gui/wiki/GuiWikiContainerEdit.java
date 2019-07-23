/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiWikiContainerEdit extends GuiWikiEdit {
    private final List<Slot> slots;
    private final int containerWidth;
    private final int containerHeight;
    private final ResourceLocation background;

    private int focusedSlot;

    private final Set<Integer> selectedSlots = new HashSet<>();

    protected final GuiWikiTextField<Item> itemField;
    protected final GuiWikiTextField<Integer> countField;
    protected final GuiWikiTextField<Integer> damageField;

    protected GuiButton effectButton;

    protected ItemStack clipboard;
    protected ItemStack dragging;
    private boolean exporting;
    private Boolean unselect;

    protected GuiWikiContainerEdit( GuiScreen lastScreen, List<SlotLocation> slotLocations, int w, int h, ResourceLocation bg ) {
        super( lastScreen );

        slots = ImmutableList.copyOf( slotLocations.stream().map( Slot::new ).collect( Collectors.toList() ) );
        containerWidth = w;
        containerHeight = h;
        background = bg;

        textFields.add( itemField = new GuiWikiTextField<>( 0, mc.fontRenderer, width / 2 - 155, height - 64, 150, 20, GuiWikiTextField.item() ) );
        textFields.add( countField = new GuiWikiTextField<>( 0, mc.fontRenderer, width / 2 + 5, height - 64, 60, 20, GuiWikiTextField.integer( 0, 999 ) ) );
        textFields.add( damageField = new GuiWikiTextField<>( 0, mc.fontRenderer, width / 2 + 70, height - 64, 85, 20, GuiWikiTextField.integer( 0, 99999 ) ) );

        itemField.setMaxStringLength( Integer.MAX_VALUE );
        countField.setMaxStringLength( 3 );
        damageField.setMaxStringLength( 5 );
    }

    @Override
    protected void initialize() {

        addButton( new GuiWikiButton( 0, 20, 70, 60, 20, WikiTranslations.copy ).setEventHandler( this::copy ) );
        addButton( new GuiWikiButton( 0, 20, 94, 60, 20, WikiTranslations.paste ).setEventHandler( this::paste ) );
        addButton( new GuiWikiButton( 0, 20, 118, 60, 20, WikiTranslations.reset ).setEventHandler( this::reset ) );

        labels.add( new GuiWikiLabel( WikiTranslations.item, 0xffffffff, width / 2 - 155, height - 74, true ) );
        labels.add( new GuiWikiLabel( WikiTranslations.count, 0xffffffff, width / 2 + 5, height - 74, true ) );
        labels.add( new GuiWikiLabel( WikiTranslations.damage, 0xffffffff, width / 2 + 80, height - 74, true ) );

        itemField.x = width / 2 - 155;
        itemField.y = height - 64;

        countField.x = width / 2 + 5;
        countField.y = height - 64;

        damageField.x = width / 2 + 70;
        damageField.y = height - 64;

        updateSelected( focusedSlot );
    }

    @Override
    public int getContainerWidth() {
        return containerWidth;
    }

    @Override
    public int getContainerHeight() {
        return containerHeight;
    }

    @Override
    protected void reposition() {
        for( Slot slot : slots ) {
            slot.recomputePos( rootX, rootY );
        }
    }

    @Override
    public void render( int mouseX, int mouseY, float partialTicks ) {
        super.render( mouseX, mouseY, partialTicks );

        if( ! exporting ) {
            fontRenderer.drawString( I18n.format( WikiTranslations.clipboard ), 20, 20, 0xffffffff );
            drawFramedItem( clipboard, 20, 30 );
        }
    }

    public void renderContainer( int mx, int my, boolean export ) {
        GlStateManager.depthMask( false );
        drawContainerBackground();
        GlStateManager.depthMask( true );
        int idx = 0;
        for( Slot slot : slots ) {
            drawSlot( slot, mx, my, idx, export );
            idx++;
        }
    }

    protected void drawContainerBackground() {
        if( background != null ) {
            mc.textureManager.bindTexture( background );
            drawModalRectWithCustomSizedTexture( rootX, rootY, 0, 0, containerWidth, containerHeight, containerWidth, containerHeight );
        }
    }

    protected void drawSlot( Slot slot, int mx, int my, int index, boolean export ) {
        if( selectedSlots.contains( index ) && ! export ) {
            drawRect( slot.x, slot.y, slot.x + 16, slot.y + 16, 0x64ff0000 );
        }

        if( index == focusedSlot && ! export ) {
            drawRect( slot.x, slot.y, slot.x + 16, slot.y + 16, 0x6400ff00 );
        }

        if( slot.isMouseHovering( mx, my ) && ! export ) {
            drawRect( slot.x, slot.y, slot.x + 16, slot.y + 16, 0x80ffffff );
        }
        drawItem( slot.stack, slot.x, slot.y );
    }

    private ItemStack computeItemFromInput() {
        Item item = itemField.getValue();
        int count = countField.getValue() == null ? 1 : countField.getValue();
        int damage = damageField.getValue() == null ? 0 : damageField.getValue();
        ItemStack stack = new ItemStack( item, count );
        if( stack.isDamageable() ) {
            stack.setDamage( Math.min( damage, stack.getMaxDamage() ) );
        }
        return stack;
    }

    @Override
    public boolean mouseClicked( double x, double y, int button ) {
        if( super.mouseClicked( x, y, button ) ) {
            return true;
        } else {
            if( button == 0 ) {
                int selected = - 1;
                int idx = 0;
                for( Slot slot : slots ) {
                    if( slot.isMouseHovering( x, y ) ) {
                        selected = idx;
                    }
                    idx++;
                }

                if( selected != - 1 ) {
                    updateSelected( selected );
                    dragging = slots.get( selected ).stack;
                    return true;
                }
            }
            if( button == 1 ) {
                int selected = - 1;
                int idx = 0;
                for( Slot slot : slots ) {
                    if( slot.isMouseHovering( x, y ) ) {
                        selected = idx;
                    }
                    idx++;
                }

                if( selected != - 1 ) {
                    if( selectedSlots.contains( selected ) ) {
                        selectedSlots.remove( selected );
                        unselect = true;
                    } else {
                        selectedSlots.add( selected );
                        unselect = false;
                    }
                    return true;
                } else {
                    unselect = null;
                    selectedSlots.clear();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged( double x, double y, int button, double dx, double dy ) {
        if( super.mouseDragged( x, y, button, dx, dy ) ) {
            return true;
        } else {
            if( button == 0 && dragging != null ) {
                int selected = - 1;
                int idx = 0;
                for( Slot slot : slots ) {
                    if( slot.isMouseHovering( x, y ) ) {
                        selected = idx;
                    }
                    idx++;
                }

                if( selected != - 1 ) {
                    setItem( selected, dragging );
                }
            }
            if( button == 1 && unselect != null ) {
                int selected = - 1;
                int idx = 0;
                for( Slot slot : slots ) {
                    if( slot.isMouseHovering( x, y ) ) {
                        selected = idx;
                    }
                    idx++;
                }

                if( selected != - 1 ) {
                    if( unselect ) {
                        selectedSlots.remove( selected );
                    } else {
                        selectedSlots.add( selected );
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        slots.get( focusedSlot ).stack = computeItemFromInput();
    }

    protected void updateSelected( int index ) {
        focusedSlot = index;
        Slot slot = slots.get( index );
        ItemStack stack = slot.stack;
        itemField.setText( stack.getItem().getRegistryName() + "" );
        countField.setText( stack.getCount() + "" );
        damageField.setText( stack.isDamageable() ? stack.getDamage() + "" : "0" );
    }

    protected void setItem( int index, ItemStack stack ) {
        if( index < 0 || index >= slots.size() ) return;
        slots.get( index ).setStack( stack );
        if( index == focusedSlot ) {
            updateSelected( focusedSlot );
        }
    }

    protected ItemStack getItem( int index ) {
        if( index < 0 || index >= slots.size() ) return ItemStack.EMPTY;
        return slots.get( index ).stack;
    }

    public void reset() {
        selectedSlots.clear();
        for( int i = 0; i < slots.size(); i++ ) {
            setItem( i, ItemStack.EMPTY );
        }
    }

    private void copy() {
        clipboard = getItem( focusedSlot );
    }

    private void paste() {
        if( selectedSlots.isEmpty() ) {
            setItem( focusedSlot, clipboard );
        }
        for( Integer i : selectedSlots ) {
            setItem( i, clipboard );
        }
    }

    private void delete() {
        for( Integer i : selectedSlots ) {
            setItem( i, ItemStack.EMPTY );
        }
    }

    @Override
    public boolean keyPressed( int keyCode, int scanCode, int modifiers ) {
        if( super.keyPressed( keyCode, scanCode, modifiers ) ) {
            return true;
        } else {
            if( keyCode == GLFW.GLFW_KEY_BACKSPACE ) {
                delete();
            }
        }
        return false;
    }

    private void export() {
        int scale = scaleField.getValue() == null ? 2 : scaleField.getValue();
        export( scale, new File( "./wikiexports/" ), nameField.getValue() );
    }


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

        for( Slot slot : slots ) {
            slot.recomputePos( rootX, rootY );
        }

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

        // Reinit gui
        setWorldAndResolution( mc, mc.mainWindow.getScaledWidth(), mc.mainWindow.getScaledHeight() );
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

    public static Builder builder( GuiScreen lastScreen ) {
        return new Builder( lastScreen );
    }

    protected static class SlotLocation {
        private final int x;
        private final int y;

        protected SlotLocation( int x, int y ) {
            this.x = x;
            this.y = y;
        }
    }

    protected static class Slot {
        private final SlotLocation location;
        private int x;
        private int y;

        private ItemStack stack = ItemStack.EMPTY;

        Slot( SlotLocation location ) {
            this.location = location;
        }

        void recomputePos( int rootX, int rootY ) {
            this.x = location.x + rootX;
            this.y = location.y + rootY;
        }

        void setStack( ItemStack stack ) {
            this.stack = stack == null ? ItemStack.EMPTY : stack;
        }

        boolean isMouseHovering( double mx, double my ) {
            if( mx >= x && mx <= x + 16 ) {
                return my >= y && my <= y + 16;
            }
            return false;
        }
    }

    public static class Builder {

        private final List<SlotLocation> slots = new ArrayList<>();

        private final GuiScreen lastScreen;
        private int width;
        private int height;
        private ResourceLocation background;

        private Builder( GuiScreen lastScreen ) {
            this.lastScreen = lastScreen;
        }

        public Builder size( int width, int height ) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder background( ResourceLocation background ) {
            this.background = background;
            return this;
        }

        public Builder background( String bg ) {
            return background( new ResourceLocation( bg ) );
        }

        public Builder addSlot( int x, int y ) {
            slots.add( new SlotLocation( x, y ) );
            return this;
        }

        public GuiWikiContainerEdit build() {
            return new GuiWikiContainerEdit( lastScreen, slots, width, height, background );
        }
    }
}
