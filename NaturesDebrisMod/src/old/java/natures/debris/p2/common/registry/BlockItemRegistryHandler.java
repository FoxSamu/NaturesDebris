/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.p2.common.registry;

import modernity.api.util.ITriFunction;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockItemRegistryHandler {

    public BlockItemRegistryHandler add(String id, Block entry, String... aliases) {
        RegistryHandler.BLOCKS.add(id, entry, aliases);
        return this;
    }

    public BlockItemRegistryHandler add(Block entry, String... aliases) {
        RegistryHandler.BLOCKS.add(entry, aliases);
        return this;
    }

    public BlockItemRegistryHandler add(String id, Block entry, Item iEntry, String... aliases) {
        RegistryHandler.BLOCKS.add(id, entry, aliases);
        RegistryHandler.ITEMS.add(id, iEntry, aliases);
        return this;
    }

    public BlockItemRegistryHandler add(Block entry, Item iEntry, String... aliases) {
        RegistryHandler.BLOCKS.add(entry, aliases);
        RegistryHandler.ITEMS.add(iEntry, aliases);
        return this;
    }

    public <C> Configured<C> configured(Supplier<C> config) {
        return new Configured<>(config);
    }

    public <C> Configured<C> configured(C config) {
        return configured(() -> config);
    }

    public static class Configured<C> {
        private final Supplier<C> config;

        private Configured(Supplier<C> config) {
            this.config = config;
        }

        public Configured<C> add(String id, Function<C, Block> factory, String... aliases) {
            RegistryHandler.BLOCKS.add(id, factory.apply(config.get()), aliases);
            return this;
        }

        public Configured<C> add(String id, Function<C, Block> factory, Function<C, Item> itemFactory, String... aliases) {
            return add(id, factory, (c, block) -> itemFactory.apply(c), aliases);
        }

        public Configured<C> add(String id, Function<C, Block> factory, BiFunction<C, Block, Item> itemFactory, String... aliases) {
            Block block = factory.apply(config.get());
            RegistryHandler.BLOCKS.add(id, block, aliases);
            RegistryHandler.ITEMS.add(id, itemFactory.apply(config.get(), block), aliases);
            return this;
        }

        public Configured<C> addAll(BiFunction<ResourceLocation, C, Block> factory, String... names) {
            for (String name : names) {
                RegistryHandler.BLOCKS.add(name, factory.apply(RegistryHandler.BLOCKS.createID(name), config.get()));
            }
            return this;
        }

        public Configured<C> addAll(BiFunction<ResourceLocation, C, Block> factory, BiFunction<ResourceLocation, C, Item> iFactory, String... names) {
            return addAll(factory, (id, c, block) -> iFactory.apply(id, c), names);
        }

        public Configured<C> addAll(BiFunction<ResourceLocation, C, Block> factory, ITriFunction<ResourceLocation, C, Block, Item> iFactory, String... names) {
            for (String name : names) {
                Block block = factory.apply(RegistryHandler.BLOCKS.createID(name), config.get());
                RegistryHandler.BLOCKS.add(name, block);
                RegistryHandler.ITEMS.add(name, iFactory.apply(RegistryHandler.ITEMS.createID(name), config.get(), block));
            }
            return this;
        }

        public Configured<C> addAll(Function<C, Block> factory, String... names) {
            return addAll((id, c) -> factory.apply(c), names);
        }

        public Configured<C> addAll(Function<C, Block> factory, Function<C, Item> iFactory, String... names) {
            return addAll((id, c) -> factory.apply(c), (id, c) -> iFactory.apply(c), names);
        }

        public Configured<C> addAll(Function<C, Block> factory, BiFunction<C, Block, Item> iFactory, String... names) {
            return addAll((id, c) -> factory.apply(c), (id, c, block) -> iFactory.apply(c, block), names);
        }
    }
}
