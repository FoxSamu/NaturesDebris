package natures.debris.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import natures.debris.common.block.NdBlocks;

@SuppressWarnings("deprecation")
public class MurkGrass2Block extends DoublePlantBlock {
    private static final VoxelShape LO_SHAPE = makeCuboidShape(1, 0, 1, 15, 16, 15);
    private static final VoxelShape HI_SHAPE = makeCuboidShape(1, 0, 1, 15, 14, 15);

    public MurkGrass2Block(Properties props) {
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
        return state.get(HALF) == DoubleBlockHalf.LOWER ? LO_SHAPE : HI_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return VoxelShapes.empty();
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
