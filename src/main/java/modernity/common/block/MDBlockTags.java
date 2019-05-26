package modernity.common.block;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MDBlockTags {
    public static final Tag<Block> DARKWOOD_LOG = new BlockTags.Wrapper( new ResourceLocation( "modernity:darkwood_log" ) );

    public static final Tag<Block> LEAVES = new BlockTags.Wrapper( new ResourceLocation( "modernity:leaves" ) );
    public static final Tag<Block> DARKWOOD_LEAVES = new BlockTags.Wrapper( new ResourceLocation( "modernity:leaves/darkwood_leaves" ) );


}
