package net.shadew.ndebris.data.tags;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;

import net.shadew.ndebris.common.block.NdBlocks;
import net.shadew.ndebris.common.tags.NdBlockTags;

public class NdBlockTagsProvider extends AbstractTagProvider<Block> {
    public NdBlockTagsProvider(DataGenerator gen) {
        super(gen, Registry.BLOCK);
    }

    @Override
    protected void configure() {
        getOrCreateTagBuilder(BlockTags.SLABS)
            .add(NdBlocks.ROCK_SLAB)
            .add(NdBlocks.MOSSY_ROCK_SLAB)
            .add(NdBlocks.ROCK_BRICKS_SLAB)
            .add(NdBlocks.MOSSY_ROCK_BRICKS_SLAB)
            .add(NdBlocks.CRACKED_ROCK_BRICKS_SLAB)
            .add(NdBlocks.ROCK_TILES_SLAB)
            .add(NdBlocks.MOSSY_ROCK_TILES_SLAB)
            .add(NdBlocks.CRACKED_ROCK_TILES_SLAB)
            .add(NdBlocks.SMOOTH_ROCK_SLAB)
            .add(NdBlocks.POLISHED_ROCK_SLAB)

            .add(NdBlocks.DARKROCK_SLAB)
            .add(NdBlocks.MOSSY_DARKROCK_SLAB)
            .add(NdBlocks.DARKROCK_BRICKS_SLAB)
            .add(NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB)
            .add(NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB)
            .add(NdBlocks.DARKROCK_TILES_SLAB)
            .add(NdBlocks.MOSSY_DARKROCK_TILES_SLAB)
            .add(NdBlocks.CRACKED_DARKROCK_TILES_SLAB)
            .add(NdBlocks.SMOOTH_DARKROCK_SLAB)
            .add(NdBlocks.POLISHED_DARKROCK_SLAB);

        getOrCreateTagBuilder(BlockTags.STAIRS)
            .add(NdBlocks.ROCK_STAIRS)
            .add(NdBlocks.MOSSY_ROCK_STAIRS)
            .add(NdBlocks.ROCK_BRICKS_STAIRS)
            .add(NdBlocks.MOSSY_ROCK_BRICKS_STAIRS)
            .add(NdBlocks.CRACKED_ROCK_BRICKS_STAIRS)
            .add(NdBlocks.ROCK_TILES_STAIRS)
            .add(NdBlocks.MOSSY_ROCK_TILES_STAIRS)
            .add(NdBlocks.CRACKED_ROCK_TILES_STAIRS)
            .add(NdBlocks.SMOOTH_ROCK_STAIRS)
            .add(NdBlocks.POLISHED_ROCK_STAIRS)

            .add(NdBlocks.DARKROCK_STAIRS)
            .add(NdBlocks.MOSSY_DARKROCK_STAIRS)
            .add(NdBlocks.DARKROCK_BRICKS_STAIRS)
            .add(NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS)
            .add(NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS)
            .add(NdBlocks.DARKROCK_TILES_STAIRS)
            .add(NdBlocks.MOSSY_DARKROCK_TILES_STAIRS)
            .add(NdBlocks.CRACKED_DARKROCK_TILES_STAIRS)
            .add(NdBlocks.SMOOTH_DARKROCK_STAIRS)
            .add(NdBlocks.POLISHED_DARKROCK_STAIRS);

        getOrCreateTagBuilder(NdBlockTags.STEPS)
            .add(NdBlocks.ROCK_STEP)
            .add(NdBlocks.MOSSY_ROCK_STEP)
            .add(NdBlocks.ROCK_BRICKS_STEP)
            .add(NdBlocks.MOSSY_ROCK_BRICKS_STEP)
            .add(NdBlocks.CRACKED_ROCK_BRICKS_STEP)
            .add(NdBlocks.ROCK_TILES_STEP)
            .add(NdBlocks.MOSSY_ROCK_TILES_STEP)
            .add(NdBlocks.CRACKED_ROCK_TILES_STEP)
            .add(NdBlocks.SMOOTH_ROCK_STEP)
            .add(NdBlocks.POLISHED_ROCK_STEP)

            .add(NdBlocks.DARKROCK_STEP)
            .add(NdBlocks.MOSSY_DARKROCK_STEP)
            .add(NdBlocks.DARKROCK_BRICKS_STEP)
            .add(NdBlocks.MOSSY_DARKROCK_BRICKS_STEP)
            .add(NdBlocks.CRACKED_DARKROCK_BRICKS_STEP)
            .add(NdBlocks.DARKROCK_TILES_STEP)
            .add(NdBlocks.MOSSY_DARKROCK_TILES_STEP)
            .add(NdBlocks.CRACKED_DARKROCK_TILES_STEP)
            .add(NdBlocks.SMOOTH_DARKROCK_STEP)
            .add(NdBlocks.POLISHED_DARKROCK_STEP)

            .addTag(NdBlockTags.WOODEN_STEPS);

        getOrCreateTagBuilder(BlockTags.WALLS)
            .add(NdBlocks.ROCK_WALL)
            .add(NdBlocks.MOSSY_ROCK_WALL)
            .add(NdBlocks.ROCK_BRICKS_WALL)
            .add(NdBlocks.MOSSY_ROCK_BRICKS_WALL)
            .add(NdBlocks.CRACKED_ROCK_BRICKS_WALL)
            .add(NdBlocks.ROCK_TILES_WALL)
            .add(NdBlocks.MOSSY_ROCK_TILES_WALL)
            .add(NdBlocks.CRACKED_ROCK_TILES_WALL)
            .add(NdBlocks.SMOOTH_ROCK_WALL)
            .add(NdBlocks.POLISHED_ROCK_WALL)

            .add(NdBlocks.DARKROCK_WALL)
            .add(NdBlocks.MOSSY_DARKROCK_WALL)
            .add(NdBlocks.DARKROCK_BRICKS_WALL)
            .add(NdBlocks.MOSSY_DARKROCK_BRICKS_WALL)
            .add(NdBlocks.CRACKED_DARKROCK_BRICKS_WALL)
            .add(NdBlocks.DARKROCK_TILES_WALL)
            .add(NdBlocks.MOSSY_DARKROCK_TILES_WALL)
            .add(NdBlocks.CRACKED_DARKROCK_TILES_WALL)
            .add(NdBlocks.SMOOTH_DARKROCK_WALL)
            .add(NdBlocks.POLISHED_DARKROCK_WALL);

        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
            .add(NdBlocks.BLACKWOOD_FENCE)
            .add(NdBlocks.INVER_FENCE);

        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
            .add(NdBlocks.BLACKWOOD_SLAB)
            .add(NdBlocks.INVER_SLAB);

        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
            .add(NdBlocks.BLACKWOOD_STAIRS)
            .add(NdBlocks.INVER_STAIRS);

        getOrCreateTagBuilder(NdBlockTags.WOODEN_STEPS)
            .add(NdBlocks.BLACKWOOD_STEP)
            .add(NdBlocks.INVER_STEP);

//        getOrCreateTagBuilder(Tags.Blocks.DIRT)
//            .add(NdBlocks.MURKY_DIRT)
//            .add(NdBlocks.MURKY_GRASS_BLOCK)
//            .add(NdBlocks.MURKY_COARSE_DIRT)
//            .add(NdBlocks.MURKY_HUMUS)
//            .add(NdBlocks.LEAFY_HUMUS)
//            .add(NdBlocks.MURKY_PODZOL);

        getOrCreateTagBuilder(NdBlockTags.BLACKWOOD_LOGS)
            .add(NdBlocks.BLACKWOOD_LOG)
            .add(NdBlocks.STRIPPED_BLACKWOOD_LOG)
            .add(NdBlocks.BLACKWOOD)
            .add(NdBlocks.STRIPPED_BLACKWOOD);

        getOrCreateTagBuilder(NdBlockTags.INVER_LOGS)
            .add(NdBlocks.INVER_LOG)
            .add(NdBlocks.STRIPPED_INVER_LOG)
            .add(NdBlocks.INVER_WOOD)
            .add(NdBlocks.STRIPPED_INVER_WOOD);

        getOrCreateTagBuilder(BlockTags.LOGS)
            .addTag(NdBlockTags.BLACKWOOD_LOGS)
            .addTag(NdBlockTags.INVER_LOGS);

        getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE)
            .add(NdBlocks.MURKY_DIRT)
            .add(NdBlocks.MURKY_GRASS_BLOCK)
            .add(NdBlocks.MURKY_COARSE_DIRT)
            .add(NdBlocks.MURKY_HUMUS)
            .add(NdBlocks.LEAFY_HUMUS)
            .add(NdBlocks.MURKY_PODZOL)
            .add(NdBlocks.MURKY_SAND)
            .add(NdBlocks.MURKY_CLAY);
    }

    protected Tag.Builder getTagBuilder(Tag.Identified<Block> identified) {
        return super.method_27169(identified);
    }

    @Override
    protected Path getOutput(Identifier id) {
        return root.getOutput().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "NaturesDebris/BlockTags";
    }
}
