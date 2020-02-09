/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.client.shaders.post;

import com.mojang.blaze3d.platform.GLX;
import modernity.api.util.Matrix4f;
import modernity.client.shaders.LightSource;
import modernity.client.shaders.PostProcessingEffect;
import modernity.client.shaders.Program;
import modernity.client.util.DepthBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

import static com.mojang.blaze3d.platform.GlStateManager.*;
import static org.lwjgl.opengl.GL11.*;

public class MainShader extends PostProcessingEffect {
    public static final ResourceLocation WORLD_DEPTH_TEXTURE = new ResourceLocation( "modernity:world_depth" );
    public static final ResourceLocation HAND_DEPTH_TEXTURE = new ResourceLocation( "modernity:hand_depth" );
    private static final int LIGHT_COUNT = 32;

    private static final Comparator<LightSource> LIGHT_SOURCE_SORTER = ( o1, o2 ) -> {
        Vec3d projView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double dx1 = o1.pos[ 0 ] - projView.x;
        double dy1 = o1.pos[ 1 ] - projView.y;
        double dz1 = o1.pos[ 2 ] - projView.z;
        double dx2 = o2.pos[ 0 ] - projView.x;
        double dy2 = o2.pos[ 1 ] - projView.y;
        double dz2 = o2.pos[ 2 ] - projView.z;
        double d1 = Math.sqrt( dx1 * dx1 + dy1 * dy1 + dz1 * dz1 );
        double d2 = Math.sqrt( dx2 * dx2 + dy2 * dy2 + dz2 * dz2 );
        if( d1 > d2 ) {
            return 1;
        } else if( d1 < d2 ) {
            return - 1;
        }
        return 0;
    };

    private final float[] modelViewBuff = new float[ 16 ];
    private final float[] projectionBuff = new float[ 16 ];

    private final Matrix4f modelView = new Matrix4f();
    private final Matrix4f projection = new Matrix4f();
    private final Matrix4f mvp = new Matrix4f();
    private final Matrix4f inverseMVP = new Matrix4f();

    private final float[] inverseMVPBuff = new float[ 16 ];

    private final TextureManager textureManager = Minecraft.getInstance().textureManager;

    private Framebuffer diffuse;
    private DepthBuffer depth;
    private DepthBuffer handDepth;

    private final ArrayList<LightSource> lights = new ArrayList<>();

    private Program.Uniform diffuseSamplerUniform;
    private Program.Uniform depthSamplerUniform;
    private Program.Uniform handDepthSamplerUniform;
    private Program.Uniform inverseMVPUniform;
    private Program.Uniform depthRangeUniform;

    private Program.Uniform fogModeUniform;

    private Program.Uniform lightCountUniform;
    private final Program.Uniform[] lightPositionUniforms = new Program.Uniform[ LIGHT_COUNT ];
    private final Program.Uniform[] lightColorUniforms = new Program.Uniform[ LIGHT_COUNT ];
    private final Program.Uniform[] lightRadiusUniforms = new Program.Uniform[ LIGHT_COUNT ];
    private final Program.Uniform[] lightTypeUniforms = new Program.Uniform[ LIGHT_COUNT ];

    public MainShader( String shader ) {
        super( shader );
        program.addPreCompileValue( "LIGHT_COUNT", LIGHT_COUNT );
    }

    public void updateInputFBO( Framebuffer fbo ) {
        this.diffuse = fbo;
        updateDepthBuffer();
    }

    public void updateHandFBO( Framebuffer fbo ) {
        handDepth.blitDepthBufferFrom( fbo );
        fbo.bindFramebuffer( false );
        bindTexture( 0 );
    }

    private void updateDepthBuffer() {
        depth.blitDepthBufferFrom( diffuse );
        diffuse.bindFramebuffer( false );
        bindTexture( 0 );
    }

    @Override
    public void render( float partialTicks ) {
        bind( 0, diffuse.framebufferTexture );
        bind( 1, depth.getGlTextureId() );
        bind( 2, handDepth.getGlTextureId() );

        super.render( partialTicks );

        bind( 0, 0 );
        bind( 1, 0 );
        bind( 2, 0 );
    }

    private void bind( int slot, int texture ) {
        activeTexture( GLX.GL_TEXTURE0 + slot );
        bindTexture( texture );
        activeTexture( GLX.GL_TEXTURE0 );
    }

    public void updateMatrices() {
        glGetFloatv( GL_MODELVIEW_MATRIX, modelViewBuff );
        glGetFloatv( GL_PROJECTION_MATRIX, projectionBuff );
        modelView.load( modelViewBuff );
        projection.load( projectionBuff );
        Matrix4f.mul( projection, modelView, mvp );
        Matrix4f.invert( mvp, inverseMVP );
        inverseMVP.store( inverseMVPBuff );
    }

    @Override
    protected void init() {
        diffuseSamplerUniform = program.uniform( "diffuse" );
        depthSamplerUniform = program.uniform( "depth" );
        handDepthSamplerUniform = program.uniform( "handDepth" );
        inverseMVPUniform = program.uniform( "inverseMVP" );
        depthRangeUniform = program.uniform( "depthRange" );
        lightCountUniform = program.uniform( "lightCount" );
        fogModeUniform = program.uniform( "fogMode" );

        for( int i = 0; i < LIGHT_COUNT; i++ ) {
            lightColorUniforms[ i ] = program.uniform( "lights[" + i + "].color" );
            lightPositionUniforms[ i ] = program.uniform( "lights[" + i + "].position" );
            lightRadiusUniforms[ i ] = program.uniform( "lights[" + i + "].radius" );
            lightTypeUniforms[ i ] = program.uniform( "lights[" + i + "].type" );
        }

        depth = new DepthBuffer( textureManager, WORLD_DEPTH_TEXTURE );
        handDepth = new DepthBuffer( textureManager, HAND_DEPTH_TEXTURE );
    }

    @Override
    protected void preRender( float partialTicks ) {
        diffuseSamplerUniform.set( 0 );
        depthSamplerUniform.set( 1 );
        handDepthSamplerUniform.set( 2 );
        inverseMVPUniform.setMatrix( inverseMVPBuff );
        float[] dr = new float[ 2 ];
        glGetFloatv( GL_DEPTH_RANGE, dr );
        depthRangeUniform.set( dr[ 1 ] );
        fogModeUniform.set( glGetInteger( GL_FOG_MODE ) );

        lights.sort( LIGHT_SOURCE_SORTER );

        int count = Math.min( LIGHT_COUNT, lights.size() );

        lightCountUniform.set( count );

        Vec3d projView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

        for( int i = 0; i < count; i++ ) {
            LightSource src = lights.get( i );
            lightPositionUniforms[ i ].set(
                src.pos[ 0 ] - projView.x,
                src.pos[ 1 ] - projView.y,
                src.pos[ 2 ] - projView.z
            );
            lightColorUniforms[ i ].set( src.color );
            lightRadiusUniforms[ i ].set( src.radius );
            lightTypeUniforms[ i ].set( src.type );
        }

        lights.clear();
    }


    public void addLight( LightSource src ) {
        lights.add( src );
    }
}
