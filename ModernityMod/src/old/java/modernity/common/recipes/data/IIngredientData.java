/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.recipes.data;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import modernity.util.Lazy;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface IIngredientData {
    static IIngredientData item(IItemProvider provider) {
        return new IIngredientData() {
            @Override
            public Ingredient makeIngredient() {
                return Ingredient.fromItems(provider);
            }

            @Override
            public void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, MinMaxBounds.IntBound count) {
                consumer.accept(
                    "has_" + Objects.requireNonNull(provider.asItem().getRegistryName()).getPath(),
                    ItemPredicateBuilder.create().item(provider).count(count).build()
                );
            }
        };
    }
    static IIngredientData item(Supplier<IItemProvider> provider) {
        return new IIngredientData() {
            @Override
            public Ingredient makeIngredient() {
                return Ingredient.fromItems(provider.get());
            }

            @Override
            public void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, MinMaxBounds.IntBound count) {
                consumer.accept(
                    "has_" + Objects.requireNonNull(provider.get().asItem().getRegistryName()).getPath(),
                    ItemPredicateBuilder.create().item(provider.get()).count(count).build()
                );
            }
        };
    }
    static IIngredientData items(IItemProvider... providers) {
        // Remove all double items
        IItemProvider[] cleanedProviders = Stream.of(providers)
                                                 .map(IItemProvider::asItem)
                                                 .distinct()
                                                 .toArray(IItemProvider[]::new);

        return new IIngredientData() {
            @Override
            public Ingredient makeIngredient() {
                return Ingredient.fromItems(cleanedProviders);
            }

            @Override
            public void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, MinMaxBounds.IntBound count) {
                for(IItemProvider provider : cleanedProviders) {
                    consumer.accept(
                        "has_" + Objects.requireNonNull(provider.asItem().getRegistryName()).getPath(),
                        ItemPredicateBuilder.create().item(provider).count(count).build()
                    );
                }
            }
        };
    }
    @SafeVarargs
    static IIngredientData items(Supplier<IItemProvider>... providers) {
        // Remove all double items
        Lazy<IItemProvider[]> cleanedProviders = Lazy.of(() -> Stream.of(providers)
                                                                     .map(Supplier::get)
                                                                     .map(IItemProvider::asItem)
                                                                     .distinct()
                                                                     .toArray(IItemProvider[]::new));

        return new IIngredientData() {
            @Override
            public Ingredient makeIngredient() {
                return Ingredient.fromItems(cleanedProviders.get());
            }

            @Override
            public void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, MinMaxBounds.IntBound count) {
                for(IItemProvider provider : cleanedProviders.get()) {
                    consumer.accept(
                        "has_" + Objects.requireNonNull(provider.asItem().getRegistryName()).getPath(),
                        ItemPredicateBuilder.create().item(provider).count(count).build()
                    );
                }
            }
        };
    }
    static IIngredientData items(List<Supplier<IItemProvider>> providers) {
        // Remove all double items
        Lazy<IItemProvider[]> cleanedProviders = Lazy.of(() -> providers.stream()
                                                                        .map(Supplier::get)
                                                                        .map(IItemProvider::asItem)
                                                                        .distinct()
                                                                        .toArray(IItemProvider[]::new));

        return new IIngredientData() {
            @Override
            public Ingredient makeIngredient() {
                return Ingredient.fromItems(cleanedProviders.get());
            }

            @Override
            public void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, MinMaxBounds.IntBound count) {
                for(IItemProvider provider : cleanedProviders.get()) {
                    consumer.accept(
                        "has_" + Objects.requireNonNull(provider.asItem().getRegistryName()).getPath(),
                        ItemPredicateBuilder.create().item(provider).count(count).build()
                    );
                }
            }
        };
    }
    static IIngredientData tag(Tag<Item> tag) {
        return new IIngredientData() {
            @Override
            public Ingredient makeIngredient() {
                return Ingredient.fromTag(tag);
            }

            @Override
            public void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, MinMaxBounds.IntBound count) {
                consumer.accept(
                    "has_" + tag.getId().getPath(),
                    ItemPredicateBuilder.create().tag(tag).count(count).build()
                );
            }
        };
    }
    Ingredient makeIngredient();
    void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, MinMaxBounds.IntBound count);
    default void addItemCriteria(BiConsumer<String, ItemPredicate> consumer) {
        addItemCriteria(consumer, MinMaxBounds.IntBound.UNBOUNDED);
    }
    default void addItemCriteria(BiConsumer<String, ItemPredicate> consumer, int count) {
        addItemCriteria(consumer, MinMaxBounds.IntBound.atLeast(count));
    }
}
