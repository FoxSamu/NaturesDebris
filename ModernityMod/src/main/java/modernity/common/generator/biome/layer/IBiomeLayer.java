/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.biome.layer;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public interface IBiomeLayer {
    default int id( Biome biome ) {
        return ( (ForgeRegistry<Biome>) ForgeRegistries.BIOMES ).getID( biome );
    }

    default Biome biome( int id ) {
        return ( (ForgeRegistry<Biome>) ForgeRegistries.BIOMES ).getValue( id );
    }
}
