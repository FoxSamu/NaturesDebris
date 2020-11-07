package natures.debris.common.block.plant.logic;

import java.util.Random;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import natures.debris.common.block.plant.Plant;

public interface ICollisionEffect {
    @OnlyIn(Dist.CLIENT)
    void playCollisionEffect(Plant plant, BlockState state, World world, BlockPos pos, Random rand, Entity entity);
}
