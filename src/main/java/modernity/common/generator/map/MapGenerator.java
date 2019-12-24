/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.map;

import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;

import java.util.Random;

/**
 * A map generator generates a part of a large feature in a chunk. It usually generates some noise field or a cave. The
 * currently only implementation is {@link DarkrockGenerator}.
 */
// TODO: Make all chunk generation depend on these map generators
public abstract class MapGenerator {
    public final IWorld world;
    public final Random rand;

    public MapGenerator( IWorld world ) {
        this.world = world;
        this.rand = new Random( world.getSeed() );
    }

    /**
     * Generates the feature in the world. It must usually generate only in the main chunk of the region, but may
     * generate or read outside that chunk in some exceptional cases.
     */
    public abstract void generate( WorldGenRegion region );
}
