package natures.debris.common.block.plant;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.shadew.util.contract.Validate;

import natures.debris.core.util.OptionalUtil;
import natures.debris.common.block.plant.logic.IFluidLogic;
import natures.debris.common.block.plant.logic.NoFluidLogic;

public abstract class Plant extends ForgeRegistryEntry<Plant> {
    private Material material;
    private Function<BlockState, MaterialColor> color;

    private float hardness;
    private float resistance;

    private final Map<Property<?>, Comparable<?>> stateProperties;

    private Function<BlockState, SoundType> sound;

    private IFluidLogic fluidLogic = NoFluidLogic.INSTANCE;

    public Plant() {
        stateProperties = new HashMap<>();

        material(Material.PLANTS);
        color(MaterialColor.FOLIAGE, MaterialColor.WATER);
        strength(0);
        sound(SoundType.PLANT, SoundType.WET_GRASS);
    }

    protected void material(Material material) {
        this.material = material;
    }

    protected void color(MaterialColor color) {
        color(state -> color);
    }

    protected void color(MaterialColor dry, MaterialColor wet) {
        color(state -> isWet(state) ? dry : wet);
    }

    protected void color(Function<BlockState, MaterialColor> color) {
        this.color = color;
    }

    protected void strength(double strength) {
        strength(strength, strength);
    }

    protected void strength(double hardness, double resistance) {
        this.hardness = (float) hardness;
        this.resistance = (float) resistance;
    }

    protected void sound(SoundType sound) {
        sound(state -> sound);
    }

    protected void sound(SoundType dry, SoundType wet) {
        sound(state -> isWet(state) ? wet : dry);
    }

    protected void sound(Function<BlockState, SoundType> sound) {
        this.sound = sound;
    }

    protected <T extends Comparable<T>> void property(Property<T> property, T value) {
        stateProperties.put(property, value);
    }

    protected <T extends Comparable<T>> void property(Property<T> property) {
        property(property, property.getAllowedValues().iterator().next());
    }

    protected void fluidLogic(IFluidLogic fluidLogic) {
        this.fluidLogic = fluidLogic;
    }


    public IFluidLogic getFluidLogic() {
        return fluidLogic;
    }

    public void tickFluid(BlockState state, IWorld world, BlockPos pos) {
        FluidState fstate = state.getFluidState();
        Fluid fluid = fstate.getFluid();
        int tickRate = fluid.getTickRate(world);
        if (tickRate > 0) {
            world.getPendingFluidTicks().scheduleTick(pos, fluid, tickRate);
        }
    }

    public boolean isWet(BlockState state) {
        return state.getFluidState().isTagged(FluidTags.WATER);
    }


    public MaterialColor getColor(BlockState state) {
        return color.apply(state);
    }

    public SoundType getSound(BlockState state, IBlockReader world, BlockPos pos, @Nullable Entity entity) {
        return sound.apply(state);
    }


    public boolean isEmissive(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    public int getLuminance(BlockState state, IBlockReader world, BlockPos pos) {
        return 0;
    }


    @OnlyIn(Dist.CLIENT)
    public void ambientTick(BlockState state, World world, BlockPos pos, Random rand) {
    }

    @OnlyIn(Dist.CLIENT)
    public void collisionEffect(BlockState state, World world, BlockPos pos, Random rand, Entity entity) {
    }

    @OnlyIn(Dist.CLIENT)
    public void eventEffect(int event, BlockState state, World world, BlockPos pos, Random rand) {
    }

    public boolean canFeed(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    public void feed(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
    }

    public boolean canTick(BlockState state) {
        return false;
    }

    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
    }


    public boolean canRemain(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    public boolean canPlace(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    public boolean canGenerate(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    public boolean replaceable(BlockState state, IWorld world, BlockPos pos) {
        return false;
    }

    public boolean replaceable(BlockState state, World world, BlockPos pos, ItemStack usedItem, BlockItemUseContext ctx) {
        return false;
    }

    public BlockState placementState(BlockState state, World world, BlockPos pos, BlockItemUseContext ctx) {
        return updateState(state, world, pos);
    }

    public BlockState updateState(BlockState state, IWorld world, BlockPos pos) {
        return state;
    }

    public boolean canGenerateAt(BlockState state, IWorldReader world, BlockPos pos) {
        if (!canGenerate(state, world, pos))
            return false;

        FluidState fluid = world.getFluidState(pos);
        IFluidLogic.Reaction reaction = fluidLogic.generateInFluid(this, state, fluid);

        switch (OptionalUtil.orElse(reaction, IFluidLogic.Reaction.KEEP)) {
            case FLOOD:
            case KEEP:
                return true;
            case DESTROY:
            case REMOVE:
                return false;
        }
        return Validate.illegalState();
    }





    AbstractBlock.Properties buildBlockProperties() {
        return AbstractBlock.Properties.of(material, this::getColor)
                                       .emissiveLighting(this::isEmissive)
                                       .nonOpaque()
                                       .blockVision((state, world, pos) -> true)
                                       .suffocates((state, world, pos) -> false)
                                       .hardnessAndResistance(hardness, resistance);
    }

    StateContainer<Block, BlockState> buildStateContainer(Block block) {
        IStateBuilder stateBuilder = new IStateBuilder() {
            @Override
            public <T extends Comparable<T>> void addProperty(Property<T> property, T def) {
                if (def == null)
                    property(property);
                else
                    property(property, def);
            }
        };

        fluidLogic.addProperties(stateBuilder);

        StateContainer.Builder<Block, BlockState> builder = new StateContainer.Builder<>(block);
        for (Property<?> p : stateProperties.keySet())
            builder.add(p);

        return builder.build(Block::getDefaultState, BlockState::new);
    }

    BlockState withDefaults(BlockState state) {
        for (Map.Entry<Property<?>, Comparable<?>> e : stateProperties.entrySet())
            state = state.with(e.getKey(), c(e.getValue()));
        return state;
    }

    @SuppressWarnings("unchecked")
    private static <T, U> U c(T t) {
        return (U) t;
    }
}
