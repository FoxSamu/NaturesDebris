/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.position;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

public class Surface implements IDecorPosition {

    private final Heightmap.Type type;

    public Surface(Heightmap.Type type) {
        this.type = type;
    }

    @Override
    public BlockPos findPosition(IWorld world, int cx, int cz, Random rand) {
        int x = rand.nextInt(16) + cx * 16;
        int z = rand.nextInt(16) + cz * 16;

        int y = world.getHeight(type, x, z);

        return new BlockPos(x, y, z);
    }
}
