package net.shadew.ndebris.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

@SuppressWarnings("deprecation")
public class MurkyGrassPathBlock extends Block {
    protected static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 15, 16);

    protected MurkyGrassPathBlock(Settings props) {
        super(props);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return false;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return !getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos())
               ? pushEntitiesUpBeforeBlockChange(getDefaultState(), NdBlocks.MURKY_DIRT.getDefaultState(), ctx.getWorld(), ctx.getBlockPos())
               : super.getPlacementState(ctx);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState adjState, WorldAccess world, BlockPos pos, BlockPos adjPos) {
        if (facing == Direction.UP && !state.canPlaceAt(world, pos)) {
            world.getBlockTickScheduler().schedule(pos, this, 1);
        }

        return super.getStateForNeighborUpdate(state, facing, adjState, world, pos, adjPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, NdBlocks.MURKY_DIRT.getDefaultState(), world, pos));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState upstate = world.getBlockState(pos.up());
        return !upstate.getMaterial().isSolid() || upstate.getBlock() instanceof FenceGateBlock;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
