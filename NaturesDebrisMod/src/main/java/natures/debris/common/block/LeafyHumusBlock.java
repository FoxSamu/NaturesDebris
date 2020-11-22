package natures.debris.common.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

import natures.debris.common.block.soil.Fertility;
import natures.debris.common.block.soil.ISoil;

public class LeafyHumusBlock extends MurkyDirtBlock implements ISoil {
    public LeafyHumusBlock(Properties props) {
        super(props);
    }

    protected BlockState getDecayBlock() {
        return NdBlocks.MURKY_HUMUS.getDefaultState();
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

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!canSustainDeadLeaves(state, world, pos)) {
            if (!world.isAreaLoaded(pos, 3)) return;
            world.setBlockState(pos, getDecayBlock());
        }
    }

    @Override
    public Fertility getFertility(IWorld world, BlockPos pos, BlockState state) {
        return Fertility.FERTILE;
    }

    @Override
    public void setFertility(IWorld world, BlockPos pos, BlockState state, Fertility fertility) {

    }

    @Override
    public int getLevel(IWorld world, BlockPos pos, BlockState state) {
        return 1;
    }

    @Override
    public void setLevel(IWorld world, BlockPos pos, BlockState state, int level) {

    }

    @Override
    public boolean isWet(IWorld world, BlockPos pos, BlockState state) {
        return false;
    }
}
