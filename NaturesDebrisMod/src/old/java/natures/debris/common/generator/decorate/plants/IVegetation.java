/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.plants;

import net.minecraft.world.IWorld;

import java.util.Random;

@FunctionalInterface
public interface IVegetation {
    void generate(IWorld world, PlantLayer layer, int cx, int cz, Random rand);
}
