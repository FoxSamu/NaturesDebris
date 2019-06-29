/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import modernity.common.command.MDCommands;
import modernity.common.handler.CapabilityHandler;
import modernity.common.handler.StructureSyncHandler;
import modernity.common.net.MDPackets;
import modernity.common.world.dim.MDDimensions;
import modernity.common.world.gen.structure.MDStructures;

public class ProxyCommon {

    public void init() {
        MDStructures.register();
        MDPackets.register();
    }

    public void loadComplete() {

    }

    public void registerListeners() {
        MinecraftForge.EVENT_BUS.register( new CapabilityHandler() );
        MinecraftForge.EVENT_BUS.register( new StructureSyncHandler() );
    }

    public boolean fancyGraphics() {
        return false;
    }

    @SubscribeEvent
    public void serverStart( FMLServerStartingEvent event ) {
        MDCommands.register( event.getCommandDispatcher() );
    }

    @SubscribeEvent
    public void onRegisterDimensions( RegisterDimensionsEvent event ) {
        MDDimensions.init( event.getMissingNames() );
    }

    public void openContainer( EntityPlayer player, IInventory inventory ) {
        if( player instanceof EntityPlayerMP ) {
            ContainerManager.openContainerMP( (EntityPlayerMP) player, inventory );
        }
    }
}
