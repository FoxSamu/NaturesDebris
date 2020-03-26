/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.net;

import modernity.api.dimension.IEnvEventsDimension;
import modernity.common.environment.event.EnvironmentEventManager;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Packet that sends environment data to the client.
 */
public class SEnvironmentPacket implements Packet {

    private CompoundNBT nbt;

    public SEnvironmentPacket( EnvironmentEventManager manager ) {
        this.nbt = new CompoundNBT();
        manager.write( nbt );
    }

    public SEnvironmentPacket() {

    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeCompoundTag( nbt );
    }

    @Override
    public void read( PacketBuffer buf ) {
        nbt = buf.readCompoundTag();
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();

        World world = Minecraft.getInstance().world;
        if( world.dimension instanceof IEnvEventsDimension ) {
            EnvironmentEventManager mngr = ((IEnvEventsDimension) world.dimension).getEnvEventManager();
            mngr.read( nbt );
        }
    }
}
