package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import natures.debris.common.block.plant.Plant;

public interface ICollisionLogic {
    void onCollision(Plant plant, BlockState state, World world, BlockPos pos, Entity entity);
}
