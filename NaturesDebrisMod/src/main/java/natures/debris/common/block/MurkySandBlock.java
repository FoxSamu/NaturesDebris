package natures.debris.common.block;

import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class MurkySandBlock extends FallingBlock {
    public MurkySandBlock(Properties props) {
        super(props);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        PlantType type = plantable.getPlantType(world, pos);
        if (type == PlantType.BEACH) {
            return world.getBlockState(pos.east()).getMaterial() == Material.WATER ||
                       world.getBlockState(pos.west()).getMaterial() == Material.WATER ||
                       world.getBlockState(pos.north()).getMaterial() == Material.WATER ||
                       world.getBlockState(pos.south()).getMaterial() == Material.WATER;
        }
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
}
