package modernity.common.item;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MDItemTags {
    // Items assigned to this tag could be used to make plants grow (like bonemeal does), and to fertilize farmland
    public static final Tag<Item> FERTILIZER = new ItemTags.Wrapper( new ResourceLocation( "modernity:fertilizer" ) );
}
