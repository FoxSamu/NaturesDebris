/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.dirt.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface IDecayingLogicType extends IDirtLogicType {
    default boolean canDecay(World world, BlockPos pos, BlockState state) {
        return !DirtLogic.canRemain(world, pos, state);
    }

    IDirtLogicType getDecayed(World world, BlockPos pos, BlockState state);

    default void decay(World world, BlockPos pos, BlockState state) {
        IDirtLogicType decayed = getDecayed(world, pos, state);
        DirtLogic.switchType(world, pos, decayed);
    }

    default boolean decayTick(World world, BlockPos pos, BlockState state, Random rand) {
        if (canDecay(world, pos, state)) {
            decay(world, pos, state);
            return true;
        }
        return false;
    }
}
