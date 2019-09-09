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
import modernity.common.fluid.FluidEntry;
import modernity.common.registry.MDRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.fluid.Fluid;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@OnlyIn( Dist.CLIENT )
public class TextureStitchHandler {
    @SubscribeEvent
    public void onTextureStitch( TextureStitchEvent.Pre event ) {
        TextureMap map = event.getMap();

        // Only stitch fluid textures on default texture map
        if( map != Minecraft.getInstance().getTextureMap() ) return;

        IResourceManager manager = Minecraft.getInstance().getResourceManager();

        Set<Fluid> fluids = new HashSet<>( IRegistry.FLUID.stream().collect( Collectors.toList() ) );
        fluids.addAll( MDRegistries.fluids().getValues().stream().map( FluidEntry::getFluid ).collect( Collectors.toList() ) );

        for( Fluid f : fluids ) {
            if( f instanceof ICustomRenderFluid ) {
                ICustomRenderFluid crf = (ICustomRenderFluid) f;

                map.registerSprite( manager, crf.getStill() );
                map.registerSprite( manager, crf.getFlowing() );

                if( crf.getOverlay() != null ) {
                    map.registerSprite( manager, crf.getOverlay() );
                }
            }
        }
    }
}
