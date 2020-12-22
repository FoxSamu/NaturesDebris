package net.shadew.ndebris.common.tags;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;

import net.shadew.ndebris.common.NaturesDebris;

public final class NdItemTags {
    public static final Tag.Identified<Item> STEPS = tag("steps");
    public static final Tag.Identified<Item> WOODEN_STEPS = tag("wooden_steps");
    public static final Tag.Identified<Item> BLACKWOOD_LOGS = tag("blackwood_logs");
    public static final Tag.Identified<Item> INVER_LOGS = tag("inver_logs");

    private static Tag.Identified<Item> tag(String id) {
        return TagRegistry.create(NaturesDebris.id(id), ItemTags::getTagGroup);
    }
}
