/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.net;

import modernity.network.IPacket;
import modernity.network.ProcessContext;
import natures.debris.common.area.core.Area;
import natures.debris.common.area.core.ClientWorldAreaManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SAreaUpdatePacket implements IPacket {
    private Area area;
    private CompoundNBT nbt;
    private long refID;
    private DimensionType dimen;

    public SAreaUpdatePacket(Area area, IWorld world) {
        this.area = area;
        this.dimen = world.getDimension().getType();
    }

    public SAreaUpdatePacket() {
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeLong(area.getReferenceID());
        buf.writeCompoundTag(Area.serialize(area, Area.SerializeType.NETWORK));
        buf.writeInt(dimen.getId());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void read(PacketBuffer buf) {
        refID = buf.readLong();
        nbt = buf.readCompoundTag();
        dimen = DimensionType.getById(buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void process(ProcessContext ctx) {
        ctx.ensureMainThread();
        ClientWorldAreaManager.get().ifPresent(manager -> manager.receiveAreaUpdate(refID, nbt, dimen));
    }
}
