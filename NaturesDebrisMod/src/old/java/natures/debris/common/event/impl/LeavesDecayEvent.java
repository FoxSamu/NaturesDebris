/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.event.impl;

import natures.debris.common.blockold.tree.LeavesBlock;
import natures.debris.common.event.StateBlockEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Event cast when leaves decay. This spawns a bunch of leaf particles.
 */
public class LeavesDecayEvent extends StateBlockEvent {
    @Override
    @OnlyIn(Dist.CLIENT)
    public void playEvent(World world, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        if (block instanceof LeavesBlock) {
            LeavesBlock leaves = (LeavesBlock) block;
            leaves.spawnDecayLeaves(pos, world.rand, world, state);
        }
    }
}
