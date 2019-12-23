/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 23 - 2019
 * Author: rgsw
 */

package modernity.client.particle;

import modernity.api.util.ColorUtil;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Dripping particle used for Modernity fluids.
 */
public class DripParticle extends SpriteTexturedParticle {
    protected final Type type;

    private DripParticle( World world, double x, double y, double z, Type type ) {
        super( world, x, y, z );
        this.setSize( 0.01F, 0.01F );
        this.particleGravity = 0.06F;
        this.type = type;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getBrightnessForRender( float partialTicks ) {
        return type.brightness( super.getBrightnessForRender( partialTicks ), age / (double) maxAge );
    }

    @Override
    public void tick() {
        int color = type.color( age / (double) maxAge );
        setColor(
            (float) ColorUtil.red( color ),
            (float) ColorUtil.green( color ),
            (float) ColorUtil.blue( color )
        );
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        updateAge();
        if( ! isExpired ) {
            motionY -= particleGravity;

            move( motionX, motionY, motionZ );
            updateMotion();
            if( ! isExpired ) {
                motionX *= 0.98;
                motionY *= 0.98;
                motionZ *= 0.98;
                BlockPos pos = new BlockPos( posX, posY, posZ );
                IFluidState state = world.getFluidState( pos );
                if( type.dissolvesIn( state.getFluid() ) && posY < pos.getY() + state.func_215679_a( world, pos ) ) {
                    setExpired();
                }
            }
        }
    }

    protected void updateAge() {
        if( age++ > maxAge ) {
            setExpired();
        }

    }

    protected void updateMotion() {
    }

    static class Dripping extends DripParticle {
        private Dripping( World world, double x, double y, double z, Type type ) {
            super( world, x, y, z, type );
            this.particleGravity *= 0.02F;
            this.maxAge = type.hangTime( rand );
            int color = type.color( age / (double) maxAge );
            setColor(
                (float) ColorUtil.red( color ),
                (float) ColorUtil.green( color ),
                (float) ColorUtil.blue( color )
            );
        }

        @Override
        protected void updateAge() {
            if( age++ > maxAge ) {
                setExpired();
                world.addParticle( type.falling(), posX, posY, posZ, motionX, motionY, motionZ );
            }

        }

        @Override
        protected void updateMotion() {
            motionX *= 0.02;
            motionY *= 0.02;
            motionZ *= 0.02;
        }
    }

    static class Falling extends DripParticle {
        private Falling( World world, double x, double y, double z, Type type ) {
            super( world, x, y, z, type );
            this.maxAge = (int) ( 64 / ( Math.random() * 0.8 + 0.2 ) );
            int color = type.color( age / (double) maxAge );
            setColor(
                (float) ColorUtil.red( color ),
                (float) ColorUtil.green( color ),
                (float) ColorUtil.blue( color )
            );
        }

        @Override
        protected void updateMotion() {
            if( onGround ) {
                setExpired();
                world.addParticle( type.landing(), posX, posY, posZ, 0, 0, 0 );
            }
        }
    }

    static class Landing extends DripParticle {
        private Landing( World world, double x, double y, double z, Type type ) {
            super( world, x, y, z, type );
            this.maxAge = (int) ( 16 / ( Math.random() * 0.8 + 0.2 ) );
            int color = type.color( age / (double) maxAge );
            setColor(
                (float) ColorUtil.red( color ),
                (float) ColorUtil.green( color ),
                (float) ColorUtil.blue( color )
            );
        }
    }

    public interface Type {
        boolean dissolvesIn( Fluid fluid );
        int brightness( int localBrightness, double lifetime );
        int color( double lifetime );
        IParticleData falling();
        IParticleData landing();

        default int hangTime( Random rand ) {
            return 40;
        }
    }
//
//    public static class Oil implements Type {
//        private static final Oil INST = new Oil();
//
//        private Oil() {
//        }
//
//        @Override
//        public boolean dissolvesIn( Fluid fluid ) {
//            return fluid == MDFluids.OIL || fluid == MDFluids.FLOWING_OIL;
//        }
//
//        @Override
//        public int brightness( int localBrightness, double lifetime ) {
//            return localBrightness;
//        }
//
//        @Override
//        public int color( double lifetime ) {
//            return 0x222222;
//        }
//
//        @Override
//        public IParticleData falling() {
//            return MDParticleTypes.OIL_FALLING;
//        }
//
//        @Override
//        public IParticleData landing() {
//            return MDParticleTypes.OIL_LANDING;
//        }
//
//        @Override
//        public int hangTime( Random rand ) {
//            return 80 + rand.nextInt( 20 );
//        }
//
//        public static class DrippingFactory implements IParticleFactory<BasicParticleType> {
//            private final IAnimatedSprite sprite;
//
//            public DrippingFactory( IAnimatedSprite sprite ) {
//                this.sprite = sprite;
//            }
//
//            @Nullable
//            @Override
//            public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
//                Dripping particle = new Dripping( world, x, y, z, INST );
//                particle.selectSpriteRandomly( sprite );
//                return particle;
//            }
//        }
//
//        public static class FallingFactory implements IParticleFactory<BasicParticleType> {
//            private final IAnimatedSprite sprite;
//
//            public FallingFactory( IAnimatedSprite sprite ) {
//                this.sprite = sprite;
//            }
//
//            @Nullable
//            @Override
//            public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
//                Falling particle = new Falling( world, x, y, z, INST );
//                particle.selectSpriteRandomly( sprite );
//                return particle;
//            }
//        }
//
//        public static class LandingFactory implements IParticleFactory<BasicParticleType> {
//            private final IAnimatedSprite sprite;
//
//            public LandingFactory( IAnimatedSprite sprite ) {
//                this.sprite = sprite;
//            }
//
//            @Nullable
//            @Override
//            public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
//                Landing particle = new Landing( world, x, y, z, INST );
//                particle.selectSpriteRandomly( sprite );
//                return particle;
//            }
//        }
//    }
}