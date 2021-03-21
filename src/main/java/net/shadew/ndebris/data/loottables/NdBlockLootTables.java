package net.shadew.ndebris.data.loottables;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.DynamicEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.*;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import net.shadew.ndebris.common.block.NdBlocks;

public class NdBlockLootTables implements Consumer<BiConsumer<Identifier, LootTable.Builder>> {
    protected static final LootCondition.Builder WITH_SILK_TOUCH = MatchToolLootCondition.builder(
        ItemPredicate.Builder.create().enchantment(
            new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1))
        )
    );
    protected static final LootCondition.Builder WITHOUT_SILK_TOUCH = WITH_SILK_TOUCH.invert();
    protected static final LootCondition.Builder WITH_SHEARS = MatchToolLootCondition.builder(
        ItemPredicate.Builder.create().item(Items.SHEARS)
    );
    protected static final LootCondition.Builder WITH_SILK_TOUCH_OR_SHEARS = WITH_SHEARS.or(WITH_SILK_TOUCH);
    protected static final LootCondition.Builder WITHOUT_SILK_TOUCH_NOR_SHEARS = WITH_SILK_TOUCH_OR_SHEARS.invert();

    private static final Set<Item> EXPLOSION_IMMUNE = Stream.of(
        Blocks.DRAGON_EGG,
        Blocks.BEACON,
        Blocks.CONDUIT,
        Blocks.SKELETON_SKULL,
        Blocks.WITHER_SKELETON_SKULL,
        Blocks.PLAYER_HEAD,
        Blocks.ZOMBIE_HEAD,
        Blocks.CREEPER_HEAD,
        Blocks.DRAGON_HEAD,
        Blocks.SHULKER_BOX,
        Blocks.BLACK_SHULKER_BOX,
        Blocks.BLUE_SHULKER_BOX,
        Blocks.BROWN_SHULKER_BOX,
        Blocks.CYAN_SHULKER_BOX,
        Blocks.GRAY_SHULKER_BOX,
        Blocks.GREEN_SHULKER_BOX,
        Blocks.LIGHT_BLUE_SHULKER_BOX,
        Blocks.LIGHT_GRAY_SHULKER_BOX,
        Blocks.LIME_SHULKER_BOX,
        Blocks.MAGENTA_SHULKER_BOX,
        Blocks.ORANGE_SHULKER_BOX,
        Blocks.PINK_SHULKER_BOX,
        Blocks.PURPLE_SHULKER_BOX,
        Blocks.RED_SHULKER_BOX,
        Blocks.WHITE_SHULKER_BOX,
        Blocks.YELLOW_SHULKER_BOX
    ).map(ItemConvertible::asItem).collect(ImmutableSet.toImmutableSet());

    private static final float[] SAPLING_DROP_CHANCES = {1 / 20f, 1 / 16f, 1 / 12f, 1 / 10f};
    private static final float[] RARE_SAPLING_DROP_CHANCES = {1 / 40f, 1 / 36f, 1 / 32f, 1 / 24f, 1 / 10f};

    private final Map<Identifier, LootTable.Builder> lootTables = Maps.newHashMap();


    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
        addDrop(NdBlocks.MURKY_DIRT);
        addDrop(NdBlocks.MURKY_COARSE_DIRT);
        addDrop(NdBlocks.MURKY_CLAY);
        addDrop(NdBlocks.MURKY_TERRACOTTA);
        addDrop(NdBlocks.MURKY_SAND);
        addDrop(NdBlocks.MURKY_GRASS_PATH, NdBlocks.MURKY_DIRT);
        addDrop(NdBlocks.MURKY_GRASS_BLOCK, block -> dropsWithSilkTouch(block, NdBlocks.MURKY_DIRT));
        addDrop(NdBlocks.MURKY_HUMUS, block -> dropsWithSilkTouch(block, NdBlocks.MURKY_DIRT));
        addDrop(NdBlocks.MURKY_PODZOL, block -> dropsWithSilkTouch(block, NdBlocks.MURKY_DIRT));
        addDrop(NdBlocks.LEAFY_HUMUS, block -> dropsWithSilkTouch(block, NdBlocks.MURKY_DIRT));

        addDrop(NdBlocks.BLACKWOOD_LOG);
        addDrop(NdBlocks.INVER_LOG);
        addDrop(NdBlocks.BLACKWOOD);
        addDrop(NdBlocks.INVER_WOOD);
        addDrop(NdBlocks.STRIPPED_BLACKWOOD_LOG);
        addDrop(NdBlocks.STRIPPED_INVER_LOG);
        addDrop(NdBlocks.STRIPPED_BLACKWOOD);
        addDrop(NdBlocks.STRIPPED_INVER_WOOD);

        addDrop(NdBlocks.BLACKWOOD_PLANKS);
        addDrop(NdBlocks.INVER_PLANKS);
        addDrop(NdBlocks.BLACKWOOD_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.INVER_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.BLACKWOOD_STAIRS);
        addDrop(NdBlocks.INVER_STAIRS);
        addDrop(NdBlocks.BLACKWOOD_STEP);
        addDrop(NdBlocks.INVER_STEP);
        addDrop(NdBlocks.BLACKWOOD_FENCE);
        addDrop(NdBlocks.INVER_FENCE);

        addDrop(NdBlocks.ROCK);
        addDrop(NdBlocks.MOSSY_ROCK);
        addDrop(NdBlocks.ROCK_BRICKS);
        addDrop(NdBlocks.MOSSY_ROCK_BRICKS);
        addDrop(NdBlocks.CRACKED_ROCK_BRICKS);
        addDrop(NdBlocks.ROCK_TILES);
        addDrop(NdBlocks.MOSSY_ROCK_TILES);
        addDrop(NdBlocks.CRACKED_ROCK_TILES);
        addDrop(NdBlocks.SMOOTH_ROCK);
        addDrop(NdBlocks.POLISHED_ROCK);
        addDrop(NdBlocks.CHISELED_ROCK);
        addDrop(NdBlocks.ROCK_PILLAR);
        addDrop(NdBlocks.ROCK_LANTERN);

        addDrop(NdBlocks.ROCK_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.MOSSY_ROCK_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.ROCK_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.MOSSY_ROCK_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_ROCK_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.ROCK_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.MOSSY_ROCK_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_ROCK_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.SMOOTH_ROCK_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.POLISHED_ROCK_SLAB, NdBlockLootTables::dropsSlab);

        addDrop(NdBlocks.ROCK_STAIRS);
        addDrop(NdBlocks.MOSSY_ROCK_STAIRS);
        addDrop(NdBlocks.ROCK_BRICKS_STAIRS);
        addDrop(NdBlocks.MOSSY_ROCK_BRICKS_STAIRS);
        addDrop(NdBlocks.CRACKED_ROCK_BRICKS_STAIRS);
        addDrop(NdBlocks.ROCK_TILES_STAIRS);
        addDrop(NdBlocks.MOSSY_ROCK_TILES_STAIRS);
        addDrop(NdBlocks.CRACKED_ROCK_TILES_STAIRS);
        addDrop(NdBlocks.SMOOTH_ROCK_STAIRS);
        addDrop(NdBlocks.POLISHED_ROCK_STAIRS);

        addDrop(NdBlocks.ROCK_STEP);
        addDrop(NdBlocks.MOSSY_ROCK_STEP);
        addDrop(NdBlocks.ROCK_BRICKS_STEP);
        addDrop(NdBlocks.MOSSY_ROCK_BRICKS_STEP);
        addDrop(NdBlocks.CRACKED_ROCK_BRICKS_STEP);
        addDrop(NdBlocks.ROCK_TILES_STEP);
        addDrop(NdBlocks.MOSSY_ROCK_TILES_STEP);
        addDrop(NdBlocks.CRACKED_ROCK_TILES_STEP);
        addDrop(NdBlocks.SMOOTH_ROCK_STEP);
        addDrop(NdBlocks.POLISHED_ROCK_STEP);

        addDrop(NdBlocks.ROCK_WALL);
        addDrop(NdBlocks.MOSSY_ROCK_WALL);
        addDrop(NdBlocks.ROCK_BRICKS_WALL);
        addDrop(NdBlocks.MOSSY_ROCK_BRICKS_WALL);
        addDrop(NdBlocks.CRACKED_ROCK_BRICKS_WALL);
        addDrop(NdBlocks.ROCK_TILES_WALL);
        addDrop(NdBlocks.MOSSY_ROCK_TILES_WALL);
        addDrop(NdBlocks.CRACKED_ROCK_TILES_WALL);
        addDrop(NdBlocks.SMOOTH_ROCK_WALL);
        addDrop(NdBlocks.POLISHED_ROCK_WALL);

        addDrop(NdBlocks.DARKROCK);
        addDrop(NdBlocks.MOSSY_DARKROCK);
        addDrop(NdBlocks.DARKROCK_BRICKS);
        addDrop(NdBlocks.MOSSY_DARKROCK_BRICKS);
        addDrop(NdBlocks.CRACKED_DARKROCK_BRICKS);
        addDrop(NdBlocks.DARKROCK_TILES);
        addDrop(NdBlocks.MOSSY_DARKROCK_TILES);
        addDrop(NdBlocks.CRACKED_DARKROCK_TILES);
        addDrop(NdBlocks.SMOOTH_DARKROCK);
        addDrop(NdBlocks.POLISHED_DARKROCK);
        addDrop(NdBlocks.CHISELED_DARKROCK);
        addDrop(NdBlocks.DARKROCK_PILLAR);
        addDrop(NdBlocks.DARKROCK_LANTERN);

        addDrop(NdBlocks.DARKROCK_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.MOSSY_DARKROCK_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.DARKROCK_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.DARKROCK_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.MOSSY_DARKROCK_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_DARKROCK_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.SMOOTH_DARKROCK_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.POLISHED_DARKROCK_SLAB, NdBlockLootTables::dropsSlab);

        addDrop(NdBlocks.DARKROCK_STAIRS);
        addDrop(NdBlocks.MOSSY_DARKROCK_STAIRS);
        addDrop(NdBlocks.DARKROCK_BRICKS_STAIRS);
        addDrop(NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS);
        addDrop(NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS);
        addDrop(NdBlocks.DARKROCK_TILES_STAIRS);
        addDrop(NdBlocks.MOSSY_DARKROCK_TILES_STAIRS);
        addDrop(NdBlocks.CRACKED_DARKROCK_TILES_STAIRS);
        addDrop(NdBlocks.SMOOTH_DARKROCK_STAIRS);
        addDrop(NdBlocks.POLISHED_DARKROCK_STAIRS);

        addDrop(NdBlocks.DARKROCK_STEP);
        addDrop(NdBlocks.MOSSY_DARKROCK_STEP);
        addDrop(NdBlocks.DARKROCK_BRICKS_STEP);
        addDrop(NdBlocks.MOSSY_DARKROCK_BRICKS_STEP);
        addDrop(NdBlocks.CRACKED_DARKROCK_BRICKS_STEP);
        addDrop(NdBlocks.DARKROCK_TILES_STEP);
        addDrop(NdBlocks.MOSSY_DARKROCK_TILES_STEP);
        addDrop(NdBlocks.CRACKED_DARKROCK_TILES_STEP);
        addDrop(NdBlocks.SMOOTH_DARKROCK_STEP);
        addDrop(NdBlocks.POLISHED_DARKROCK_STEP);

        addDrop(NdBlocks.DARKROCK_WALL);
        addDrop(NdBlocks.MOSSY_DARKROCK_WALL);
        addDrop(NdBlocks.DARKROCK_BRICKS_WALL);
        addDrop(NdBlocks.MOSSY_DARKROCK_BRICKS_WALL);
        addDrop(NdBlocks.CRACKED_DARKROCK_BRICKS_WALL);
        addDrop(NdBlocks.DARKROCK_TILES_WALL);
        addDrop(NdBlocks.MOSSY_DARKROCK_TILES_WALL);
        addDrop(NdBlocks.CRACKED_DARKROCK_TILES_WALL);
        addDrop(NdBlocks.SMOOTH_DARKROCK_WALL);
        addDrop(NdBlocks.POLISHED_DARKROCK_WALL);

        addDrop(NdBlocks.LIMESTONE);
        addDrop(NdBlocks.LIMESTONE_BRICKS);
        addDrop(NdBlocks.CRACKED_LIMESTONE_BRICKS);
        addDrop(NdBlocks.LIMESTONE_TILES);
        addDrop(NdBlocks.CRACKED_LIMESTONE_TILES);
        addDrop(NdBlocks.POLISHED_LIMESTONE);
        addDrop(NdBlocks.CARVED_LIMESTONE);
        addDrop(NdBlocks.LIMESTONE_PILLAR);
        addDrop(NdBlocks.LIMESTONE_LANTERN);

        addDrop(NdBlocks.LIMESTONE_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.LIMESTONE_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_LIMESTONE_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.LIMESTONE_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_LIMESTONE_TILES_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.POLISHED_LIMESTONE_SLAB, NdBlockLootTables::dropsSlab);

        addDrop(NdBlocks.LIMESTONE_STAIRS);
        addDrop(NdBlocks.LIMESTONE_BRICKS_STAIRS);
        addDrop(NdBlocks.CRACKED_LIMESTONE_BRICKS_STAIRS);
        addDrop(NdBlocks.LIMESTONE_TILES_STAIRS);
        addDrop(NdBlocks.CRACKED_LIMESTONE_TILES_STAIRS);
        addDrop(NdBlocks.POLISHED_LIMESTONE_STAIRS);

        addDrop(NdBlocks.LIMESTONE_STEP);
        addDrop(NdBlocks.LIMESTONE_BRICKS_STEP);
        addDrop(NdBlocks.CRACKED_LIMESTONE_BRICKS_STEP);
        addDrop(NdBlocks.LIMESTONE_TILES_STEP);
        addDrop(NdBlocks.CRACKED_LIMESTONE_TILES_STEP);
        addDrop(NdBlocks.POLISHED_LIMESTONE_STEP);

        addDrop(NdBlocks.LIMESTONE_WALL);
        addDrop(NdBlocks.LIMESTONE_BRICKS_WALL);
        addDrop(NdBlocks.CRACKED_LIMESTONE_BRICKS_WALL);
        addDrop(NdBlocks.LIMESTONE_TILES_WALL);
        addDrop(NdBlocks.CRACKED_LIMESTONE_TILES_WALL);
        addDrop(NdBlocks.POLISHED_LIMESTONE_WALL);

        addDrop(NdBlocks.SUMESTONE);
        addDrop(NdBlocks.SUMESTONE_BRICKS);
        addDrop(NdBlocks.CRACKED_SUMESTONE_BRICKS);
        addDrop(NdBlocks.POLISHED_SUMESTONE);
        addDrop(NdBlocks.CHISELED_SUMESTONE);
        addDrop(NdBlocks.SUMESTONE_PILLAR);
        addDrop(NdBlocks.SUMESTONE_LANTERN);

        addDrop(NdBlocks.SUMESTONE_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.SUMESTONE_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_SUMESTONE_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.POLISHED_SUMESTONE_SLAB, NdBlockLootTables::dropsSlab);

        addDrop(NdBlocks.SUMESTONE_STAIRS);
        addDrop(NdBlocks.SUMESTONE_BRICKS_STAIRS);
        addDrop(NdBlocks.CRACKED_SUMESTONE_BRICKS_STAIRS);
        addDrop(NdBlocks.POLISHED_SUMESTONE_STAIRS);

        addDrop(NdBlocks.SUMESTONE_STEP);
        addDrop(NdBlocks.SUMESTONE_BRICKS_STEP);
        addDrop(NdBlocks.CRACKED_SUMESTONE_BRICKS_STEP);
        addDrop(NdBlocks.POLISHED_SUMESTONE_STEP);

        addDrop(NdBlocks.SUMESTONE_WALL);
        addDrop(NdBlocks.SUMESTONE_BRICKS_WALL);
        addDrop(NdBlocks.CRACKED_SUMESTONE_BRICKS_WALL);
        addDrop(NdBlocks.POLISHED_SUMESTONE_WALL);

        addDrop(NdBlocks.DARK_SUMESTONE);
        addDrop(NdBlocks.DARK_SUMESTONE_BRICKS);
        addDrop(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS);
        addDrop(NdBlocks.POLISHED_DARK_SUMESTONE);
        addDrop(NdBlocks.CHISELED_DARK_SUMESTONE);
        addDrop(NdBlocks.DARK_SUMESTONE_PILLAR);
        addDrop(NdBlocks.DARK_SUMESTONE_LANTERN);

        addDrop(NdBlocks.DARK_SUMESTONE_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.DARK_SUMESTONE_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_SLAB, NdBlockLootTables::dropsSlab);
        addDrop(NdBlocks.POLISHED_DARK_SUMESTONE_SLAB, NdBlockLootTables::dropsSlab);

        addDrop(NdBlocks.DARK_SUMESTONE_STAIRS);
        addDrop(NdBlocks.DARK_SUMESTONE_BRICKS_STAIRS);
        addDrop(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STAIRS);
        addDrop(NdBlocks.POLISHED_DARK_SUMESTONE_STAIRS);

        addDrop(NdBlocks.DARK_SUMESTONE_STEP);
        addDrop(NdBlocks.DARK_SUMESTONE_BRICKS_STEP);
        addDrop(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STEP);
        addDrop(NdBlocks.POLISHED_DARK_SUMESTONE_STEP);

        addDrop(NdBlocks.DARK_SUMESTONE_WALL);
        addDrop(NdBlocks.DARK_SUMESTONE_BRICKS_WALL);
        addDrop(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_WALL);
        addDrop(NdBlocks.POLISHED_DARK_SUMESTONE_WALL);

        Set<Identifier> set = Sets.newHashSet();
        Iterable<Block> blocks = Registry.BLOCK
                                     .stream()
                                     .filter(block -> Registry.BLOCK.getId(block)
                                                                    .getNamespace()
                                                                    .equals("ndebris"))
                                     ::iterator;

        for (Block block : blocks) {
            Identifier id = block.getLootTableId();
            if (id != LootTables.EMPTY && set.add(id)) {
                LootTable.Builder builder = lootTables.remove(id);
                if (builder == null) {
                    throw new IllegalStateException(
                        String.format(
                            "Missing loottable '%s' for '%s'", id,
                            Registry.BLOCK.getId(block)
                        )
                    );
                }

                biConsumer.accept(id, builder);
            }
        }

        if (!lootTables.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + lootTables.keySet());
        }
    }


    private static <T> T explosionFunc(ItemConvertible drop, LootFunctionConsumingBuilder<T> builder) {
        return !EXPLOSION_IMMUNE.contains(drop.asItem())
               ? builder.apply(ExplosionDecayLootFunction.builder())
               : builder.getThis();
    }

    private static <T> T explosionCond(ItemConvertible drop, LootConditionConsumingBuilder<T> builder) {
        return !EXPLOSION_IMMUNE.contains(drop.asItem())
               ? builder.conditionally(SurvivesExplosionLootCondition.builder())
               : builder.getThis();
    }

    private static ConditionalLootFunction.Builder<?> setCount(LootTableRange range) {
        return SetCountLootFunction.builder(range);
    }

    private static LootPool.Builder pool(LootTableRange rolls) {
        return LootPool.builder().rolls(rolls);
    }

    private static LootPool.Builder pool() {
        return pool(count(1));
    }

    private static LootTableRange count(int count) {
        return ConstantLootTableRange.create(count);
    }

    private static LootTableRange countRandom(float min, float max) {
        return UniformLootTableRange.between(min, max);
    }

    private static LootTableRange countBiased(int n, float p) {
        return BinomialLootTableRange.create(n, p);
    }

    private static <T extends Comparable<T>> StatePredicate.Builder stateProp(Property<T> prop, T val) {
        return StatePredicate.Builder.create().exactMatch(SlabBlock.TYPE, SlabType.DOUBLE);
    }

    private static <T extends Comparable<T>> BlockStatePropertyLootCondition.Builder stateCond(Block block, Property<T> prop, T val) {
        return BlockStatePropertyLootCondition.builder(block)
                                              .properties(stateProp(prop, val));
    }

    private static BoundedIntUnaryOperator atLeast(int min) {
        return BoundedIntUnaryOperator.createMin(min);
    }

    private static BoundedIntUnaryOperator atMost(int max) {
        return BoundedIntUnaryOperator.createMax(max);
    }

    private static BoundedIntUnaryOperator minMax(int min, int max) {
        return BoundedIntUnaryOperator.create(min, max);
    }

    private static RandomChanceLootCondition.Builder chance(float chance) {
        return RandomChanceLootCondition.builder(chance);
    }



    protected static LootTable.Builder drops(ItemConvertible drop) {
        return LootTable.builder().pool(
            explosionCond(
                drop,
                LootPool.builder().rolls(count(1))
                        .with(ItemEntry.builder(drop))
            )
        );
    }

    protected static LootTable.Builder drops(ItemConvertible drop, LootTableRange count) {
        return LootTable.builder().pool(
            pool().with(explosionFunc(drop, ItemEntry.builder(drop).apply(setCount(count))))
        );
    }

    protected static LootTable.Builder dropsConditionally(ItemConvertible drop, LootCondition.Builder condition, LootPoolEntry.Builder<?> orElse) {
        return LootTable.builder().pool(
            pool().with(ItemEntry.builder(drop).conditionally(condition).alternatively(orElse))
        );
    }

    protected static LootTable.Builder dropsWithSilkTouch(ItemConvertible drop, LootPoolEntry.Builder<?> orElse) {
        return dropsConditionally(drop, WITH_SILK_TOUCH, orElse);
    }

    protected static LootTable.Builder dropsWithShears(ItemConvertible drop, LootPoolEntry.Builder<?> orElse) {
        return dropsConditionally(drop, WITH_SHEARS, orElse);
    }

    protected static LootTable.Builder dropsWithSilkTouchOrShears(ItemConvertible drop, LootPoolEntry.Builder<?> orElse) {
        return dropsConditionally(drop, WITH_SILK_TOUCH_OR_SHEARS, orElse);
    }

    protected static LootTable.Builder dropsWithSilkTouch(ItemConvertible drop, ItemConvertible orElse) {
        return dropsWithSilkTouch(drop, explosionCond(drop, ItemEntry.builder(orElse)));
    }

    protected static LootTable.Builder dropsWithSilkTouch(ItemConvertible drop, ItemConvertible orElse, LootTableRange elseCount) {
        return dropsWithSilkTouch(drop, explosionFunc(drop, ItemEntry.builder(orElse).apply(setCount(elseCount))));
    }

    protected static LootTable.Builder dropsWithSilkTouch(ItemConvertible drop) {
        return LootTable.builder().pool(
            pool().conditionally(WITH_SILK_TOUCH)
                  .with(ItemEntry.builder(drop))
        );
    }

    protected static LootTable.Builder dropsFlowerPotWithPlant(ItemConvertible plant) {
        return LootTable.builder().pool(
            explosionCond(
                Blocks.FLOWER_POT,
                pool().with(ItemEntry.builder(Blocks.FLOWER_POT))
            )
        ).pool(
            explosionCond(
                plant,
                pool().with(ItemEntry.builder(plant))
            )
        );
    }

    protected static LootTable.Builder dropsSlab(Block slab) {
        return LootTable.builder().pool(
            pool().with(explosionFunc(
                slab,
                ItemEntry.builder(slab).apply(
                    setCount(count(2)).conditionally(
                        stateCond(slab, SlabBlock.TYPE, SlabType.DOUBLE)
                    )
                )
            )));
    }

    protected static <T extends Comparable<T>> LootTable.Builder dropsWithProperty(Block drop, Property<T> prop, T val) {
        return LootTable.builder().pool(
            explosionCond(
                drop,
                pool().with(ItemEntry.builder(drop).conditionally(stateCond(drop, prop, val)))
            )
        );
    }

    protected static LootTable.Builder dropsNamedContainer(Block drop) {
        return LootTable.builder().pool(explosionCond(
            drop, pool().with(
                ItemEntry.builder(drop)
                         .apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY))
            )
        ));
    }

    protected static LootTable.Builder dropsShulkerBox(Block drop) {
        return LootTable.builder().pool(explosionCond(
            drop, pool().with(
                ItemEntry.builder(drop)
                         .apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY))
                         .apply(CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY)
                                                   .withOperation("Lock", "BlockEntityTag.Lock")
                                                   .withOperation("LootTable", "BlockEntityTag.LootTable")
                                                   .withOperation("LootTableSeed", "BlockEntityTag.LootTableSeed")
                         )
                         .apply(SetContentsLootFunction.builder()
                                                       .withEntry(DynamicEntry.builder(ShulkerBoxBlock.CONTENTS))
                         )
            )
        ));
    }

    protected static LootTable.Builder dropsBanner(Block drop) {
        return LootTable.builder().pool(explosionCond(
            drop, pool().with(
                ItemEntry.builder(drop)
                         .apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY))
                         .apply(CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY)
                                                   .withOperation("Patterns", "BlockEntityTag.Patterns")
                         )
            )
        ));
    }

    protected static LootTable.Builder dropsBeeNest(Block drop) {
        return LootTable.builder().pool(
            pool().conditionally(WITH_SILK_TOUCH).with(
                ItemEntry.builder(drop)
                         .apply(CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY)
                                                   .withOperation("Bees", "BlockEntityTag.Bees")
                         )
                         .apply(CopyStateFunction.getBuilder(drop).method_21898(BeehiveBlock.HONEY_LEVEL))
            )
        );
    }

    protected static LootTable.Builder dropsBeehive(Block drop) {
        return LootTable.builder().pool(
            pool().with(
                ItemEntry.builder(drop)
                         .conditionally(WITH_SILK_TOUCH)
                         .apply(CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY)
                                                   .withOperation("Bees", "BlockEntityTag.Bees")
                         )
                         .apply(CopyStateFunction.getBuilder(drop).method_21898(BeehiveBlock.HONEY_LEVEL))
                         .alternatively(ItemEntry.builder(drop))
            )
        );
    }

    protected static LootTable.Builder dropsMineral(ItemConvertible ore, ItemConvertible mineral) {
        return dropsWithSilkTouch(
            ore,
            explosionFunc(
                ore,
                ItemEntry.builder(mineral)
                         .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
            )
        );
    }

    protected static LootTable.Builder dropsMushroomBlock(Block block, ItemConvertible mushroom) {
        return dropsWithSilkTouch(
            block,
            explosionFunc(
                block,
                ItemEntry.builder(mushroom)
                         .apply(setCount(countRandom(-6, 2)))
                         .apply(LimitCountLootFunction.builder(atLeast(0)))
            )
        );
    }

    protected static LootTable.Builder dropsGrass(Block grass) {
        return dropsWithShears(
            grass,
            explosionFunc(
                grass,
                ItemEntry.builder(Items.WHEAT_SEEDS)
                         .conditionally(chance(0.125f))
                         .apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE, 2))
            )
        );
    }

    protected static LootTable.Builder dropsCropStem(Block stem, ItemConvertible seed) {
        return LootTable.builder().pool(explosionFunc(
            stem, pool().with(
                ItemEntry.builder(seed)
                         .apply(setCount(countBiased(3, 1 / 15f)).conditionally(stateCond(stem, StemBlock.AGE, 0)))
                         .apply(setCount(countBiased(3, 1 / 7.5f)).conditionally(stateCond(stem, StemBlock.AGE, 1)))
                         .apply(setCount(countBiased(3, 1 / 5f)).conditionally(stateCond(stem, StemBlock.AGE, 2)))
                         .apply(setCount(countBiased(3, 1 / 3.75f)).conditionally(stateCond(stem, StemBlock.AGE, 3)))
                         .apply(setCount(countBiased(3, 1 / 3f)).conditionally(stateCond(stem, StemBlock.AGE, 4)))
                         .apply(setCount(countBiased(3, 1 / 2.5f)).conditionally(stateCond(stem, StemBlock.AGE, 5)))
                         .apply(setCount(countBiased(3, 1 / 2.142857f)).conditionally(stateCond(stem, StemBlock.AGE, 6)))
                         .apply(setCount(countBiased(3, 1 / 1.875f)).conditionally(stateCond(stem, StemBlock.AGE, 7)))
            )
        ));
    }

    protected static LootTable.Builder dropsCropStemAttached(Block stem, ItemConvertible seed) {
        return LootTable.builder().pool(explosionFunc(
            stem, pool().with(
                ItemEntry.builder(seed).apply(setCount(countBiased(3, 1 / 1.875f)))
            )
        ));
    }

    protected static LootTable.Builder dropsWithShears(ItemConvertible drop) {
        return LootTable.builder().pool(
            pool().conditionally(WITH_SHEARS)
                  .with(ItemEntry.builder(drop))
        );
    }

    protected static LootTable.Builder dropLeaves(Block leaves, ItemConvertible sapling, float... chance) {
        return dropsWithSilkTouchOrShears(
            leaves,
            explosionCond(leaves, ItemEntry.builder(sapling))
                .conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, chance))
        ).pool(
            pool().conditionally(WITHOUT_SILK_TOUCH_NOR_SHEARS).with(
                explosionFunc(
                    leaves,
                    ItemEntry.builder(Items.STICK)
                             .apply(setCount(countRandom(1, 2)))
                ).conditionally(
                    TableBonusLootCondition.builder(
                        Enchantments.FORTUNE,
                        1 / 50f, 1 / 45f, 1 / 40f, 1 / 30f, 1 / 10f
                    )
                )
            )
        );
    }

    protected static LootTable.Builder dropLeavesExtra(Block leaves, ItemConvertible sapling, ItemConvertible extra, float... chance) {
        return dropLeaves(leaves, sapling, chance).pool(
            pool().conditionally(WITHOUT_SILK_TOUCH_NOR_SHEARS).with(
                explosionCond(leaves, ItemEntry.builder(extra)).conditionally(
                    TableBonusLootCondition.builder(
                        Enchantments.FORTUNE,
                        1 / 200f, 1 / 180f, 1 / 160f, 1 / 120f, 1 / 40f
                    )
                )
            )
        );
    }

    protected static LootTable.Builder dropsCrop(Block crop, ItemConvertible product, ItemConvertible seeds, LootCondition.Builder condition) {
        return explosionFunc(crop, LootTable.builder().pool(
            pool().with(
                ItemEntry.builder(product)
                         .conditionally(condition)
                         .alternatively(ItemEntry.builder(seeds))
            )
        ).pool(
            LootPool.builder()
                    .conditionally(condition)
                    .with(ItemEntry.builder(seeds).apply(
                        ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 1 / 1.75f, 3)
                    ))
        ));
    }

    protected static LootTable.Builder dropsDoublePlant(ItemConvertible drop) {
        return LootTable.builder().pool(
            pool().conditionally(WITH_SHEARS)
                  .with(ItemEntry.builder(drop).apply(setCount(count(2))))
        );
    }

    protected static LootTable.Builder dropsDoubleGrass(Block plant, ItemConvertible shearDrops) {
        LootPoolEntry.Builder<?> seeds
            = ItemEntry.builder(shearDrops)
                       .apply(setCount(count(2)))
                       .conditionally(WITH_SHEARS)
                       .alternatively(
                           explosionCond(plant, ItemEntry.builder(Items.WHEAT_SEEDS))
                               .conditionally(chance(1 / 8f))
                       );

        return LootTable.builder().pool(
            pool().with(seeds)
                  .conditionally(stateCond(plant, TallPlantBlock.HALF, DoubleBlockHalf.LOWER))
                  .conditionally(checkDoublePlantHalf(plant, DoubleBlockHalf.UPPER))
        ).pool(
            pool().with(seeds)
                  .conditionally(stateCond(plant, TallPlantBlock.HALF, DoubleBlockHalf.UPPER))
                  .conditionally(checkDoublePlantHalf(plant, DoubleBlockHalf.LOWER))
        );
    }

    private static LocationCheckLootCondition.Builder checkDoublePlantHalf(Block plant, DoubleBlockHalf half) {
        int d = half == DoubleBlockHalf.UPPER ? 1 : -1;
        StatePredicate expectState = stateProp(TallPlantBlock.HALF, half).build();

        return LocationCheckLootCondition.method_30151(
            LocationPredicate.Builder.create().block(
                BlockPredicate.Builder.create().block(plant).state(expectState).build()
            ),
            new BlockPos(0, d, 0)
        );
    }

    protected static LootTable.Builder dropsNothing() {
        return LootTable.builder();
    }

    public static LootTable.Builder addDoorDrop(Block block) {
        return dropsWithProperty(block, DoorBlock.HALF, DoubleBlockHalf.LOWER);
    }

    public void addPottedPlantDrop(Block block) {
        addDrop(block, blockx -> dropsFlowerPotWithPlant(((FlowerPotBlock) blockx).getContent()));
    }

    public void addDropWithSilkTouch(Block block, Block drop) {
        addDrop(block, dropsWithSilkTouch(drop));
    }

    public void addDrop(Block block, ItemConvertible drop) {
        addDrop(block, drops(drop));
    }

    public void addDropWithSilkTouch(Block block) {
        addDropWithSilkTouch(block, block);
    }

    public void addDrop(Block block) {
        addDrop(block, block);
    }

    private void addDrop(Block block, Function<Block, LootTable.Builder> function) {
        addDrop(block, function.apply(block));
    }

    private void addDrop(Block block, LootTable.Builder lootTable) {
        lootTables.put(block.getLootTableId(), lootTable);
    }
}
