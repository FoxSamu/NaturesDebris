package natures.debris.data.models;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import natures.debris.common.block.NdBlocks;
import natures.debris.common.item.NdItems;
import natures.debris.data.models.modelgen.IModelGen;

import static natures.debris.data.models.modelgen.InheritingModelGen.*;

public final class ItemModelTable {
    private static BiConsumer<Item, IModelGen> consumer;

    public static void registerItemModels(BiConsumer<Item, IModelGen> c) {
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



        register(NdItems.MUSIC_DISC_DARK, item -> generated(name(item, "item/%s")));
        register(NdItems.MUSIC_DISC_M1, item -> generated(name(item, "item/%s")));
    }



    private static void register(IItemProvider provider, Function<Item, IModelGen> genFactory) {
        Item item = provider.asItem();
        IModelGen gen = genFactory.apply(item);
        consumer.accept(item, gen);
    }

    private static String name(Item item, String nameFormat) {
        ResourceLocation id = item.getRegistryName();
        assert id != null;

        return String.format("%s:%s", id.getNamespace(), String.format(nameFormat, id.getPath()));
    }

    private static String name(Item item) {
        ResourceLocation id = item.getRegistryName();
        assert id != null;
        return id.toString();
    }

    private static String name(Item item, String nameFormat, String omitSuffix) {
        ResourceLocation id = item.getRegistryName();
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
