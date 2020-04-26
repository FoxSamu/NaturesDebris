/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.generator.blocks;

import modernity.common.block.MDPlantBlocks;
import modernity.common.block.plant.IPlantSustainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class SoulLightBlockGenerator implements IBlockGenerator {
    @Override
    public boolean generateBlock( IWorld world, BlockPos pos, Random rand ) {
        BlockPos down = pos.down();
        if( world.getFluidState( down ).isEmpty() ) {
            if( ! isBlockSideSustainable( world.getBlockState( down ), world, down, Direction.UP ) ) {
                return false;
            }
        }
        if( ! world.getBlockState( pos ).isAir( world, pos ) ) {
            return false;
        }
        return world.setBlockState( pos, MDPlantBlocks.SOUL_LIGHT.getDefaultState(), 2 );
    }


    private static boolean isBlockSideSustainable( BlockState state, IWorld world, BlockPos pos, Direction side ) {
        Block block = state.getBlock();
        if( block instanceof IPlantSustainer ) {
            return ( (IPlantSustainer) block ).canSustainPlant( world, pos, state, MDPlantBlocks.SOUL_LIGHT, side );
        } else {
            return state.isSolid() && state.isSolidSide( world, pos, side );
        }
    }
}
