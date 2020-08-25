/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.provider;

// TODO Re-evaluate
//public class ColormapColorProvider implements IColorProvider {
//    private final ColorMap map;
//    private final float x;
//    private final float y;
//    private final IColorProvider fallback;
//
//    public ColormapColorProvider( ResourceLocation loc, float x, float y, IColorProvider fallback ) {
//        this.x = x;
//        this.y = y;
//        this.fallback = fallback;
//
//        map = ColorProfile.getColorMap( loc );
//    }
//
//
//    @Override
//    public int getColor( IEnviromentBlockReader world, BlockPos pos ) {
//        if( map.isLoaded() ) {
//            return map.get( x, y );
//        }
//        return fallback.getColor( world, pos );
//    }
//
//    @Override
//    public void initForSeed( long seed ) {
//        fallback.initForSeed( seed );
//    }
//}
