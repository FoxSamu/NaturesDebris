package natures.debris.common.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

public class MurkyGrassBlock extends MurkyDirtBlock {
    public MurkyGrassBlock(Properties props) {
        super(props);
    }

    protected boolean isGrowableDirt(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == NdBlocks.MURKY_DIRT;
    }

    protected BlockState getDecayBlock() {
        return NdBlocks.MURKY_DIRT.getDefaultState();
    }

    private static boolean canSustainGrass(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if (upState.getBlock() == Blocks.SNOW && upState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else {
            int opacity = LightEngine.func_215613_a(world, state, pos, upState, upPos, Direction.UP, upState.getOpacity(world, upPos));
            return opacity < world.getMaxLightLevel();
        }
    }

    private static boolean canGrow(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos up = pos.up();
        return canSustainGrass(state, world, pos) && !world.getFluidState(up).isTagged(FluidTags.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!canSustainGrass(state, world, pos)) {
            if (!world.isAreaLoaded(pos, 3)) return;
            world.setBlockState(pos, getDecayBlock());
        } else {
            BlockState myState = getDefaultState();

            for (int i = 0; i < 4; ++i) {
                BlockPos randomPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                if (isGrowableDirt(world, randomPos) && canGrow(myState, world, randomPos)) {
                    world.setBlockState(randomPos, myState);
                }
            }
        }
    }
}
