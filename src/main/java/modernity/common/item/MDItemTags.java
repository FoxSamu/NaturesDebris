/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 3 - 2019
 */

package modernity.common.item;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MDItemTags {
    public static final Tag<Item> FERTILIZER = new ItemTags.Wrapper( new ResourceLocation( "modernity:fertilizer" ) );

    public static final Tag<Item> LOGS = new ItemTags.Wrapper( new ResourceLocation( "modernity:logs" ) );
    public static final Tag<Item> STICKS = new ItemTags.Wrapper( new ResourceLocation( "modernity:sticks" ) );
    public static final Tag<Item> PLANKS = new ItemTags.Wrapper( new ResourceLocation( "modernity:planks" ) );
    public static final Tag<Item> WOODEN_STAIRS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_stairs" ) );
    public static final Tag<Item> WOODEN_STEPS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_steps" ) );
    public static final Tag<Item> WOODEN_CORNERS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_corners" ) );
    public static final Tag<Item> WOODEN_SLABS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_slabs" ) );
}
