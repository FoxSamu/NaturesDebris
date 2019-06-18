/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 18 - 2019
 */

package modernity.common.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.api.item.IColoredItem;
import modernity.common.item.base.ItemBase;

import java.util.ArrayList;

public class MDItems {
    private static final ArrayList<Entry> ENTRIES = Lists.newArrayList();

    // Minerals
    public static final ItemBase SALT_DUST = item( new ItemBase( "salt_dust", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase SALT_NUGGET = item( new ItemBase( "salt_nugget", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase ALUMINIUM_INGOT = item( new ItemBase( "aluminium_ingot", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase ALUMINIUM_NUGGET = item( new ItemBase( "aluminium_nugget", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );

    public static void register( IForgeRegistry<Item> registry ) {
        for( Entry e : ENTRIES ) {
            registry.register( e.getItem() );
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static void registerClient( ItemColors itemColors ) {
        for( Entry e : ENTRIES ) {
            if( e instanceof IColoredItem ) {
                itemColors.register( ( (IColoredItem) e )::colorMultiplier );
            }
        }
    }

    private static <T extends Entry> T item( T item ) {
        ENTRIES.add( item );
        return item;
    }

    public interface Entry {
        Item getItem();
    }
}
