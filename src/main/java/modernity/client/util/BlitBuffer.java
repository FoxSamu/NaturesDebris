/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 25 - 2019
 * Author: rgsw
 */

package modernity.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;

import static com.mojang.blaze3d.platform.GlStateManager.*;
import static org.lwjgl.opengl.GL11.*;

public class BlitBuffer {
    private Framebuffer fbo;
    private int width;
    private int height;

    public BlitBuffer() {

    }

    public Framebuffer getFBO() {
        return fbo;
    }

    public Framebuffer setSize( int width, int height ) {
        if( fbo == null ) {
            this.width = width;
            this.height = height;
            fbo = new Framebuffer( width, height, true, Minecraft.IS_RUNNING_ON_MAC );
        } else {
            if( this.width != width || this.height != height ) {
                this.width = width;
                this.height = height;
                fbo.createBuffers( width, height, Minecraft.IS_RUNNING_ON_MAC );
            }
        }
        return fbo;
    }

    // TODO Re-evaluate the use of GlStateManager
    public void render() {
        Minecraft mc = Minecraft.getInstance();

        fbo.bindFramebufferTexture();

        Framebuffer out = mc.getFramebuffer();

        matrixMode( GL_TEXTURE );
        pushMatrix();
        loadIdentity();
        matrixMode( GL_PROJECTION );
        pushMatrix();
        loadIdentity();
        ortho( 0, out.framebufferTextureWidth, out.framebufferTextureHeight, 0, 1000, 3000 );
        matrixMode( GL_MODELVIEW );
        pushMatrix();
        loadIdentity();
        translatef( 0, 0, - 2000 );

        depthMask( false );

        out.framebufferClear( Minecraft.IS_RUNNING_ON_MAC );
        out.bindFramebuffer( false );

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        int w = out.framebufferTextureWidth;
        int h = out.framebufferTextureHeight;
        viewport( 0, 0, w, h );

        buff.begin( GL_QUADS, DefaultVertexFormats.POSITION_TEX );
        buff.pos( 0, 0, 0 ).tex( 0, 0 ).endVertex();
        buff.pos( 0, h, 0 ).tex( 0, 1 ).endVertex();
        buff.pos( w, h, 0 ).tex( 1, 1 ).endVertex();
        buff.pos( w, 0, 0 ).tex( 1, 0 ).endVertex();
        tess.draw();

        matrixMode( GL_TEXTURE );
        popMatrix();
        matrixMode( GL_PROJECTION );
        popMatrix();
        matrixMode( GL_MODELVIEW );
        popMatrix();

        depthMask( true );

        fbo.unbindFramebufferTexture();

        mc.getFramebuffer().bindFramebuffer( true );
    }


}
