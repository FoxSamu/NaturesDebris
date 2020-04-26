/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.net;

import modernity.common.area.core.Area;
import modernity.common.area.core.ClientWorldAreaManager;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SAreaUpdatePacket implements Packet {
    private Area area;
    private CompoundNBT nbt;
    private long refID;
    private DimensionType dimen;

    public SAreaUpdatePacket( Area area, IWorld world ) {
        this.area = area;
        this.dimen = world.getDimension().getType();
    }

    public SAreaUpdatePacket() {
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeLong( area.getReferenceID() );
        buf.writeCompoundTag( Area.serialize( area, Area.SerializeType.NETWORK ) );
        buf.writeInt( dimen.getId() );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void read( PacketBuffer buf ) {
        refID = buf.readLong();
        nbt = buf.readCompoundTag();
        dimen = DimensionType.getById( buf.readInt() );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        ClientWorldAreaManager.get().ifPresent( manager -> manager.receiveAreaUpdate( refID, nbt, dimen ) );
    }
}
