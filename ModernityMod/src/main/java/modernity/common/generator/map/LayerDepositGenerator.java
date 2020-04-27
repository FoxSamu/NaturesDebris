/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.map;

import modernity.generic.util.MovingBlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;
import net.redgalaxy.util.MathUtil;
import net.rgsw.noise.FractalPerlin3D;
import net.rgsw.noise.INoise3D;

public abstract class LayerDepositGenerator<D extends IMapGenData> extends MapGenerator<D> {

    private final INoise3D noise;

    private final int lower;
    private final int upper;
    private final int fuzzySize;

    public LayerDepositGenerator( IWorld world, int lower, int upper, int fuzzySize ) {
        super( world );
        this.lower = lower;
        this.upper = upper;
        this.fuzzySize = fuzzySize;

        noise = new FractalPerlin3D( rand.nextInt(), 3.52891, 0.1241141, 3.52891, 3 );
    }

    @Override
    public void generate( WorldGenRegion region, D data ) {
        MovingBlockPos mpos = new MovingBlockPos();

        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();

        for( int y = lower - fuzzySize; y <= upper + fuzzySize; y++ ) {
            if( y >= lower && y <= upper ) {
                for( int x = 0; x < 16; x++ ) {
                    for( int z = 0; z < 16; z++ ) {
                        mpos.setPos( cx * 16, 0, cz * 16 ).addPos( x, y, z );
                        place( region, mpos, data );
                    }
                }
            } else if( y < lower && y >= lower - fuzzySize ) {

                double noiseMul = MathUtil.relerp( lower - fuzzySize, lower, - 1, 1, y );

                for( int x = 0; x < 16; x++ ) {
                    for( int z = 0; z < 16; z++ ) {

                        double n = noise.generate( x, y, z ) + noiseMul;

                        if( n > 0 ) {
                            mpos.setPos( cx * 16, 0, cz * 16 ).addPos( x, y, z );
                            place( region, mpos, data );
                        }
                    }
                }
            } else if( y > upper && y <= upper + fuzzySize ) {

                double noiseMul = MathUtil.relerp( upper, upper + fuzzySize, 1, - 1, y );

                for( int x = 0; x < 16; x++ ) {
                    for( int z = 0; z < 16; z++ ) {

                        double n = noise.generate( x, y, z ) + noiseMul;

                        if( n > 0 ) {
                            mpos.setPos( cx * 16, 0, cz * 16 ).addPos( x, y, z );
                            place( region, mpos, data );
                        }
                    }
                }
            }
        }
    }

    protected abstract void place( WorldGenRegion region, MovingBlockPos pos, D data );
}
