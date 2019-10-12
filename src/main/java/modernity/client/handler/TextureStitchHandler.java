/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.handler;

import modernity.api.block.fluid.ICustomRenderFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@OnlyIn( Dist.CLIENT )
public enum TextureStitchHandler {
    INSTANCE;

    @SubscribeEvent
    public void onTextureStitch( TextureStitchEvent.Pre event ) {
        AtlasTexture map = event.getMap();

        // Only stitch fluid textures on default texture map
        if( map != Minecraft.getInstance().getTextureMap() ) return;

        Set<Fluid> fluids = new HashSet<>( ForgeRegistries.FLUIDS.getValues() );

        for( Fluid f : fluids ) {
            if( f instanceof ICustomRenderFluid ) {
                ICustomRenderFluid crf = (ICustomRenderFluid) f;

                event.addSprite( crf.getStill() );
                event.addSprite( crf.getFlowing() );

                if( crf.getOverlay() != null ) {
                    event.addSprite( crf.getOverlay() );
                }
            }
        }
    }
}
