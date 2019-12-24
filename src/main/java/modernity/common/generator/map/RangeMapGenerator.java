/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.map;

import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;

public abstract class RangeMapGenerator extends MapGenerator {
    protected final int radius;

    public RangeMapGenerator( IWorld world, int radius ) {
        super( world );
        this.radius = radius;
    }

    @Override
    public void generate( WorldGenRegion region ) {
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();
        for( int x = - radius; x <= radius; x++ ) {
            for( int z = - radius; z <= radius; z++ ) {
                int ox = cx + x;
                int oz = cz + z;
                rand.setSeed( world.getSeed() ^ ox * 58192931923L + oz * 42789215L );
                generateRecursively( region, cx, cz, ox, oz );
            }
        }
    }

    protected abstract void generateRecursively( IWorld world, int cx, int cz, int ox, int oz );
}
