/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.recipes.data;

import com.google.common.collect.Lists;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPredicateBuilder {
    private final List<EnchantmentPredicate> enchantments = Lists.newArrayList();
    private final List<EnchantmentPredicate> field_226657_b_ = Lists.newArrayList();
    @Nullable
    private Item item;
    @Nullable
    private Tag<Item> tag;
    private MinMaxBounds.IntBound count = MinMaxBounds.IntBound.UNBOUNDED;
    private MinMaxBounds.IntBound durability = MinMaxBounds.IntBound.UNBOUNDED;
    @Nullable
    private Potion potion;
    private NBTPredicate nbt = NBTPredicate.ANY;

    private ItemPredicateBuilder() {
    }

    public static ItemPredicateBuilder create() {
        return new ItemPredicateBuilder();
    }

    public ItemPredicateBuilder item(IItemProvider provider) {
        this.item = provider.asItem();
        return this;
    }

    public ItemPredicateBuilder tag(Tag<Item> tag) {
        this.tag = tag;
        return this;
    }

    public ItemPredicateBuilder nbt(CompoundNBT p_218002_1_) {
        this.nbt = new NBTPredicate(p_218002_1_);
        return this;
    }

    public ItemPredicateBuilder enchantment(EnchantmentPredicate p_218003_1_) {
        this.enchantments.add(p_218003_1_);
        return this;
    }

    public ItemPredicateBuilder count(MinMaxBounds.IntBound bound) {
        this.count = bound;
        return this;
    }

    public ItemPredicate build() {
        return new ItemPredicate(this.tag, this.item, this.count, this.durability, this.enchantments.toArray(EnchantmentPredicate.field_226534_b_), this.field_226657_b_.toArray(EnchantmentPredicate.field_226534_b_), this.potion, this.nbt);
    }
}
