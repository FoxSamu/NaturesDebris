/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.plant;

import natures.debris.common.blockold.plant.growing.HangMossGrowLogic;
import natures.debris.common.particle.MDParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class HangingMossBlock extends HangingPlantBlock {
    public HangingMossBlock(Properties properties) {
        super(properties, HANG_MOSS_SHAPE, HANG_MOSS_END_SHAPE);
        setGrowLogic(new HangMossGrowLogic());
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(50) == 0) {
            double x = pos.getX() + rand.nextDouble();
            double y = pos.getY() + rand.nextDouble();
            double z = pos.getZ() + rand.nextDouble();

            world.addParticle(MDParticleTypes.MOSS_DRIP_HANGING, x, y, z, 0, 0, 0);
        }
    }
}
