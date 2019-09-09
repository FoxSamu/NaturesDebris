/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.util;

import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.api.particle.ICustomParticle;
import modernity.client.particle.ParticleDiggingModernity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Random;

@OnlyIn( Dist.CLIENT )
public class ClientHooks {
    public static void preRenderShaders( float partialTicks ) {

    }

    public static void preRenderParticles( Entity e, float partialTicks ) {
        ProxyClient.get().renderParticles( e, partialTicks );
    }

    public static Particle addParticle( Particle particle ) {
        if( particle instanceof ICustomParticle ) {
            if( particle.shouldDisableDepth() ) {
                ProxyClient.get().getNoDepthParticleList().addParticle( particle );
            } else {
                ProxyClient.get().getDepthParticleList().addParticle( particle );
            }

            // Stop particle from being adding to main particle list: return null particle...
            return null;
        }
        if( particle instanceof ParticleDigging ) {
            // Copy particle properties using reflection, they're all private or protected
            ParticleDiggingModernity p1 = new ParticleDiggingModernity(
                    ObfuscationReflectionHelper.getPrivateValue( Particle.class, particle, "field_187122_b" ), // world
                    ObfuscationReflectionHelper.getPrivateValue( Particle.class, particle, "field_187126_f" ), // posX
                    ObfuscationReflectionHelper.getPrivateValue( Particle.class, particle, "field_187127_g" ), // posY
                    ObfuscationReflectionHelper.getPrivateValue( Particle.class, particle, "field_187128_h" ), // posZ
                    ObfuscationReflectionHelper.getPrivateValue( Particle.class, particle, "field_187129_i" ), // motionX
                    ObfuscationReflectionHelper.getPrivateValue( Particle.class, particle, "field_187130_j" ), // motionY
                    ObfuscationReflectionHelper.getPrivateValue( Particle.class, particle, "field_187131_k" ), // motionZ
                    ObfuscationReflectionHelper.getPrivateValue( ParticleDigging.class, (ParticleDigging) particle, "field_174847_a" ) // sourceState
            );
            // Special case...
            p1.setBlockPos( ObfuscationReflectionHelper.getPrivateValue( ParticleDigging.class, (ParticleDigging) particle, "field_181019_az" ) ); // sourcePos
            return p1;
        }
        return particle;
    }

    // NOTE: Unused, but will be used to temporarily render custom block models as long as forge hasn't implemented them.
    public static Boolean onRenderBlock( BlockRendererDispatcher blockRenderer, IBlockState state, BlockPos pos, IWorldReader world, BufferBuilder buff, Random rand, IModelData modelData ) {
        // Return null to pass render action
        return null;
    }

    public static Boolean onRenderFluid( BlockRendererDispatcher blockRenderer, BlockPos pos, IWorldReader reader, BufferBuilder buff, IFluidState state ) {
        if( state.getFluid() instanceof ICustomRenderFluid || state.getFluid() instanceof WaterFluid || state.getFluid() instanceof LavaFluid ) {
            return ProxyClient.get().getFluidRenderer().render( reader, pos, buff, state );
        }
        // Return null to pass render action
        return null;
    }
}
