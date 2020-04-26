/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SelectiveDecoration implements IDecoration {
    private int total = - 1;
    private HashMap<IDecoration, Integer> weights = new HashMap<>();

    public SelectiveDecoration add( IDecoration dec, int weight ) {
        weights.put( dec, weight );
        total = - 1;
        return this;
    }

    private int totalWeight() {
        if( total < 0 ) {
            total = 0;
            for( int i : weights.values() ) {
                total += i;
            }
        }
        return total;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        int r = rand.nextInt( totalWeight() );

        for( Map.Entry<IDecoration, Integer> entry : weights.entrySet() ) {
            r -= entry.getValue();
            if( r <= 0 ) {
                entry.getKey().generate( world, pos, rand, chunkGenerator );
                break;
            }
        }
    }
}
