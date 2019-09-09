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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class ItemMDArmor extends ItemArmor implements MDItems.Entry {
    public ItemMDArmor( String id, IArmorMaterial material, EntityEquipmentSlot slots, Properties builder ) {
        super( material, slots, builder );
        setRegistryName( "modernity:" + id );
    }

    @Override
    public Item getItem() {
        return this;
    }
}
