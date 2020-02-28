/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.item;

import modernity.api.item.IColoredItem;
import modernity.common.block.MDBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.item.base.*;
import modernity.common.item.util.ItemUtil;
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




    /* ==== MINERALS ==== */

    public static final Item SALT_DUST = item( "salt_dust", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item SALT_NUGGET = item( "salt_nugget", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item PINCH_OF_SALT = item( "pinch_of_salt", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item ALUMINIUM_INGOT = item( "aluminium_ingot", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item ALUMINIUM_NUGGET = item( "aluminium_nugget", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item ANTHRACITE = item( "anthracite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item FINNERITE = item( "finnerite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item IVERITE = item( "iverite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item SAGERITE = item( "sagerite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item LUMINOSITE_SHARDS = item( "luminosite_shards", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );



    /* ==== MISCELLANEOUS ==== */

    public static final Item ASH = item( "ash", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item BLACKWOOD_STICK = item( "blackwood_stick", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item INVER_STICK = item( "inver_stick", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );

    public static final GooBallItem GOO_BALL = item( "goo_ball", new GooBallItem( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item GLAZED_GOO_BALL = item( "glazed_goo_ball", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final PoisonousGooBallItem POISONOUS_GOO_BALL = item( "poisonous_goo_ball", new PoisonousGooBallItem( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final ShadeBallItem SHADE_BALL = item( "shade_ball", new ShadeBallItem( new Item.Properties().group( MDItemGroup.MISC ) ) );

    public static final Item BLACKBONE = item( "blackbone", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );



    /* ==== PLANTS ==== */

    public static final Item SHADE_BLUE_FLOWER = item( "shade_blue_flower", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item NUDWART_PETALS = item( "nudwart_petals", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item FOXGLOVE_PETALS = item( "foxglove_petals", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item SEEPWEED_LEAVES = item( "seepweed_leaves", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final Item PLANT_WIRE = item( "plant_wire", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );



    /* ==== TOOLS & ARMOR ==== */

    // Buckets
    public static final BaseBucketItem ALUMINIUM_BUCKET = item( "aluminium_bucket", new AluminiumBucketItem( Fluids.EMPTY, new Item.Properties().group( MDItemGroup.MISC ) ) );
    public static final BaseBucketItem ALUMINIUM_WATER_BUCKET = item( "aluminium_water_bucket", new AluminiumBucketItem( MDFluids.MURKY_WATER, new Item.Properties().group( MDItemGroup.MISC ).containerItem( ALUMINIUM_BUCKET ).maxStackSize( 1 ) ) );
    public static final BaseBucketItem ALUMINIUM_LAVA_BUCKET = item( "aluminium_lava_bucket", new AluminiumBucketItem( MDFluids.MOLTEN_ROCK, new Item.Properties().group( MDItemGroup.MISC ).containerItem( ALUMINIUM_BUCKET ).maxStackSize( 1 ) ), "aluminium_heatrock_bucket" );

    // Blackwood Tools
    public static final PickaxeItem BLACKWOOD_PICKAXE = item( "blackwood_pickaxe", new PickaxeItem( MDItemTier.BLACKWOOD, 1, - 2.8F, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final AxeItem BLACKWOOD_AXE = item( "blackwood_axe", new AxeItem( MDItemTier.BLACKWOOD, 6, - 3.2F, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final ShovelItem BLACKWOOD_SHOVEL = item( "blackwood_shovel", new ShovelItem( MDItemTier.BLACKWOOD, 1.5F, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final HoeItem BLACKWOOD_HOE = item( "blackwood_hoe", new HoeItem( MDItemTier.BLACKWOOD, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final SwordItem BLACKWOOD_SWORD = item( "blackwood_sword", new SwordItem( MDItemTier.BLACKWOOD, 3, - 2.4F, new Item.Properties().group( MDItemGroup.COMBAT ) ) );

    // Blackwood Tools
    public static final PickaxeItem DARKROCK_PICKAXE = item( "darkrock_pickaxe", new PickaxeItem( MDItemTier.DARKROCK, 1, - 2.8F, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final AxeItem DARKROCK_AXE = item( "darkrock_axe", new AxeItem( MDItemTier.DARKROCK, 7, - 3.2F, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final ShovelItem DARKROCK_SHOVEL = item( "darkrock_shovel", new ShovelItem( MDItemTier.DARKROCK, 1.5F, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final HoeItem DARKROCK_HOE = item( "darkrock_hoe", new HoeItem( MDItemTier.DARKROCK, - 2, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final SwordItem DARKROCK_SWORD = item( "darkrock_sword", new SwordItem( MDItemTier.DARKROCK, 3, - 2.4F, new Item.Properties().group( MDItemGroup.COMBAT ) ) );

    // Aluminium Tools
    public static final PickaxeItem ALUMINIUM_PICKAXE = item( "aluminium_pickaxe", new PickaxeItem( MDItemTier.ALUMINIUM, 1, - 2.8F, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final AxeItem ALUMINIUM_AXE = item( "aluminium_axe", new AxeItem( MDItemTier.ALUMINIUM, 6, - 3.1F, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final ShovelItem ALUMINIUM_SHOVEL = item( "aluminium_shovel", new ShovelItem( MDItemTier.ALUMINIUM, 1.5F, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final HoeItem ALUMINIUM_HOE = item( "aluminium_hoe", new HoeItem( MDItemTier.ALUMINIUM, - 1, new Item.Properties().group( MDItemGroup.TOOLS ) ) );
    public static final SwordItem ALUMINIUM_SWORD = item( "aluminium_sword", new SwordItem( MDItemTier.ALUMINIUM, 3, - 2.4F, new Item.Properties().group( MDItemGroup.COMBAT ) ) );

    // Aluminium Armor
    public static final ArmorItem ALUMINIUM_HELMET = item( "aluminium_helmet", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.HEAD, new Item.Properties().group( MDItemGroup.COMBAT ) ) );
    public static final ArmorItem ALUMINIUM_CHESTPLATE = item( "aluminium_chestplate", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.CHEST, new Item.Properties().group( MDItemGroup.COMBAT ) ) );
    public static final ArmorItem ALUMINIUM_LEGGINGS = item( "aluminium_leggings", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.LEGS, new Item.Properties().group( MDItemGroup.COMBAT ) ) );
    public static final ArmorItem ALUMINIUM_BOOTS = item( "aluminium_boots", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.FEET, new Item.Properties().group( MDItemGroup.COMBAT ) ) );



    /* ==== PORTAL ==== */

    public static final Item EYE_OF_THE_CURSE = item( "eye_of_the_curse", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) );








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

        ItemUtil.addShovelFlattenBehaviour( MDBlocks.MURKY_GRASS_BLOCK, MDBlocks.MURKY_GRASS_PATH.getDefaultState() );

        ItemUtil.addHoeTillBehaviour( MDBlocks.MURKY_DIRT, MDBlocks.MURKY_DIRT_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDBlocks.MURKY_GRASS_BLOCK, MDBlocks.MURKY_GRASS_BLOCK_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDBlocks.MURKY_HUMUS, MDBlocks.MURKY_HUMUS_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDBlocks.LEAFY_HUMUS, MDBlocks.LEAFY_HUMUS_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDBlocks.MURKY_PODZOL, MDBlocks.MURKY_PODZOL_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDBlocks.HEATH_BLOCK, MDBlocks.HEATH_FARMLAND.getDefaultState() );
    }

    private MDItems() {
    }
}
