package natures.debris.common.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

import natures.debris.common.NaturesDebris;

public final class NdItemTags {
    public static final ITag.INamedTag<Item> STEPS = tag("steps");
    public static final ITag.INamedTag<Item> WOODEN_STEPS = tag("wooden_steps");
    public static final ITag.INamedTag<Item> BLACKWOOD_LOGS = tag("blackwood_logs");
    public static final ITag.INamedTag<Item> INVER_LOGS = tag("inver_logs");

    private static ITag.INamedTag<Item> tag(String id) {
        return ItemTags.makeWrapperTag(NaturesDebris.resStr(id));
    }
}
