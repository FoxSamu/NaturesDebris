/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.particle;

import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ParticleEntry extends ForgeRegistryEntry<ParticleEntry> {
    public final ParticleType<?> type;

    public ParticleEntry( ParticleType<?> type ) {
        this.type = type;
        setRegistryName( type.getId() );
    }
}
