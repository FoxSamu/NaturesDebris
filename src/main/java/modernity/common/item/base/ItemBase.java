package modernity.common.item.base;

import net.minecraft.item.Item;

import modernity.common.item.MDItems;

public class ItemBase extends Item implements MDItems.Entry {
    public ItemBase( String id, Properties properties ) {
        super( properties );
        setRegistryName( "modernity:" + id );
    }

    @Override
    public Item getItem() {
        return this;
    }
}
