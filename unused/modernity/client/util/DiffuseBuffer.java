/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.client.util;

import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL11.*;

public class DiffuseBuffer extends Texture {
    private int texID = - 1;
    private int width;
    private int height;

    public DiffuseBuffer( @Nullable TextureManager textureManager, @Nullable ResourceLocation name ) {
        if( textureManager != null && name != null ) {
            textureManager.loadTexture( name, this );
        }
    }

    public boolean blitDiffuseBufferFrom( Framebuffer input ) {
        boolean changed = false;

        if( this.texID != - 1 && ( width != input.framebufferWidth || height != input.framebufferHeight ) ) {
            glDeleteTextures( texID );
            this.texID = - 1;
        }

        if( texID == - 1 ) {
            width = input.framebufferWidth;
            height = input.framebufferHeight;

            texID = glGenTextures();
            glBindTexture( texID, GL_TEXTURE_2D );
            TextureUtil.prepareImage( texID, width, height );

            changed = true;
        }

        input.bindFramebuffer( false );
        glBindTexture( GL_TEXTURE_2D, texID );

        glCopyTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, 0, 0, width, height, 0 );

        return changed;
    }

    public void delete() {
        if( texID != - 1 ) {
            glDeleteTextures( texID );
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void loadTexture( IResourceManager resourceManager ) {
    }

    @Override
    public int getGlTextureId() {
        return texID;
    }

    @Override
    public void deleteGlTexture() {
        delete();
    }
}