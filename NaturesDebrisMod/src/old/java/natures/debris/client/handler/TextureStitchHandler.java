/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.handler;

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
