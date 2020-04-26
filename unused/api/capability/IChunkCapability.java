/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.generic.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.Chunk;

public interface IChunkCapability {
    void save( CompoundNBT cpd );
    void load( CompoundNBT cpd );

    @SuppressWarnings( "ConstantConditions" )
    static IChunkCapability get( Chunk chunk ) {
        return chunk.getCapability( MDCapabilities.chunkCapability ).orElse( null );
    }
}
