package net.shadew.ndebris.data.models;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.shadew.ndebris.common.block.NdBlocks;
import net.shadew.ndebris.common.item.NdItems;
import net.shadew.ndebris.data.models.modelgen.ModelGen;

import static net.shadew.ndebris.data.models.modelgen.InheritingModelGen.*;

public final class ItemModelTable {
    private static BiConsumer<Item, ModelGen> consumer;

    public static void registerItemModels(BiConsumer<Item, ModelGen> c) {
        consumer = c;

        register(NdBlocks.MURKY_DIRT, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_GRASS_BLOCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_COARSE_DIRT, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_HUMUS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_PODZOL, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_CLAY, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_TERRACOTTA, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_SAND, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MURKY_GRASS_PATH, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LEAFY_HUMUS, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.BLACKWOOD_LOG, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.INVER_LOG, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.BLACKWOOD, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.INVER_WOOD, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.STRIPPED_BLACKWOOD_LOG, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.STRIPPED_INVER_LOG, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.STRIPPED_BLACKWOOD, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.STRIPPED_INVER_WOOD, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.BLACKWOOD_PLANKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.INVER_PLANKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.BLACKWOOD_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.INVER_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.BLACKWOOD_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.INVER_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.BLACKWOOD_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.INVER_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.BLACKWOOD_FENCE, item -> fenceInventory(name(item, "block/%s_planks", "_fence")));
        register(NdBlocks.INVER_FENCE, item -> fenceInventory(name(item, "block/%s_planks", "_fence")));

        register(NdBlocks.ROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_ROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_ROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CHISELED_ROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_PILLAR, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_LANTERN, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.ROCK_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_ROCK_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_ROCK_SLAB, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.ROCK_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_ROCK_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_ROCK_STAIRS, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.ROCK_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.ROCK_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_ROCK_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_ROCK_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_ROCK_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_ROCK_STEP, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.ROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.MOSSY_ROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.ROCK_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.MOSSY_ROCK_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_ROCK_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.ROCK_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.MOSSY_ROCK_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_ROCK_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.SMOOTH_ROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.POLISHED_ROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));

        register(NdBlocks.DARKROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_DARKROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARKROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CHISELED_DARKROCK, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_PILLAR, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_LANTERN, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARKROCK_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_DARKROCK_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARKROCK_SLAB, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARKROCK_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_DARKROCK_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARKROCK_STAIRS, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARKROCK_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARKROCK_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.MOSSY_DARKROCK_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARKROCK_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SMOOTH_DARKROCK_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARKROCK_STEP, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARKROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.MOSSY_DARKROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.DARKROCK_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.MOSSY_DARKROCK_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_DARKROCK_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.DARKROCK_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.MOSSY_DARKROCK_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_DARKROCK_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.SMOOTH_DARKROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.POLISHED_DARKROCK_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));

        register(NdBlocks.LIMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_TILES, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_LIMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CARVED_LIMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_PILLAR, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_LANTERN, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.LIMESTONE_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_TILES_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_LIMESTONE_SLAB, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.LIMESTONE_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_TILES_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_LIMESTONE_STAIRS, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.LIMESTONE_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.LIMESTONE_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_LIMESTONE_TILES_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_LIMESTONE_STEP, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.LIMESTONE_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.LIMESTONE_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_LIMESTONE_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.LIMESTONE_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_LIMESTONE_TILES_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.POLISHED_LIMESTONE_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));

        register(NdBlocks.SUMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SUMESTONE_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_SUMESTONE_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_SUMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CHISELED_SUMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SUMESTONE_PILLAR, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SUMESTONE_LANTERN, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.SUMESTONE_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SUMESTONE_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_SUMESTONE_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_SUMESTONE_SLAB, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.SUMESTONE_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SUMESTONE_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_SUMESTONE_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_SUMESTONE_STAIRS, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.SUMESTONE_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.SUMESTONE_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_SUMESTONE_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_SUMESTONE_STEP, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.SUMESTONE_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.SUMESTONE_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_SUMESTONE_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.POLISHED_SUMESTONE_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));

        register(NdBlocks.DARK_SUMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARK_SUMESTONE_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARK_SUMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CHISELED_DARK_SUMESTONE, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARK_SUMESTONE_PILLAR, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARK_SUMESTONE_LANTERN, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARK_SUMESTONE_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARK_SUMESTONE_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_SLAB, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARK_SUMESTONE_SLAB, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARK_SUMESTONE_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARK_SUMESTONE_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STAIRS, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARK_SUMESTONE_STAIRS, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARK_SUMESTONE_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.DARK_SUMESTONE_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STEP, item -> inherit(name(item, "block/%s")));
        register(NdBlocks.POLISHED_DARK_SUMESTONE_STEP, item -> inherit(name(item, "block/%s")));

        register(NdBlocks.DARK_SUMESTONE_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.DARK_SUMESTONE_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));
        register(NdBlocks.POLISHED_DARK_SUMESTONE_WALL, item -> wallInventory(name(item, "block/%s", "_wall")));



        register(NdItems.MUSIC_DISC_DARK, item -> generated(name(item, "item/%s")));
        register(NdItems.MUSIC_DISC_M1, item -> generated(name(item, "item/%s")));
    }



    private static void register(ItemConvertible provider, Function<Item, ModelGen> genFactory) {
        Item item = provider.asItem();
        ModelGen gen = genFactory.apply(item);
        consumer.accept(item, gen);
    }

    private static String name(Item item, String nameFormat) {
        Identifier id = Registry.ITEM.getId(item);
        assert id != null;

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, id.getPath()));
    }

    private static String name(Item item) {
        Identifier id = Registry.ITEM.getId(item);
        assert id != null;
        return id.toString();
    }

    private static String name(Item item, String nameFormat, String omitSuffix) {
        Identifier id = Registry.ITEM.getId(item);
        assert id != null;

        String path = id.getPath();
        if (path.endsWith(omitSuffix)) {
            path = path.substring(0, path.length() - omitSuffix.length());
        }

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, path));
    }

    private ItemModelTable() {
    }
}
