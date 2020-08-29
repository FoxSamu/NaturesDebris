/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.tree;

import natures.debris.generic.block.IColoredBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

import javax.annotation.Nullable;

/**
 * Describes blackwood-colored leaves.
 */
public class BlackwoodLeavesBlock extends HangLeavesBlock implements IColoredBlock {

    public BlackwoodLeavesBlock(Tag<Block> logTag, Properties properties) {
        super(logTag, properties);
    }

    @Override
    public int colorMultiplier(BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex) {
//        return ModernityClient.get().getBlackwoodColors().getColor( reader, pos );
        return 0;
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
//        return ModernityClient.get().getBlackwoodColors().getItemColor();
        return 0;
    }
}