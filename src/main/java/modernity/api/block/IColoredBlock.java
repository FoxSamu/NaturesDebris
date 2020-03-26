/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.block;

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
    @OnlyIn( Dist.CLIENT )
    int colorMultiplier( BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex );
    @OnlyIn( Dist.CLIENT )
    default int colorMultiplier( ItemStack stack, int tintIndex ) {
        return 0xffffff;
    }
}
