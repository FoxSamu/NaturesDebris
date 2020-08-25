/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.world.World;

public class AmbientParticle extends SpriteTexturedParticle {

    protected AmbientParticle(World world, double x, double y, double z, double xv, double yv, double zv, double r, double g, double b) {
        super(world, x, y, z);
        motionX = xv + rand.nextGaussian() * 0.04;
        motionY = yv + rand.nextGaussian() * 0.04;
        motionZ = zv + rand.nextGaussian() * 0.04;

        particleRed = (float) r;
        particleGreen = (float) g;
        particleBlue = (float) b;

        particleScale *= (rand.nextFloat() * 0.6F + 1.0F) * 0.3F;

        maxAge = 90 + rand.nextInt(11);

        setSize(0.2F, 0.2F);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (age++ >= maxAge) {
            setExpired();
        } else {
            move(motionX, motionY, motionZ);
            motionX *= 0.99;
            motionY *= 0.99;
            motionZ *= 0.99;
        }
    }

    @Override
    public void move(double x, double y, double z) {
        if (x != 0 || y != 0 || z != 0) {
            setBoundingBox(getBoundingBox().offset(x, y, z));
            resetPositionToBB();
        }
    }

//    TODO Re-evaluate
//    public static class Factory implements IParticleFactory<RgbParticleData> {
//        private final IAnimatedSprite sprite;
//
//        public Factory( IAnimatedSprite sprite ) {
//            this.sprite = sprite;
//        }
//
//        @Nullable
//        @Override
//        public Particle makeParticle( RgbParticleData data, World world, double x, double y, double z, double xv, double yv, double zv ) {
//            AmbientParticle p = new AmbientParticle( world, x, y, z, xv, yv, zv, data.red(), data.green(), data.blue() );
//            p.selectSpriteRandomly( sprite );
//            return p;
//        }
//    }
}
