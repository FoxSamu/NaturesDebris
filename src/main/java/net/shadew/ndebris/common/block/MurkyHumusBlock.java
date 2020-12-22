package net.shadew.ndebris.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

import java.util.Random;

public class MurkyHumusBlock extends MurkyDirtBlock {
    public MurkyHumusBlock(Settings props) {
        super(props);
    }

    protected boolean isGrowableDirt(BlockView world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == NdBlocks.MURKY_DIRT;
    }

    protected BlockState getLeafyBlock() {
        return NdBlocks.LEAFY_HUMUS.getDefaultState();
    }

    private static boolean canSustainDeadLeaves(BlockState state, WorldView world, BlockPos pos) {
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if (upState.getBlock() == Blocks.SNOW && upState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else {
            int opacity = ChunkLightProvider.getRealisticOpacity(world, state, pos, upState, upPos, Direction.UP, upState.getOpacity(world, upPos));
            return opacity < world.getMaxLightLevel();
        }
    }

    private static boolean canBecomeLeafy(BlockState state, WorldView world, BlockPos pos) {
        BlockPos up = pos.up();
        return canSustainDeadLeaves(state, world, pos) && !world.getFluidState(up).isIn(FluidTags.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        int blocked = 0;
        if (canBecomeLeafy(state, world, pos)) {
            BlockPos.Mutable mpos = new BlockPos.Mutable();
            for (int i = 1; i < 14; i++) {
                mpos.set(pos).move(Direction.UP, i);

                if (world.getBlockState(mpos).isSideSolidFullSquare(world, mpos, Direction.DOWN) || world.getBlockState(mpos).isSideSolidFullSquare(world, mpos, Direction.UP)) {
                    blocked |= 1;
                }

                if (world.getBlockState(mpos).isIn(BlockTags.LEAVES) && (blocked & 1) == 0) {
                    world.setBlockState(pos, getLeafyBlock());
                    return;
                }

                for (Direction dir : Direction.Type.HORIZONTAL) {
                    int mask = 2 << dir.getHorizontal();
                    mpos.move(dir);

                    if (world.getBlockState(mpos).isSideSolidFullSquare(world, mpos, Direction.DOWN) || world.getBlockState(mpos).isSideSolidFullSquare(world, mpos, Direction.UP)) {
                        blocked |= mask;
                    }
                    if (world.getBlockState(mpos).isIn(BlockTags.LEAVES) && (blocked & (mask | 1)) != (mask | 1)) {
                        world.setBlockState(pos, getLeafyBlock());
                        return;
                    }
                    mpos.move(dir, -1);
                }
            }
        }
    }
}
