/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;

import modernity.Modernity;
import modernity.common.net.SPacketOpenContainer;

public class ContainerManager {
    public static void openContainer( EntityPlayer player, IInventory inventory ) {
        Modernity.proxy.openContainer( player, inventory );
    }

    @SuppressWarnings( "ConstantConditions" )
    public static void openContainerMP( EntityPlayerMP player, IInventory inventory ) {
        if( inventory instanceof ILootContainer && ( (ILootContainer) inventory ).getLootTable() != null && player.isSpectator() ) {
            player.sendStatusMessage( new TextComponentTranslation( "container.spectatorCantOpen" ).applyTextStyle( TextFormatting.RED ), true );
        } else {
            if( player.openContainer != player.inventoryContainer ) {
                player.closeScreen();
            }

            if( inventory instanceof ILockableContainer ) {
                ILockableContainer lc = (ILockableContainer) inventory;
                if( lc.isLocked() && ! player.canOpen( lc.getLockCode() ) && ! player.isSpectator() ) {
                    player.connection.sendPacket( new SPacketChat( new TextComponentTranslation( "container.isLocked", inventory.getDisplayName() ), ChatType.GAME_INFO ) );
                    player.connection.sendPacket( new SPacketSoundEffect( SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, player.posX, player.posY, player.posZ, 1, 1 ) );
                    return;
                }
            }

            player.getNextWindowId();
            if( inventory instanceof IInteractionObject ) {
                player.connection.sendPacket( new SPacketOpenContainer( player.currentWindowId, ( (IInteractionObject) inventory ).getGuiID(), inventory.getDisplayName(), inventory.getSizeInventory() ) );
                player.openContainer = ( (IInteractionObject) inventory ).createContainer( player.inventory, player );

                player.openContainer.windowId = player.currentWindowId;
                player.openContainer.addListener( player );
                MinecraftForge.EVENT_BUS.post( new PlayerContainerEvent.Open( player, player.openContainer ) );
            }
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static void openContainerSP( EntityPlayerSP player, IInventory inventory ) {

    }
}
