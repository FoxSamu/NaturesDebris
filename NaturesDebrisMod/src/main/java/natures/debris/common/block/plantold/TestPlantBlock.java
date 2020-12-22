package natures.debris.common.block.plantold;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

public class TestPlantBlock extends TallVerticalPlantBlock {
    public TestPlantBlock(Properties props) {
        super(props, GrowDir.UP);

        setDefaultState(getDefaultState().with(FlowingFluidBlock.LEVEL, 9));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FlowingFluidBlock.LEVEL);
    }

    @Override
    public boolean replaceable(IWorld world, BlockPos pos, BlockState state) {
        return true;
    }

    @Nullable
    @Override
    protected BlockPos spreadingPos(IWorld world, BlockPos pos, BlockState state, Random rand) {
        return findFeasibleHeight(
            3, world, randomMultiHOffset(1, 2, pos, rand),
            p -> canSpawnIn(world, p, 0),
            p -> canGenerate(world, pos, state)
        );
    }

    @Override
    public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean client) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
        kill(world, pos);
//        spread(world, getRootPos(world, pos, state), 4, 1);
    }
}
