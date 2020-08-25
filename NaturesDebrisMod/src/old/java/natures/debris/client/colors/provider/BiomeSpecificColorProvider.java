/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.colors.provider;

// TODO Re-evaluate
//public class BiomeSpecificColorProvider implements IColorProvider {
//    private final HashMap<Biome, IColorProvider> colors = new HashMap<>();
//    private final IColorProvider defaultColor;
//    private final IColorProvider itemColor;
//    private final int radius;
//
//    public BiomeSpecificColorProvider( IColorProvider defaultColor, IColorProvider itemColor, int radius ) {
//        this.defaultColor = defaultColor;
//        this.itemColor = itemColor;
//        this.radius = radius;
//    }
//
//    public void addColor( Biome biome, IColorProvider provider ) {
//        colors.put( biome, provider );
//    }
//
//    @Override
//    public int getColor( IEnviromentBlockReader world, BlockPos pos ) {
//        if( world == null ) return itemColor.getColor( null, pos );
//
//        int radius = this.radius;
//        if( radius <= - 1 ) radius = Minecraft.getInstance().gameSettings.biomeBlendRadius;
//
//        if( radius == 0 ) {
//            Biome biome = world.getBiome( pos );
//            return colors.getOrDefault( biome, defaultColor ).getColor( world, pos );
//        } else {
//            float r = 0, g = 0, b = 0;
//            int total = 0;
//
//            MovingBlockPos mpos = new MovingBlockPos();
//
//            for( int x = - radius; x <= radius; x++ ) {
//                for( int z = - radius; z <= radius; z++ ) {
//                    mpos.setPos( pos ).addPos( x, 0, z );
//                    Biome biome = world.getBiome( mpos );
//                    int color = colors.getOrDefault( biome, defaultColor ).getColor( world, pos );
//
//                    r += ColorUtil.redf( color );
//                    g += ColorUtil.greenf( color );
//                    b += ColorUtil.bluef( color );
//                    total++;
//                }
//            }
//
//            r /= total;
//            g /= total;
//            b /= total;
//            return ColorUtil.rgb( r, g, b );
//        }
//    }
//
//    @Override
//    public void initForSeed( long seed ) {
//        for( IColorProvider provider : colors.values() ) {
//            provider.initForSeed( seed );
//        }
//        defaultColor.initForSeed( seed );
//        itemColor.initForSeed( seed );
//    }
//}
