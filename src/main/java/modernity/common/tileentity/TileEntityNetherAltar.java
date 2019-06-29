/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import modernity.common.container.NetherAltarContainer;

import javax.annotation.Nullable;

public class TileEntityNetherAltar extends TileEntityContainer {
    private static final ITextComponent NAME = new TextComponentTranslation( Util.makeTranslationKey( "gui", new ResourceLocation( "modernity:nether_altar" ) ) );

    public TileEntityNetherAltar() {
        super( MDTileEntities.NETHER_ALTAR );
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return write( new NBTTagCompound() );
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity( pos, 0, getUpdateTag() );
    }

    @Override
    public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity pkt ) {
        read( pkt.getNbtCompound() );
    }

    @Override
    public int getSizeInventory() {
        return 5;
    }

    @Override
    public Container createContainer( InventoryPlayer playerInventory, EntityPlayer player ) {
        return new NetherAltarContainer( playerInventory, this );
    }

    @Override
    public String getGuiID() {
        return "modernity:nether_altar";
    }

    @Override
    public ITextComponent getName() {
        return NAME;
    }
}
