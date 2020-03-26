/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 12 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.plant.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.ArrayList;
import java.util.Random;

public class BushesDecoration implements IDecoration {

    private final PlantBlock bush;

    public BushesDecoration( PlantBlock bush ) {
        this.bush = bush;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        ArrayList<BlockPos> locs = new ArrayList<>();
        addBush( world, pos, rand, 2, 0, locs );

        for( BlockPos loc : locs ) {
            world.setBlockState( loc, bush.computeStateForGeneration( world, loc, rand ), 2 );
        }
    }

    private void addBush( IWorld world, BlockPos pos, Random rand, int size, int recursion, ArrayList<BlockPos> locs ) {
        if( recursion > 6 ) return;
        if( size <= 0 ) return;

        if( size == 2 ) {
            int xoff = rand.nextBoolean() ? 1 : - 1;
            int zoff = rand.nextBoolean() ? 1 : - 1;

            MovingBlockPos mpos = new MovingBlockPos( pos );

            place( world, mpos.setPos( pos ).addPos( 0, 0, 0 ), locs );
            place( world, mpos.setPos( pos ).addPos( 0, 0, zoff ), locs );
            place( world, mpos.setPos( pos ).addPos( xoff, 0, zoff ), locs );
            place( world, mpos.setPos( pos ).addPos( xoff, 0, 0 ), locs );
            place( world, mpos.setPos( pos ).addPos( 0, 1, 0 ), locs );
            place( world, mpos.setPos( pos ).addPos( 0, 1, zoff ), locs );
            place( world, mpos.setPos( pos ).addPos( xoff, 1, zoff ), locs );
            place( world, mpos.setPos( pos ).addPos( xoff, 1, 0 ), locs );
        } else if( size == 1 ) {
            MovingBlockPos mpos = new MovingBlockPos( pos );

            place( world, mpos.setPos( pos ), locs );
        }
    }

    private void place( IWorld world, BlockPos pos, ArrayList<BlockPos> locs ) {
        if( bush.getDefaultState().isValidPosition( world, pos ) ) {
            world.setBlockState( pos, bush.getDefaultState(), 2 );
            locs.add( pos.toImmutable() );
        }
    }
}
