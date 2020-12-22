package net.shadew.ndebris.data.tags;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;
import java.util.function.Function;

import net.shadew.ndebris.common.item.NdItems;
import net.shadew.ndebris.common.tags.NdBlockTags;
import net.shadew.ndebris.common.tags.NdItemTags;

public class NdItemTagsProvider extends AbstractTagProvider<Item> {
    private final Function<Tag.Identified<Block>, Tag.Builder> builderGetter;

    public NdItemTagsProvider(DataGenerator gen, NdBlockTagsProvider blockTags) {
        super(gen, Registry.ITEM);
        this.builderGetter = blockTags::getTagBuilder;
    }

    @Override
    protected void configure() {
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);
        copy(NdBlockTags.STEPS, NdItemTags.STEPS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(NdBlockTags.WOODEN_STEPS, NdItemTags.WOODEN_STEPS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(NdBlockTags.BLACKWOOD_LOGS, NdItemTags.BLACKWOOD_LOGS);
        copy(NdBlockTags.INVER_LOGS, NdItemTags.INVER_LOGS);
        copy(BlockTags.LOGS, ItemTags.LOGS);

        getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
            .add(NdItems.MUSIC_DISC_DARK, NdItems.MUSIC_DISC_M1);
    }


    protected void copy(Tag.Identified<Block> blockTag, Tag.Identified<Item> itemTag) {
        Tag.Builder itemBuilder = method_27169(itemTag);
        Tag.Builder blockBuilder = builderGetter.apply(blockTag);
        blockBuilder.streamEntries().forEach(itemBuilder::add);
    }

    @Override
    protected Path getOutput(Identifier id) {
        return root.getOutput().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "NaturesDebris/ItemTags";
    }
}
