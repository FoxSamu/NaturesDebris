package modernity.common.block.base;

import net.minecraft.item.Item;

/**
 * Implementing blocks have a custom item
 */
@FunctionalInterface
public interface ICustomBlockItem {
    Item createBlockItem( Item.Properties properties );
}
