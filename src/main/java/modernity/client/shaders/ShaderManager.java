/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 26 - 2019
 * Author: rgsw
 */

package modernity.client.shaders;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import modernity.api.dimension.IShaderDimension;
import modernity.api.reflect.MethodAccessor;
import modernity.client.ModernityClient;
import modernity.client.shaders.post.MainShader;
import modernity.client.util.BlitBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.*;

import java.util.function.Predicate;

/**
 *
 */
// TODO: Configurations for shaders
public class ShaderManager implements ISelectiveResourceReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MIN_REQUIRED_TEX_UNITS = 6;

    private static final MethodAccessor<GameRenderer, Void> setupCameraTransformMethod = new MethodAccessor<>( GameRenderer.class, "func_195460_g", float.class );
    private static final MethodAccessor<GameRenderer, Void> renderHandMethod = new MethodAccessor<>( GameRenderer.class, "func_215308_a", ActiveRenderInfo.class, float.class );

    private final Minecraft mc = Minecraft.getInstance();

    private MainShader mainShader = new MainShader( "modernity:main" );

    private final BlitBuffer blit = new BlitBuffer();

    private boolean cancelOverlays;

    private boolean shaderError;
    private boolean required;

    private static boolean capabilitiesChecked;
    private static boolean supportsShaders;
    private static boolean supportsFloatbuffer;
    private static boolean supportsARBFloatbuffer;


    public boolean canUseShaders() {
        return areShadersSupported() && ! shaderError;
    }

    public boolean cancelOverlays() {
        return cancelOverlays;
    }

    public boolean shouldUseShaders() {
        return required || mc.world.dimension instanceof IShaderDimension;
    }

    public void updateShaders( float partialTicks ) {
        if( canUseShaders() && shouldUseShaders() ) {
            cancelOverlays = true;
            GlStateManager.matrixMode( GL11.GL_TEXTURE );
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode( GL11.GL_PROJECTION );
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode( GL11.GL_MODELVIEW );
            GlStateManager.pushMatrix();
            cancelOverlays = true;
            renderHandMethod.call( mc.gameRenderer, mc.gameRenderer.getActiveRenderInfo(), partialTicks );
            setupCameraTransformMethod.call( mc.gameRenderer, partialTicks );
            cancelOverlays = false;
            GlStateManager.matrixMode( GL11.GL_TEXTURE );
            GlStateManager.popMatrix();
            GlStateManager.matrixMode( GL11.GL_PROJECTION );
            GlStateManager.popMatrix();
            GlStateManager.matrixMode( GL11.GL_MODELVIEW );
            GlStateManager.popMatrix();

            mainShader.updateInputFBO( mc.getFramebuffer() );
            mainShader.updateMatrices();
        }
    }

    public void renderShaders( float partialTicks ) {
        if( canUseShaders() && shouldUseShaders() ) {
            blit.setSize( mc.mainWindow.getFramebufferWidth(), mc.mainWindow.getFramebufferHeight() );
            mainShader.setMirrorY( true );
            mainShader.setOutputFBO( blit.getFBO() );
            mainShader.render( partialTicks );
            blit.render();
        }
        required = false;
    }

    @Override
    public void onResourceManagerReload( IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate ) {
        if( resourcePredicate.test( VanillaResourceType.SHADERS ) ) {
            shaderError = false;
            try {
                mainShader.reload();
            } catch( ShaderException exc ) {
                LOGGER.error( "Error loading shaders: " + exc.getMessage(), exc );
                shaderError = true;
            }
        }
    }

    private static void checkGLCaps() {
        if( ! capabilitiesChecked ) {
            capabilitiesChecked = true;
            GLCapabilities caps = GL.getCapabilities();
            boolean supported = caps.OpenGL21 || caps.GL_ARB_vertex_shader && caps.GL_ARB_fragment_shader && caps.GL_ARB_shader_objects;
            boolean arbMultitexture = caps.GL_ARB_multitexture && ! caps.OpenGL13;
            int maxTextureUnits = arbMultitexture
                                  ? GL11.glGetInteger( ARBMultitexture.GL_MAX_TEXTURE_UNITS_ARB )
                                  : ! caps.OpenGL20
                                    ? GL11.glGetInteger( GL13.GL_MAX_TEXTURE_UNITS )
                                    : GL11.glGetInteger( GL20.GL_MAX_TEXTURE_IMAGE_UNITS );
            boolean textureUnitsSupported = maxTextureUnits >= MIN_REQUIRED_TEX_UNITS;
            supportsShaders = GLX.usePostProcess && supported && GLX.useFbo && textureUnitsSupported;
            supportsFloatbuffer = caps.OpenGL30;
            supportsARBFloatbuffer = caps.GL_ARB_texture_float;
        }
    }

    public static boolean areShadersSupported() {
        checkGLCaps();
        return supportsShaders;
    }

    public static boolean isARBFloatBuffSupported() {
        checkGLCaps();
        return supportsARBFloatbuffer;
    }

    public static boolean isFloatBuffSupported() {
        checkGLCaps();
        return supportsFloatbuffer;
    }

    public static void require() {
        get().required = true;
    }

    public static void addLight( LightSource src ) {
        get().mainShader.addLight( src );
    }

    public static ShaderManager get() {
        return ModernityClient.get().getShaderManager();
    }
}
