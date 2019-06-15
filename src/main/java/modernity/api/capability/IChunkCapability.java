/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.api.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;

public interface IChunkCapability {
    void save( NBTTagCompound cpd );
    void load( NBTTagCompound cpd );

    @SuppressWarnings( "ConstantConditions" )
    static IChunkCapability get( Chunk chunk ) {
        return chunk.getCapability( MDCapabilities.chunkCapability ).orElse( null );
    }
}
