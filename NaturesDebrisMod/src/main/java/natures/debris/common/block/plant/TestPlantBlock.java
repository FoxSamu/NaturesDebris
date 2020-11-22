package natures.debris.common.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class TestPlantBlock extends VerticalPlantBlock {
    public TestPlantBlock(Properties props) {
        super(props, GrowDir.UP);

        setDefaultState(stateContainer.getBaseState().with(FlowingFluidBlock.LEVEL, 9));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FlowingFluidBlock.LEVEL);
    }

    @Override
    public boolean replaceable(IWorld world, BlockPos pos, BlockState state) {
        return true;
    }
}
