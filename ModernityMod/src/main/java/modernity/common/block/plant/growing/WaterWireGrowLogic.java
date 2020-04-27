/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmland;
import modernity.common.block.plant.PlantBlock;

import java.util.Random;

public class WaterWireGrowLogic extends BushGrowLogic {
    public WaterWireGrowLogic( PlantBlock plant ) {
        super( plant );
    }

    @Override
    protected GrowType checkResources( IFarmland logic, Random rand ) {
        return GrowType.NONE;
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
    }
}
