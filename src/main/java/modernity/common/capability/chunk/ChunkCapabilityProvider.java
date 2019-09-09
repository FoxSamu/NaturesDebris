/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.common.capability.chunk;

import modernity.api.capability.MDCapabilities;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkCapabilityProvider implements ICapabilityProvider {

    private final ChunkCapability cap;

    public ChunkCapabilityProvider( Chunk chunk ) {
        this.cap = new ChunkCapability( chunk );
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable EnumFacing side ) {
        if( side != null ) return LazyOptional.empty();
        if( cap == MDCapabilities.chunkCapability ) {
            return LazyOptional.of( () -> (T) this.cap );
        }
        return LazyOptional.empty();
    }
}
