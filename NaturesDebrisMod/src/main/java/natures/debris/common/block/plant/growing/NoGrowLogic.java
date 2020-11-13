package natures.debris.common.block.plant.growing;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import natures.debris.common.block.plant.PlantBlock;

public class NoGrowLogic implements IGrowLogic {
    public static final NoGrowLogic INSTANCE = new NoGrowLogic();

    @Override
    public GrowType canGrow(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx) {
        return GrowType.NONE;
    }

    @Override
    public void grow(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx) {

    }

    @Override
    public void decay(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx) {

    }

    @Override
    public void kill(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx) {

    }

    @Override
    public void heal(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx) {

    }
}
