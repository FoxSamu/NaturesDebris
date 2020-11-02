package natures.debris.common.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

import natures.debris.common.NaturesDebris;

public final class NdBlockTags {
    public static final ITag.INamedTag<Block> STEPS = tag("steps");
    public static final ITag.INamedTag<Block> WOODEN_STEPS = tag("wooden_steps");
    public static final ITag.INamedTag<Block> BLACKWOOD_LOGS = tag("blackwood_logs");
    public static final ITag.INamedTag<Block> INVER_LOGS = tag("inver_logs");

    private static ITag.INamedTag<Block> tag(String id) {
        return BlockTags.makeWrapperTag(NaturesDebris.resStr(id));
    }
}
