/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.p2.common.item;

import natures.debris.p2.common.Modernity;
import natures.debris.p2.common.item.group.MDItemGroup;
import natures.debris.p2.common.registry.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("natures/debris")
public final class MDItems {

    public static final Item SALT_DUST = Modernity.injected();

    public static void register(RegistryHandler<Item> reg) {
        reg.configured(new ItemBuilder().group(MDItemGroup.MISC))
           .add("salt_dust", ItemBuilder::makeItem);
    }

    private MDItems() {
    }
}
