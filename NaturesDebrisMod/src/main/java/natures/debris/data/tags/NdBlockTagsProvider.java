package natures.debris.data.tags;

import natures.debris.common.block.NdBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;

public class NdBlockTagsProvider extends TagsProvider<Block> {
    @SuppressWarnings("deprecation") // We need Registry.BLOCK. Sorry Forge...
    public NdBlockTagsProvider(DataGenerator gen) {
        super(gen, Registry.BLOCK);
    }

    @Override
    protected void registerTags() {
        getBuilder(BlockTags.SLABS).replace(false).add(
            NdBlocks.ROCK_SLAB,
            NdBlocks.MOSSY_ROCK_SLAB,
            NdBlocks.ROCK_BRICKS_SLAB,
            NdBlocks.MOSSY_ROCK_BRICKS_SLAB,
            NdBlocks.CRACKED_ROCK_BRICKS_SLAB,
            NdBlocks.ROCK_TILES_SLAB,
            NdBlocks.MOSSY_ROCK_TILES_SLAB,
            NdBlocks.CRACKED_ROCK_TILES_SLAB,
            NdBlocks.SMOOTH_ROCK_SLAB,
            NdBlocks.POLISHED_ROCK_SLAB,

            NdBlocks.DARKROCK_SLAB,
            NdBlocks.MOSSY_DARKROCK_SLAB,
            NdBlocks.DARKROCK_BRICKS_SLAB,
            NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB,
            NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB,
            NdBlocks.DARKROCK_TILES_SLAB,
            NdBlocks.MOSSY_DARKROCK_TILES_SLAB,
            NdBlocks.CRACKED_DARKROCK_TILES_SLAB,
            NdBlocks.SMOOTH_DARKROCK_SLAB,
            NdBlocks.POLISHED_DARKROCK_SLAB
        );

        getBuilder(BlockTags.STAIRS).replace(false).add(
            NdBlocks.ROCK_STAIRS,
            NdBlocks.MOSSY_ROCK_STAIRS,
            NdBlocks.ROCK_BRICKS_STAIRS,
            NdBlocks.MOSSY_ROCK_BRICKS_STAIRS,
            NdBlocks.CRACKED_ROCK_BRICKS_STAIRS,
            NdBlocks.ROCK_TILES_STAIRS,
            NdBlocks.MOSSY_ROCK_TILES_STAIRS,
            NdBlocks.CRACKED_ROCK_TILES_STAIRS,
            NdBlocks.SMOOTH_ROCK_STAIRS,
            NdBlocks.POLISHED_ROCK_STAIRS,

            NdBlocks.DARKROCK_STAIRS,
            NdBlocks.MOSSY_DARKROCK_STAIRS,
            NdBlocks.DARKROCK_BRICKS_STAIRS,
            NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS,
            NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS,
            NdBlocks.DARKROCK_TILES_STAIRS,
            NdBlocks.MOSSY_DARKROCK_TILES_STAIRS,
            NdBlocks.CRACKED_DARKROCK_TILES_STAIRS,
            NdBlocks.SMOOTH_DARKROCK_STAIRS,
            NdBlocks.POLISHED_DARKROCK_STAIRS
        );
    }

    @Override
    protected void setCollection(TagCollection<Block> collection) {
        BlockTags.setCollection(collection);
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "NaturesDebris/BlockTags";
    }
}
