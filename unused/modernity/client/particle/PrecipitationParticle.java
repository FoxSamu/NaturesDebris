/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PrecipitationParticle extends SpriteTexturedParticle {
    private final boolean remain;

    protected PrecipitationParticle( World world, double x, double y, double z, float r, float g, float b, boolean remains ) {
        super( world, x, y, z, 0, 0, 0 );
        motionX *= 0.3;
        motionY = Math.random() * 0.2 + 0.1;
        motionZ *= 0.3;
        setSize( 0.01F, 0.01F );
        particleGravity = 0.06F;
        maxAge = (int) ( 8 / ( remains ? Math.random() * 0.4 + 0.6 : Math.random() * 0.8 + 0.2 ) );

        particleRed = r;
        particleGreen = g;
        particleBlue = b;
        particleAlpha = 1;

        remain = remains;
    }

    @Override
    public void renderParticle( IVertexBuilder builder, ActiveRenderInfo info, float partialTicks ) {
        if( remain ) {
            posY += 0.05;
            prevPosY += 0.05;
        }
        super.renderParticle( builder, info, partialTicks );
        if( remain ) {
            posY -= 0.05;
            prevPosY -= 0.05;
        }
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
        if( maxAge-- <= 0 ) {
            setExpired();
        } else {
            motionY -= particleGravity;
            move( motionX, motionY, motionZ );
            motionX *= 0.98;
            motionY *= 0.98;
            motionZ *= 0.98;
            if( onGround ) {
                if( ! remain && Math.random() < 0.5 ) {
                    setExpired();
                }

                motionX *= 0.7;
                motionZ *= 0.7;
            }

            if( ! remain ) {
                BlockPos pos = new BlockPos( posX, posY, posZ );
                double h = Math.max(
                    world.getBlockState( pos ).getCollisionShape( world, pos ).max( Direction.Axis.Y, posX - pos.getX(), posZ - pos.getZ() ),
                    world.getFluidState( pos ).getActualHeight( world, pos )
                );
                if( h > 0 && posY < pos.getY() + h ) {
                    setExpired();
                }
            }
        }
    }

//    TODO Re-evaluate
//    @OnlyIn( Dist.CLIENT )
//    public static class DripFactory implements IParticleFactory<BasicParticleType> {
//        private final IAnimatedSprite sprite;
//
//        public DripFactory( IAnimatedSprite sprite ) {
//            this.sprite = sprite;
//        }
//
//        @Override
//        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
//            BlockPos pos = new BlockPos( x, y, z );
//            int color = ModernityClient.get().getWaterColors().getColor( world, pos );
//            PrecipitationParticle particle = new PrecipitationParticle( world, x, y, z, ColorUtil.redf( color ), ColorUtil.greenf( color ), ColorUtil.bluef( color ), false );
//            particle.selectSpriteRandomly( sprite );
//            return particle;
//        }
//    }
//
//    @OnlyIn( Dist.CLIENT )
//    public static class HailFactory implements IParticleFactory<BasicParticleType> {
//        private final IAnimatedSprite sprite;
//
//        public HailFactory( IAnimatedSprite sprite ) {
//            this.sprite = sprite;
//        }
//
//        @Override
//        public Particle makeParticle( BasicParticleType type, World world, double x, double y, double z, double xv, double yv, double zv ) {
//            PrecipitationParticle particle = new PrecipitationParticle( world, x, y, z, 1, 1, 1, true );
//            particle.selectSpriteRandomly( sprite );
//            return particle;
//        }
//    }
}
