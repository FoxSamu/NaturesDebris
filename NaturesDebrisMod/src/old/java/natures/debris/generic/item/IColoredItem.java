/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.generic.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Implementing items have a color multiplier.
 */
@FunctionalInterface
public interface IColoredItem {
    @OnlyIn(Dist.CLIENT)
    int colorMultiplier(ItemStack stack, int tintIndex);
}
