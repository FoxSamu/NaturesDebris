/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.api.util.ColorUtil;
import modernity.client.colors.IColorProvider;
import modernity.common.util.CaveUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.redgalaxy.util.MathUtil;

import javax.annotation.Nullable;

public class HeightmapColorProvider implements IColorProvider {

    private final int lowOffset;
    private final int highOffset;
    private final IColorProvider above;
    private final IColorProvider below;
    private final HeightProvider heightmap;

    public HeightmapColorProvider( int lowOffset, int highOffset, IColorProvider above, IColorProvider below, HeightProvider heightmap ) {
        this.lowOffset = lowOffset;
        this.highOffset = highOffset;
        this.above = above;
        this.below = below;
        this.heightmap = heightmap;
    }

    @Override
    public int getColor( @Nullable IEnviromentBlockReader world, BlockPos pos ) {
        if( world == null ) return above.getColor( null, pos );
        if( Minecraft.getInstance().world == null ) return above.getColor( null, pos );

        double y = pos.getY();
        int h = heightmap.getHeight( Minecraft.getInstance().world, pos.getX(), pos.getZ() );
        int lo = h + lowOffset;
        int hi = h + highOffset;

        double interp = MathUtil.clamp( MathUtil.unlerp( lo, hi, y ), 0, 1 );

        if( interp == 1 ) return above.getColor( world, pos );
        if( interp == 0 ) return below.getColor( world, pos );

        return ColorUtil.interpolate(
            below.getColor( world, pos ),
            above.getColor( world, pos ),
            interp
        );
    }

    @Override
    public void initForSeed( long seed ) {
        above.initForSeed( seed );
        below.initForSeed( seed );
    }

    public enum HeightProvider {
        WORLD_SURFACE {
            @Override
            int getHeight( World world, int x, int z ) {
                return world.getHeight( Heightmap.Type.WORLD_SURFACE, x, z );
            }
        },
        OCEAN_FLOOR {
            @Override
            int getHeight( World world, int x, int z ) {
                return world.getHeight( Heightmap.Type.OCEAN_FLOOR, x, z );
            }
        },
        MOTION_BLOCKING {
            @Override
            int getHeight( World world, int x, int z ) {
                return world.getHeight( Heightmap.Type.MOTION_BLOCKING, x, z );
            }
        },
        MOTION_BLOCKING_NO_LEAVES {
            @Override
            int getHeight( World world, int x, int z ) {
                return world.getHeight( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z );
            }
        },
        CAVE {
            @Override
            int getHeight( World world, int x, int z ) {
                return CaveUtil.caveHeight( x, z, world );
            }
        };

        abstract int getHeight( World world, int x, int z );
    }
}
