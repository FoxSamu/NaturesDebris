package natures.debris.common.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;

import natures.debris.common.NaturesDebris;

public final class NdItemTags {
    public static final Tag<Item> STEPS = tag("steps");
    public static final Tag<Item> BLACKWOOD_LOGS = tag("blackwood_logs");
    public static final Tag<Item> INVER_LOGS = tag("inver_logs");

    private static Tag<Item> tag(String id) {
        return new ItemTags.Wrapper(NaturesDebris.resLoc(id));
    }
}
