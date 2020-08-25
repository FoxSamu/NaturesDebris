/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.map.surface;

import modernity.common.blockold.MDBlockTags;
import modernity.common.blockold.MDNatureBlocks;
import modernity.common.generator.map.NoiseDepositGenerator;
import modernity.generic.util.BlockUpdates;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;
import net.rgsw.noise.FractalOpenSimplex3D;
import net.rgsw.noise.INoise3D;

public class DarkrockGenerator extends NoiseDepositGenerator<SurfaceGenData> {
    private final INoise3D noise;

    public DarkrockGenerator(IWorld world) {
        super(world);
        noise = new FractalOpenSimplex3D(rand.nextInt(), 43.51234, 4).subtract(0.3);
    }

    @Override
    protected double generateNoise(int x, int y, int z, SurfaceGenData data) {
        return noise.generate(x, y, z);
    }

    @Override
    protected void place(WorldGenRegion region, MovingBlockPos pos, double noise, SurfaceGenData data) {
        if(canPlace(region, pos, noise)) {
            region.setBlockState(pos, MDNatureBlocks.DARKROCK.getDefaultState(), BlockUpdates.NOTIFY_CLIENTS);
        }
    }

    private boolean canPlace(WorldGenRegion region, BlockPos pos, double noise) {
        return noise > 0 && region.getBlockState(pos).isIn(MDBlockTags.ROCK);
    }
}
