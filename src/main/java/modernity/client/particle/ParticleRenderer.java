/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.particle;

import modernity.client.texture.ParticleSprite;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.MathHelper;

public class ParticleRenderer {
    public static void renderParticle( BufferBuilder buffer, double x, double y, double z, float width, float height, float rot, float u1, float v1, float u2, float v2, int lu, int lv, float r, float g, float b, float a ) {
        float rx = ActiveRenderInfo.getRotationX();
        float ryz = ActiveRenderInfo.getRotationZ();
        float rxy = ActiveRenderInfo.getRotationYZ();
        float rxz = ActiveRenderInfo.getRotationXY();
        float rz = ActiveRenderInfo.getRotationXZ();

        float vtx1x = - width;
        float vtx1y = - height;

        float vtx2x = - width;
        float vtx2y = height;

        float vtx3x = width;
        float vtx3y = height;

        float vtx4x = width;
        float vtx4y = - height;

        if( rot != 0 ) {
            float sin = MathHelper.sin( rot );
            float cos = MathHelper.cos( rot );

            float vtx1x1 = vtx1x * cos - vtx1y * sin;
            float vtx1y1 = vtx1y * cos + vtx1x * sin;
            vtx1x = vtx1x1;
            vtx1y = vtx1y1;

            float vtx2x1 = vtx2x * cos - vtx2y * sin;
            float vtx2y1 = vtx2y * cos + vtx2x * sin;
            vtx2x = vtx2x1;
            vtx2y = vtx2y1;

            float vtx3x1 = vtx3x * cos - vtx3y * sin;
            float vtx3y1 = vtx3y * cos + vtx3x * sin;
            vtx3x = vtx3x1;
            vtx3y = vtx3y1;

            float vtx4x1 = vtx4x * cos - vtx4y * sin;
            float vtx4y1 = vtx4y * cos + vtx4x * sin;
            vtx4x = vtx4x1;
            vtx4y = vtx4y1;
        }

        buffer.pos( x + rx * vtx1x + rxy * vtx1y, y + rz * vtx1y, z + ryz * vtx1x + rxz * vtx1y ).tex( u2, v2 ).color( r, g, b, a ).lightmap( lu, lv ).endVertex();
        buffer.pos( x + rx * vtx2x + rxy * vtx2y, y + rz * vtx2y, z + ryz * vtx2x + rxz * vtx2y ).tex( u2, v1 ).color( r, g, b, a ).lightmap( lu, lv ).endVertex();
        buffer.pos( x + rx * vtx3x + rxy * vtx3y, y + rz * vtx3y, z + ryz * vtx3x + rxz * vtx3y ).tex( u1, v1 ).color( r, g, b, a ).lightmap( lu, lv ).endVertex();
        buffer.pos( x + rx * vtx4x + rxy * vtx4y, y + rz * vtx4y, z + ryz * vtx4x + rxz * vtx4y ).tex( u1, v2 ).color( r, g, b, a ).lightmap( lu, lv ).endVertex();
    }


    public static void renderParticle( BufferBuilder buffer, double x, double y, double z, float rad, float rot, float u1, float v1, float u2, float v2, int lu, int lv, float r, float g, float b, float a ) {
        renderParticle( buffer, x, y, z, rad, rad, rot, u1, v1, u2, v2, lu, lv, r, g, b, a );
    }

    public static void renderParticle( BufferBuilder buffer, double x, double y, double z, float w, float h, float rot, ParticleSprite sprite, int lu, int lv, float r, float g, float b, float a ) {
        TextureAtlasSprite s = sprite.getSprite();
        renderParticle( buffer, x, y, z, w, h, rot, s.getMinU(), s.getMinV(), s.getMaxU(), s.getMaxV(), lu, lv, r, g, b, a );
    }

    public static void renderParticle( BufferBuilder buffer, double x, double y, double z, float rad, float rot, ParticleSprite sprite, int lu, int lv, float r, float g, float b, float a ) {
        renderParticle( buffer, x, y, z, rad, rad, rot, sprite, lu, lv, r, g, b, a );
    }

    public static void renderParticle( BufferBuilder buffer, double x, double y, double z, float rad, ParticleSprite sprite, int lu, int lv, float r, float g, float b, float a ) {
        renderParticle( buffer, x, y, z, rad, rad, 0, sprite, lu, lv, r, g, b, a );
    }

    public static void renderParticle( BufferBuilder buffer, double x, double y, double z, float rad, ParticleSprite sprite, int lu, int lv ) {
        renderParticle( buffer, x, y, z, rad, rad, 0, sprite, lu, lv, 1, 1, 1, 1 );
    }

    public static void renderParticle( BufferBuilder buffer, double x, double y, double z, float rad, float rot, ParticleSprite sprite, int lu, int lv ) {
        renderParticle( buffer, x, y, z, rad, rad, rot, sprite, lu, lv, 1, 1, 1, 1 );
    }
}
