package modernity.client.render.area.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import modernity.client.render.area.IAreaRenderer;
import modernity.common.area.ForestRunesArea;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ForestRunesAreaRender implements IAreaRenderer<ForestRunesArea> {
    @Override
    public void render( World world, ForestRunesArea area, double x, double y, double z, float partialTicks ) {
        if( area.isActive() ) {
            GlStateManager.shadeModel( GL11.GL_SMOOTH );
            GlStateManager.pushMatrix();
            GlStateManager.translated( x, y, z );
            GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GlStateManager.disableAlphaTest();
            GlStateManager.disableTexture();
            GlStateManager.enableColorMaterial();
            GlStateManager.disableLighting();
            GlStateManager.depthMask( false );
            GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE );

            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buff = tess.getBuffer();

            buff.begin( 7, DefaultVertexFormats.POSITION_COLOR );
            // A small offset (0.001) to prevent z-fighting with near blocks
            buff.pos( 5.001, 2, 5.001 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 6.999, 2, 5.001 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 6.999, 5, 5.001 ).color( 255, 162, 0, 0 ).endVertex();
            buff.pos( 5.001, 5, 5.001 ).color( 255, 162, 0, 0 ).endVertex();

            buff.pos( 5.001, 2, 6.999 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 6.999, 2, 6.999 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 6.999, 5, 6.999 ).color( 255, 162, 0, 0 ).endVertex();
            buff.pos( 5.001, 5, 6.999 ).color( 255, 162, 0, 0 ).endVertex();

            buff.pos( 5.001, 2, 5.001 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 5.001, 2, 6.999 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 5.001, 5, 6.999 ).color( 255, 162, 0, 0 ).endVertex();
            buff.pos( 5.001, 5, 5.001 ).color( 255, 162, 0, 0 ).endVertex();

            buff.pos( 6.999, 2, 5.001 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 6.999, 2, 6.999 ).color( 255, 162, 0, 200 ).endVertex();
            buff.pos( 6.999, 5, 6.999 ).color( 255, 162, 0, 0 ).endVertex();
            buff.pos( 6.999, 5, 5.001 ).color( 255, 162, 0, 0 ).endVertex();

            for( double j = 0; j < 1; j += 0.1 ) {
                buff.pos( 5, 2.001 + j * 0.9, 5 ).color( 255, 162, 0, (int) ( 255 * ( 1 - j ) ) ).endVertex();
                buff.pos( 5, 2.001 + j * 0.9, 7 ).color( 255, 162, 0, (int) ( 255 * ( 1 - j ) ) ).endVertex();
                buff.pos( 7, 2.001 + j * 0.9, 7 ).color( 255, 162, 0, (int) ( 255 * ( 1 - j ) ) ).endVertex();
                buff.pos( 7, 2.001 + j * 0.9, 5 ).color( 255, 162, 0, (int) ( 255 * ( 1 - j ) ) ).endVertex();
            }

            tess.draw();

            GlStateManager.blendFuncSeparate( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE );
            GlStateManager.enableTexture();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.enableAlphaTest();
            GlStateManager.depthMask( true );
        }
    }
}
