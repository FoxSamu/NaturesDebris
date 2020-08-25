/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class BiomeGroups {
    private static final HashMap<String, Set<Biome>> GROUPS = new HashMap<>();

    private BiomeGroups() {
    }

    public static Set<String> getBiomeGroup(String loc) {
        return GROUPS.getOrDefault(loc, Collections.emptySet())
                     .stream()
                     .map(biome -> biome.getRegistryName() + "")
                     .collect(Collectors.toSet());
    }

    public static void registerBiomeToGroup(ResourceLocation loc, Biome biome) {
        GROUPS.computeIfAbsent(loc.toString(), key -> new HashSet<>()).add(biome);
    }

    public static void registerBiomeToGroup(String loc, Biome biome) {
        GROUPS.computeIfAbsent("modernity:" + loc, key -> new HashSet<>()).add(biome);
    }
}
