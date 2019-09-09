/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.item.base;

import modernity.common.item.MDItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;

public class ItemMDAxe extends ItemAxe implements MDItems.Entry {
    public ItemMDAxe( String id, IItemTier tier, float attackDamage, float attackSpeed, Properties builder ) {
        super( tier, attackDamage, attackSpeed, builder );
        setRegistryName( "modernity:" + id );
    }

    @Override
    public Item getItem() {
        return this;
    }
}
