/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.chunks.core;

import modernity.generic.util.BMFRegionCacher;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class OverlayChunkIO extends BMFRegionCacher {
    public static final Logger LOGGER = LogManager.getLogger();
    private final Function<ChunkPos, OverlayChunk> factory;

    public OverlayChunkIO(File folder, Function<ChunkPos, OverlayChunk> factory) {
        super(folder);
        this.factory = factory;
    }

    public void saveChunk(OverlayChunk chunk) {
        try {
            int x = chunk.getX() >> 5;
            int z = chunk.getZ() >> 5;
            long key = chunk.getPos().asLong();

            saveNBTIfNotEmpty(x, z, key, writeChunk(chunk));
        } catch(IOException exc) {
            LOGGER.error("Unable to save overlay chunk at " + chunk.getPos(), exc);
        }
    }

    private CompoundNBT writeChunk(OverlayChunk chunk) {
        CompoundNBT nbt = new CompoundNBT();
        chunk.writeToNBT(nbt);
        return nbt;
    }

    public OverlayChunk loadChunk(ChunkPos pos) {
        try {
            int x = pos.x >> 5;
            int z = pos.z >> 5;
            long key = pos.asLong();

            CompoundNBT nbt = loadNBT(x, z, key);

            OverlayChunk chunk = factory.apply(pos);
            if(nbt != null) {
                chunk.readFromNBT(nbt);
            }

            return chunk;
        } catch(IOException exc) {
            LOGGER.error("Unable to load overlay chunk at " + pos, exc);
            return null;
        }
    }
}
