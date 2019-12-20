/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.item;

import modernity.api.item.IColoredItem;
import modernity.common.fluid.MDFluids;
import modernity.common.item.base.AluminiumBucketItem;
import modernity.common.item.base.BaseBucketItem;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity items.
 */
@ObjectHolder( "modernity" )
public final class MDItems {
    private static final RegistryHandler<Item> ENTRIES = new RegistryHandler<>( "modernity" );

    // V I0.2.0
    public static final Item SALT_DUST = item( "salt_dust", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );
    public static final Item SALT_NUGGET = item( "salt_nugget", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );
    public static final Item ALUMINIUM_INGOT = item( "aluminium_ingot", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );
    public static final Item ALUMINIUM_NUGGET = item( "aluminium_nugget", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );
    public static final Item ANTHRACITE = item( "anthracite", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );

    public static final Item ASH = item( "ash", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );
    public static final Item BLACKWOOD_STICK = item( "blackwood_stick", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );
    public static final Item INVER_STICK = item( "inver_stick", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );

    public static final BaseBucketItem ALUMINIUM_BUCKET = item( "aluminium_bucket", new AluminiumBucketItem( Fluids.EMPTY, new Item.Properties().group( MDItemGroups.MISC ) ) );
    public static final BaseBucketItem ALUMINIUM_WATER_BUCKET = item( "aluminium_water_bucket", new AluminiumBucketItem( MDFluids.MURKY_WATER, new Item.Properties().group( MDItemGroups.MISC ).containerItem( ALUMINIUM_BUCKET ) ) );
    public static final BaseBucketItem ALUMINIUM_LAVA_BUCKET = item( "aluminium_lava_bucket", new AluminiumBucketItem( MDFluids.MOLTEN_ROCK, new Item.Properties().group( MDItemGroups.MISC ).containerItem( ALUMINIUM_BUCKET ) ) );
    public static final BaseBucketItem ALUMINIUM_OIL_BUCKET = item( "aluminium_oil_bucket", new AluminiumBucketItem( MDFluids.OIL, new Item.Properties().group( MDItemGroups.MISC ).containerItem( ALUMINIUM_BUCKET ) ) );

    public static final PickaxeItem BLACKWOOD_PICKAXE = item( "blackwood_pickaxe", new PickaxeItem( ItemTier.WOOD, 1, - 2.8F, new Item.Properties().group( MDItemGroups.TOOLS ) ) );
    public static final AxeItem BLACKWOOD_AXE = item( "blackwood_axe", new AxeItem( ItemTier.WOOD, 6, - 3.2F, new Item.Properties().group( MDItemGroups.TOOLS ) ) );
    public static final ShovelItem BLACKWOOD_SHOVEL = item( "blackwood_shovel", new ShovelItem( ItemTier.WOOD, 1.5F, - 3.0F, new Item.Properties().group( MDItemGroups.TOOLS ) ) );
    public static final SwordItem BLACKWOOD_SWORD = item( "blackwood_sword", new SwordItem( ItemTier.WOOD, 3, - 2.4F, new Item.Properties().group( MDItemGroups.COMBAT ) ) );

    public static final PickaxeItem ALUMINIUM_PICKAXE = item( "aluminium_pickaxe", new PickaxeItem( ItemTier.IRON, 1, - 2.8F, new Item.Properties().group( MDItemGroups.TOOLS ) ) );
    public static final AxeItem ALUMINIUM_AXE = item( "aluminium_axe", new AxeItem( ItemTier.IRON, 6, - 3.2F, new Item.Properties().group( MDItemGroups.TOOLS ) ) );
    public static final ShovelItem ALUMINIUM_SHOVEL = item( "aluminium_shovel", new ShovelItem( ItemTier.IRON, 1.5F, - 3.0F, new Item.Properties().group( MDItemGroups.TOOLS ) ) );
    public static final SwordItem ALUMINIUM_SWORD = item( "aluminium_sword", new SwordItem( ItemTier.IRON, 3, - 2.4F, new Item.Properties().group( MDItemGroups.COMBAT ) ) );

    public static final ArmorItem ALUMINIUM_HELMET = item( "aluminium_helmet", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.HEAD, new Item.Properties().group( MDItemGroups.COMBAT ) ) );
    public static final ArmorItem ALUMINIUM_CHESTPLATE = item( "aluminium_chestplate", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.CHEST, new Item.Properties().group( MDItemGroups.COMBAT ) ) );
    public static final ArmorItem ALUMINIUM_LEGGINGS = item( "aluminium_leggings", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.LEGS, new Item.Properties().group( MDItemGroups.COMBAT ) ) );
    public static final ArmorItem ALUMINIUM_BOOTS = item( "aluminium_boots", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.FEET, new Item.Properties().group( MDItemGroups.COMBAT ) ) );

    // V I0.3.0
    public static final Item EYE_OF_THE_CURSE = item( "eye_of_the_curse", new Item( new Item.Properties().group( MDItemGroups.MISC ) ) );

    private static <T extends Item> T item( String id, T item, String... aliases ) {
        ENTRIES.register( id, item, aliases );
        return item;
    }
    @OnlyIn( Dist.CLIENT )
    public static void initItemColors() {
        for( Item block : ENTRIES ) {
            if( block instanceof IColoredItem ) {
                Minecraft.getInstance().getItemColors().register( ( (IColoredItem) block )::colorMultiplier, block );
            }
        }
    }

    /**
     * Adds the registry handler to the {@link RegistryEventHandler}. Must be called internally only.
     */
    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( Item.class, ENTRIES );
    }

    private MDItems() {
    }
}
