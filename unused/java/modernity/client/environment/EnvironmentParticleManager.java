/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.client.environment;

import modernity.generic.util.MovingBlockPos;
import modernity.client.environment.particles.CaveAmbientEffect;
import modernity.client.environment.particles.IEnvironmentParticleEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Random;

public class EnvironmentParticleManager {
    // TODO: Settings for these values
    private static final int RADIUS = 32;
    private static final int ITERATIONS = 667;

    private final Random rand = new Random();
    private final Minecraft mc = Minecraft.getInstance();

    private final HashSet<IEnvironmentParticleEffect> particleEffects = new HashSet<>();

    public EnvironmentParticleManager() {
        registerEffect( new CaveAmbientEffect() );
    }

    private int computeRadius() {
        return RADIUS / ( mc.gameSettings.fancyGraphics ? 1 : 2 );
    }

    private int computeIterations() {
        return ITERATIONS / ( mc.gameSettings.fancyGraphics ? 1 : 8 );
    }

    public void tick() {
        World world = mc.world;
        if( world != null && ! mc.isGamePaused() ) {
            int rad = computeRadius();
            int itr = computeIterations();

            ActiveRenderInfo info = mc.gameRenderer.getActiveRenderInfo();
            Vec3d pv = info.getProjectedView();
            BlockPos pos = new BlockPos( pv );

            MovingBlockPos mpos = new MovingBlockPos();

            for( int i = 0; i < itr; i++ ) {
                particleTick( pos.getX(), pos.getY(), pos.getZ(), rad / 2, mpos );
                particleTick( pos.getX(), pos.getY(), pos.getZ(), rad, mpos );
            }
        }
    }

    private void particleTick( int x, int y, int z, int offset, MovingBlockPos mpos ) {
        int xp = x + rand.nextInt( offset ) - rand.nextInt( offset );
        int yp = y + rand.nextInt( offset ) - rand.nextInt( offset );
        int zp = z + rand.nextInt( offset ) - rand.nextInt( offset );
        mpos.setPos( xp, yp, zp );

        particleTick( mpos );
    }

    private void particleTick( BlockPos pos ) {
        for( IEnvironmentParticleEffect effect : particleEffects ) {
            effect.addParticleEffect( mc.world, pos, rand );
        }
    }

    public void registerEffect( IEnvironmentParticleEffect effect ) {
        particleEffects.add( effect );
    }
}
