/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant.growing;

import natures.debris.common.blockold.farmland.IFarmland;
import natures.debris.common.blockold.plant.CropBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class CropGrowLogic implements IGrowLogic {
    private final CropBlock crop;
    private final IntegerProperty age;
    private final int maxAge;

    protected CropGrowLogic(CropBlock crop) {
        this.crop = crop;
        this.age = crop.ageProperty();
        this.maxAge = crop.maxAge();
    }

    @Override
    public void grow(World world, BlockPos pos, BlockState state, Random rand, @Nullable IFarmland farmland) {
        if (farmland == null) return;

        GrowType grow = checkResources(farmland, rand);

        if (grow == GrowType.DIE) {
            crop.kill(world, pos, state);
        } else if (grow == GrowType.GROW) {
            if (state.get(age) < maxAge) {
                state = state.with(age, state.get(age) + 1);
                world.setBlockState(pos, state);
                consumeResources(farmland, rand);
            }
        }
    }

    @Override
    public boolean grow(World world, BlockPos pos, BlockState state, Random rand, ItemStack item) {
        if (isFertilizer(item, rand)) {
            if (world.isRemote) return true;
            int grow = getItemGrow(item, rand);
            if (grow > 0) {
                if (state.get(age) < maxAge) {
                    state = state.with(age, Math.min(state.get(age) + grow, maxAge));
                    world.setBlockState(pos, state);
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract boolean isFertilizer(ItemStack stack, Random rand);

    protected abstract int getItemGrow(ItemStack stack, Random rand);

    protected abstract GrowType checkResources(IFarmland logic, Random rand);
    protected abstract void consumeResources(IFarmland logic, Random rand);

    public enum GrowType {
        NONE,
        GROW,
        DIE
    }
}
