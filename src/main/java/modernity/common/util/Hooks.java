/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.util;

import modernity.api.dimension.IPrecipitationDimension;
import modernity.api.event.CheckEntityInWaterEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;

/**
 * Handles all common coremod hooks.
 */
public final class Hooks {

    private Hooks() {
    }


    public static boolean checkInWater( Entity e, boolean inWater ) {
        CheckEntityInWaterEvent ev = new CheckEntityInWaterEvent( inWater, e );
        MinecraftForge.EVENT_BUS.post( ev );
        return ev.isInWater();
    }

    public static boolean mustSyncHeightmap( Heightmap.Type type ) {
        return type.func_222683_c();
    }

    public static boolean isMotionBlockingNoLeaves( BlockState block ) {
        return ( block.getMaterial().blocksMovement() || ! block.getFluidState().isEmpty() ) && ! block.isIn( BlockTags.LEAVES );
    }

    public static boolean checkRaining( World world ) {
        if( world.dimension instanceof IPrecipitationDimension ) {
            return ( (IPrecipitationDimension) world.dimension ).isRaining();
        }
        return world.getRainStrength( 1 ) > 0.2;
    }

    public static boolean checkRainingAt( World world, BlockPos pos ) {
        if( world.dimension instanceof IPrecipitationDimension ) {
            return ( (IPrecipitationDimension) world.dimension ).isRainingAt( pos );
        }
        if( ! world.isRaining() ) {
            return false;
        } else if( ! world.isSkyLightMax( pos ) ) {
            return false;
        } else if( world.getHeight( Heightmap.Type.MOTION_BLOCKING, pos ).getY() > pos.getY() ) {
            return false;
        } else {
            return world.getBiome( pos ).getPrecipitation() == Biome.RainType.RAIN;
        }
    }
}
