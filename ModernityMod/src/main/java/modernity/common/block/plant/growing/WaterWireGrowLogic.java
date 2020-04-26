/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
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
