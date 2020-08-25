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

public class MushroomGrowLogic extends SpreadingGrowLogic {
    public MushroomGrowLogic(PlantBlock plant) {
        super(plant);
    }

    @Override
    protected boolean isFertilizer(ItemStack item) {
        return item.getItem().isIn(MDItemTags.FERTILIZER);
    }

    @Override
    protected GrowType checkResources(IFarmland logic, Random rand) {
        if (logic.isDecayed() || logic.isFertile()) {
            return rand.nextInt(16) < 6 ? GrowType.GROW : GrowType.NONE;
        }
        return GrowType.NONE;
    }

    @Override
    protected void consumeResources(IFarmland logic, Random rand) {
        logic.decreaseLevel();
    }
}
