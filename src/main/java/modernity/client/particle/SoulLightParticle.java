/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import modernity.api.util.ESoulLightColor;
import modernity.common.particle.SoulLightParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.redgalaxy.util.MathUtil;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class SoulLightParticle extends Particle implements IRenderLastParticle {
    public static final ResourceLocation TEXTURE = new ResourceLocation( "modernity:textures/particle/light.png" );

    private static final VertexFormat VERTEX_FORMAT
        = new VertexFormat().addElement( DefaultVertexFormats.POSITION_3F )
                            .addElement( DefaultVertexFormats.TEX_2F )
                            .addElement( DefaultVertexFormats.COLOR_4UB )
                            .addElement( DefaultVertexFormats.TEX_2S )
                            .addElement( DefaultVertexFormats.NORMAL_3B );

    private float red;
    private float green;
    private float blue;
    private float scale;
    private boolean fade;

    protected SoulLightParticle( World world, double x, double y, double z, double xv, double yv, double zv, float r, float g, float b, float scaleMultiplier, float grav, int age, boolean mustFade ) {
        super( world, x, y, z );
        motionX = xv;
        motionY = yv;
        motionZ = zv;

        scale = ( rand.nextFloat() * 0.25F + 0.75F ) * scaleMultiplier;

        red = r;
        green = g;
        blue = b;

        particleGravity = grav;
        fade = mustFade;

        maxAge = age;
    }

    @Override
    public void renderParticleLast( BufferBuilder buffer, ActiveRenderInfo info, float partialTicks, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ ) {

        Minecraft.getInstance().getTextureManager().bindTexture( TEXTURE );

        // Calculate scale, which scales down over time
        float rem = ( maxAge - age ) / (float) maxAge;
        float size = ( rem * 0.8F + 0.2F ) * scale;

        // Calculate local pos
        float x = (float) ( prevPosX + ( posX - prevPosX ) * partialTicks - interpPosX );
        float y = (float) ( prevPosY + ( posY - prevPosY ) * partialTicks - interpPosY );
        float z = (float) ( prevPosZ + ( posZ - prevPosZ ) * partialTicks - interpPosZ );

        // Calculate fading alpha
        float fadeFactor = 1;
        if( fade ) {
            float distance = MathHelper.sqrt( x * x + y * y + z * z );

            if( distance < 10 ) {
                return;
            } else if( distance < 16 ) {
                fadeFactor = MathUtil.unlerp( 10, 16, distance );
            }
        }

        // Draw
        GlStateManager.depthMask( false );
        GlStateManager.enableBlend();
        GlStateManager.enableTexture();
        GlStateManager.enableFog();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE );
        GlStateManager.alphaFunc( GL11.GL_GREATER, 0.004F );
        buffer.begin( 7, VERTEX_FORMAT );

        buffer.pos( x - rotX * size - rotXY * size, y - rotZ * size, z - rotYZ * size - rotXZ * size )
              .tex( 1, 1 )
              .color( red, green, blue, rem * fadeFactor )
              .lightmap( 0, 240 )
              .normal( 0, 1, 0 )
              .endVertex();

        buffer.pos( x - rotX * size + rotXY * size, y + rotZ * size, z - rotYZ * size + rotXZ * size )
              .tex( 1, 0 )
              .color( red, green, blue, rem * fadeFactor )
              .lightmap( 0, 240 )
              .normal( 0, 1, 0 )
              .endVertex();

        buffer.pos( x + rotX * size + rotXY * size, y + rotZ * size, z + rotYZ * size + rotXZ * size )
              .tex( 0, 0 )
              .color( red, green, blue, rem * fadeFactor )
              .lightmap( 0, 240 )
              .normal( 0, 1, 0 )
              .endVertex();

        buffer.pos( x + rotX * size - rotXY * size, y - rotZ * size, z + rotYZ * size - rotXZ * size )
              .tex( 0, 1 )
              .color( red, green, blue, rem * fadeFactor )
              .lightmap( 0, 240 )
              .normal( 0, 1, 0 )
              .endVertex();

        Tessellator.getInstance().draw();
        GlStateManager.depthMask( true );
        GlStateManager.blendFuncSeparate( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO );
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableAlphaTest();
        GlStateManager.disableFog();
        GlStateManager.disableColorMaterial();
        GlStateManager.color4f( 1, 1, 1, 1 );
        GlStateManager.alphaFunc( GL11.GL_GREATER, 0.1F );
    }

    @Override
    public void renderParticle( BufferBuilder buffer, ActiveRenderInfo info, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ ) {
        renderLast();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    public static class BaseFactory implements IParticleFactory<SoulLightParticleData> {

        @Nullable
        @Override
        public Particle makeParticle( SoulLightParticleData data, World world, double x, double y, double z, double xv, double yv, double zv ) {
            ESoulLightColor color = data.getColor();
            boolean fades = data.fades();
            return new SoulLightParticle(
                world,
                x, y, z,
                xv, yv, zv,
                color.red, color.green, color.blue,
                0.4F,
                - 0.007F,
                100,
                fades
            );
        }
    }

    public static class CloudFactory implements IParticleFactory<SoulLightParticleData> {

        @Nullable
        @Override
        public Particle makeParticle( SoulLightParticleData data, World world, double x, double y, double z, double xv, double yv, double zv ) {
            ESoulLightColor color = data.getColor();
            boolean fades = data.fades();
            return new SoulLightParticle(
                world,
                x, y, z,
                xv, yv, zv,
                color.red, color.green, color.blue,
                0.1F,
                0,
                200,
                fades
            );
        }
    }
}
