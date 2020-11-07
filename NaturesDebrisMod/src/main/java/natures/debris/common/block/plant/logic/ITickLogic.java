package natures.debris.common.block.plant.logic;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import natures.debris.common.block.plant.Plant;

public interface ITickLogic {
    boolean canTick(Plant plant, BlockState state);
    void tick(Plant plant, BlockState state, ServerWorld world, BlockPos pos, Random rand);
}
