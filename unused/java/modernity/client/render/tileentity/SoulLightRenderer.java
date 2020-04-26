/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.client.render.tileentity;

import modernity.client.shaders.LightSource;
import modernity.client.shaders.ShaderManager;
import modernity.common.block.misc.SoulLightColor;
import modernity.common.tileentity.SoulLightTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.redgalaxy.util.MathUtil;

public class SoulLightRenderer extends TileEntityRenderer<SoulLightTileEntity> {

    @Override
    public void render( SoulLightTileEntity te, double x, double y, double z, float partialTicks, int destroyStage ) {
        BlockPos pos = te.getPos();
        float tx = pos.getX() + 0.5F;
        float ty = pos.getY() + 0.5F;
        float tz = pos.getZ() + 0.5F;
        float fade = 1;
        if( te.fades() ) {
            Vec3d projView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            float dx = tx - (float) projView.x;
            float dy = ty - (float) projView.y;
            float dz = tz - (float) projView.z;

            float distance = MathHelper.sqrt( dx * dx + dy * dy + dz * dz );

            if( distance < 10 ) {
                return;
            } else if( distance < 16 ) {
                fade = MathUtil.unlerp( 10, 16, distance );
            }
        }

        SoulLightColor col = te.getColor();
        ShaderManager.addLight( new LightSource(
            tx, ty, tz,
            col.red * 2,
            col.green * 2,
            col.blue * 2,
            fade,
            3,
            LightSource.LIGHT
        ) );

        te.markBeingRendered();
    }


}
