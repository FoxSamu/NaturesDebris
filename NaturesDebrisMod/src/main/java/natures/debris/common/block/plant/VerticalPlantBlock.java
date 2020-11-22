package natures.debris.common.block.plant;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class VerticalPlantBlock extends PlantBlock {
    protected final GrowDir growDir;

    public VerticalPlantBlock(Properties properties, GrowDir growDir) {
        super(properties);
        this.growDir = growDir;
    }

    public final GrowDir getGrowDir() {
        return growDir;
    }

    @Nonnull
    @Override
    public BlockPos getRootPos(IBlockReader world, BlockPos pos, BlockState state) {
        return growOffset(pos, -(getHeight(world, pos, state) - 1));
    }

    @Nonnull
    @Override
    public BlockPos getSoilPos(IBlockReader world, BlockPos pos, BlockState state) {
        return growOffset(pos, -getHeight(world, pos, state));
    }

    protected int getHeight(IBlockReader world, BlockPos pos, BlockState state) {
        return 1;
    }

    @Override
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state) {
        BlockPos soilPos = getSoilPos(world, pos, state);
        return canBlockSustain(world, soilPos, world.getBlockState(soilPos));
    }

    public boolean canBlockSustain(IBlockReader world, BlockPos pos, BlockState state) {
        return state.isSideSolidFullSquare(world, pos, growDir.getDir());
    }

    protected final BlockPos growOffset(BlockPos pos, int amount) {
        return pos.offset(growDir.getDir(), amount);
    }

    protected final BlockPos growOffset(BlockPos pos) {
        return pos.offset(growDir.getDir());
    }

    public enum GrowDir {
        UP(Direction.UP),
        DOWN(Direction.DOWN);

        private final Direction dir;

        GrowDir(Direction dir) {
            this.dir = dir;
        }

        public Direction getDir() {
            return dir;
        }
    }
}
