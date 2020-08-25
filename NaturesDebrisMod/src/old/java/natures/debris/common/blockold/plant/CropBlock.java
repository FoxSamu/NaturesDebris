/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.blockold.MDBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public abstract class CropBlock extends SingleDirectionalPlantBlock {
    protected final IntegerProperty age;
    protected final int maxAge;

    public CropBlock(Properties properties) {
        super(properties, Direction.UP);

        IntegerProperty age = getAgeProperty();
        int maxAge = 0;
        int minAge = Integer.MAX_VALUE;
        for (int i : age.getAllowedValues()) {
            if (i > maxAge) maxAge = i;
            if (i < minAge) minAge = i;
        }

        this.age = age;
        this.maxAge = maxAge;

        setDefaultState(stateContainer.getBaseState().with(age, minAge));
    }

    @Override
    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.FARMLAND);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(getAgeProperty());
    }

    protected abstract IntegerProperty getAgeProperty();

    public IntegerProperty ageProperty() {
        return age;
    }

    public int maxAge() {
        return maxAge;
    }
}
