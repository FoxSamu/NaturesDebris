/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.feature;

import modernity.common.generator.tree.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class TreeFeature extends Feature<Tree> {

    public TreeFeature() {
        super( dyn -> null );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, Tree tree ) {

        if( ! tree.isSustainable( world, pos.down(), world.getBlockState( pos.down() ) ) ) {
            for( int i = 0; i < 20; i++ ) {
                pos = pos.down();
                if( world.getBlockState( pos.down() ).isSolid() ) {
                    break;
                }
            }
            if( ! tree.isSustainable( world, pos, world.getBlockState( pos.down() ) ) ) {
                return false;
            }
        }

        long seed = rand.nextLong();
        Random local = new Random( seed );

        if( ! tree.canGenerate( world, local, pos ) ) {
            return false;
        }

        local.setSeed( seed );

        tree.generate( world, local, pos );
        return true;
    }
}
