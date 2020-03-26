/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.item;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Holder class for Modernity item tags.
 */
public final class MDItemTags {
    public static final Tag<Item> LITTLE_FERTILIZER = new ItemTags.Wrapper( new ResourceLocation( "modernity:little_fertilizer" ) );
    public static final Tag<Item> FERTILIZER = new ItemTags.Wrapper( new ResourceLocation( "modernity:fertilizer" ) );
    public static final Tag<Item> LITTLE_SALTY = new ItemTags.Wrapper( new ResourceLocation( "modernity:little_salty" ) );
    public static final Tag<Item> SALTY = new ItemTags.Wrapper( new ResourceLocation( "modernity:salty" ) );

    public static final Tag<Item> BLACKWOOD_LOGS = new ItemTags.Wrapper( new ResourceLocation( "modernity:logs/blackwood_logs" ) );
    public static final Tag<Item> INVER_LOGS = new ItemTags.Wrapper( new ResourceLocation( "modernity:logs/inver_logs" ) );
    public static final Tag<Item> LOGS = new ItemTags.Wrapper( new ResourceLocation( "modernity:logs" ) );
    public static final Tag<Item> STICKS = new ItemTags.Wrapper( new ResourceLocation( "modernity:sticks" ) );
    public static final Tag<Item> PLANKS = new ItemTags.Wrapper( new ResourceLocation( "modernity:planks" ) );
    public static final Tag<Item> ROCK = new ItemTags.Wrapper( new ResourceLocation( "modernity:rock" ) );
    public static final Tag<Item> WOODEN_STAIRS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_stairs" ) );
    public static final Tag<Item> WOODEN_STEPS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_steps" ) );
    public static final Tag<Item> WOODEN_CORNERS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_corners" ) );
    public static final Tag<Item> WOODEN_SLABS = new ItemTags.Wrapper( new ResourceLocation( "modernity:wooden_slabs" ) );
    public static final Tag<Item> ASHABLE = new ItemTags.Wrapper( new ResourceLocation( "modernity:ashable" ) );

    private MDItemTags() {
    }
}
