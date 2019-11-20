/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.util;

import modernity.api.event.CheckEntityInWaterEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashSet;

/**
 * Handles all common coremod hooks.
 */
public final class Hooks {
    private static final HashSet<Biome> STRUCTURE_BIOMES = new HashSet<>();

    private Hooks() {
    }


    public static boolean checkInWater( Entity e, boolean inWater ) {
        CheckEntityInWaterEvent ev = new CheckEntityInWaterEvent( inWater, e );
        MinecraftForge.EVENT_BUS.post( ev );
        return ev.isInWater();
    }

    public static boolean mustSyncHeightmap( Heightmap.Type type ) {
        return true;
    }

    public static boolean isMotionBlockingNoLeaves( BlockState block ) {
        return ( block.getMaterial().blocksMovement() || ! block.getFluidState().isEmpty() ) && ! block.isIn( BlockTags.LEAVES );
    }
}
