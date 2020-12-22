package net.shadew.ndebris.common.tags;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;

import net.shadew.ndebris.common.NaturesDebris;

public final class NdBlockTags {
    public static final Tag.Identified<Block> STEPS = tag("steps");
    public static final Tag.Identified<Block> WOODEN_STEPS = tag("wooden_steps");
    public static final Tag.Identified<Block> BLACKWOOD_LOGS = tag("blackwood_logs");
    public static final Tag.Identified<Block> INVER_LOGS = tag("inver_logs");

    private static Tag.Identified<Block> tag(String id) {
        return TagRegistry.create(NaturesDebris.id(id), BlockTags::getTagGroup);
    }
}
