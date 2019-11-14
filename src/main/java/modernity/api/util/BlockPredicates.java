/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.util;

import modernity.common.block.MDBlocks;
import net.minecraft.block.BlockState;

import java.util.function.Predicate;

public final class BlockPredicates {
    /**
     * Matches any of the rock types (rock, darkrock, lightrock and redrock)
     */
    public static final Predicate<BlockState> ROCK_TYPES = state ->
                                                               state.getBlock() == MDBlocks.ROCK ||
                                                                   state.getBlock() == MDBlocks.DARKROCK ||
                                                                   state.getBlock() == MDBlocks.LIGHTROCK ||
                                                                   state.getBlock() == MDBlocks.REDROCK;

    /**
     * Matches everything
     */
    public static final Predicate<BlockState> TRUE = state -> true;

    /**
     * Matches nothing
     */
    public static final Predicate<BlockState> FALSE = state -> false;

    private BlockPredicates() {
    }
}
