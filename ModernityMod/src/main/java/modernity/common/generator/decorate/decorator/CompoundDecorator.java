/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.decorator;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.ArrayList;
import java.util.Random;

public class CompoundDecorator implements IDecorator {

    private final ArrayList<IDecorator> children = new ArrayList<>();

    @Override
    public void decorate( IWorld world, int cx, int cz, Biome biome, Random rand, ChunkGenerator<?> chunkGenerator ) {
        for( IDecorator decorator : children ) {
            decorator.decorate( world, cx, cz, biome, rand, chunkGenerator );
        }
    }

    public void addDecorator( IDecorator decorator ) {
        children.add( decorator );
    }

    public void removeDecorator( IDecorator decorator ) {
        children.remove( decorator );
    }
}
