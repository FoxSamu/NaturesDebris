/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import modernity.client.handler.OptionsButtonHandler;
import modernity.client.handler.TextureStitchHandler;
import modernity.client.handler.WorldListenerInjectionHandler;
import modernity.client.particle.MDParticleManager;
import modernity.client.render.block.MDBlockRendererDispatcher;
import modernity.common.block.MDBlocks;
import modernity.common.entity.MDEntityTypes;
import modernity.common.item.MDItems;
import modernity.common.util.ContainerManager;
import modernity.common.util.ProxyCommon;


@OnlyIn( Dist.CLIENT )
public class ProxyClient extends ProxyCommon {
    public final Minecraft mc = Minecraft.getInstance();

    @Override
    public void init() {
        super.init();

        MDEntityTypes.registerClient();
    }

    @Override
    public void loadComplete() {
        super.loadComplete();

        MDBlockRendererDispatcher dispatcher = new MDBlockRendererDispatcher( mc.getModelManager().getBlockModelShapes(), mc.getBlockColors() );
        ObfuscationReflectionHelper.setPrivateValue( Minecraft.class, mc, dispatcher, "field_175618_aM" );

        addResourceManagerReloadListener( dispatcher );


        mc.particles = new MDParticleManager( mc.world, mc.textureManager );

        MDBlocks.registerClient( mc.getBlockColors(), mc.getItemColors() );
        MDItems.registerClient( mc.getItemColors() );
    }

    @Override
    public boolean fancyGraphics() {
        return mc.gameSettings.fancyGraphics;
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        MinecraftForge.EVENT_BUS.register( new TextureStitchHandler() );
        MinecraftForge.EVENT_BUS.register( new WorldListenerInjectionHandler() );
        MinecraftForge.EVENT_BUS.register( new OptionsButtonHandler() );
    }

    @SuppressWarnings( "deprecation" )
    public void addResourceManagerReloadListener( IResourceManagerReloadListener listener ) {
        IResourceManager manager = mc.getResourceManager();
        if( manager instanceof IReloadableResourceManager ) {
            ( (IReloadableResourceManager) manager ).addReloadListener( listener );
        }
    }

    @Override
    public void openContainer( EntityPlayer player, IInventory inventory ) {
        if( player instanceof EntityPlayerSP ) {
            ContainerManager.openContainerSP( (EntityPlayerSP) player, inventory );
            return;
        }
        super.openContainer( player, inventory );
    }

    public Minecraft getMinecraft() {
        return mc;
    }

    @Override
    public IThreadListener getThreadListener() {
        return mc;
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    public static ProxyClient get() {
        return (ProxyClient) ProxyCommon.get();
    }
}
