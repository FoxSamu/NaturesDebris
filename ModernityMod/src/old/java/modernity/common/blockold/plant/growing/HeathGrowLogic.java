/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant.growing;

import modernity.common.blockold.farmland.IFarmland;
import modernity.common.blockold.plant.PlantBlock;
import modernity.common.itemold.MDItemTags;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class HeathGrowLogic extends SpreadingGrowLogic {
    public HeathGrowLogic(PlantBlock plant) {
        super(plant);
    }

    @Override
    protected boolean isFertilizer(ItemStack item) {
        return item.getItem().isIn(MDItemTags.FERTILIZER);
    }

    @Override
    protected GrowType checkResources(IFarmland logic, Random rand) {
        if(logic.isDecayed()) {
            return GrowType.NONE;
        }
        int chance = 2;
        if(logic.isWet()) {
            chance = 6;
        }
        return rand.nextInt(16) < chance ? GrowType.GROW : GrowType.NONE;
    }

    @Override
    protected void consumeResources(IFarmland logic, Random rand) {
    }
}
