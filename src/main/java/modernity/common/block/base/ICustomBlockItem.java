package modernity.common.block.base;

import net.minecraft.item.Item;

@FunctionalInterface
public interface ICustomBlockItem {
    Item createBlockItem( Item.Properties properties );
}
