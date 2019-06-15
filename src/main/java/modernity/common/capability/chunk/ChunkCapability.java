/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.common.capability.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;

import modernity.api.capability.IChunkCapability;

public class ChunkCapability implements IChunkCapability {
    private final Chunk chunk;

    public ChunkCapability( Chunk chunk ) {
        this.chunk = chunk;
    }

    @Override
    public void save( NBTTagCompound cpd ) {
    }

    @Override
    public void load( NBTTagCompound cpd ) {
    }
}
