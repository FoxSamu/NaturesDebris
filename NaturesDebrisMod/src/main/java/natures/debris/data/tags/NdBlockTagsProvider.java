package natures.debris.data.tags;

import java.nio.file.Path;

import net.minecraftforge.common.Tags;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import natures.debris.common.block.NdBlocks;
import natures.debris.common.tags.NdBlockTags;

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

        getBuilder(NdBlockTags.STEPS).replace(false).add(
            NdBlocks.ROCK_STEP,
            NdBlocks.MOSSY_ROCK_STEP,
            NdBlocks.ROCK_BRICKS_STEP,
            NdBlocks.MOSSY_ROCK_BRICKS_STEP,
            NdBlocks.CRACKED_ROCK_BRICKS_STEP,
            NdBlocks.ROCK_TILES_STEP,
            NdBlocks.MOSSY_ROCK_TILES_STEP,
            NdBlocks.CRACKED_ROCK_TILES_STEP,
            NdBlocks.SMOOTH_ROCK_STEP,
            NdBlocks.POLISHED_ROCK_STEP,

            NdBlocks.DARKROCK_STEP,
            NdBlocks.MOSSY_DARKROCK_STEP,
            NdBlocks.DARKROCK_BRICKS_STEP,
            NdBlocks.MOSSY_DARKROCK_BRICKS_STEP,
            NdBlocks.CRACKED_DARKROCK_BRICKS_STEP,
            NdBlocks.DARKROCK_TILES_STEP,
            NdBlocks.MOSSY_DARKROCK_TILES_STEP,
            NdBlocks.CRACKED_DARKROCK_TILES_STEP,
            NdBlocks.SMOOTH_DARKROCK_STEP,
            NdBlocks.POLISHED_DARKROCK_STEP
        );

        getBuilder(Tags.Blocks.DIRT).replace(false).add(
            NdBlocks.MURKY_DIRT,
            NdBlocks.MURKY_GRASS_BLOCK,
            NdBlocks.MURKY_COARSE_DIRT,
            NdBlocks.MURKY_HUMUS,
            NdBlocks.LEAFY_HUMUS,
            NdBlocks.MURKY_PODZOL
        );

        getBuilder(NdBlockTags.BLACKWOOD_LOGS).replace(false).add(
            NdBlocks.BLACKWOOD_LOG,
            NdBlocks.STRIPPED_BLACKWOOD_LOG,
            NdBlocks.BLACKWOOD,
            NdBlocks.STRIPPED_BLACKWOOD
        );

        getBuilder(NdBlockTags.INVER_LOGS).replace(false).add(
            NdBlocks.INVER_LOG,
            NdBlocks.STRIPPED_INVER_LOG,
            NdBlocks.INVER_WOOD,
            NdBlocks.STRIPPED_INVER_WOOD
        );

        getBuilder(BlockTags.LOGS).replace(false).add(
            NdBlockTags.BLACKWOOD_LOGS,
            NdBlockTags.INVER_LOGS
        );

        getBuilder(BlockTags.ENDERMAN_HOLDABLE).replace(false).add(
            NdBlocks.MURKY_DIRT,
            NdBlocks.MURKY_GRASS_BLOCK,
            NdBlocks.MURKY_COARSE_DIRT,
            NdBlocks.MURKY_HUMUS,
            NdBlocks.LEAFY_HUMUS,
            NdBlocks.MURKY_PODZOL,
            NdBlocks.MURKY_SAND,
            NdBlocks.MURKY_CLAY
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
