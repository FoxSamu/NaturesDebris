/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.plants;

import net.minecraft.world.IWorld;

import java.util.Random;

@FunctionalInterface
public interface IVegetation {
    void generate( IWorld world, PlantLayer layer, int cx, int cz, Random rand );
}
