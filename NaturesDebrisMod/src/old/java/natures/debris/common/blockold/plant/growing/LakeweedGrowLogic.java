/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant.growing;

import natures.debris.common.blockold.farmland.IFarmland;
import natures.debris.common.blockold.plant.PlantBlock;
import natures.debris.common.itemold.MDItemTags;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class LakeweedGrowLogic extends SpreadingGrowLogic {
    public LakeweedGrowLogic(PlantBlock plant) {
        super(plant);
    }

    @Override
    protected boolean isFertilizer(ItemStack item) {
        return item.getItem().isIn(MDItemTags.FERTILIZER);
    }

    @Override
    protected GrowType checkResources(IFarmland logic, Random rand) {
        return GrowType.NONE;
    }

    @Override
    protected void consumeResources(IFarmland logic, Random rand) {
    }
}
