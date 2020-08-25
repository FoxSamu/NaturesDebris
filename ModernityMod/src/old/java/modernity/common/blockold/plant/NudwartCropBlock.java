/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant;

import modernity.common.blockold.MDBlockStateProperties;
import modernity.common.blockold.plant.growing.FertileCropGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.shapes.VoxelShape;

public class NudwartCropBlock extends CropBlock {
    private static final int[] HEIGHTS = {
        2, 3, 4, 5, 6, 7
    };

    private static final VoxelShape[] SHAPES = new VoxelShape[6];

    static {
        for(int i = 0; i < 6; i++) {
            SHAPES[i] = makePlantShape(14, HEIGHTS[i]);
        }
    }

    public NudwartCropBlock(Properties properties) {
        super(properties);
        setGrowLogic(new FertileCropGrowLogic(this));
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return MDBlockStateProperties.AGE_1_6;
    }

    @Override
    public OffsetType getOffsetType() {
        return super.getOffsetType();
    }

    @Override
    public VoxelShape getShape(BlockState state) {
        return SHAPES[state.get(age) - 1];
    }
}
