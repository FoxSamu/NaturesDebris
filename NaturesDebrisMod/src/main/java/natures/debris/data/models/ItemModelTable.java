package natures.debris.data.models;

import natures.debris.common.block.NdBlocks;
import natures.debris.data.models.modelgen.IModelGen;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static natures.debris.data.models.modelgen.InheritingModelGen.*;

public final class ItemModelTable {
    private static BiConsumer<Item, IModelGen> consumer;

    public static void registerItemModels(BiConsumer<Item, IModelGen> c) {
        consumer = c;

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

    private ItemModelTable() {
    }
}
