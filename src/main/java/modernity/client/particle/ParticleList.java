/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayDeque;
import java.util.ArrayList;

public abstract class ParticleList {
    private final ArrayDeque<Particle> queued = new ArrayDeque<>();
    private final ArrayDeque<Particle> dead = new ArrayDeque<>();
    private final ArrayList<Particle> particles = new ArrayList<>();

    private final ResourceLocation particleAtlas;

    public ParticleList( ResourceLocation particleAtlas ) {
        this.particleAtlas = particleAtlas;
    }

    public void tick() {
        while( ! queued.isEmpty() ) {
            particles.add( queued.remove() );
        }

        for( Particle particle : particles ) {
            particle.tick();
            if( ! particle.isAlive() ) {
                dead.push( particle );
            }
        }

        while( ! dead.isEmpty() ) {
            particles.remove( dead.remove() );
        }
    }

    public void clear() {
        queued.clear();
        dead.clear();
        particles.clear();
    }

    public void render( Entity e, float partialTicks ) {
        // MCP Sucks....
        float rx = ActiveRenderInfo.getRotationX();
        float ryz = ActiveRenderInfo.getRotationZ();
        float rxy = ActiveRenderInfo.getRotationYZ();
        float rxz = ActiveRenderInfo.getRotationXY();
        float rz = ActiveRenderInfo.getRotationXZ();

        Particle.interpPosX = e.lastTickPosX + ( e.posX - e.lastTickPosX ) * (double) partialTicks;
        Particle.interpPosY = e.lastTickPosY + ( e.posY - e.lastTickPosY ) * (double) partialTicks;
        Particle.interpPosZ = e.lastTickPosZ + ( e.posZ - e.lastTickPosZ ) * (double) partialTicks;
        Particle.cameraViewDir = e.getLook( partialTicks );

        beforeRender();
        Minecraft.getInstance().textureManager.bindTexture( particleAtlas );
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        buff.begin( 7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP );

        for( Particle particle : particles ) {
            particle.renderParticle( buff, e, partialTicks, rx, rz, ryz, rxy, rxz );
        }
        tess.draw();
        afterRender();
    }

    public void addParticle( Particle particle ) {
        queued.add( particle );
    }

    public abstract void beforeRender();

    public abstract void afterRender();
}
