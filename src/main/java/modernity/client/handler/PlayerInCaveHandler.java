/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 13 - 2020
 * Author: rgsw
 */

package modernity.client.handler;

import modernity.client.environment.EnvironmentRenderingManager;
import modernity.common.util.CaveUtil;
import modernity.common.world.dimen.MurkSurfaceDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
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
                    int x0 = MathHelper.floor( mc.player.posX );
                    int z0 = MathHelper.floor( mc.player.posZ );
                    int x1 = x0 + 1;
                    int z1 = x0 + 1;

                    int h00 = CaveUtil.caveHeight( x0, z0, mc.world ) - 16;
                    int h01 = CaveUtil.caveHeight( x0, z1, mc.world ) - 16;
                    int h10 = CaveUtil.caveHeight( x1, z0, mc.world ) - 16;
                    int h11 = CaveUtil.caveHeight( x1, z1, mc.world ) - 16;

                    float h0 = MathUtil.lerp( h00, h01, (float) mc.player.posZ );
                    float h1 = MathUtil.lerp( h10, h11, (float) mc.player.posZ );
                    float h = MathUtil.lerp( h0, h1, (float) mc.player.posX );

                    float off = (float) mc.player.getEyePosition( 0 ).y - h;

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
