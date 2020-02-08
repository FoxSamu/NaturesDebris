/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
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
