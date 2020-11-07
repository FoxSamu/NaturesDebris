package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import natures.debris.common.block.plant.Plant;

public interface IPlantHitboxLogic {
    Direction getHitboxDirection(Plant plant, BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx);

    default Vector3d getHitboxOffset(Plant plant, BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx, Vector3d current) {
        return current;
    }
}
