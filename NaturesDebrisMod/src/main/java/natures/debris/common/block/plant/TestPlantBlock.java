package natures.debris.common.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class TestPlantBlock extends PlantBlock {
    public TestPlantBlock(Properties props) {
        super(props);

        setDefaultState(stateContainer.getBaseState().with(FlowingFluidBlock.LEVEL, 9));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FlowingFluidBlock.LEVEL);
    }

    @Override
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.UP);
    }

    @Override
    public boolean replaceable(IWorld world, BlockPos pos, BlockState state) {
        return true;
    }
}
