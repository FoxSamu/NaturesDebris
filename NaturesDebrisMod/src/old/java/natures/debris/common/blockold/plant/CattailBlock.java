/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.blockold.MDBlockTags;
import natures.debris.common.blockold.plant.growing.WetGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class CattailBlock extends DoublePlantBlock {
    public CattailBlock(Properties properties) {
        super(properties);
        setGrowLogic(new WetGrowLogic(this));
    }

    @Override
    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.SOIL) && isBlockSideSustainable(state, world, pos, Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state) {
        return REGULAR_SHAPE;
    }
}
