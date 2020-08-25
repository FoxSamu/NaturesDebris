/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.decorate.decoration;

import natures.debris.generic.util.BlockUpdates;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;
import java.util.function.Predicate;

public class DepositDecoration implements IDecoration {
    private final int size;
    private final Predicate<BlockState> replace;
    private final BlockState state;

    public DepositDecoration(int size, Predicate<BlockState> replace, BlockState state) {
        this.size = size;
        this.replace = replace;
        this.state = state;
    }

    public int getSize() {
        return size;
    }

    public BlockState getState() {
        return state;
    }

    public Predicate<BlockState> getReplace() {
        return replace;
    }

    @Override
    public void generate(IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator) {
        MovingBlockPos rpos = new MovingBlockPos();

        int itr = size * 5 + rand.nextInt(size);
        for (int i = 0; i < itr; i++) {
            double radius = rand.nextDouble() / 2 * size;
            double sx = (rand.nextDouble() - rand.nextDouble()) * size / 2;
            double sy = (rand.nextDouble() - rand.nextDouble()) * size / 2;
            double sz = (rand.nextDouble() - rand.nextDouble()) * size / 2;

            int nx = MathHelper.floor(sx - radius);
            int ny = MathHelper.floor(sy - radius);
            int nz = MathHelper.floor(sz - radius);
            int px = MathHelper.ceil(sx + radius);
            int py = MathHelper.ceil(sy + radius);
            int pz = MathHelper.ceil(sz + radius);

            for (int x = nx; x <= px; x++) {
                double lx = x - sx;
                for (int z = nz; z <= pz; z++) {
                    double lz = z - sz;
                    for (int y = ny; y <= py; y++) {
                        double ly = y - sy;
                        if (lx * lx + ly * ly + lz * lz < radius * radius) {
                            rpos.setPos(pos);
                            rpos.addPos(x, y, z);
                            rpos.moveDown();
                            BlockState current = world.getBlockState(rpos);
                            if (replace.test(current)) {
                                world.setBlockState(rpos, state, BlockUpdates.NO_NEIGHBOR_REACTIONS | BlockUpdates.NOTIFY_CLIENTS);
                            }
                        }
                    }
                }
            }
        }
    }
}
