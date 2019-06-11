/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface IColoredBlock {
    @OnlyIn( Dist.CLIENT )
    int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex );
    @OnlyIn( Dist.CLIENT )
    default int colorMultiplier( ItemStack stack, int tintIndex ) { return 0xffffff; }
}
