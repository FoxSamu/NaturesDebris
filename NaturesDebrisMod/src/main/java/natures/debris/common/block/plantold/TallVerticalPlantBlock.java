package natures.debris.common.block.plantold;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

/**
 * A plant that can grow infinitely tall
 */
public class TallVerticalPlantBlock extends VerticalPlantBlock {
    public TallVerticalPlantBlock(Properties properties, GrowDir growDir) {
        super(properties, growDir);
    }

    @Override
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state) {
        BlockPos below = growOffset(pos, -1);
        return isThisPlant(world, below) || super.canRemain(world, pos, state);
    }

    @Override
    protected int getHeight(IBlockReader world, BlockPos pos, BlockState state) {
        int h = 1; // First is given because in case of placement the plant might not be there yet
        BlockPos.Mutable mpos = new BlockPos.Mutable();
        mpos.setPos(pos);
        growOffset(mpos, -1);
        while (isThisPlant(world, mpos)) {
            growOffset(mpos, -1);
            h++;
        }
        return h;
    }

    @Override
    protected int spreadingMeta(IWorld world, BlockPos pos, BlockState state, Random rand) {
        return 1; // We need 1 because meta for tall plants determines its height
    }

    protected BlockState spawnState(IWorld world, BlockPos pos, BlockState origin, int height) {
        return updateState(world, pos, origin);
    }

    @Override
    public boolean spawn(IWorld world, BlockPos pos, BlockState origin, int meta) {
        BlockPos.Mutable mpos = new BlockPos.Mutable();
        mpos.setPos(pos);

        int h = 0;
        for (int i = 0; i < meta; i++) {
            if (!canSpawnIn(world, mpos, i))
                break;
            growOffset(mpos);
            h++;
        }

        if (h < 1) return false;

        mpos.setPos(pos);
        for (int i = 0; i < h; i++) {
            if (!placeAt(world, mpos, spawnState(world, mpos, origin, i), 3))
                return false;
            growOffset(mpos);
        }

        return true;
    }

    @Override
    public boolean kill(IWorld world, BlockPos pos) {
        if (!isThisPlant(world, pos))
            return false;

        BlockPos.Mutable mpos = new BlockPos.Mutable();
        mpos.setPos(pos);

        while (isThisPlant(world, mpos)) {
            growOffset(mpos);
        }

        growOffset(mpos, -1);
        while (isThisPlant(world, mpos)) {
            if (!removeAt(world, mpos, 3)) {
                return false;
            }
            growOffset(mpos, -1);
        }

        return true;
    }
}
