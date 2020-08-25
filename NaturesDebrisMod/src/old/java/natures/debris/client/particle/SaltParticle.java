/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Salt particle, which drops from salt blocks and salt crystals
 */
@OnlyIn(Dist.CLIENT)
public class SaltParticle extends PhysicsParticle {
    public SaltParticle(World world, double x, double y, double z, double xv, double yv, double zv, IAnimatedSprite sprite) {
        super(world, x, y, z, xv, yv, zv, sprite);
        particleScale = 0.08F;
        bounciness = 0.7; // Jumps and dances: "Yaay, I'm salt!"
        motionX = xv;
        motionY = yv;
        motionZ = zv;
        particleGravity = 1;
        maxAge = 40 + rand.nextInt(20);
        weight = 0.5;
        selectSpriteRandomly(sprite);
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv) {
            return new SaltParticle(world, x, y, z, xv, yv, zv, sprite);
        }
    }
}
