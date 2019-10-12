/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn( Dist.CLIENT )
public class SaltParticle extends PhysicsParticle {
    public SaltParticle( World world, double x, double y, double z, double xv, double yv, double zv, IAnimatedSprite sprite ) {
        super( world, x, y, z, xv, yv, zv, sprite );
        particleScale = 0.08F;
        bounciness = 0.7;
        motionX = xv;
        motionY = yv;
        motionZ = zv;
        particleGravity = 1;
        maxAge = 40 + rand.nextInt( 20 );
        weight = 0.5;
        selectSpriteRandomly( sprite );
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public Factory( IAnimatedSprite sprite ) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
            return new SaltParticle( world, x, y, z, xv, yv, zv, sprite );
        }
    }
}
