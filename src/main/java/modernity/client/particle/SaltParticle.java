/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class SaltParticle extends PhysicsParticle {
    private static final ResourceLocation EXPLOSION_TEXTURE = new ResourceLocation( "modernity:textures/particle/salt.png" );

    public SaltParticle( World world, double x, double y, double z, double xv, double yv, double zv ) {
        super( world, x, y, z, xv, yv, zv );
        particleScale = 0.8F;
        bounciness = 0.7;
        motionX = xv;
        motionY = yv;
        motionZ = zv;
        particleGravity = 1;
        maxAge = 40 + rand.nextInt( 20 );
        weight = 0.5;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void renderParticle( BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ ) {
        Minecraft.getInstance().textureManager.bindTexture( EXPLOSION_TEXTURE );
        float u1 = 0;
        float u2 = 1;
        float v1 = 0;
        float v2 = 1;
        float radius = particleScale * 0.07F;
        double x = prevPosX + ( posX - prevPosX ) * partialTicks - interpPosX;
        double y = prevPosY + ( posY - prevPosY ) * partialTicks - interpPosY;
        double z = prevPosZ + ( posZ - prevPosZ ) * partialTicks - interpPosZ;
        int packed = getBrightnessForRender( partialTicks );
        int lu = packed >> 16 & '\uffff';
        int lv = packed & '\uffff';
        GlStateManager.color4f( 1, 1, 1, 1 );
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        buffer.begin( 7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP );
        buffer.pos( x - rotationX * radius - rotationXY * radius, y - rotationZ * radius, z - rotationYZ * radius - rotationXZ * radius ).tex( u2, v2 ).color( this.particleRed, this.particleGreen, this.particleBlue, 1 ).lightmap( lu, lv ).endVertex();
        buffer.pos( x - rotationX * radius + rotationXY * radius, y + rotationZ * radius, z - rotationYZ * radius + rotationXZ * radius ).tex( u2, v1 ).color( this.particleRed, this.particleGreen, this.particleBlue, 1 ).lightmap( lu, lv ).endVertex();
        buffer.pos( x + rotationX * radius + rotationXY * radius, y + rotationZ * radius, z + rotationYZ * radius + rotationXZ * radius ).tex( u1, v1 ).color( this.particleRed, this.particleGreen, this.particleBlue, 1 ).lightmap( lu, lv ).endVertex();
        buffer.pos( x + rotationX * radius - rotationXY * radius, y - rotationZ * radius, z + rotationYZ * radius - rotationXZ * radius ).tex( u1, v2 ).color( this.particleRed, this.particleGreen, this.particleBlue, 1 ).lightmap( lu, lv ).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableLighting();
    }
}
