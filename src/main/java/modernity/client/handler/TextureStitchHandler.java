/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
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

// TODO Re-evaluate
///**
// * Handles texture stitching, and forces the texture map to stitch the textures of all {@link ICustomRenderFluid}s.
// */
//@OnlyIn( Dist.CLIENT )
//public enum TextureStitchHandler {
//    INSTANCE;
//
//    @SubscribeEvent
//    public void onTextureStitch( TextureStitchEvent.Pre event ) {
//        AtlasTexture map = event.getMap();
//
//        // Only stitch fluid textures on default texture map
//        if( map != Minecraft.getInstance().getTextureMap() ) return;
//
//        Set<Fluid> fluids = new HashSet<>( ForgeRegistries.FLUIDS.getValues() );
//
//        for( Fluid f : fluids ) {
//            if( f instanceof ICustomRenderFluid ) {
//                ICustomRenderFluid crf = (ICustomRenderFluid) f;
//
//                event.addSprite( crf.getStill() );
//                event.addSprite( crf.getFlowing() );
//
//                if( crf.getOverlay() != null ) {
//                    event.addSprite( crf.getOverlay() );
//                }
//            }
//        }
//    }
//}
