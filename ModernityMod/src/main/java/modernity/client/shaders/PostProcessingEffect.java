/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 25 - 2019
 * Author: rgsw
 */

package modernity.client.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

import static com.mojang.blaze3d.platform.GlStateManager.*;
import static org.lwjgl.opengl.GL11.*;

// TODO Re-evaluate the use of GlStateManager
public class PostProcessingEffect {
    protected final Program program;

    private Framebuffer out;

    private boolean mirrorY;

    public PostProcessingEffect( Program program ) {
        this.program = program;
    }

    public PostProcessingEffect( ResourceLocation shader ) {
        this( new Program(
            new ResourceLocation( shader.getNamespace(), "shaders/" + shader.getPath() + "/vertex.vsh" ),
            new ResourceLocation( shader.getNamespace(), "shaders/" + shader.getPath() + "/fragment.fsh" )
        ) );
    }

    public PostProcessingEffect( String shader ) {
        this( new ResourceLocation( shader ) );
    }

    public void setOutputFBO( Framebuffer out ) {
        this.out = out;
    }

    public Framebuffer getOutputFramebuffer() {
        return out;
    }

    public Program getProgram() {
        return program;
    }

    public void setMirrorY( boolean mirrorY ) {
        this.mirrorY = mirrorY;
    }

    public boolean getMirrorY() {
        return mirrorY;
    }

    public void render( float partialTicks ) {
        matrixMode( GL_TEXTURE );
        pushMatrix();
        loadIdentity();

        color4f( 1, 1, 1, 1 );
        disableBlend();
        disableDepthTest();
        disableAlphaTest();
        disableFog();
        disableLighting();
        disableColorMaterial();
        disableCull();
        enableTexture();
        depthMask( false );
        colorMask( true, true, true, true );

        int w = out.framebufferTextureWidth;
        int h = out.framebufferTextureHeight;
        viewport( 0, 0, w, h );

        matrixMode( GL_PROJECTION );
        pushMatrix();
        loadIdentity();
        ortho( 0, w, h, 0, 1000, 3000 );
        matrixMode( GL_MODELVIEW );
        pushMatrix();
        loadIdentity();
        translatef( 0, 0, - 2000 );

        out.framebufferClear( Minecraft.IS_RUNNING_ON_MAC );
        out.bindFramebuffer( false );

        program.use();
        preRender( partialTicks );

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        buff.begin( GL_QUADS, DefaultVertexFormats.POSITION_TEX );
        buff.pos( 0, 0, 0 ).tex( 0, mirrorY ? 1 : 0 ).endVertex();
        buff.pos( 0, h, 0 ).tex( 0, mirrorY ? 0 : 1 ).endVertex();
        buff.pos( w, h, 0 ).tex( 1, mirrorY ? 0 : 1 ).endVertex();
        buff.pos( w, 0, 0 ).tex( 1, mirrorY ? 1 : 0 ).endVertex();
        tess.draw();

        Program.useNone();

        depthMask( true );
        colorMask( true, true, true, true );
        enableCull();

        out.unbindFramebuffer();

        matrixMode( GL_TEXTURE );
        popMatrix();
        matrixMode( GL_PROJECTION );
        popMatrix();
        matrixMode( GL_MODELVIEW );
        popMatrix();
    }

    public void render( Framebuffer out, float partialTicks ) {
        setOutputFBO( out );
        render( partialTicks );
    }

    protected void preRender( float partialTicks ) {

    }

    protected void init() {

    }

    public void reload() {
        if( program.isLoaded() )
            program.cleanup();
        program.load();
        init();
    }
}
