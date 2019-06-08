package modernity.common.block;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MDBlockTags {
    public static final Tag<Block> BLACKWOOD_LOG = tag( "log/blackwood_log" );
    public static final Tag<Block> LOG = tag( "log" );

    public static final Tag<Block> BLACKWOOD_LEAVES = tag( "leaves/blackwood_leaves" );
    public static final Tag<Block> LEAVES = tag( "leaves" );

    public static final Tag<Block> DARKROCK = tag( "rock/darkrock" );
    public static final Tag<Block> ROCK = tag( "rock" );

    private static Tag<Block> tag( String id ) {
        return new BlockTags.Wrapper( new ResourceLocation( "modernity", id ) );
    }
}
