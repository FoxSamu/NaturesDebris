package natures.debris.common.block.plant.logic;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import natures.debris.common.block.plant.Plant;

public interface IGrowingLogic {
    void grow(Plant plant, BlockState state, IWorld world, BlockPos pos, Random rand, ILeveledGrowingContext ctx);
    void decay(Plant plant, BlockState state, IWorld world, BlockPos pos, Random rand, ILeveledGrowingContext ctx);
    void heal(Plant plant, BlockState state, IWorld world, BlockPos pos, Random rand, ILeveledGrowingContext ctx);
    void kill(Plant plant, BlockState state, IWorld world, BlockPos pos, Random rand, IGrowingContext ctx);
    void place(Plant plant, IWorld world, BlockPos pos, Random rand, IGrowingContext ctx);
    boolean canReplaceOnGrow(Plant plant, BlockState state, IBlockReader world, BlockPos pos);
}
