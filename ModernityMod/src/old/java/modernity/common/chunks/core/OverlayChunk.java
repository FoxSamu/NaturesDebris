/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.chunks.core;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;

public class OverlayChunk {
    private final ChunkPos pos;

    private boolean dirty;

    protected OverlayChunk(ChunkPos pos) {
        this.pos = pos;
    }

    public final ChunkPos getPos() {
        return pos;
    }

    public final int getX() {
        return pos.x;
    }

    public final int getZ() {
        return pos.z;
    }

    public final boolean isDirty() {
        return dirty;
    }

    public final void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public final void markDirty() {
        dirty = true;
    }

    public void writeToNBT(CompoundNBT nbt) {
    }

    public void readFromNBT(CompoundNBT nbt) {
    }

    public void writeToPacket(PacketBuffer buf) {
    }

    public void readFromPacket(PacketBuffer buf) {
    }
}
