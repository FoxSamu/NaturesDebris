/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.event;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A block event that takes an integer as data.
 */
public abstract class IntBlockEvent extends BlockEvent<Integer> {

    protected IntBlockEvent( int range ) {
        super( range );
    }

    protected IntBlockEvent() {
        super();
    }

    @OnlyIn( Dist.CLIENT )
    public abstract void playEvent( World world, BlockPos pos, int data );

    @Override
    @OnlyIn( Dist.CLIENT )
    public final void playEvent( World world, BlockPos pos, Integer data ) {
        playEvent( world, pos, data.intValue() );
    }

    @Override
    public final void writeData( Integer data, PacketBuffer buffer ) {
        buffer.writeInt( data );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public final Integer readData( PacketBuffer buffer ) {
        return buffer.readInt();
    }
}
