/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant.growing;

import modernity.common.blockold.MDPlantBlocks;
import modernity.common.blockold.farmland.IFarmland;
import modernity.common.itemold.MDItemTags;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class HangMossGrowLogic implements IGrowLogic {
    @Override
    public void grow(World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmland farmland) {
        MovingBlockPos mpos = new MovingBlockPos(pos);
        int i = 0;
        while(world.getBlockState(mpos).getBlock() == MDPlantBlocks.HANGING_MOSS) {
            mpos.moveDown();
            i++;
        }

        if(i < 5 && rand.nextInt(Math.max(1, i / 2)) == 0) {
            BlockPos growPos = pos.down(i);
            if(MDPlantBlocks.HANGING_MOSS.canGenerateAt(world, growPos, world.getBlockState(growPos))) {
                MDPlantBlocks.HANGING_MOSS.growAt(world, growPos);
            }
        }
    }

    @Override
    public boolean grow(World world, BlockPos pos, BlockState state, Random rand, ItemStack item) {
        if(!item.getItem().isIn(MDItemTags.FERTILIZER)) return false;
        if(world.isRemote) return true;
        pos = MDPlantBlocks.HANGING_MOSS.getRootPos(world, pos, state);

        MovingBlockPos mpos = new MovingBlockPos(pos);
        int i = 0;
        while(world.getBlockState(mpos).getBlock() == MDPlantBlocks.HANGING_MOSS) {
            mpos.moveDown();
            i++;
        }

        if(i < 5 && rand.nextInt(Math.max(1, i / 2)) == 0) {
            BlockPos growPos = pos.down(i);
            if(MDPlantBlocks.HANGING_MOSS.canGenerateAt(world, growPos, world.getBlockState(growPos))) {
                MDPlantBlocks.HANGING_MOSS.growAt(world, growPos);
                return true;
            }
        }
        return false;
    }
}
