/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.block.farmland.IFarmland;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class MurinaGrowLogic implements IGrowLogic {
    @Override
    public void grow( World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmland farmland ) {
        MovingBlockPos mpos = new MovingBlockPos();
        int i = 0;
        while( world.getBlockState( mpos ).getBlock() == MDBlocks.MURINA ) {
            mpos.moveUp();
            i++;
        }

        if( i < 16 && rand.nextInt( Math.max( 1, i / 2 ) ) == 0 ) {
            BlockPos growPos = pos.down();
            if( MDBlocks.MURINA.canGenerateAt( world, growPos, world.getBlockState( growPos ) ) ) {
                MDBlocks.MURINA.growAt( world, growPos );
            }
        }
    }

    @Override
    public boolean grow( World world, BlockPos pos, BlockState state, Random rand, ItemStack item ) {
        MovingBlockPos mpos = new MovingBlockPos();
        int i = 0;
        while( world.getBlockState( mpos ).getBlock() == MDBlocks.MURINA ) {
            mpos.moveUp();
            i++;
        }

        if( i < 16 && rand.nextInt( Math.max( 1, i / 2 ) ) == 0 ) {
            BlockPos growPos = pos.down();
            if( MDBlocks.MURINA.canGenerateAt( world, growPos, world.getBlockState( growPos ) ) ) {
                MDBlocks.MURINA.growAt( world, growPos );
                return true;
            }
        }
        return false;
    }
}
