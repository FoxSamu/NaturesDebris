/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.blocks;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.block.plant.FlyFlowerBlock;
import modernity.common.block.plant.FlyFlowerStalkBlock;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class FlyFlowerBlockGenerator implements IBlockGenerator {
    private static final FlyFlowerStalkBlock STALK = MDBlocks.FLY_FLOWER_STALK;
    private static final FlyFlowerBlock FLOWER = MDBlocks.FLY_FLOWER;

    @Override
    public boolean generateBlock( IWorld world, BlockPos pos, Random rand ) {
        BlockState sustainer = world.getBlockState( pos.down() );
        if( ! STALK.canBlockSustain( world, pos.down(), sustainer ) ) {
            return false;
        }



        MovingBlockPos mpos = new MovingBlockPos();
        int height = 0;
        for( int i = 0; i <= 20; i++ ) {
            if( i == 20 ) return false; // Too long

            mpos.setPos( pos ).moveUp( i );
            if( ! isMurkyWater( world.getBlockState( mpos ) ) ) {
                return false;
            }

            mpos.moveUp();
            boolean air = world.isAirBlock( mpos );
            if( ! air && ! isMurkyWater( world.getBlockState( mpos ) ) ) {
                return false;
            }

            if( air ) {
                height = i + 1;
                break;
            }
        }

        if( height > 0 ) {
            mpos.setPos( pos );
            for( int i = 0; i < height; i++ ) {
                world.setBlockState( mpos, STALK.getDefaultState(), 2 );
                mpos.moveUp();
            }
            world.setBlockState( mpos, FLOWER.getDefaultState(), 2 );
            return true;
        }
        return false;
    }

    private boolean isMurkyWater( BlockState state ) {
        return state.getFluidState().getFluid() == MDFluids.MURKY_WATER && state.getBlock() == MDBlocks.MURKY_WATER;
    }
}
