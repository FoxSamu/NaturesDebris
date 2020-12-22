package natures.debris.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import natures.debris.common.block.NdBlocks;

@SuppressWarnings("deprecation")
public class MurkGrass1Block extends BushBlock {
    private static final VoxelShape SHAPE = makeCuboidShape(2, 0, 2, 14, 13, 14);

    public MurkGrass1Block(Properties props) {
        super(props);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
        return super.isValidGround(state, world, pos)
                   || state.isIn(NdBlocks.MURKY_DIRT)
                   || state.isIn(NdBlocks.MURKY_COARSE_DIRT)
                   || state.isIn(NdBlocks.MURKY_GRASS_BLOCK)
                   || state.isIn(NdBlocks.MURKY_HUMUS)
                   || state.isIn(NdBlocks.LEAFY_HUMUS)
                   || state.isIn(NdBlocks.MURKY_PODZOL);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return VoxelShapes.empty();
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XYZ;
    }
}
