/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.particle.ICustomParticle;
import modernity.client.texture.ParticleSprite;

import javax.annotation.Nullable;

@OnlyIn( Dist.CLIENT )
public class SaltParticle extends PhysicsParticle implements ICustomParticle {
    private static final ResourceLocation EXPLOSION_TEXTURE = new ResourceLocation( "modernity:textures/particle/salt.png" );
    private static final ParticleSprite SPRITE = ParticleSprite.SALT;

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
        float radius = particleScale * 0.07F;
        double x = prevPosX + ( posX - prevPosX ) * partialTicks - interpPosX;
        double y = prevPosY + ( posY - prevPosY ) * partialTicks - interpPosY;
        double z = prevPosZ + ( posZ - prevPosZ ) * partialTicks - interpPosZ;
        int packed = getBrightnessForRender( partialTicks );
        int lu = packed >> 16 & '\uffff';
        int lv = packed & '\uffff';
        ParticleRenderer.renderParticle( buffer, x, y, z, radius, SPRITE, lu, lv );
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {

        @Nullable
        @Override
        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
            return new SaltParticle( world, x, y, z, xv, yv, zv );
        }
    }
}
