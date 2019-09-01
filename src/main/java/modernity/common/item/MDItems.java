/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.api.item.IColoredItem;
import modernity.common.fluid.MDFluids;
import modernity.common.item.base.*;

import java.util.ArrayList;

public class MDItems {
    private static final ArrayList<Entry> ENTRIES = Lists.newArrayList();

    // Minerals
    public static final ItemBase SALT_DUST = item( new ItemBase( "salt_dust", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase SALT_NUGGET = item( new ItemBase( "salt_nugget", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase ALUMINIUM_INGOT = item( new ItemBase( "aluminium_ingot", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase ALUMINIUM_NUGGET = item( new ItemBase( "aluminium_nugget", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase ANTHRACITE = item( new ItemBase( "anthracite", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );

    // Utils
    public static final ItemBase ASH = item( new ItemBase( "ash", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase BLACKWOOD_STICK = item( new ItemBase( "blackwood_stick", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase INVER_STICK = item( new ItemBase( "inver_stick", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );

    // Buckets
    public static final ItemPortalBucket PORTAL_BUCKET = item( new ItemPortalBucket( "portal_bucket", new Item.Properties().maxStackSize( 1 ).group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBucketBase ALUMINIUM_BUCKET = item( new ItemAluminiumBucket( "aluminium_bucket", Fluids.EMPTY, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBucketBase ALUMINIUM_WATER_BUCKET = item( new ItemAluminiumBucket( "aluminium_water_bucket", MDFluids.MODERNIZED_WATER, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBucketBase ALUMINIUM_HEATROCK_BUCKET = item( new ItemAluminiumBucket( "aluminium_heatrock_bucket", MDFluids.HEATROCK_FLUID, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBucketBase ALUMINIUM_OIL_BUCKET = item( new ItemAluminiumBucket( "aluminium_oil_bucket", MDFluids.OIL, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBucketBase ALUMINIUM_PORTAL_BUCKET = item( new ItemPortalBucket( "aluminium_portal_bucket", ALUMINIUM_BUCKET, f -> null, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );

    // Tools
    public static final ItemMDPickaxe BLACKWOOD_PICKAXE = item( new ItemMDPickaxe( "blackwood_pickaxe", ItemTier.WOOD, 1, - 2.8F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemMDAxe BLACKWOOD_AXE = item( new ItemMDAxe( "blackwood_axe", ItemTier.WOOD, 6, - 3.2F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemMDShovel BLACKWOOD_SHOVEL = item( new ItemMDShovel( "blackwood_shovel", ItemTier.WOOD, 1.5F, - 3.0F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemMDSword BLACKWOOD_SWORD = item( new ItemMDSword( "blackwood_sword", ItemTier.WOOD, 3, - 2.4F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );

    public static final ItemMDPickaxe ALUMINIUM_PICKAXE = item( new ItemMDPickaxe( "aluminium_pickaxe", ItemTier.IRON, 1, - 2.8F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemMDAxe ALUMINIUM_AXE = item( new ItemMDAxe( "aluminium_axe", ItemTier.IRON, 6, - 3.2F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemMDShovel ALUMINIUM_SHOVEL = item( new ItemMDShovel( "aluminium_shovel", ItemTier.IRON, 1.5F, - 3.0F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemMDSword ALUMINIUM_SWORD = item( new ItemMDSword( "aluminium_sword", ItemTier.IRON, 3, - 2.4F, new Item.Properties().group( MDItemGroups.BLOCKS ) ) );

    // Curse crystal
    public static final ItemBase CURSE_CRYSTAL_SHARD_1 = item( new ItemBase( "curse_crystal/shard_1", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase CURSE_CRYSTAL_SHARD_2 = item( new ItemBase( "curse_crystal/shard_2", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase CURSE_CRYSTAL_SHARD_3 = item( new ItemBase( "curse_crystal/shard_3", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemBase CURSE_CRYSTAL_SHARD_4 = item( new ItemBase( "curse_crystal/shard_4", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );
    public static final ItemCurseCrystal CURSE_CRYSTAL = item( new ItemCurseCrystal( "curse_crystal", new Item.Properties().group( MDItemGroups.BLOCKS ) ) );

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
