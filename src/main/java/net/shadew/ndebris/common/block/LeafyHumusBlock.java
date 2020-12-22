package net.shadew.ndebris.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

import java.util.Random;

public class LeafyHumusBlock extends MurkyDirtBlock {
    public LeafyHumusBlock(Settings props) {
        super(props);
    }

    protected BlockState getDecayBlock() {
        return NdBlocks.MURKY_HUMUS.getDefaultState();
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

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!canSustainDeadLeaves(state, world, pos)) {
            world.setBlockState(pos, getDecayBlock());
        }
    }
}
