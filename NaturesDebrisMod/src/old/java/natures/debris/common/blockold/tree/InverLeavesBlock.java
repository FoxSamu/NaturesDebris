/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.tree;

import natures.debris.client.colors.IColorProfile;
import natures.debris.generic.block.IColoredBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;

import javax.annotation.Nullable;

/**
 * Inver-color leaves
 */
public class InverLeavesBlock extends DecayLeavesBlock implements IColoredBlock {

    private final boolean red;

    public InverLeavesBlock(Tag<Block> logTag, Properties properties, boolean red) {
        super(logTag, properties);
        this.red = red;
    }

    private IColorProfile getColorProfile() {
//        return red ? ModernityClient.get().getRedInverColors()
//                   : ModernityClient.get().getInverColors();
        return null;
    }

    @Override
    public int colorMultiplier(BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex) {
        return getColorProfile().getColor(reader, pos);
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return getColorProfile().getItemColor();
    }
}
