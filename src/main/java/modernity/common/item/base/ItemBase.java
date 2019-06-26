/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
 */

package modernity.common.item.base;

import net.minecraft.item.Item;

import modernity.common.item.MDItems;

public class ItemBase extends Item implements MDItems.Entry {
    public ItemBase( String id, Properties properties ) {
        super( properties );
        String rl;
        if( id.contains( ":" ) ) {
            rl = id;
        } else {
            rl = "modernity:" + id;
        }
        setRegistryName( rl );
    }

    @Override
    public Item getItem() {
        return this;
    }
}
