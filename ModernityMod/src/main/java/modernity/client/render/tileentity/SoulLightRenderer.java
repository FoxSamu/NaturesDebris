/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.render.tileentity;

// TODO Re-evaluate
//public class SoulLightRenderer extends TileEntityRenderer<SoulLightTileEntity> {
//
//    @Override
//    public void render( SoulLightTileEntity te, double x, double y, double z, float partialTicks, int destroyStage ) {
//        BlockPos pos = te.getPos();
//        float tx = pos.getX() + 0.5F;
//        float ty = pos.getY() + 0.5F;
//        float tz = pos.getZ() + 0.5F;
//        float fade = 1;
//        if( te.fades() ) {
//            Vec3d projView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
//            float dx = tx - (float) projView.x;
//            float dy = ty - (float) projView.y;
//            float dz = tz - (float) projView.z;
//
//            float distance = MathHelper.sqrt( dx * dx + dy * dy + dz * dz );
//
//            if( distance < 10 ) {
//                return;
//            } else if( distance < 16 ) {
//                fade = MathUtil.unlerp( 10, 16, distance );
//            }
//        }
//
//        SoulLightColor col = te.getColor();
//        ShaderManager.addLight( new LightSource(
//            tx, ty, tz,
//            col.red * 2,
//            col.green * 2,
//            col.blue * 2,
//            fade,
//            3,
//            LightSource.LIGHT
//        ) );
//
//        te.markBeingRendered();
//    }
//
//
//}
