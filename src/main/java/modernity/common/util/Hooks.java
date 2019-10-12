/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 8 - 2019
 */

package modernity.common.util;

import modernity.api.event.CheckEntityInWaterEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashSet;

public class Hooks {
    private static final HashSet<Biome> STRUCTURE_BIOMES = new HashSet<>();


    public static boolean checkInWater( Entity e, boolean inWater ) {
        CheckEntityInWaterEvent ev = new CheckEntityInWaterEvent( inWater, e );
        MinecraftForge.EVENT_BUS.post( ev );
        return ev.isInWater();
    }
}
