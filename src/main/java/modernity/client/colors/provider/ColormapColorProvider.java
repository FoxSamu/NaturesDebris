/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.colormap.ColorMap;
import modernity.client.colors.ColorProfile;
import modernity.client.colors.IColorProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

public class ColormapColorProvider implements IColorProvider {
    private final ColorMap map;
    private final float x;
    private final float y;
    private final IColorProvider fallback;

    public ColormapColorProvider( ResourceLocation loc, float x, float y, IColorProvider fallback ) {
        this.x = x;
        this.y = y;
        this.fallback = fallback;

        map = ColorProfile.getColorMap( loc );
    }


    @Override
    public int getColor( IEnviromentBlockReader world, BlockPos pos ) {
        if( map.isLoaded() ) {
            return map.get( x, y );
        }
        return fallback.getColor( world, pos );
    }

    @Override
    public void initForSeed( long seed ) {
        fallback.initForSeed( seed );
    }
}
