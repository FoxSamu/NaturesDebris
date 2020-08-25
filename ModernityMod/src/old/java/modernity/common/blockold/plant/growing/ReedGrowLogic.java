/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant.growing;

import modernity.common.blockold.MDNatureBlocks;
import modernity.common.blockold.MDPlantBlocks;
import modernity.common.blockold.farmland.IFarmland;
import modernity.common.blockold.plant.MurkReedBlock;
import modernity.common.fluidold.MDFluids;
import modernity.common.itemold.MDItemTags;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ReedGrowLogic implements IGrowLogic {
    @Override
    public void grow(World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmland farmland) {
        if(state.get(MurkReedBlock.AGE) < 15) {
            world.setBlockState(pos, state.with(MurkReedBlock.AGE, state.get(MurkReedBlock.AGE) + 1));
        } else if(canGrow(world, pos, state)) {
            world.setBlockState(pos, state.with(MurkReedBlock.AGE, 0));
            world.setBlockState(pos.up(), MDPlantBlocks.MURK_REED.computeStateForPos(world, pos.up()).with(MurkReedBlock.WATERLOGGED, world.getFluidState(pos.up()).getFluid() == MDFluids.MURKY_WATER));
        }
    }

    @Override
    public boolean grow(World world, BlockPos pos, BlockState state, Random rand, ItemStack item) {
        if(!item.getItem().isIn(MDItemTags.FERTILIZER)) return false;
        if(world.isRemote) return true;
        MovingBlockPos mpos = new MovingBlockPos(pos);
        while(world.getBlockState(mpos).getBlock() == MDPlantBlocks.MURK_REED) {
            mpos.moveUp();
        }
        mpos.moveDown();
        if(canGrow(world, mpos, state)) {
            world.setBlockState(mpos, world.getBlockState(mpos).with(MurkReedBlock.AGE, 0));
            mpos.moveUp();
            world.setBlockState(mpos, MDPlantBlocks.MURK_REED.computeStateForPos(world, mpos).with(MurkReedBlock.WATERLOGGED, world.getFluidState(mpos).getFluid() == MDFluids.MURKY_WATER));
            return true;
        }
        return false;
    }

    private boolean canGrow(World world, BlockPos pos, BlockState state) {
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if(upPos.getY() > 255 || !upState.isAir(world, upPos) && upState.getBlock() != MDNatureBlocks.MURKY_WATER) {
            return false;
        }
        int owHeight = 0, totHeight = 0;
        MovingBlockPos mpos = new MovingBlockPos(pos);
        boolean uw = false;
        while(mpos.getY() >= 0 && state.getBlock() == MDPlantBlocks.MURK_REED && totHeight < 10) {
            if(state.get(MurkReedBlock.WATERLOGGED)) {
                uw = true;
            }
            if(!uw) {
                owHeight++;
            }
            totHeight++;
            mpos.moveDown();
            state = world.getBlockState(mpos);
        }
        return totHeight < 10 && owHeight < 3;
    }
}
