package natures.debris.common.block.plantold;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import natures.debris.core.util.WorldEvents;

/**
 * A double vertical plant, which is exactly two blocks high.
 */
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
        if (!canSpawnIn(world, pos, 0))
            return false;

        BlockPos off = growOffset(pos);
        if (!canSpawnIn(world, off, 1))
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

        return removeDoubleAt(world, root, end, 3);
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack item) {
        player.addStat(Stats.BLOCK_MINED.get(this));
        player.addExhaustion(0.005f);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        BlockPos root = getRootPos(world, pos, state);
        BlockPos end = growOffset(root);
        if (!world.isRemote) {
            // Exclude caused player from events, player client will play these events itself (see below)
            world.playEvent(player, WorldEvents.DESTROY, root, getStateId(world.getBlockState(root)));
            world.playEvent(player, WorldEvents.DESTROY, end, getStateId(world.getBlockState(end)));

            if (!removeDoubleAt(world, root, end, 3 | 32)) {
                return false;
            }

            if (!player.isCreative()) {
                spawnDrops(state, world, pos, null, player, player.getHeldItemMainhand());
            }

            // From: onBlockHarvested
            // We don't reach onBlockHarvested so we do it here
            if (isIn(BlockTags.GUARDED_BY_PIGLINS)) {
                PiglinTasks.onGuardedBlockInteracted(player, false);
            }

            return true;
        } else {
            // Play these events manually on client, we don't need to send a useless packet on the server when we
            // can predict this is gonna happen
            world.playEvent(null, WorldEvents.DESTROY, root, getStateId(world.getBlockState(root)));
            world.playEvent(null, WorldEvents.DESTROY, end, getStateId(world.getBlockState(end)));

            return removeDoubleAt(world, root, end, 3 | 8);
        }
    }

    protected final boolean removeDoubleAt(IWorld world, BlockPos root, BlockPos end, int flags) {
        BlockState fromRoot = world.getBlockState(root);
        if (removeAt(world, root, flags | 16) && removeAt(world, end, flags)) {
            if ((flags & 16) == 0) {
                // Ensure we cause a block update for the lower block when needed (we skipped it ITFP)
                BlockState toRoot = world.getBlockState(root);
                doManualBlockUpdate(world, root, fromRoot, toRoot, flags, 512);
            }
            return true;
        }
        return false;
    }

    // This allows us to trigger updatePostPlacement manually on neighbors
    // When removing this plant, we remove the lower block without updating neighbors, so that the upper block won't
    // destroy and drop an item without waiting for us to remove it (we might not want it to play the destroy effect at
    // all). However, removing the lower block without neighbor updates also means that observers and other relevant
    // blocks won't respond to our change either. Hence, after removing the upper block we call this function for the
    // lower block so that neighbors still get notified for removal of the lower block
    private static void doManualBlockUpdate(IWorld world, BlockPos pos, BlockState fromState, BlockState toState, int flags, int iterationDepth) {
        int f = flags & ~(32 | 1);
        fromState.prepare(world, pos, f, iterationDepth - 1);
        toState.updateNeighbors(world, pos, f, iterationDepth - 1);
        toState.prepare(world, pos, f, iterationDepth - 1);
    }
}
