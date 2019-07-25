/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.particle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class NoDepthParticleList extends ParticleList {
    public NoDepthParticleList( ResourceLocation particleAtlas ) {
        super( particleAtlas );
    }

    @Override
    public void beforeRender() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA );
        GlStateManager.alphaFunc( 516, 0.003921569F );
        GlStateManager.depthMask( false );
    }

    @Override
    public void afterRender() {
        GlStateManager.depthMask( true );
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc( 516, 0.1F );
    }
}
