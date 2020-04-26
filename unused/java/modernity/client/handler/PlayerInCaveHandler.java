/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 17 - 2020
 * Author: rgsw
 */

package modernity.client.handler;

import modernity.client.environment.EnvironmentRenderingManager;
import modernity.common.util.CaveUtil;
import modernity.common.world.dimen.MurkSurfaceDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.redgalaxy.util.MathUtil;

public enum PlayerInCaveHandler {
    INSTANCE;

    @SubscribeEvent
    public void onRenderTick( TickEvent.RenderTickEvent event ) {
        if( event.phase == TickEvent.Phase.START ) {
            Minecraft mc = Minecraft.getInstance();

            if( mc.world != null && mc.player != null ) {
                if( mc.world.dimension instanceof MurkSurfaceDimension ) {
                    Vec3d eyes = mc.player.getEyePosition( event.renderTickTime );
                    int x0 = MathHelper.floor( eyes.x );
                    int z0 = MathHelper.floor( eyes.z );
                    int x1 = x0 + 1;
                    int z1 = z0 + 1;

                    int h00 = CaveUtil.caveHeight( x0, z0, mc.world );
                    int h01 = CaveUtil.caveHeight( x0, z1, mc.world );
                    int h10 = CaveUtil.caveHeight( x1, z0, mc.world );
                    int h11 = CaveUtil.caveHeight( x1, z1, mc.world );

                    float h0 = MathUtil.lerp( h00, h01, MathUtil.positiveModulo( (float) eyes.z, 1 ) );
                    float h1 = MathUtil.lerp( h10, h11, MathUtil.positiveModulo( (float) eyes.z, 1 ) );
                    float h = MathUtil.lerp( h0, h1, MathUtil.positiveModulo( (float) eyes.x, 1 ) );

                    float off = (float) eyes.y - h;

                    float caveFac = 0;

                    if( off < - 8 ) {
                        caveFac = 1;
                    } else if( off < 8 ) {
                        caveFac = MathUtil.unlerp( 8, - 8, off );
                    }

                    EnvironmentRenderingManager.setCaveFactor( caveFac );
                }
            }
        }
    }
}
