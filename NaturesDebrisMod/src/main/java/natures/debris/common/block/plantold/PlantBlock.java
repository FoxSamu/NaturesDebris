package natures.debris.common.block.plantold;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.shadew.util.contract.Validate;

import natures.debris.core.util.OptionalUtil;
import natures.debris.core.util.reflect.MethodAccessor;
import natures.debris.common.block.plantold.sys.fluid.FluidReaction;
import natures.debris.common.block.plantold.sys.fluid.IFluidLogic;
import natures.debris.common.block.plantold.sys.fluid.NoFluidLogic;
import natures.debris.common.block.plantold.sys.growing.IGrowLogic;
import natures.debris.common.block.plantold.sys.growing.NoGrowLogic;
import natures.debris.common.block.soil.ISoil;
import natures.debris.common.block.soil.ISoilContext;

/**
 * A block that forms the base for all Nature's Debris plants
 */
@SuppressWarnings("deprecation")
public abstract class PlantBlock extends Block implements ILiquidContainer, IBucketPickupHandler, IPlantable, IGrowable {
    private static final MethodAccessor<FlowingFluid, Void> BEFORE_REPLACING_BLOCK_ACCESS = new MethodAccessor<>(
        FlowingFluid.class, "func_205580_a", "beforeReplacingBlock",
        IWorld.class, BlockPos.class, BlockState.class
    );

    private PlantType type;
    private OffsetType offsetType;

    private Function<BlockState, VoxelShape> hitbox;
    private Function<BlockState, VoxelShape> collider;

    private IFluidLogic fluidLogic;
    private IGrowLogic growLogic;

    public PlantBlock(Properties properties) {
        super(properties.blockProps);
        this.type = properties.type;
        this.offsetType = properties.offset;
        this.hitbox = properties.hitbox;
        this.collider = properties.collider;
        this.fluidLogic = properties.fluidLogic;
        this.growLogic = properties.growLogic;
    }


    /**
     * Returns the root position of this plant at the given position. The root position is the position where the plant
     * is attached, if it is, and otherwise it's the position of the plant itself (the position given). Often, when the
     * plant at the returned position is broken, the complete plant will break. For example, in case of a double plant,
     * the root position is that of the lower block.
     * <p>
     * For any given position that is part of the same plant, this method should likely return the same position.
     * </p>
     *
     * @param world The world this plant is in
     * @param pos   The position of the plant
     * @param state The block state of the plant
     * @return The root position
     */
    @Nonnull
    public BlockPos getRootPos(IBlockReader world, BlockPos pos, BlockState state) {
        return pos;
    }

    /**
     * Returns the soil position of this plant at the given position. The soil position is the position of the soil
     * block the plant is attached to, if any. Typically this should only return a non-null value when the block the
     * plant is attached to is a soil (implements {@link ISoil}). When the returned value is non-null, the plant will
     * receive events about changes of this soil and is able to obtain a {@link ISoilContext} at any time.
     *
     * @param world The world this plant is in
     * @param pos   The position of the plant
     * @param state The block state of the plant
     * @return The soil position
     */
    @Nullable
    public BlockPos getSoilPos(IBlockReader world, BlockPos pos, BlockState state) {
        return null;
    }

    /**
     * Adds this plant to a world, at the given position. This method directly delegates to {@link #spawn(IWorld,
     * BlockPos, BlockState, int)}, and uses the {@linkplain #getDefaultState() default state} and meta value 0 as
     * default parameter values.
     *
     * @param world The world to add this plant to
     * @param pos   The position to place it at
     * @return True when the plant was successfully spawned
     */
    public boolean spawn(IWorld world, BlockPos pos) {
        return spawn(world, pos, getDefaultState(), 0);
    }

    /**
     * Adds this plant to a world, at the given position. This method directly delegates to {@link #spawn(IWorld,
     * BlockPos, BlockState, int)}, and uses the {@linkplain #getDefaultState() default state} as default parameter
     * value. The given meta value can be any integer, it depends on the plant how this value is used.
     *
     * @param world The world to spawn this plant in
     * @param pos   The position to spawn it at
     * @param meta  The meta value given
     * @return True when the plant was successfully spawned
     */
    public boolean spawn(IWorld world, BlockPos pos, int meta) {
        return spawn(world, pos, getDefaultState(), meta);
    }

    /**
     * Adds this plant to a world, at the given position. This method directly delegates to {@link #spawn(IWorld,
     * BlockPos, BlockState, int)}, and uses the meta value 0 as default parameter value. The origin block state is a
     * valid state of this plant which is usually the plant the new plant originated from, but it might also be the
     * default state of this plant.
     *
     * @param world  The world to spawn this plant in
     * @param pos    The position to spawn it at
     * @param origin The origin block state
     * @return True when the plant was successfully spawned
     */
    public boolean spawn(IWorld world, BlockPos pos, BlockState origin) {
        return spawn(world, pos, origin, 0);
    }

    /**
     * Adds this plant to a world, at the given position. This is likely the only method you want to override when
     * implementing a different spawning behaviour of a plant.
     * <p>
     * The origin block state is a valid state of this plant which is usually the plant the new plant originated from,
     * but it might also be the default state of this plant. Ideally, when spawning, the properties of this origin state
     * are duplicated, but it is not required. When placing, some properties might also be ignored, such as what part of
     * the plant the given state is (e.g. upper part, lower part) or the age of the origin plant.
     * </p>
     * <p>
     * The meta value is a value that is used differently per plant. Some plants may use this to determine the type of
     * plant placed, and other plants might use it to determine their height
     * </p>
     * <p>
     * When spawning, the environment should be checked, and to place the plant blocks {@link #placeAt} should ideally
     * be used. When the plant was unable to spawn at the given position, this method should return false.
     * </p>
     *
     * @param world  The world to spawn this plant in
     * @param pos    The position to spawn it at
     * @param origin The origin block state
     * @return True when the plant was successfully spawned
     */
    public boolean spawn(IWorld world, BlockPos pos, BlockState origin, int meta) {
        return canSpawnIn(world, pos, meta) && placeAt(world, pos, origin, 3);
    }

    /**
     * Checks whether this plant can be spawned at the given position. This is often the method called from {@link
     * #spawn(IWorld, BlockPos, BlockState, int)} to check whether a given position for the plant is valid. This should
     * at least consider that plants can be placed in air and any level of water. The {@linkplain #getFluidLogic() fluid
     * logic} of the plant may be checked, but this one is already hinted itself when using {@link #placeAt}.
     *
     * @param world The world this plant is tried to spawn in
     * @param pos   The position the plant might spawn at
     * @param meta  The spawn meta, see {@link #spawn(IWorld, BlockPos, BlockState, int)} for more info
     * @return True when the plant can be spawned at the given positionc
     */
    public boolean canSpawnIn(IWorld world, BlockPos pos, int meta) {
        if (world.isAirBlock(pos)) {
            return true;
        }
        BlockState state = world.getBlockState(pos);
        return state.getMaterial().isLiquid();
    }

    /**
     * Removes the plant at the given position. This should ideally remove the complete plant and not only the block at
     * the given position. This should not play any destroy effects, the plant should disappear silently and should not
     * drop any item. Ideally this method first checks whether the plant is at the given position and then removes it
     * via {@link #removeAt}.
     *
     * @param world The world this plant is in
     * @param pos   The position of the plant
     * @return True when the plant was successfully removed
     */
    public boolean kill(IWorld world, BlockPos pos) {
        return isThisPlant(world, pos) && removeAt(world, pos, 3);
    }


    // Growing
    // ========================================================================

    /**
     * Returns a random position in the given bounding box. The bounding box is local to the given position.
     *
     * @param minX Local lower bound x (inclusive)
     * @param minY Local lower bound y (inclusive)
     * @param minZ Local lower bound z (inclusive)
     * @param maxX Local upper bound x (inclusive)
     * @param maxY Local upper bound y (inclusive)
     * @param maxZ Local upper bound z (inclusive)
     * @param pos  The global position of the local box
     * @param rand The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomInLocalBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPos pos, Random rand) {
        int rx = rand.nextInt(maxX - minX + 1) + minX;
        int ry = rand.nextInt(maxY - minY + 1) + minY;
        int rz = rand.nextInt(maxZ - minZ + 1) + minZ;

        return pos.add(rx, ry, rz);
    }

    /**
     * Returns a random position in the given bounding box. The bounding box is local to the given position, and the box
     * is centered horizontally at the given position.
     *
     * @param radius The horizontal radius of the box, the size of x and z axis can be computed from this via {@code
     *               radius * 2 + 1}
     * @param minY   Local lower bound y (inclusive)
     * @param maxY   Local upper bound y (inclusive)
     * @param pos    The global position of the local box
     * @param rand   The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomInLocalBox(int radius, int minY, int maxY, BlockPos pos, Random rand) {
        return randomInLocalBox(-radius, minY, -radius, radius, maxY, radius, pos, rand);
    }

    /**
     * Returns a random position in the given bounding box. The bounding box is local to the given position, and the box
     * is centered horizontally and vertically at the given position.
     *
     * @param radiusXZ The horizontal radius of the box, the size of x and z axis can be computed from this via {@code
     *                 radiusXZ * 2 + 1}
     * @param radiusY  The vertical radius of the box, the size of the y axis can be computed from this via {@code
     *                 radiusY * 2 + 1}
     * @param pos      The global position of the local box
     * @param rand     The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomInLocalBox(int radiusXZ, int radiusY, BlockPos pos, Random rand) {
        return randomInLocalBox(radiusXZ, -radiusY, radiusY, pos, rand);
    }

    /**
     * Returns a random position in the given bounding box. The bounding box is local to the given position, and the box
     * is centered horizontally and vertically at the given position.
     *
     * @param radius The radius of the box, the size of x, y and z axis can be computed from this via {@code radius * 2
     *               + 1}
     * @param pos    The global position of the local box
     * @param rand   The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomInLocalBox(int radius, BlockPos pos, Random rand) {
        return randomInLocalBox(radius, radius, pos, rand);
    }

    /**
     * Returns a random horizontal neighbor of the given position, shifted up or down by a random value. The vertical
     * bounds are local to the given position.
     *
     * @param minY Local lower bound y (inclusive)
     * @param maxY Local upper bound y (inclusive)
     * @param pos  The global position
     * @param rand The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomHOffset(int minY, int maxY, BlockPos pos, Random rand) {
        Direction dir = Direction.byHorizontalIndex(rand.nextInt(4));
        int ry = rand.nextInt(maxY - minY + 1) + minY;
        return pos.offset(dir).add(0, ry, 0);
    }

    /**
     * Returns a random horizontal neighbor of the given position, shifted up or down by a random value. The vertical
     * bounds are local and centered to the given position.
     *
     * @param radiusY The radius of the vertical bounds, the size of this bound can be computed via {@code radiusY * 2 +
     *                1}
     * @param pos     The global position
     * @param rand    The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomHOffset(int radiusY, BlockPos pos, Random rand) {
        return randomHOffset(-radiusY, radiusY, pos, rand);
    }

    /**
     * Returns a position in a biased, diamond-shaped distribution around the given position, where the y value is
     * chosen randomly between the given bounds. This method takes a random amount of iterations between 1 and the given
     * amount of iterations and moves the given position in any random horizontal direction every iteration. This
     * additionally ensures that the offsetted position is never the same as the given position (ignoring y axis).
     *
     * @param minY       Local lower bound y (inclusive)
     * @param maxY       Local upper bound y (inclusive)
     * @param iterations The max amount of iterations
     * @param pos        The global position
     * @param rand       The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomMultiHOffset(int minY, int maxY, int iterations, BlockPos pos, Random rand) {
        iterations -= rand.nextInt(rand.nextInt(iterations) + 1);
        int offX = 0, offZ = 0;
        while ((offX == 0 && offZ == 0) || iterations > 0) {
            Direction dir = Direction.byHorizontalIndex(rand.nextInt(4));
            offX += dir.getXOffset();
            offZ += dir.getZOffset();
            iterations--;
        }
        int ry = rand.nextInt(maxY - minY + 1) + minY;
        return pos.add(offX, ry, offZ);
    }

    /**
     * Returns a position in a biased, diamond-shaped distribution around the given position, where the y value is
     * chosen randomly between the given bounds. This method takes a random amount of iterations between 1 and the given
     * amount of iterations and moves the given position in any random horizontal direction every iteration. This
     * additionally ensures that the offsetted position is never the same as the given position (ignoring y axis).
     *
     * @param radiusY    The radius of the vertical bounds, the size of this bound can be computed via {@code radiusY *
     *                   2 + 1}
     * @param iterations The max amount of iterations
     * @param pos        The global position
     * @param rand       The random number generator to use for random values
     * @return The random position, in global coordinates
     */
    protected static BlockPos randomMultiHOffset(int radiusY, int iterations, BlockPos pos, Random rand) {
        return randomMultiHOffset(-radiusY, radiusY, iterations, pos, rand);
    }

    /**
     * Finds a feasible height that is stable for a plant and where there is enough space for that plant to grow. This
     * method accepts two predicates: one checks whether there is enough space at the given position, and the other
     * checks whether the plant is stable at the given position. Only when both predicates return true for the same
     * position, the position is feasible.
     * <p>
     * The searching for a feasible height is as follows:
     * <ul>
     * <li>The method starts at the given position and checks it against {@code empty}</li>
     * <li>When {@code empty} returned false, it moves up until {@code empty} returns true</li>
     * <li>When {@code empty} returns true, it checks {@code stable}</li>
     * <li>When {@code stable} returned false and it did not already move up, it moves down until {@code stable} returns
     * true.</li>
     * <li>When both {@code empty} and {@code stable} return true, it returns the position found</li>
     * <li>In any other case it returns null</li>
     * </ul>
     * This method never goes down when it already moved up, and vice versa, to prevent recursion and infinite loops.
     * </p>
     *
     * @param minY   Local lower bound y (inclusive)
     * @param maxY   Local upper bound y (inclusive)
     * @param itrs   The maximum amount of iterations
     * @param world  The world to check in
     * @param pos    The position to check at
     * @param empty  The predicate that checks for empty space
     * @param stable The predicate that checks whether the plant is stable
     * @return The stable position found, if any, and null otherwise
     */
    protected static BlockPos findFeasibleHeight(int minY, int maxY, int itrs, IWorld world, BlockPos pos, Predicate<BlockPos> empty, Predicate<BlockPos> stable) {
        BlockPos.Mutable mpos = new BlockPos.Mutable();
        int y = 0;
        int dir = 0;
        while (itrs > 0) {
            itrs--;
            mpos.setPos(pos).move(0, y, 0);
            if (y < minY || y > maxY)
                return null;
            if (!empty.test(mpos)) {
                if (dir < 0)
                    return null; // Recursing, we go back up to the place that pushed us down
                dir = 1;
                y++;
            } else {
                if (stable.test(mpos))
                    return mpos.toImmutable();
                if (dir > 0)
                    return null; // Recursing, we go back down to the place that pushed us up
                dir = -1;
                y--;
            }
        }
        return null;
    }

    /**
     * Finds a feasible height that is stable for a plant and where there is enough space for that plant to grow. This
     * method accepts two predicates: one checks whether there is enough space at the given position, and the other
     * checks whether the plant is stable at the given position. Only when both predicates return true for the same
     * position, the position is feasible.
     * <p>
     * See the definition of {@link #findFeasibleHeight(int, int, int, IWorld, BlockPos, Predicate, Predicate)} for more
     * information about this method.
     * </p>
     *
     * @param minY   Local lower bound y (inclusive)
     * @param maxY   Local upper bound y (inclusive)
     * @param world  The world to check in
     * @param pos    The position to check at
     * @param empty  The predicate that checks for empty space
     * @param stable The predicate that checks whether the plant is stable
     * @return The stable position found, if any, and null otherwise
     */
    protected static BlockPos findFeasibleHeight(int minY, int maxY, IWorld world, BlockPos pos, Predicate<BlockPos> empty, Predicate<BlockPos> stable) {
        return findFeasibleHeight(minY, maxY, Math.max(minY, maxY) + 2, world, pos, empty, stable);
    }

    /**
     * Finds a feasible height that is stable for a plant and where there is enough space for that plant to grow. This
     * method accepts two predicates: one checks whether there is enough space at the given position, and the other
     * checks whether the plant is stable at the given position. Only when both predicates return true for the same
     * position, the position is feasible.
     * <p>
     * See the definition of {@link #findFeasibleHeight(int, int, int, IWorld, BlockPos, Predicate, Predicate)} for more
     * information about this method.
     * </p>
     *
     * @param radius The radius of the vertical bounds, the size of this bound can be computed via {@code radius * 2 +
     *               1}
     * @param world  The world to check in
     * @param pos    The position to check at
     * @param empty  The predicate that checks for empty space
     * @param stable The predicate that checks whether the plant is stable
     * @return The stable position found, if any, and null otherwise
     */
    protected static BlockPos findFeasibleHeight(int radius, IWorld world, BlockPos pos, Predicate<BlockPos> empty, Predicate<BlockPos> stable) {
        return findFeasibleHeight(-radius, radius, world, pos, empty, stable);
    }

    /**
     * Finds a feasible height that is stable for a plant and where there is enough space for that plant to grow. This
     * method accepts two predicates: one checks whether there is enough space at the given position, and the other
     * checks whether the plant is stable at the given position. Only when both predicates return true for the same
     * position, the position is feasible. The vertical bounds go from 0 to the world height.
     * <p>
     * See the definition of {@link #findFeasibleHeight(int, int, int, IWorld, BlockPos, Predicate, Predicate)} for more
     * information about this method.
     * </p>
     *
     * @param world  The world to check in
     * @param pos    The position to check at
     * @param empty  The predicate that checks for empty space
     * @param stable The predicate that checks whether the plant is stable
     * @return The stable position found, if any, and null otherwise
     */
    protected static BlockPos findFeasibleHeight(IWorld world, BlockPos pos, Predicate<BlockPos> empty, Predicate<BlockPos> stable) {
        return findFeasibleHeight(0, world.getHeight(), world, pos, empty, stable);
    }

    /**
     * Returns a random position this plant can spread to. This can use functions such as {@link #findFeasibleHeight}
     * and {@link #randomInLocalBox}. When this method returns null, the spreading iteration is cancelled.
     *
     * @param world The world to spread in
     * @param pos   The root position of the origin plant
     * @param state The state to be planted, as returned by {@link #spreadingState}
     * @param rand  A random number generator
     * @return The random position (in global coordinates) where this plant should spread to.
     */
    @Nullable
    protected BlockPos spreadingPos(IWorld world, BlockPos pos, BlockState state, Random rand) {
        return null;
    }

    /**
     * Returns a random state that this plant spreads. Should return either null or a valid state of this plant. When
     * this method returns null, the spreading iteration is cancelled.
     *
     * @param world  The world to spread in
     * @param pos    The root position of the origin plant
     * @param origin The state (at root position) of the origin plant
     * @param rand   A random number generator
     * @return The random state to place
     */
    @Nullable
    protected BlockState spreadingState(IWorld world, BlockPos pos, BlockState origin, Random rand) {
        return getDefaultState();
    }

    /**
     * Returns random spreading meta to be passed to {@link #spawn(IWorld, BlockPos, BlockState, int)}. Can return any
     * value, dependent on what plant is being placed.
     *
     * @param world The world to spread in
     * @param pos   The position the plant spreads to, as returned by {@link #spreadingPos}
     * @param state The state to be planted, as returned by {@link #spreadingState}
     * @param rand  A random number generator
     * @return The random state to place
     */
    protected int spreadingMeta(IWorld world, BlockPos pos, BlockState state, Random rand) {
        return 0;
    }

    /**
     * Spreads this plant, i.e. duplicates to a nearby random position, often as a cause of growing.
     *
     * @param world      The world to spread in
     * @param pos        The position to spread at
     * @param iterations The maximum amount of attempts to spread
     * @param maxSuccess The maximum amount of successful attempts
     * @return The amount of successful attempts
     */
    public final int spread(IWorld world, BlockPos pos, int iterations, int maxSuccess) {
        if (isThisPlant(world, pos)) {
            BlockState state = world.getBlockState(pos);
            Random rand = world.getRandom();
            BlockPos root = getRootPos(world, pos, state);
            state = world.getBlockState(root);

            int success = 0;
            while (iterations > 0 && success < maxSuccess) {

                BlockState growState = spreadingState(world, root, state, rand);
                if (growState == null) {
                    iterations--;
                    continue;
                }

                BlockPos toPos = spreadingPos(world, root, growState, rand);
                if (toPos == null || !world.isBlockLoaded(toPos)) {
                    iterations--;
                    continue;
                }

                int meta = spreadingMeta(world, toPos, growState, rand);

                if (spawn(world, toPos, growState, meta)) {
                    success++;
                }
                iterations--;
            }
            return success;
        }
        return 0;
    }

    /**
     * Returns whether this plant needs random ticks. Most likely set in the properties of the plant. This also checks
     * whether the fluid state in the given state needs ticks.
     *
     * @param state The state that needs to be ticked
     * @return True when random ticks are needed for this plant.
     */
    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.getFluidState().ticksRandomly() || super.ticksRandomly(state);
    }

    /**
     * Called when this plant receives a random tick. This should tick the contained fluid state, or ideally just call
     * super.
     *
     * @param state The state to be ticked
     * @param world The world to tick in
     * @param pos   The position to be ticked at
     * @param rand  A random number generator
     */
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        state.getFluidState().randomTick(world, pos, rand);
    }

    /**
     * Checks whether this plant can be grown using a fertilizer, e.g. bonemeal. When this returns true, the fertilizer
     * is consumed.
     *
     * @param world  The world to be fertilized in
     * @param pos    The position to be grown at
     * @param state  The state being fertilized
     * @param client Whether this is called on the client or the server
     * @return True when fertilizer can be consumed
     */
    @Override
    public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean client) {
        return false;
    }

    /**
     * Checks whether bonemeal or any other fertilizer can be used. When this returns true, {@link #grow} is called, but
     * this does not determine whether the fertilizer is consumed or not (it always consumes).
     *
     * @param world The world to be fertilized in
     * @param rand  A random number generator
     * @param pos   The position to be grown at
     * @param state The state being fertilized
     * @return True when {@link #grow} should be called.
     */
    @Override
    public final boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Grows this plant from being fertilized.
     *
     * @param world The world to grow in
     * @param rand  A random number generator
     * @param pos   The position to grow from
     * @param state The state to grow from
     */
    @Override
    public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
    }


    // Placement and generation traits
    // ========================================================================

    /**
     * Checks whether the state at the given position is a state of this plant. Default implementation checks whether
     * the state is this block.
     *
     * @param world The world to check in
     * @param pos   The position to check at
     * @return True when the found state is this plant, false otherwise
     */
    public boolean isThisPlant(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).isIn(this);
    }

    /**
     * Checks in general whether the plant is stable at a certain position.
     *
     * @param world The world to check in
     * @param pos   The position to check at
     * @param state The state to check (note that this might not always be a valid state of this plant)
     * @return True when this plant can remain at the given position
     */
    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Checks whether this plant is stable at a certain position for being placed by a player
     *
     * @param world The world being placed in
     * @param pos   The position to be placed at
     * @param state The state to be placed (determined by {@link #placementState}.
     * @return True when this plant is placeable at the given position.
     */
    public boolean canPlace(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Checks whether this plant is stable at a certain position for being generated by some terrain feature
     *
     * @param world The world being placed in
     * @param pos   The position to be placed at
     * @param state The state to be placed
     * @return True when this plant is generatable at the given position
     */
    public boolean canGenerate(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Returns whether this plant can be replaced by other blocks or plants. Might also be called from world generation
     * in order to determine whether a plant can be replaced.
     *
     * @param world The world to be replaced in
     * @param pos   The position to be replaced at
     * @param state The state being replaced
     * @return True if replacing is possible
     */
    public boolean replaceable(IWorld world, BlockPos pos, BlockState state) {
        return state.getMaterial().isReplaceable();
    }

    /**
     * Returns whether this plant can be replaced by other blocks or plants. Only called from players placing blocks in
     * this plant.
     *
     * @param world The world to be replaced in
     * @param pos   The position to be replaced at
     * @param state The state being replaced
     * @return True if replacing is possible
     */
    public boolean replaceable(World world, BlockPos pos, BlockState state, ItemStack usedItem, BlockItemUseContext ctx) {
        return replaceable(world, pos, state);
    }

    /**
     * Returns the state to be placed by a player at a given position, or null if no valid state could be determined. By
     * default this calls {@link #updateState} on the default state of this plant (the given state).
     *
     * @param world The world to place in
     * @param pos   The position being targeted
     * @param state The default state of this plant
     * @param ctx   The placement context
     * @return The state to be placed, or null
     */
    @Nullable
    public BlockState placementState(World world, BlockPos pos, BlockState state, BlockItemUseContext ctx) {
        return updateState(world, pos, state);
    }

    /**
     * Updates the state of this plant in the world, in general. Likely called from {@link #placementState} or {@link
     * #updateStateDirectionally}. This must return a state of this plant updated to be connected to nearby blocks, or
     * return null if the plant is no longer valid.
     *
     * @param world The world to update in
     * @param pos   The position of this plant
     * @param state The current state
     * @return The updated state, or null
     */
    @Nullable
    public BlockState updateState(IWorld world, BlockPos pos, BlockState state) {
        return state;
    }

    /**
     * Updates the state of this plant in the world, in general. This must return a state of this plant updated to be
     * connected to nearby blocks, ideally checking only in the given direction. Should return air if the plant is no
     * longer valid.
     *
     * @param world The world to update in
     * @param pos   The position of this plant
     * @param state The current state
     * @return The updated state, or air if no longer valid
     */
    public BlockState updateStateDirectionally(IWorld world, BlockPos pos, BlockState state, Direction dir, BlockState adjState, BlockPos adjPos) {
        return OptionalUtil.orElse(updateState(world, pos, state), Blocks.AIR.getDefaultState());
    }

    /**
     * Triggered when this plant becomes unstable. The plant can be made stable if needed by returning a stable state or
     * setting some blocks in the world. When air is returned, the plant is instead destroyed. This might also schedule
     * a tick where the plant is destroyed.
     *
     * @param world The world to become unstable in
     * @param pos   The position this plant is at
     * @param state The state that becomes unstable
     * @return The state that replaces the unstable plant
     */
    public BlockState unstable(IWorld world, BlockPos pos, BlockState state) {
        int rate = unstableTickRate(world);
        if (rate <= 0)
            return Blocks.AIR.getDefaultState();
        else
            world.getPendingBlockTicks().scheduleTick(pos, this, rate);
        return state;
    }

    /**
     * Returns how long this plant waits with destroying once it becomes unstable
     *
     * @param world The world
     * @return How many ticks before destroying, when 0 it destroys immediately
     */
    protected int unstableTickRate(IWorld world) {
        return 0;
    }

    /**
     * Called on a scheduled tick, when the plant is not unstable and did not get removed.
     *
     * @param world The world to be ticked in
     * @param pos   The position to be ticked at
     * @param state The state to be ticked
     * @param rand  A random number generator
     */
    public void tick(ServerWorld world, BlockPos pos, BlockState state, Random rand) {

    }


    // Placement and generation
    // ========================================================================

    /**
     * Checks whether a block of this plant can be placed the given position. This checks {@link #canRemain} and passes
     * the request to the {@linkplain #getFluidLogic() fluid logic} of this plant.
     *
     * @param world The world to place in
     * @param pos   The position to place at
     * @param state The state to place
     * @return The placed plant
     */
    public final boolean canPlaceAt(IWorldReader world, BlockPos pos, BlockState state) {
        if (!canRemain(world, pos, state))
            return false;

        FluidState fluid = world.getFluidState(pos);
        FluidReaction reaction = fluidLogic.generateInFluid(state, fluid);

        switch (OptionalUtil.orElse(reaction, FluidReaction.KEEP)) {
            case FLOOD:
            case KEEP:
                return true;
            case DESTROY:
            case REMOVE:
                return false;
        }
        return Validate.illegalState();
    }

    private boolean place(IWorld world, BlockPos pos, BlockState state, int flags) {
        FluidState fluid = world.getFluidState(pos);
        FluidReaction reaction = fluidLogic.generateInFluid(state, fluid);

        switch (OptionalUtil.orElse(reaction, FluidReaction.KEEP)) {
            case FLOOD:
                state = updateState(world, pos, state);
                if (state == null) return false;

                state = fluidLogic.withFluidState(state, fluid);
                return world.setBlockState(pos, state, flags);
            case KEEP:
                state = updateState(world, pos, state);
                return state != null && world.setBlockState(pos, state, flags);
            case DESTROY:
            case REMOVE:
                return false;
        }
        return Validate.illegalState();
    }

    /**
     * Places a plant block at the given position, if possible. This checks {@link #canPlaceAt} and the {@linkplain
     * #getFluidLogic()}  fluid logic} of this plant.
     *
     * @param world The world to place in
     * @param pos   The position to place at
     * @param state The state to place
     * @param flags The place flags, see {@link World#setBlockState(BlockPos, BlockState, int)}
     * @return True when successfully placed
     */
    public final boolean placeAt(IWorld world, BlockPos pos, BlockState state, int flags) {
        if (state == null) return false;
        return canPlaceAt(world, pos, state) && place(world, pos, state, flags);
    }

    /**
     * Removes a plant block at the given position, if possible. This replaces the block at the given position with the
     * contained fluid state.
     *
     * @param world The world to remove in
     * @param pos   The position to remove at
     * @param flags The place flags, see {@link World#setBlockState(BlockPos, BlockState, int)}
     * @return True when successfully placed
     */
    public final boolean removeAt(IWorld world, BlockPos pos, int flags) {
        return world.setBlockState(pos, world.getBlockState(pos).getFluidState().getBlockState(), flags);
    }

    @Override
    public final void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!canRemain(world, pos, state)) {
            replace(state, Blocks.AIR.getDefaultState(), world, pos, 3, 512);
            return;
        }
        tick(world, pos, state, rand);
    }

    @Override
    public final BlockState updatePostPlacement(BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos) {
        tickFluid(world, pos);

        state = super.updatePostPlacement(state, dir, adjState, world, pos, adjPos);
        if (canRemain(world, pos, state)) {
            state = updateStateDirectionally(world, pos, state, dir, adjState, adjPos);
        } else {
            return unstable(world, pos, state);
        }
        return state;
    }

    @Override
    public final boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return canRemain(world, pos, state);
    }

    @Nullable
    @Override
    public final BlockState getStateForPlacement(BlockItemUseContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getPos();
        BlockState state = placementState(world, pos, getDefaultState(), ctx);
        if (state == null)
            return null;

        if (!canPlace(world, pos, state))
            return null;

        FluidState fluid = world.getFluidState(pos);
        FluidReaction reaction = fluidLogic.placeInFluid(state, fluid);
        switch (OptionalUtil.orElse(reaction, FluidReaction.KEEP)) {
            case KEEP:
                return state;
            case DESTROY:
            case REMOVE:
                return null;
            case FLOOD:
                return fluidLogic.withFluidState(state, fluid);
        }

        return Validate.illegalState();
    }

    @Override
    public final boolean isReplaceable(BlockState state, BlockItemUseContext ctx) {
        return replaceable(ctx.getWorld(), ctx.getPos(), state, ctx.getItem(), ctx)
                   && (ctx.getItem().isEmpty() || ctx.getItem().getItem() != asItem());
    }


    // Basic logic
    // ========================================================================

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        Vector3d vec = state.getOffset(world, pos);
        return hitbox.apply(state).withOffset(vec.x, vec.y, vec.z);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        Vector3d vec = state.getOffset(world, pos);
        return collider.apply(state).withOffset(vec.x, vec.y, vec.z);
    }

    /**
     * Returns the {@link IFluidLogic} of this plant.
     */
    public IFluidLogic getFluidLogic() {
        return fluidLogic;
    }

    /**
     * Returns the {@link IGrowLogic} of this plant.
     *
     * @deprecated Still being worked on
     */
    @Deprecated
    public IGrowLogic getGrowLogic() {
        return growLogic;
    }

    @Override
    public OffsetType getOffsetType() {
        return offsetType;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public long getPositionRandom(BlockState state, BlockPos pos) {
        BlockPos root = getRootPos(Minecraft.getInstance().world, pos, state);
        return MathHelper.getCoordinateRandom(root.getX(), root.getY(), root.getZ());
    }


    // Waterlogging implementation
    // ========================================================================

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving) {
        // Triggers the mixing effects of water and lava properly
        state.getFluidState().getBlockState().onBlockAdded(world, pos, oldState, moving);
    }

    /**
     * Ticks the fluid state at the given position.
     *
     * @param world The world
     * @param pos   The position
     */
    public void tickFluid(IWorld world, BlockPos pos) {
        FluidState fstate = world.getFluidState(pos);
        Fluid fluid = fstate.getFluid();
        int tickRate = fluid.getTickRate(world);
        if (tickRate > 0) {
            world.getPendingFluidTicks().scheduleTick(pos, fluid, tickRate);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return fluidLogic.getFluidState(state);
    }

    @Override
    public Fluid pickupFluid(IWorld world, BlockPos pos, BlockState state) {
        FluidState fstate = world.getFluidState(pos);
        if (fstate.isEmpty())
            return Fluids.EMPTY;
        if (!fstate.isSource())
            return Fluids.EMPTY;

        FluidReaction reaction = fluidLogic.bucketRemoveFluid(state);
        switch (OptionalUtil.orElse(reaction, FluidReaction.KEEP)) {
            case KEEP:
                return Fluids.EMPTY;

            case DESTROY:
                world.destroyBlock(pos, true);
            case REMOVE:
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3 | 8);

                break;

            case FLOOD:
                BlockState newState = fluidLogic.withFluidState(state, Fluids.EMPTY.getDefaultState());
                world.setBlockState(pos, newState, 3 | 8);
        }
        return fstate.getFluid();
    }

    @Override
    public boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
        return true;
    }

    @Override
    public boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, FluidState fstate) {
        Fluid fluid = fstate.getFluid();
        FluidReaction reaction = fluidLogic.fluidReplace(state, fstate);
        switch (OptionalUtil.orElse(reaction, FluidReaction.KEEP)) {
            case KEEP:
                return false;
            case DESTROY:
                if (fluid instanceof FlowingFluid) {
                    FlowingFluid ffluid = (FlowingFluid) fluid;
                    BEFORE_REPLACING_BLOCK_ACCESS.call(ffluid, world, pos, state);
                } else {
                    world.destroyBlock(pos, true);
                }
            case REMOVE:
                world.setBlockState(pos, fstate.getBlockState(), 3);
                break;
            case FLOOD:
                BlockState newState = fluidLogic.withFluidState(state, fstate);
                world.setBlockState(pos, newState, 3);
        }
        tickFluid(world, pos);
        return true;
    }


    // IPlantable implementation
    // ========================================================================

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isIn(this) ? state : getDefaultState();
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return type;
    }

    public static class Properties {
        private final AbstractBlock.Properties blockProps;
        private PlantType type = PlantType.PLAINS;
        private OffsetType offset = OffsetType.NONE;
        private IFluidLogic fluidLogic = NoFluidLogic.INSTANCE;
        private IGrowLogic growLogic = NoGrowLogic.INSTANCE;
        private Function<BlockState, VoxelShape> hitbox = state -> VoxelShapes.fullCube();
        private Function<BlockState, VoxelShape> collider = state -> VoxelShapes.empty();

        private Properties(AbstractBlock.Properties blockProps) {
            this.blockProps = blockProps;
        }

        public Properties doesNotBlockMovement() {
            blockProps.doesNotBlockMovement();
            return this;
        }

        public Properties nonOpaque() {
            blockProps.nonOpaque();
            return this;
        }

        public Properties harvestLevel(int harvestLevel) {
            blockProps.harvestLevel(harvestLevel);
            return this;
        }

        public Properties harvestTool(ToolType harvestTool) {
            blockProps.harvestTool(harvestTool);
            return this;
        }

        public Properties slipperiness(float slipperiness) {
            blockProps.slipperiness(slipperiness);
            return this;
        }

        public Properties velocityMultiplier(float mul) {
            blockProps.velocityMultiplier(mul);
            return this;
        }

        public Properties jumpVelocityMultiplier(float mul) {
            blockProps.jumpVelocityMultiplier(mul);
            return this;
        }

        public Properties sound(SoundType sound) {
            blockProps.sound(sound);
            return this;
        }

        public Properties luminance(ToIntFunction<BlockState> fn) {
            blockProps.luminance(fn);
            return this;
        }

        public Properties hardnessAndResistance(float hardness, float resistance) {
            blockProps.hardnessAndResistance(hardness, resistance);
            return this;
        }

        public Properties zeroHardnessAndResistance() {
            return hardnessAndResistance(0);
        }

        public Properties hardnessAndResistance(float strength) {
            return hardnessAndResistance(strength, strength);
        }

        public Properties tickRandomly() {
            blockProps.tickRandomly();
            return this;
        }

        public Properties variableOpacity() {
            blockProps.variableOpacity();
            return this;
        }

        public Properties noDrops() {
            blockProps.noDrops();
            return this;
        }

        public Properties lootFrom(Block block) {
            blockProps.lootFrom(block);
            return this;
        }

        public Properties air() {
            blockProps.air();
            return this;
        }

        public Properties allowsSpawning(AbstractBlock.IExtendedPositionPredicate<EntityType<?>> predicate) {
            blockProps.allowsSpawning(predicate);
            return this;
        }

        public Properties solidBlock(AbstractBlock.IPositionPredicate predicate) {
            blockProps.solidBlock(predicate);
            return this;
        }

        public Properties suffocates(AbstractBlock.IPositionPredicate predicate) {
            blockProps.suffocates(predicate);
            return this;
        }

        public Properties blockVision(AbstractBlock.IPositionPredicate predicate) {
            blockProps.blockVision(predicate);
            return this;
        }

        public Properties postProcess(AbstractBlock.IPositionPredicate predicate) {
            blockProps.postProcess(predicate);
            return this;
        }

        public Properties emissiveLighting(AbstractBlock.IPositionPredicate predicate) {
            blockProps.emissiveLighting(predicate);
            return this;
        }

        public Properties requiresTool() {
            blockProps.requiresTool();
            return this;
        }

        public Properties offset(OffsetType offset) {
            this.offset = offset;
            return this;
        }

        public Properties type(PlantType type) {
            this.type = type;
            return this;
        }

        public Properties fluidLogic(IFluidLogic fluidLogic) {
            this.fluidLogic = fluidLogic;
            return this;
        }

        public Properties hitbox(VoxelShape hitbox) {
            this.hitbox = state -> hitbox;
            return this;
        }

        public Properties collider(VoxelShape collider) {
            this.collider = state -> collider;
            return this;
        }

        public Properties hitbox(Function<BlockState, VoxelShape> hitbox) {
            this.hitbox = hitbox;
            return this;
        }

        public Properties collider(Function<BlockState, VoxelShape> collider) {
            this.collider = collider;
            return this;
        }

        public static Properties create(Material material) {
            return new Properties(AbstractBlock.Properties.create(material));
        }

        public static Properties create(Material material, MaterialColor color) {
            return new Properties(AbstractBlock.Properties.create(material, color));
        }

        public static Properties create(Material material, DyeColor color) {
            return new Properties(AbstractBlock.Properties.create(material, color));
        }

        public static Properties create(Material material, Function<BlockState, MaterialColor> color) {
            return new Properties(AbstractBlock.Properties.of(material, color));
        }
    }
}
