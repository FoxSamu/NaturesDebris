/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.particle;

import modernity.api.particle.ICustomParticle;
import modernity.api.util.ColorUtil;
import modernity.client.texture.ParticleSprite;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class LeafParticle extends PhysicsParticle implements ICustomParticle {
    private static final ParticleSprite[] SPRITES = {
            ParticleSprite.LEAF_1,
            ParticleSprite.LEAF_2,
            ParticleSprite.LEAF_3,
            ParticleSprite.LEAF_4,
            ParticleSprite.LEAF_5,
            ParticleSprite.LEAF_6,
            ParticleSprite.LEAF_7,
            ParticleSprite.LEAF_8,
            ParticleSprite.LEAF_9,
            ParticleSprite.LEAF_10,
            ParticleSprite.LEAF_11,
            ParticleSprite.LEAF_12,
            ParticleSprite.LEAF_13,
            ParticleSprite.LEAF_14,
            ParticleSprite.LEAF_15,
            ParticleSprite.LEAF_16
    };

    private double angle;
    private double angleIncr;
    private double lastAngle;
    private final ParticleSprite sprite;

    public LeafParticle( World world, double x, double y, double z, double xv, double yv, double zv, double r, double g, double b ) {
        super( world, x, y, z, xv, yv, zv );
        particleScale = 0.8F;
        bounciness = 0;
        airFriction = 0.2;

        motionX = xv;
        motionY = yv;
        motionZ = zv;
        particleGravity = 0.2f;
        maxAge = 100 + rand.nextInt( 60 );
        weight = 0.1;
        angle = lastAngle = rand.nextDouble() * Math.PI;
        angleIncr = rand.nextDouble() * ( rand.nextBoolean() ? - 6 : 6 ) / 180 * Math.PI;
        sprite = SPRITES[ rand.nextInt( SPRITES.length ) ];

        particleRed = (float) r;
        particleGreen = (float) g;
        particleBlue = (float) b;
    }

    public LeafParticle( World world, double x, double y, double z, double xv, double yv, double zv, int rgb, int lifeMin, int lifeRand ) {
        this( world, x, y, z, xv, yv, zv, ColorUtil.red( rgb ), ColorUtil.green( rgb ), ColorUtil.blue( rgb ) );
        maxAge = lifeMin + rand.nextInt( lifeRand );
    }

    @Override
    public void tick() {
        lastAngle = angle;
        double speed = Math.sqrt( motionX * motionX + motionY * motionY + motionZ * motionZ );
        angle += angleIncr * speed * 35;
        super.tick();
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void renderParticle( BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ ) {
        float radius = particleScale * 0.10F;
        double x = prevPosX + ( posX - prevPosX ) * partialTicks - interpPosX;
        double y = prevPosY + ( posY - prevPosY ) * partialTicks - interpPosY;
        double z = prevPosZ + ( posZ - prevPosZ ) * partialTicks - interpPosZ;
        int packed = getBrightnessForRender( partialTicks );
        int lu = packed >> 16 & '\uffff';
        int lv = packed & '\uffff';
        double r = lastAngle + ( angle - lastAngle ) * partialTicks;
        ParticleRenderer.renderParticle( buffer, x, y + radius * 0.6, z, radius, (float) r, sprite, lu, lv, particleRed, particleGreen, particleBlue, 1 );
    }
}
