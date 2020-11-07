package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import natures.debris.common.block.plant.Plant;

public interface IPlantHitbox {
    IPlantHitbox EMPTY = (plant, state, world, pos, ctx, facing, offset) -> VoxelShapes.empty();
    IPlantHitbox FULL_CUBE = (plant, state, world, pos, ctx, facing, offset) -> VoxelShapes.fullCube();

    VoxelShape getShape(Plant plant, BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx, Direction facing, Vector3d offset);
}
