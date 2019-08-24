/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 24 - 2019
 */

package modernity.common.world.gen.util;

import net.minecraft.block.state.IBlockState;

import modernity.common.block.MDBlocks;

import java.util.function.Predicate;

public class BlockPredicates {
    public static final Predicate<IBlockState> ROCK_TYPES = state ->
            state.getBlock() == MDBlocks.ROCK_SLAB ||
                    state.getBlock() == MDBlocks.DARKROCK ||
                    state.getBlock() == MDBlocks.LIGHTROCK ||
                    state.getBlock() == MDBlocks.REDROCK;

    public static final Predicate<IBlockState> TRUE = state -> true;
    public static final Predicate<IBlockState> FALSE = state -> false;
}
