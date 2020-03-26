/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.recipes.data;

import net.minecraft.util.IItemProvider;

import java.util.function.Supplier;

public interface IResultData {
    IItemProvider item();
    int count( int def );

    static IResultData item( IItemProvider item ) {
        return new IResultData() {
            @Override
            public IItemProvider item() {
                return item;
            }

            @Override
            public int count( int def ) {
                return def;
            }
        };
    }

    static IResultData count( IItemProvider item, int count ) {
        return new IResultData() {
            @Override
            public IItemProvider item() {
                return item;
            }

            @Override
            public int count( int def ) {
                return count;
            }
        };
    }
    static IResultData item( Supplier<IItemProvider> item ) {
        return new IResultData() {
            @Override
            public IItemProvider item() {
                return item.get();
            }

            @Override
            public int count( int def ) {
                return def;
            }
        };
    }

    static IResultData count( Supplier<IItemProvider> item, int count ) {
        return new IResultData() {
            @Override
            public IItemProvider item() {
                return item.get();
            }

            @Override
            public int count( int def ) {
                return count;
            }
        };
    }
}
