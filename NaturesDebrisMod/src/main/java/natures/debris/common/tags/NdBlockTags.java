package natures.debris.common.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;

import natures.debris.common.NaturesDebris;

public final class NdBlockTags {
    public static final Tag<Block> STEPS = tag("steps");

    private static Tag<Block> tag(String id) {
        return new BlockTags.Wrapper(NaturesDebris.resLoc(id));
    }
}
