package natures.debris.common.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;

import natures.debris.common.NaturesDebris;

public final class NdBlockTags {
    public static final Tag<Block> STEPS = tag("steps");
    public static final Tag<Block> WOODEN_STEPS = tag("wooden_steps");
    public static final Tag<Block> BLACKWOOD_LOGS = tag("blackwood_logs");
    public static final Tag<Block> INVER_LOGS = tag("inver_logs");

    private static Tag<Block> tag(String id) {
        return new BlockTags.Wrapper(NaturesDebris.resLoc(id));
    }
}
