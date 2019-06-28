/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
 */

package modernity.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.fluid.Fluid;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.common.fluid.FluidEntry;
import modernity.common.registry.MDRegistries;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TextureStitchHandler {
    @SubscribeEvent
    public void onTextureStitch( TextureStitchEvent.Pre event ) {
        TextureMap map = event.getMap();
        IResourceManager manager = Minecraft.getInstance().getResourceManager();

        Set<Fluid> fluids = new HashSet<>( IRegistry.FLUID.stream().collect( Collectors.toList() ) );
        fluids.addAll( MDRegistries.fluids().getValues().stream().map( FluidEntry::getFluid ).collect( Collectors.toList() ) );

        for( Fluid f : fluids ) {
            System.out.println( "Injecting fluid textures into texture map for fluid " + IRegistry.FLUID.getKey( f ) + "..." );
            if( f instanceof ICustomRenderFluid ) {
                ICustomRenderFluid crf = (ICustomRenderFluid) f;

                map.registerSprite( manager, crf.getStill() );
                System.out.println( "Registered fluid texture: " + crf.getStill() );
                map.registerSprite( manager, crf.getFlowing() );
                System.out.println( "Registered fluid texture: " + crf.getFlowing() );

                if( crf.getOverlay() != null ) {
                    map.registerSprite( manager, crf.getOverlay() );
                    System.out.println( "Registered fluid texture: " + crf.getOverlay() );
                }
            }
        }
    }
}