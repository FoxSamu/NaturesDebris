/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.blockold.MDPlantBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeathBlock extends SimplePlantBlock {
    public HeathBlock(Properties properties) {
        super(properties, HEATH);
    }


    @Override
    public void growAt(World world, BlockPos pos) {
        if (world.rand.nextInt(5) == 0) {
            world.setBlockState(pos, MDPlantBlocks.FLOWERED_HEATH.computeStateForPos(world, pos), 3);
        } else {
            world.setBlockState(pos, MDPlantBlocks.HEATH.computeStateForPos(world, pos), 3);
        }
    }
}
