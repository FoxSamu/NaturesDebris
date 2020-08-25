/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.blocks;

import natures.debris.common.blockold.MDNatureBlocks;
import natures.debris.common.blockold.MDPlantBlocks;
import natures.debris.common.blockold.plant.AlgaeBlock;
import natures.debris.common.fluidold.MDFluids;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class AlgaeBlockGenerator implements IBlockGenerator {
    private static final AlgaeBlock ALGAE = MDPlantBlocks.ALGAE;

    @Override
    public boolean generateBlock(IWorld world, BlockPos pos, Random rand) {
        BlockState state = world.getBlockState(pos);
        int dens = 0;
        if (state.getBlock() == ALGAE) {
            dens = state.get(AlgaeBlock.DENSITY) + 1;
            if (dens > 16) dens = 0;
        } else if (state.getBlock() == MDNatureBlocks.MURKY_WATER && state.getFluidState().getFluid() == MDFluids.MURKY_WATER) {
            dens = 1;
        }
        if (dens > 0) {
            return world.setBlockState(pos, ALGAE.getDefaultState().with(AlgaeBlock.DENSITY, dens), 2);
        }
        return false;
    }
}
