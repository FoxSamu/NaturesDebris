package natures.debris.common.block.plant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToIntFunction;

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
import natures.debris.common.block.plant.fluid.FluidReaction;
import natures.debris.common.block.plant.fluid.IFluidLogic;
import natures.debris.common.block.plant.fluid.NoFluidLogic;
import natures.debris.common.block.plant.growing.IGrowLogic;
import natures.debris.common.block.plant.growing.NoGrowLogic;

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


    @Nonnull
    public BlockPos getRootPos(IBlockReader world, BlockPos pos, BlockState state) {
        return pos;
    }

    @Nullable
    public BlockPos getSoilPos(IBlockReader world, BlockPos pos, BlockState state) {
        return null;
    }

    public boolean spawn(IWorld world, BlockPos pos) {
        return world.isAirBlock(pos) && placeAt(world, pos, getDefaultState(), 3);
    }

    public boolean kill(IWorld world, BlockPos pos) {
        return isThisPlant(world, pos) && removeAt(world, pos, 3);
    }


    // Growing
    // ========================================================================

    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.getFluidState().ticksRandomly() || super.ticksRandomly(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        state.getFluidState().randomTick(world, pos, rand);
    }

    @Override
    public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean client) {
        return false;
    }

    @Override
    public final boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
    }


    // Placement and generation traits
    // ========================================================================

    public boolean isThisPlant(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).isIn(this);
    }

    public boolean canRemain(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean canPlace(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean canGenerate(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean replaceable(IWorld world, BlockPos pos, BlockState state) {
        return false;
    }

    public boolean replaceable(World world, BlockPos pos, BlockState state, ItemStack usedItem, BlockItemUseContext ctx) {
        return replaceable(world, pos, state);
    }

    public BlockState placementState(World world, BlockPos pos, BlockState state, BlockItemUseContext ctx) {
        return updateState(world, pos, state);
    }

    public BlockState updateState(IWorld world, BlockPos pos, BlockState state) {
        return state;
    }

    public BlockState updateStateDirectionally(IWorld world, BlockPos pos, BlockState state, Direction dir, BlockState adjState, BlockPos adjPos) {
        return updateState(world, pos, state);
    }


    // Placement and generation
    // ========================================================================

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
                state = fluidLogic.withFluidState(state, fluid);
                return world.setBlockState(pos, state, flags);
            case KEEP:
                state = updateState(world, pos, state);
                return world.setBlockState(pos, state, flags);
            case DESTROY:
            case REMOVE:
                return false;
        }
        return Validate.illegalState();
    }

    public final boolean placeAt(IWorld world, BlockPos pos, BlockState state, int flags) {
        return canPlaceAt(world, pos, state) && place(world, pos, state, flags);
    }

    public final boolean removeAt(IWorld world, BlockPos pos, int flags) {
        return world.setBlockState(pos, world.getBlockState(pos).getFluidState().getBlockState(), flags);
    }

    @Override
    public final BlockState updatePostPlacement(BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos) {
        tickFluid(world, pos);

        state = super.updatePostPlacement(state, dir, adjState, world, pos, adjPos);
        if (canRemain(world, pos, state)) {
            state = updateStateDirectionally(world, pos, state, dir, adjState, adjPos);
        } else {
            return Blocks.AIR.getDefaultState();
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

    public IFluidLogic getFluidLogic() {
        return fluidLogic;
    }

    public IGrowLogic getGrowLogic() {
        return growLogic;
    }

    @Override
    public OffsetType getOffsetType() {
        return offsetType;
    }


    // Waterlogging implementation
    // ========================================================================

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving) {
        // Triggers the mixing effects of water and lava properly
        state.getFluidState().getBlockState().onBlockAdded(world, pos, oldState, moving);
    }

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
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

                break;

            case FLOOD:
                BlockState newState = fluidLogic.withFluidState(state, Fluids.EMPTY.getDefaultState());
                world.setBlockState(pos, newState, 11);
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
