/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.generic.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MDCapabilities {
    @CapabilityInject( IChunkCapability.class )
    public static Capability<IChunkCapability> chunkCapability;
}
