/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class ShadeParticle extends SpriteTexturedParticle {
    private final double rootPosX;
    private final double rootPosY;
    private final double rootPosZ;

    private ShadeParticle( World world, double x, double y, double z, double xv, double yv, double zv ) {
        super( world, x, y, z );
        motionX = xv;
        motionY = yv;
        motionZ = zv;

        posX = x;
        posY = y;
        posZ = z;

        rootPosX = x;
        rootPosY = y;
        rootPosZ = z;

        particleScale = 0.1F * ( rand.nextFloat() * 0.2F + 0.5F );

        particleRed = 0;
        particleGreen = rand.nextFloat() * 0.6F + 0.4F;
        particleBlue = 1;
        maxAge = (int) ( Math.random() * 10 ) + 40;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move( double x, double y, double z ) {
        setBoundingBox( getBoundingBox().offset( x, y, z ) );
        resetPositionToBB();
    }

    @Override
    public float getScale( float partialTicks ) {
        float lifeTime = ( age + partialTicks ) / maxAge;
        lifeTime = 1 - lifeTime;
        lifeTime *= lifeTime;
        lifeTime = 1 - lifeTime;
        return particleScale * lifeTime;
    }

    @Override
    public int getBrightnessForRender( float partialTicks ) {
        int packed = super.getBrightnessForRender( partialTicks );
        float lifeTime = (float) age / maxAge;
        lifeTime *= lifeTime;
        lifeTime *= lifeTime;

        int sky = packed & 255;
        int block = packed >> 16 & 255;

        block += (int) ( lifeTime * 15 * 16 );
        if( block > 240 ) {
            block = 240;
        }

        return sky | block << 16;
    }

    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if( age++ >= maxAge ) {
            setExpired();
        } else {
            float lifeTime = (float) age / maxAge;
            float jumpOffset = - lifeTime + lifeTime * lifeTime * 2;
            float distanceJumped = 1 - jumpOffset;
            posX = rootPosX + motionX * distanceJumped;
            posY = rootPosY + motionY * distanceJumped + ( 1 - lifeTime );
            posZ = rootPosZ + motionZ * distanceJumped;
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory( IAnimatedSprite sprite ) {
            spriteSet = sprite;
        }

        @Override
        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
            ShadeParticle p = new ShadeParticle( world, x, y, z, xv, yv, zv );
            p.selectSpriteRandomly( spriteSet );
            return p;
        }
    }
}