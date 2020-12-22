package natures.debris.common.block.plantold;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import natures.debris.common.block.soil.ISoil;

/**
 * A plant that grows in one line vertically.
 */
public class VerticalPlantBlock extends PlantBlock {
    protected final GrowDir growDir;

    public VerticalPlantBlock(Properties properties, GrowDir growDir) {
        super(properties);
        this.growDir = growDir;
    }

    /**
     * Returns the growth direction of this plant
     */
    public final GrowDir getGrowDir() {
        return growDir;
    }

    @Nonnull
    @Override
    public BlockPos getRootPos(IBlockReader world, BlockPos pos, BlockState state) {
        return growOffset(pos, -(getHeight(world, pos, state) - 1));
    }

    @Nonnull
    public BlockPos getSupportPos(IBlockReader world, BlockPos pos, BlockState state) {
        return growOffset(pos, -getHeight(world, pos, state));
    }

    @Nullable
    @Override
    public BlockPos getSoilPos(IBlockReader world, BlockPos pos, BlockState state) {
        BlockPos support = getSupportPos(world, pos, state);
        BlockState soil = world.getBlockState(support);
        return soil.getBlock() instanceof ISoil ? support : null;
    }

    protected int getHeight(IBlockReader world, BlockPos pos, BlockState state) {
        return 1;
    }

    @Override
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state) {
        BlockPos support = getSupportPos(world, pos, state);
        return canBlockSustain(world, support, world.getBlockState(support));
    }

    /**
     * Returns whether the given block can sustain this plant.
     *
     * @param world The world the block is in
     * @param pos   The position of the block
     * @param state The state of the block
     * @return True when the block can sustain this plant
     */
    public boolean canBlockSustain(IBlockReader world, BlockPos pos, BlockState state) {
        return state.isSideSolidFullSquare(world, pos, growDir.getDir());
    }

    /**
     * Offsets the given position in the growth direction of this plant.
     *
     * @param pos    The position to offset
     * @param amount The amount of blocks to offset, may be negative to go in reverse direction
     * @return The new block position
     */
    protected final BlockPos growOffset(BlockPos pos, int amount) {
        return pos.offset(growDir.getDir(), amount);
    }

    /**
     * Offsets the given position one step in the growth direction of this plant.
     *
     * @param pos The position to offset
     * @return The new block position
     */
    protected final BlockPos growOffset(BlockPos pos) {
        return pos.offset(growDir.getDir());
    }

    /**
     * Offsets the given position in the growth direction of this plant.
     *
     * @param pos    The position to offset
     * @param amount The amount of blocks to offset, may be negative to go in reverse direction
     * @return The new block position
     */
    protected final BlockPos.Mutable growOffset(BlockPos.Mutable pos, int amount) {
        return pos.move(growDir.getDir(), amount);
    }

    /**
     * Offsets the given position one step in the growth direction of this plant.
     *
     * @param pos The position to offset
     * @return The new block position
     */
    protected final BlockPos.Mutable growOffset(BlockPos.Mutable pos) {
        return pos.move(growDir.getDir());
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
