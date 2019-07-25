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
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Particles;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.common.fluid.MDFluids;

@OnlyIn( Dist.CLIENT )
public class DripParticle extends Particle {
    private final Fluid fluid;
    private int bobTimer;
    private boolean splash;

    protected DripParticle( World world, double x, double y, double z, int rgb, boolean splash, Fluid fluid ) {
        super( world, x, y, z, 0, 0, 0 );
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.splash = splash;

        particleRed = ( rgb >>> 16 & 0xff ) / 255F;
        particleGreen = ( rgb >>> 8 & 0xff ) / 255F;
        particleBlue = ( rgb & 0xff ) / 255F;

        this.setParticleTextureIndex( 113 );
        this.setSize( 0.01F, 0.01F );
        this.particleGravity = 0.06F;
        this.fluid = fluid;
        this.bobTimer = 40;
        this.maxAge = (int) ( 64.0D / ( Math.random() * 0.8D + 0.2D ) );
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    public int getBrightnessForRender( float brightness ) {
        return this.fluid.isIn( FluidTags.WATER ) ? super.getBrightnessForRender( brightness ) : 257;
    }

    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= (double) this.particleGravity;
        if( this.bobTimer-- > 0 ) {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
            this.setParticleTextureIndex( 113 );
        } else {
            this.setParticleTextureIndex( 112 );
        }

        this.move( this.motionX, this.motionY, this.motionZ );
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        if( this.maxAge-- <= 0 ) {
            this.setExpired();
        }

        if( this.onGround ) {
            if( splash ) {
                this.setExpired();
                // TODO: Custom splash particle here...
                this.world.addParticle( Particles.SPLASH, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D );
            } else {
                this.setParticleTextureIndex( 114 );
            }

            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos pos = new BlockPos( this.posX, this.posY, this.posZ );
        IFluidState state = this.world.getFluidState( pos );
        if( state.getFluid() == this.fluid ) {
            double height = MathHelper.floor( this.posY ) + state.getHeight();
            if( this.posY < height ) {
                this.setExpired();
            }
        }

    }

    @OnlyIn( Dist.CLIENT )
    public static class ModernizedWaterFactory implements IParticleFactory<BasicParticleType> {
        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zc ) {
            BlockPos pos = new BlockPos( MathHelper.floor( x ), MathHelper.floor( y + 1.5 ), MathHelper.floor( z ) );
            int color = MDFluids.MODERNIZED_WATER.getColor( world.getFluidState( pos ), pos, world );
            return new DripParticle( world, x, y, z, color, false, MDFluids.MODERNIZED_WATER );
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static class PortalFactory implements IParticleFactory<BasicParticleType> {
        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
            return new DripParticle( world, x, y, z, 0xffaa00, false, MDFluids.PORTAL );
        }
    }
}
