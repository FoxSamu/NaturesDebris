/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.item;

import modernity.common.Modernity;
import modernity.common.item.group.MDItemGroup;
import modernity.common.registry.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("modernity")
public final class MDItems {

    public static final Item SALT_DUST = Modernity.injected();

    public static void register(RegistryHandler<Item> reg) {
        reg.configured(new ItemBuilder().group(MDItemGroup.MISC))
           .add("salt_dust", ItemBuilder::makeItem);
    }

    private MDItems() {
    }
}
