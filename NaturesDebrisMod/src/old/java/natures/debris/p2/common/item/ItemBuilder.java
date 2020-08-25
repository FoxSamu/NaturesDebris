/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.p2.common.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import natures.debris.p2.common.item.group.ItemGroupCategory;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemBuilder {
    private final List<Consumer<Item>> postProcess = Lists.newArrayList();
    protected Function<Item.Properties, Item> itemFactory = Item::new;
    private int stackability = 64;
    private int durability;
    private Supplier<Item> container;
    private ItemGroup group;
    private Rarity rarity = Rarity.COMMON;
    private Food food;
    private boolean repairable = true;
    private Map<ToolType, Integer> toolClasses = Maps.newHashMap();
    private Supplier<Callable<ItemStackTileEntityRenderer>> renderer;
    private ItemGroupCategory category;

    public ItemBuilder itemFactory(Function<Item.Properties, Item> factory) {
        this.itemFactory = factory;
        return this;
    }

    public ItemBuilder itemProcessor(Consumer<Item> processor) {
        postProcess.add(processor);
        return this;
    }

    public ItemBuilder category(ItemGroupCategory category) {
        this.category = category;
        return this;
    }

    public ItemBuilder food(Food food) {
        this.food = food;
        return this;
    }

    public ItemBuilder stackability(int stackability) {
        this.stackability = stackability;
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder container(Supplier<Item> container) {
        this.container = container;
        return this;
    }

    public ItemBuilder group(ItemGroup group) {
        this.group = group;
        return this;
    }

    public ItemBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public ItemBuilder noRepair() {
        this.repairable = false;
        return this;
    }

    public ItemBuilder toolType(ToolType type, int level) {
        toolClasses.put(type, level);
        return this;
    }

    public ItemBuilder renderer(Supplier<Callable<ItemStackTileEntityRenderer>> renderer) {
        this.renderer = renderer;
        return this;
    }

    protected Item.Properties makeItemProps() {
        Item.Properties props = new Item.Properties();
        props.food(food);
        props.maxStackSize(stackability);
        props.maxDamage(durability);
        if (container != null) props.containerItem(container.get());
        props.group(group);
        props.rarity(rarity);
        if (!repairable) props.setNoRepair();
        props.setISTER(renderer);
        for (ToolType type : toolClasses.keySet()) {
            props.addToolType(type, toolClasses.get(type));
        }

        return props;
    }

    protected Item processItem(Item item) {
        for (Consumer<Item> processor : postProcess) {
            processor.accept(item);
        }
        return item;
    }

    public Item makeItemFactory(Function<Item.Properties, Item> factory) {
        return processItem(factory.apply(makeItemProps()));
    }

    public Item makeItem() {
        if (itemFactory == null) {
            throw new NullPointerException("No factory specified");
        }
        return makeItemFactory(itemFactory);
    }

    protected boolean hasFactory() {
        return itemFactory != null;
    }
}
