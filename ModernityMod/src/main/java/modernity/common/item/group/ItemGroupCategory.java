/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.item.group;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;

import java.util.List;
import java.util.function.Consumer;

public class ItemGroupCategory {
    // BLOCKS item group
    public static final ItemGroupCategory NATURE = new ItemGroupCategory();
    public static final ItemGroupCategory BUILDING = new ItemGroupCategory();
    public static final ItemGroupCategory BUILDING_SLABS = new ItemGroupCategory();
    public static final ItemGroupCategory BUILDING_STAIRS = new ItemGroupCategory();
    public static final ItemGroupCategory BUILDING_STEPS = new ItemGroupCategory();
    public static final ItemGroupCategory BUILDING_CORNERS = new ItemGroupCategory();
    public static final ItemGroupCategory MINERALS = new ItemGroupCategory();

    private final List<Item> items = Lists.newArrayList();

    public void add(Item item) {
        items.add(item);
    }

    public void export(Consumer<Item> export) {
        for(Item item : items) {
            export.accept(item);
        }
    }
}
