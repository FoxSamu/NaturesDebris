/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Holder class for Modernity block tags
 */
public final class MDBlockTags {
    public static final Tag<Block> BLACKWOOD_LOG = tag( "log/blackwood_log" );
    public static final Tag<Block> INVER_LOG = tag( "log/inver_log" );
    public static final Tag<Block> LOG = tag( "log" );

    public static final Tag<Block> BLACKWOOD_LEAVES = tag( "leaves/blackwood_leaves" );
    public static final Tag<Block> INVER_LEAVES = tag( "leaves/inver_leaves" );
    public static final Tag<Block> LEAVES = tag( "leaves" );

    public static final Tag<Block> ROCK = tag( "rock" );

    public static final Tag<Block> PORTAL = tag( "portal" );

    public static final Tag<Block> WALLS = tag( "walls" );
    public static final Tag<Block> FENCES = tag( "fences" );


    public static final Tag<Block> SALT_SOURCE = tag( "salt_source" );
    public static final Tag<Block> REEDS_GROWABLE = tag( "reeds_growable" );
    public static final Tag<Block> DIRTLIKE = tag( "dirtlike" );
    public static final Tag<Block> PODZOL_SOURCE = tag( "podzol_source" );

    private MDBlockTags() {
    }

    private static Tag<Block> tag( String id ) {
        return new BlockTags.Wrapper( new ResourceLocation( "modernity", id ) );
    }
}
