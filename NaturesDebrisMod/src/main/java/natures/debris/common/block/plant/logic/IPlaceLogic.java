package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import natures.debris.common.block.plant.Plant;

public interface IPlaceLogic {
    boolean canRemain(Plant plant, BlockState state, IBlockReader world, BlockPos pos);
    boolean canPlace(Plant plant, BlockState state, IBlockReader world, BlockPos pos);

    default boolean replaceable(Plant plant, BlockState state, World world, BlockPos pos, BlockItemUseContext ctx) {
        return false;
    }

    default BlockState placementState(Plant plant, BlockState state, World world, BlockPos pos, BlockItemUseContext ctx) {
        return update(plant, state, world, pos);
    }

    default BlockState update(Plant plant, BlockState state, IWorld world, BlockPos pos) {
        return state;
    }
}
