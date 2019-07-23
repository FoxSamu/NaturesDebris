/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import modernity.client.gui.wiki.fakeworld.FakePlayer;
import modernity.client.gui.wiki.fakeworld.FakeWorld;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class GuiWikiEntityEdit extends GuiWikiEdit {

    private final GuiWikiTextField<WikiEntityType> entityInput;
    private final GuiWikiTextField<Integer> xMarginInput;
    private final GuiWikiTextField<Integer> yMarginInput;

    private RotateMode rotateMode = RotateMode.TOP_3D;
    private boolean enableFluids = true;
    private int containerWidth = 1;
    private int containerHeight = 1;
    private Entity entity;
    private Render renderer;
    private WikiEntityType lastType;

    private final FakeWorld worldInstance = new FakeWorld();
    private final FakePlayer fakePlayer = new FakePlayer( worldInstance );

    public GuiWikiEntityEdit( GuiScreen lastScreen ) {
        super( lastScreen );

        WikiEntityInputParser highlighter = new WikiEntityInputParser();
        textFields.add( entityInput = new GuiWikiTextField<>( 0, mc.fontRenderer, width / 2 - 155, height - 64, 310, 20, highlighter ) );
        textFields.add( xMarginInput = new GuiWikiTextField<>( 0, mc.fontRenderer, 20, 108, 49, 20, "10", GuiWikiTextField.integer( 0, 40 ) ) );
        textFields.add( yMarginInput = new GuiWikiTextField<>( 0, mc.fontRenderer, 70, 108, 49, 20, "10", GuiWikiTextField.integer( 0, 40 ) ) );

        entityInput.setTextFormatter( highlighter );
        entityInput.setMaxStringLength( Integer.MAX_VALUE );
        xMarginInput.setMaxStringLength( 2 );
        yMarginInput.setMaxStringLength( 2 );
    }

    @Override
    protected void initialize() {
        labels.add( new GuiWikiLabel( WikiTranslations.entity, 0xffffffff, width / 2 - 155, height - 74, true ) );
        labels.add( new GuiWikiLabel( WikiTranslations.margin, 0xffffffff, 20, 99, true ) );
        addButton( new GuiWikiButton( 0, 20, 50, 100, 20, rotateMode.name ).setEventHandler(
                b -> b.setText( ( rotateMode = rotateMode.next() ).name )
        ) );
        addButton( new GuiWikiButton( 0, 20, 74, 100, 20, WikiTranslations.fluidEnabled + "." + enableFluids ).setEventHandler(
                b -> b.setText( WikiTranslations.fluidEnabled + "." + ( enableFluids = ! enableFluids ) )
        ) );

        entityInput.x = width / 2 - 155;
        entityInput.y = height - 64;
    }

    @Override
    public void tick() {
        for( GuiTextField tf : textFields ) {
            tf.tick();
        }

        if( entityInput.getValue() != lastType ) {
            lastType = entityInput.getValue();
            updateEntity();
        }

        if( entity != null ) {
            containerWidth = (int) ( entity.width * 16 ) + xMarginInput.getValue() * 2;
            containerHeight = (int) ( entity.height * 16 ) + yMarginInput.getValue() * 2;
        } else {
            containerWidth = 1;
            containerHeight = 1;
        }

        updateRoot();
    }

    private void updateEntity() {
        if( lastType == null ) {
            return;
        }
        EntityType type = lastType.type;
        NBTTagCompound nbt = lastType.entityNBT;


        Entity e;
        try {
            e = type.create( worldInstance );
        } catch( Exception exc ) {
            e = null;
        }
        if( e != null ) {
            if( nbt != null ) {
                e.read( nbt );
            }
            entity = e;
            renderer = Minecraft.getInstance().getRenderManager().getEntityRenderObject( e );
        } else {
            entity = null;
            renderer = null;
        }
    }

    @Override
    protected int getContainerWidth() {
        return containerWidth;
    }

    @Override
    protected int getContainerHeight() {
        return containerHeight;
    }

    @Override
    public void renderContainer( int mx, int my, boolean export ) {
        if( ! export ) {
            drawRect( rootX, rootY, rootX + containerWidth, rootY + containerHeight, 0xffffffff );
        }
        if( entity != null && renderer != null ) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef( this.rootX + containerWidth / 2, this.rootY + containerHeight / 2, this.zLevel );
            float s = this.rotateMode.scale * 16;
            GlStateManager.scalef( s, - s, s );
            GlStateManager.pushMatrix();
            this.rotateMode.rotate( 0 );
            this.drawEntity( 0, 0 );
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }

    public void drawEntity( int x, int y ) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translatef( 0, - entity.height / 2, 0 );
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableDepthTest();
        GlStateManager.depthMask( true );
        GlStateManager.enableCull();
        GlStateManager.blendFuncSeparate( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );

        this.mc.getTextureManager().bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
        this.mc.getTextureManager().getTexture( TextureMap.LOCATION_BLOCKS_TEXTURE ).setBlurMipmap( false, false );

        EntityPlayerSP realPlayer = mc.player;
        this.mc.player = fakePlayer;
        renderer.doRender( entity, 0, 0, 0, 0, 0 );
        this.mc.player = realPlayer;

        this.mc.getTextureManager().getTexture( TextureMap.LOCATION_BLOCKS_TEXTURE ).restoreLastBlurMipmap();

        GlStateManager.disableLighting();
        GlStateManager.disableColorMaterial();
        GlStateManager.disableDepthTest();
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.alphaFunc( 516, 0.01F );
        GlStateManager.popMatrix();
    }

    protected enum RotateMode {
        TOP_3D( 0.625F, 30, 45, "top_3d" ),
        TOP_3D_SIDE( 0.625F, 30, 0, "top_3d_side" ),
        BOTTOM_3D( 0.625F, - 30, 45, "bottom_3d" ),
        BOTTOM_3D_SIDE( 0.625F, - 30, 0, "bottom_3d_side" ),
        FRONT( 1, 0, 0, "front" ),
        RIGHT( 1, 0, - 90, "right" ),
        BACK( 1, 0, 180, "back" ),
        LEFT( 1, 0, 90, "left" );

        private final float scale;
        private final float rotateX;
        private final float rotateY;
        public final float cameraX;
        public final float cameraY;
        public final float cameraZ;
        public final String name;

        RotateMode( float scale, float rotateX, float rotateY, String name ) {
            this.scale = scale;
            this.rotateX = rotateX;
            this.rotateY = rotateY;
            this.name = WikiTranslations.rotationMode + "." + name;

            Vector3f vec = new Vector3f( - 1, 0, 0 );
            Matrix4f mat = new Matrix4f();
            mat.rotX( rotateX );
            mat.rotY( rotateY );
            mat.transform( vec );

            this.cameraX = vec.x;
            this.cameraY = vec.y;
            this.cameraZ = vec.z;
        }

        public RotateMode next() {
            return values()[ ordinal() + 1 & 7 ];
        }

        public void rotate( float lastValidRot ) {
            GlStateManager.rotatef( this.rotateX, 1, 0, 0 );
            GlStateManager.rotatef( this.rotateY + lastValidRot, 0, 1, 0 );
        }
    }
}
