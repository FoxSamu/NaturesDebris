/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.net;

import modernity.client.util.ClientContainerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class SPacketOpenContainer implements Packet<INetHandlerPlayClient> {
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

    public void processPacket( INetHandlerPlayClient handler ) {
        PacketThreadUtil.checkThreadAndEnqueue( this, handler, Minecraft.getInstance() );
        ClientContainerManager.receivePacket( this );
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData( PacketBuffer buf ) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.inventoryType = buf.readString( 32 );
        this.windowTitle = buf.readTextComponent();
        this.slotCount = buf.readUnsignedByte();

    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData( PacketBuffer buf ) throws IOException {
        buf.writeByte( this.windowId );
        buf.writeString( this.inventoryType );
        buf.writeTextComponent( this.windowTitle );
        buf.writeByte( this.slotCount );

    }

    @OnlyIn( Dist.CLIENT )
    public int getWindowId() {
        return this.windowId;
    }

    @OnlyIn( Dist.CLIENT )
    public String getGuiId() {
        return this.inventoryType;
    }

    @OnlyIn( Dist.CLIENT )
    public ITextComponent getWindowTitle() {
        return this.windowTitle;
    }

    @OnlyIn( Dist.CLIENT )
    public int getSlotCount() {
        return this.slotCount;
    }

    @OnlyIn( Dist.CLIENT )
    public boolean hasSlots() {
        return this.slotCount > 0;
    }
}