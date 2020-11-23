package natures.debris.common.block.plant;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import natures.debris.core.util.WorldEvents;

public class DoubleVerticalPlantBlock extends VerticalPlantBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public DoubleVerticalPlantBlock(Properties properties, GrowDir growDir) {
        super(properties, growDir);

        setDefaultState(stateContainer.getBaseState().with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    protected int getHeight(IBlockReader world, BlockPos pos, BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.UPPER ? 2 : 1;
    }

    @Override
    public BlockState updateStateDirectionally(IWorld world, BlockPos pos, BlockState state, Direction dir, BlockState adjState, BlockPos adjPos) {
        Direction up = growDir.getDir();
        Direction down = up.getOpposite();

        DoubleBlockHalf half = state.get(HALF);
        if (dir.getAxis() != Direction.Axis.Y
                || half == DoubleBlockHalf.LOWER != (dir == up)
                || adjState.isIn(this) && adjState.get(HALF) != half) {

            return half == DoubleBlockHalf.LOWER && dir == down && !state.isValidPosition(world, pos)
                   ? Blocks.AIR.getDefaultState()
                   : super.updateStateDirectionally(world, pos, state, dir, adjState, adjPos);
        }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public BlockState placementState(World world, BlockPos pos, BlockState state, BlockItemUseContext ctx) {
        BlockPos off = growOffset(pos);
        if (off.getY() < 0 || off.getY() > 255)
            return null;

        return world.getBlockState(off).isReplaceable(ctx)
               ? super.placementState(world, pos, state, ctx)
               : null;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack item) {
        placeAt(world, growOffset(pos), getDefaultState().with(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state) {
        if (state.getBlock() != this) {
            return super.canRemain(world, pos, state);
        }

        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            return super.canRemain(world, pos, state);
        }

        BlockState other = world.getBlockState(growOffset(pos, -1));
        return other.isIn(this) && other.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public boolean spawn(IWorld world, BlockPos pos, BlockState origin, int meta) {
        if (!world.isAirBlock(pos))
            return false;

        BlockPos off = growOffset(pos);
        if (!world.isAirBlock(off))
            return false;

        BlockState lower = getDefaultState().with(HALF, DoubleBlockHalf.LOWER);
        BlockState upper = getDefaultState().with(HALF, DoubleBlockHalf.UPPER);

        return placeAt(world, pos, lower, 3) && placeAt(world, off, upper, 3);
    }

    @Override
    public boolean kill(IWorld world, BlockPos pos) {
        if (!isThisPlant(world, pos))
            return false;

        BlockState state = world.getBlockState(pos);
        BlockPos root = getRootPos(world, pos, state);
        BlockPos end = growOffset(root);
        BlockState fromRoot = world.getBlockState(root);

        boolean removed = removeAt(world, root, 3 | 16) && removeAt(world, end, 3);
        if (removed) {
            BlockState toRoot = world.getBlockState(root);
            doManualBlockUpdate(world, root, fromRoot, toRoot, 3, 512);
        }

        return removed;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isRemote && state.get(HALF) == DoubleBlockHalf.LOWER) {
            spawnDrops(state, world, pos, null, player, player.getHeldItemMainhand());
        }

        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack item) {
        player.addStat(Stats.BLOCK_MINED.get(this));
        player.addExhaustion(0.005f);
    }

    protected final void destroyInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf half = state.get(HALF);
        if (half == DoubleBlockHalf.UPPER) {
            BlockPos off = growOffset(pos, -1);
            BlockState other = world.getBlockState(off);

            if (other.getBlock() == state.getBlock() && other.get(HALF) == DoubleBlockHalf.LOWER) {
                removeAt(world, off, 3 | 32);
                world.playEvent(player, WorldEvents.DESTROY, off, getStateId(other));
            }
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (!world.isRemote) {
            if (player.isCreative()) {
                BlockPos root = getRootPos(world, pos, state);
                BlockPos end = growOffset(root);
                BlockState fromRoot = world.getBlockState(root);

                world.playEvent(pos == root ? player : null, WorldEvents.DESTROY, root, getStateId(world.getBlockState(root)));
                world.playEvent(pos == end ? player : null, WorldEvents.DESTROY, end, getStateId(world.getBlockState(end)));

                removeAt(world, root, 3 | 16 | 32);
                removeAt(world, end, 3 | 32);

                BlockState toRoot = world.getBlockState(root);
                doManualBlockUpdate(world, root, fromRoot, toRoot, 3 | 32, 512);
                return true;
            }
        }

        onBlockHarvested(world, pos, state, player);
        return removeAt(world, pos, world.isRemote ? 3 | 8 : 3);
    }

    private static void doManualBlockUpdate(IWorld world, BlockPos pos, BlockState fromState, BlockState toState, int flags, int iterationDepth) {
        int f = flags & ~(32 | 1);
        fromState.prepare(world, pos, f, iterationDepth - 1);
        toState.updateNeighbors(world, pos, f, iterationDepth - 1);
        toState.prepare(world, pos, f, iterationDepth - 1);
    }
}
