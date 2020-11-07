package natures.debris.common.block.plant.logic;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import natures.debris.common.block.plant.Plant;

@FunctionalInterface
public interface IFeedLogic {
    boolean feed(Plant plant, BlockState state, ServerWorld world, BlockPos pos, Random rand);
}
