package natures.debris.common.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

public class MurkyHumusBlock extends MurkyDirtBlock {
    public MurkyHumusBlock(Properties props) {
        super(props);
    }

    protected boolean isGrowableDirt(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == NdBlocks.MURKY_DIRT;
    }

    protected BlockState getLeafyBlock() {
        return NdBlocks.LEAFY_HUMUS.getDefaultState();
    }

    private static boolean canSustainDeadLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if (upState.getBlock() == Blocks.SNOW && upState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else {
            int opacity = LightEngine.func_215613_a(world, state, pos, upState, upPos, Direction.UP, upState.getOpacity(world, upPos));
            return opacity < world.getMaxLightLevel();
        }
    }

    private static boolean canBecomeLeafy(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos up = pos.up();
        return canSustainDeadLeaves(state, world, pos) && !world.getFluidState(up).isTagged(FluidTags.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        int blocked = 0;
        if (canBecomeLeafy(state, world, pos)) {
            BlockPos.Mutable mpos = new BlockPos.Mutable();
            for (int i = 1; i < 14; i++) {
                mpos.setPos(pos).move(Direction.UP, i);

                if (world.getBlockState(mpos).isSolidSide(world, mpos, Direction.DOWN) || world.getBlockState(mpos).isSolidSide(world, mpos, Direction.UP)) {
                    blocked |= 1;
                }

                if (world.getBlockState(mpos).isIn(BlockTags.LEAVES) && (blocked & 1) == 0) {
                    world.setBlockState(pos, getLeafyBlock());
                    return;
                }

                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    int mask = 2 << dir.getHorizontalIndex();
                    mpos.move(dir);

                    if (world.getBlockState(mpos).isSolidSide(world, mpos, Direction.DOWN) || world.getBlockState(mpos).isSolidSide(world, mpos, Direction.UP)) {
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
