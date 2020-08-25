/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.generic.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Implementing blocks have a color multiplier.
 *
 * @author RGSW
 */
@FunctionalInterface
public interface IColoredBlock {
    @OnlyIn(Dist.CLIENT)
    int colorMultiplier(BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex);
    @OnlyIn(Dist.CLIENT)
    default int colorMultiplier(ItemStack stack, int tintIndex) {
        return 0xffffff;
    }
}
