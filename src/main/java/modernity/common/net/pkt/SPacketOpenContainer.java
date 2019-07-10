/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.common.net.pkt;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.client.util.ClientContainerManager;
import modernity.net.IPacket;
import modernity.net.ProcessContext;

public class SPacketOpenContainer implements IPacket {
    private int windowId;
    private String inventoryType;
    private ITextComponent windowTitle;
    private int slotCount;

    public SPacketOpenContainer() {
    }

    public SPacketOpenContainer( int windowId, String inventoryType, ITextComponent windowTitle ) {
        this( windowId, inventoryType, windowTitle, 0 );
    }

    public SPacketOpenContainer( int windowId, String inventoryType, ITextComponent windowTitle, int slotCount ) {
        this.windowId = windowId;
        this.inventoryType = inventoryType;
        this.windowTitle = windowTitle;
        this.slotCount = slotCount;
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeByte( this.windowId );
        buf.writeString( this.inventoryType );
        buf.writeTextComponent( this.windowTitle );
        buf.writeByte( this.slotCount );
    }

    @Override
    public void read( PacketBuffer buf ) {
        this.windowId = buf.readUnsignedByte();
        this.inventoryType = buf.readString( 32 );
        this.windowTitle = buf.readTextComponent();
        this.slotCount = buf.readUnsignedByte();
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.scheduleOnMainThread();
        ClientContainerManager.receivePacket0( this );
    }

    @OnlyIn( Dist.CLIENT )
    public int getWindowId() {
        return windowId;
    }

    @OnlyIn( Dist.CLIENT )
    public String getGuiId() {
        return inventoryType;
    }

    @OnlyIn( Dist.CLIENT )
    public ITextComponent getWindowTitle() {
        return windowTitle;
    }

    @OnlyIn( Dist.CLIENT )
    public int getSlotCount() {
        return slotCount;
    }

    @OnlyIn( Dist.CLIENT )
    public boolean hasSlots() {
        return slotCount > 0;
    }
}
