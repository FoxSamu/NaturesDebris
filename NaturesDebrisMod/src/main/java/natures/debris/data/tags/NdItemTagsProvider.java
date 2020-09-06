package natures.debris.data.tags;

import java.nio.file.Path;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import natures.debris.common.block.NdBlocks;
import natures.debris.common.tags.NdItemTags;

public class NdItemTagsProvider extends TagsProvider<Item> {
    @SuppressWarnings("deprecation") // We need Registry.ITEM. Sorry Forge...
    public NdItemTagsProvider(DataGenerator gen) {
        super(gen, Registry.ITEM);
    }

    @Override
    protected void registerTags() {
        getBuilder(ItemTags.SLABS).replace(false).add(
            NdBlocks.ROCK_SLAB.asItem(),
            NdBlocks.MOSSY_ROCK_SLAB.asItem(),
            NdBlocks.ROCK_BRICKS_SLAB.asItem(),
            NdBlocks.MOSSY_ROCK_BRICKS_SLAB.asItem(),
            NdBlocks.CRACKED_ROCK_BRICKS_SLAB.asItem(),
            NdBlocks.ROCK_TILES_SLAB.asItem(),
            NdBlocks.MOSSY_ROCK_TILES_SLAB.asItem(),
            NdBlocks.CRACKED_ROCK_TILES_SLAB.asItem(),
            NdBlocks.SMOOTH_ROCK_SLAB.asItem(),
            NdBlocks.POLISHED_ROCK_SLAB.asItem(),

            NdBlocks.DARKROCK_SLAB.asItem(),
            NdBlocks.MOSSY_DARKROCK_SLAB.asItem(),
            NdBlocks.DARKROCK_BRICKS_SLAB.asItem(),
            NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB.asItem(),
            NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB.asItem(),
            NdBlocks.DARKROCK_TILES_SLAB.asItem(),
            NdBlocks.MOSSY_DARKROCK_TILES_SLAB.asItem(),
            NdBlocks.CRACKED_DARKROCK_TILES_SLAB.asItem(),
            NdBlocks.SMOOTH_DARKROCK_SLAB.asItem(),
            NdBlocks.POLISHED_DARKROCK_SLAB.asItem()
        );

        getBuilder(ItemTags.STAIRS).replace(false).add(
            NdBlocks.ROCK_STAIRS.asItem(),
            NdBlocks.MOSSY_ROCK_STAIRS.asItem(),
            NdBlocks.ROCK_BRICKS_STAIRS.asItem(),
            NdBlocks.MOSSY_ROCK_BRICKS_STAIRS.asItem(),
            NdBlocks.CRACKED_ROCK_BRICKS_STAIRS.asItem(),
            NdBlocks.ROCK_TILES_STAIRS.asItem(),
            NdBlocks.MOSSY_ROCK_TILES_STAIRS.asItem(),
            NdBlocks.CRACKED_ROCK_TILES_STAIRS.asItem(),
            NdBlocks.SMOOTH_ROCK_STAIRS.asItem(),
            NdBlocks.POLISHED_ROCK_STAIRS.asItem(),

            NdBlocks.DARKROCK_STAIRS.asItem(),
            NdBlocks.MOSSY_DARKROCK_STAIRS.asItem(),
            NdBlocks.DARKROCK_BRICKS_STAIRS.asItem(),
            NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS.asItem(),
            NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS.asItem(),
            NdBlocks.DARKROCK_TILES_STAIRS.asItem(),
            NdBlocks.MOSSY_DARKROCK_TILES_STAIRS.asItem(),
            NdBlocks.CRACKED_DARKROCK_TILES_STAIRS.asItem(),
            NdBlocks.SMOOTH_DARKROCK_STAIRS.asItem(),
            NdBlocks.POLISHED_DARKROCK_STAIRS.asItem()
        );

        getBuilder(NdItemTags.STEPS).replace(false).add(
            NdBlocks.ROCK_STEP.asItem(),
            NdBlocks.MOSSY_ROCK_STEP.asItem(),
            NdBlocks.ROCK_BRICKS_STEP.asItem(),
            NdBlocks.MOSSY_ROCK_BRICKS_STEP.asItem(),
            NdBlocks.CRACKED_ROCK_BRICKS_STEP.asItem(),
            NdBlocks.ROCK_TILES_STEP.asItem(),
            NdBlocks.MOSSY_ROCK_TILES_STEP.asItem(),
            NdBlocks.CRACKED_ROCK_TILES_STEP.asItem(),
            NdBlocks.SMOOTH_ROCK_STEP.asItem(),
            NdBlocks.POLISHED_ROCK_STEP.asItem(),

            NdBlocks.DARKROCK_STEP.asItem(),
            NdBlocks.MOSSY_DARKROCK_STEP.asItem(),
            NdBlocks.DARKROCK_BRICKS_STEP.asItem(),
            NdBlocks.MOSSY_DARKROCK_BRICKS_STEP.asItem(),
            NdBlocks.CRACKED_DARKROCK_BRICKS_STEP.asItem(),
            NdBlocks.DARKROCK_TILES_STEP.asItem(),
            NdBlocks.MOSSY_DARKROCK_TILES_STEP.asItem(),
            NdBlocks.CRACKED_DARKROCK_TILES_STEP.asItem(),
            NdBlocks.SMOOTH_DARKROCK_STEP.asItem(),
            NdBlocks.POLISHED_DARKROCK_STEP.asItem()
        ).add(
            NdItemTags.WOODEN_STEPS
        );

        getBuilder(ItemTags.WALLS).replace(false).add(
            NdBlocks.ROCK_WALL.asItem(),
            NdBlocks.MOSSY_ROCK_WALL.asItem(),
            NdBlocks.ROCK_BRICKS_WALL.asItem(),
            NdBlocks.MOSSY_ROCK_BRICKS_WALL.asItem(),
            NdBlocks.CRACKED_ROCK_BRICKS_WALL.asItem(),
            NdBlocks.ROCK_TILES_WALL.asItem(),
            NdBlocks.MOSSY_ROCK_TILES_WALL.asItem(),
            NdBlocks.CRACKED_ROCK_TILES_WALL.asItem(),
            NdBlocks.SMOOTH_ROCK_WALL.asItem(),
            NdBlocks.POLISHED_ROCK_WALL.asItem(),

            NdBlocks.DARKROCK_WALL.asItem(),
            NdBlocks.MOSSY_DARKROCK_WALL.asItem(),
            NdBlocks.DARKROCK_BRICKS_WALL.asItem(),
            NdBlocks.MOSSY_DARKROCK_BRICKS_WALL.asItem(),
            NdBlocks.CRACKED_DARKROCK_BRICKS_WALL.asItem(),
            NdBlocks.DARKROCK_TILES_WALL.asItem(),
            NdBlocks.MOSSY_DARKROCK_TILES_WALL.asItem(),
            NdBlocks.CRACKED_DARKROCK_TILES_WALL.asItem(),
            NdBlocks.SMOOTH_DARKROCK_WALL.asItem(),
            NdBlocks.POLISHED_DARKROCK_WALL.asItem()
        );

        getBuilder(ItemTags.WOODEN_SLABS).replace(false).add(
            NdBlocks.BLACKWOOD_SLAB.asItem(),
            NdBlocks.INVER_SLAB.asItem()
        );

        getBuilder(ItemTags.WOODEN_STAIRS).replace(false).add(
            NdBlocks.BLACKWOOD_STAIRS.asItem(),
            NdBlocks.INVER_STAIRS.asItem()
        );

        getBuilder(NdItemTags.WOODEN_STEPS).replace(false).add(
            NdBlocks.BLACKWOOD_STEP.asItem(),
            NdBlocks.INVER_STEP.asItem()
        );

        getBuilder(ItemTags.WOODEN_FENCES).replace(false).add(
            NdBlocks.BLACKWOOD_FENCE.asItem(),
            NdBlocks.INVER_FENCE.asItem()
        );

        getBuilder(NdItemTags.BLACKWOOD_LOGS).replace(false).add(
            NdBlocks.BLACKWOOD_LOG.asItem(),
            NdBlocks.STRIPPED_BLACKWOOD_LOG.asItem(),
            NdBlocks.BLACKWOOD.asItem(),
            NdBlocks.STRIPPED_BLACKWOOD.asItem()
        );

        getBuilder(NdItemTags.INVER_LOGS).replace(false).add(
            NdBlocks.INVER_LOG.asItem(),
            NdBlocks.STRIPPED_INVER_LOG.asItem(),
            NdBlocks.INVER_WOOD.asItem(),
            NdBlocks.STRIPPED_INVER_WOOD.asItem()
        );

        getBuilder(ItemTags.LOGS).replace(false).add(
            NdItemTags.BLACKWOOD_LOGS,
            NdItemTags.INVER_LOGS
        );
    }

    @Override
    protected void setCollection(TagCollection<Item> collection) {
        ItemTags.setCollection(collection);
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "NaturesDebris/ItemTags";
    }
}
