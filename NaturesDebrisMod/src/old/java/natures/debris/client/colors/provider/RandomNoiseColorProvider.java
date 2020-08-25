/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.colors.provider;

// TODO Re-evaluate
//public abstract class RandomNoiseColorProvider extends NoiseColorProvider {
//    private final IColorProvider[] colors;
//
//    protected RandomNoiseColorProvider( IColorProvider[] colors ) {
//        this.colors = colors;
//    }
//
//    @Override
//    protected int computeColor( IEnviromentBlockReader world, BlockPos pos, double noise ) {
//        int idx = Math.min(
//            MathHelper.floor(
//                MathUtil.relerp(
//                    - 1, 1,
//                    0, colors.length,
//                    noise
//                )
//            ),
//            colors.length - 1
//        );
//        return colors[ idx ].getColor( world, pos );
//    }
//
//    @Override
//    public void initForSeed( long seed ) {
//        super.initForSeed( seed );
//        for( IColorProvider provider : colors ) {
//            provider.initForSeed( seed );
//        }
//    }
//}
