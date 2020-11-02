package natures.debris.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("deprecation")
public class MurkyGrassPathBlock extends Block {
    protected static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 15, 16);

    protected MurkyGrassPathBlock(Properties props) {
        super(props);
    }

    @Override // isTransparent
    public boolean func_220074_n(BlockState state) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return !getDefaultState().isValidPosition(ctx.getWorld(), ctx.getPos())
               ? nudgeEntitiesWithNewState(getDefaultState(), NdBlocks.MURKY_DIRT.getDefaultState(), ctx.getWorld(), ctx.getPos())
               : super.getStateForPlacement(ctx);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos) {
        if (facing == Direction.UP && !state.isValidPosition(world, pos)) {
            world.getPendingBlockTicks().scheduleTick(pos, this, 1);
        }

        return super.updatePostPlacement(state, facing, adjState, world, pos, adjPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        world.setBlockState(pos, nudgeEntitiesWithNewState(state, NdBlocks.MURKY_DIRT.getDefaultState(), world, pos));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        BlockState upstate = world.getBlockState(pos.up());
        return !upstate.getMaterial().isSolid() || upstate.getBlock() instanceof FenceGateBlock;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
        return false;
    }
}
