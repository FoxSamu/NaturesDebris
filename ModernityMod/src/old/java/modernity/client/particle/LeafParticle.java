/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import modernity.api.util.math.ColorUtil;

/**
 * Leaf particle spawned by humus and leaves.
 */
@OnlyIn(Dist.CLIENT)
public class LeafParticle extends PhysicsParticle {
    private double angleIncr;

    public LeafParticle(World world, double x, double y, double z, double xv, double yv, double zv, double r, double g, double b, IAnimatedSprite sprite) {
        super(world, x, y, z, xv, yv, zv, sprite);
        particleScale = 0.08F;
        bounciness = 0;
        airFriction = 0.2 - rand.nextDouble() * 0.1;

        motionX = xv;
        motionY = yv;
        motionZ = zv;
        particleGravity = 0.2f;
        maxAge = 100 + rand.nextInt(60);
        weight = 0.1;
        particleAngle = prevParticleAngle = rand.nextFloat() * (float) Math.PI;
        angleIncr = rand.nextDouble() * (rand.nextBoolean() ? -6 : 6) / 180 * Math.PI;

        particleRed = (float) r;
        particleGreen = (float) g;
        particleBlue = (float) b;

        selectSpriteRandomly(sprite);
    }

    public LeafParticle(World world, double x, double y, double z, double xv, double yv, double zv, int rgb, int lifeMin, int lifeRand, IAnimatedSprite sprite) {
        this(world, x, y, z, xv, yv, zv, ColorUtil.red(rgb), ColorUtil.green(rgb), ColorUtil.blue(rgb), sprite);
        maxAge = lifeMin + rand.nextInt(lifeRand);
    }

    @Override
    public void tick() {
        prevParticleAngle = particleAngle;
        double speed = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        // Rotate the particle during falling
        particleAngle += angleIncr * speed * 35;
        super.tick();
    }
//    TODO Re-evaluate
//    public static class FallingLeafFactory implements IParticleFactory<RgbParticleData> {
//        private final IAnimatedSprite sprite;
//
//        public FallingLeafFactory( IAnimatedSprite sprite ) {
//            this.sprite = sprite;
//        }
//
//        @Nullable
//        @Override
//        public Particle makeParticle( RgbParticleData type, World world, double x, double y, double z, double xv, double yv, double zv ) {
//            return new LeafParticle( world, x, y, z, xv, yv, zv, type.red(), type.green(), type.blue(), sprite );
//        }
//    }
//
//    public static class HumusFactory implements IParticleFactory<BasicParticleType> {
//        private final IAnimatedSprite sprite;
//
//        public HumusFactory( IAnimatedSprite sprite ) {
//            this.sprite = sprite;
//        }
//
//        @Nullable
//        @Override
//        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
//            int rgb = ModernityClient.get().getFallenLeafColors().random( world.rand );
//            return new LeafParticle( world, x, y, z, xv, yv, zv, rgb, 30, 20, sprite );
//        }
//    }
}
