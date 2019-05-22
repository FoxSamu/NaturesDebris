package modernity.common.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import modernity.common.block.MDBlocks;

public class MDItemGroups {
    public static final ItemGroup BLOCKS = new ItemGroup( "modernity" ) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDBlocks.ROCK.getBlockItem() );
        }
    };
}
