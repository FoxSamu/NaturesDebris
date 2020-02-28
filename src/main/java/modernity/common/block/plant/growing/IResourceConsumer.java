/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmlandLogic;

import java.util.Random;

@FunctionalInterface
public interface IResourceConsumer {
    void consumeResources( IFarmlandLogic logic, Random rand );
}