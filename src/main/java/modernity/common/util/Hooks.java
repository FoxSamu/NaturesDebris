/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.common.util;

import net.minecraft.world.biome.Biome;

public class Hooks {
    public static void registerCustomBiomeStructures( Biome biome ) {
        System.out.println( "Injecting biome structures!!!!!!!!! For biome: " + biome.getRegistryName() );
    }
}
