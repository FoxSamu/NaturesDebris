/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.client.render.area;

import com.mojang.blaze3d.platform.GlStateManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import modernity.client.ModernityClient;
import modernity.common.area.core.Area;
import modernity.common.area.core.AreaBox;
import modernity.common.area.core.AreaType;
import modernity.common.area.core.ClientWorldAreaManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;


public class AreaRenderManager {
    private final Object2ObjectLinkedOpenHashMap<AreaType<? extends Area>, IAreaRenderer<?>> renderers = new Object2ObjectLinkedOpenHashMap<>();

    @SuppressWarnings( "unchecked" )
    public <T extends Area> IAreaRenderer<T> getRendererFor( T area ) {
        return (IAreaRenderer<T>) renderers.get( area.getType() );
    }

    @SuppressWarnings( "unchecked" )
    public <T extends Area> IAreaRenderer<T> getRendererFor( AreaType<? extends T> area ) {
        return (IAreaRenderer<T>) renderers.get( area );
    }

    public <T extends Area> void register( AreaType<? extends T> area, IAreaRenderer<T> renderer ) {
        renderers.put( area, renderer );
    }

    @SuppressWarnings( "unchecked" )
    public void renderAreas( float partialTicks ) {
        ClientWorldAreaManager manager = ModernityClient.get().getWorldAreaManager();
        if( manager == null ) return;
        Minecraft mc = Minecraft.getInstance();
        ActiveRenderInfo info = mc.gameRenderer.getActiveRenderInfo();
        double x = info.getProjectedView().x;
        double y = info.getProjectedView().y;
        double z = info.getProjectedView().z;
        manager.streamAreas().forEach( area -> {
            IAreaRenderer renderer = getRendererFor( area );
            if( renderer == null ) return;
            Vec3i v = area.getBox().getMin();
            renderer.render( mc.world, area, v.getX() - x, v.getY() - y, v.getZ() - z, partialTicks );
            drawAreaBox( area.getBox(), v.getX() - x, v.getY() - y, v.getZ() - z );
        } );
    }

    private void drawAreaBox( AreaBox box, double x, double y, double z ) {
        // Draw area bounding boxes when the 'Show bounding boxes' debug option is enabled
        // MAYBE: Link this to our own debug tools?
        if( Minecraft.getInstance().getRenderManager().isDebugBoundingBox() ) {
            GlStateManager.depthMask( false );
            GlStateManager.disableTexture();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.shadeModel( GL11.GL_FLAT );
            GlStateManager.pushMatrix();
            GlStateManager.translated( x, y, z );
            WorldRenderer.drawBoundingBox( 0, 0, 0, box.getXSize(), box.getYSize(), box.getZSize(), 0, 1, 1, 1 );
            GlStateManager.popMatrix();
            GlStateManager.enableTexture();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.enableBlend();
            GlStateManager.depthMask( true );
        }
    }
}
