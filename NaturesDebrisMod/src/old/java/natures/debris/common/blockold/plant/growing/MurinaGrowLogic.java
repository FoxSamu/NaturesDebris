/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant.growing;

import natures.debris.common.blockold.MDPlantBlocks;
import natures.debris.common.blockold.farmland.IFarmland;
import natures.debris.common.itemold.MDItemTags;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class MurinaGrowLogic implements IGrowLogic {
    @Override
    public void grow(World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmland farmland) {
        MovingBlockPos mpos = new MovingBlockPos(pos);
        int i = 0;
        while (world.getBlockState(mpos).getBlock() == MDPlantBlocks.MURINA) {
            mpos.moveDown();
            i++;
        }

        if (i < 16 && rand.nextInt(Math.max(1, i / 5)) == 0) {
            BlockPos growPos = pos.down(i);
            if (MDPlantBlocks.MURINA.canGenerateAt(world, growPos, world.getBlockState(growPos))) {
                MDPlantBlocks.MURINA.growAt(world, growPos);
            }
        }
    }

    @Override
    public boolean grow(World world, BlockPos pos, BlockState state, Random rand, ItemStack item) {
        if (!item.getItem().isIn(MDItemTags.FERTILIZER)) return false;
        if (world.isRemote) return true;
        pos = MDPlantBlocks.HANGING_MOSS.getRootPos(world, pos, state);

        MovingBlockPos mpos = new MovingBlockPos(pos);
        int i = 0;
        while (world.getBlockState(mpos).getBlock() == MDPlantBlocks.MURINA) {
            mpos.moveDown();
            i++;
        }

        if (i < 16 && rand.nextInt(Math.max(1, i / 5)) == 0) {
            BlockPos growPos = pos.down(i);
            if (MDPlantBlocks.MURINA.canGenerateAt(world, growPos, world.getBlockState(growPos))) {
                MDPlantBlocks.MURINA.growAt(world, growPos);
                return true;
            }
        }
        return false;
    }
}
