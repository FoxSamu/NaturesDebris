/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.item;

import com.google.common.collect.Lists;
import modernity.api.IModernity;
import modernity.api.data.IRecipeData;
import modernity.api.item.IColoredItem;
import modernity.common.block.MDBuildingBlocks;
import modernity.common.block.MDMineralBlocks;
import modernity.common.block.MDNatureBlocks;
import modernity.common.block.MDPlantBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.item.base.*;
import modernity.common.item.util.ItemUtil;
import modernity.common.recipes.data.RecipeDataTypes;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.redgalaxy.util.Lazy;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Holder class for Modernity items.
 */
@ObjectHolder( "modernity" )
public final class MDItems {
    private static final RegistryHandler<Item> ENTRIES = new RegistryHandler<>( "modernity" );




    /* ==== MINERALS ==== */

    public static final Item SALT_DUST
        = item( "salt_dust", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDBuildingBlocks.SALT_DUST_BLOCK, 9, "%s_from_block" )
              .recipeBlock9( () -> MDItems.PINCH_OF_SALT, 1, "%s_from_pinch" )
              .recipeOne( () -> MDItems.SALT_NUGGET, 1, "%s_from_nugget" )
              .create();

    public static final Item SALT_NUGGET
        = item( "salt_nugget", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .create();

    public static final Item PINCH_OF_SALT
        = item( "pinch_of_salt", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> SALT_DUST, 9, "%s" )
              .create();

    public static final Item ALUMINIUM_INGOT
        = item( "aluminium_ingot", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.ALUMINIUM_BLOCK, 9, "%s_from_block" )
              .recipeBlock9( () -> MDItems.ALUMINIUM_NUGGET, 1, "%s_from_nugget" )
              .recipeSmelting( () -> MDMineralBlocks.ALUMINIUM_ORE, 0.1, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.ALUMINIUM_ORE, 0.1, 100, "%s_from_blasting" )
              .create();

    public static final Item ALUMINIUM_NUGGET
        = item( "aluminium_nugget", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> ALUMINIUM_INGOT, 9, "%s" )
              .recipeSmelting( Arrays.asList(
                  () -> MDItems.ALUMINIUM_PICKAXE,
                  () -> MDItems.ALUMINIUM_AXE,
                  () -> MDItems.ALUMINIUM_SWORD,
                  () -> MDItems.ALUMINIUM_SHOVEL,
                  () -> MDItems.ALUMINIUM_HOE,
                  () -> MDItems.ALUMINIUM_CHESTPLATE,
                  () -> MDItems.ALUMINIUM_LEGGINGS,
                  () -> MDItems.ALUMINIUM_HELMET,
                  () -> MDItems.ALUMINIUM_BOOTS
              ), 0.1, 200, "%s_from_tool" )
              .create();

    public static final Item ANTHRACITE
        = item( "anthracite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.ANTHRACITE_BLOCK, 9, "%s_from_block" )
              .recipeSmelting( () -> MDMineralBlocks.ANTHRACITE_ORE, 0.1, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.ANTHRACITE_ORE, 0.1, 100, "%s_from_blasting" )
              .create();

    public static final Item FINNERITE
        = item( "finnerite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.FINNERITE_BLOCK, 9, "%s_from_block" )
              .recipeSmelting( () -> MDMineralBlocks.FINNERITE_ORE, 0.2, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.FINNERITE_ORE, 0.2, 100, "%s_from_blasting" )
              .create();

    public static final Item IVERITE
        = item( "iverite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.IVERITE_BLOCK, 9, "%s_from_block" )
              .recipeSmelting( () -> MDMineralBlocks.IVERITE_ORE, 0.2, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.IVERITE_ORE, 0.2, 100, "%s_from_blasting" )
              .create();

    public static final Item SAGERITE
        = item( "sagerite", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.SAGERITE_BLOCK, 9, "%s_from_block" )
              .recipeSmelting( () -> MDMineralBlocks.SAGERITE_ORE, 0.2, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.SAGERITE_ORE, 0.2, 100, "%s_from_blasting" )
              .create();

    public static final Item LUMINOSITE_SHARDS
        = item( "luminosite_shards", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.LUMINOSITE_BLOCK, 9, "%s_from_block" )
              .recipeSmelting( () -> MDMineralBlocks.LUMINOSITE_ORE, 0.2, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.LUMINOSITE_ORE, 0.2, 100, "%s_from_blasting" )
              .create();



    /* ==== MISCELLANEOUS ==== */

    public static final Item ASH
        = item( "ash", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDBuildingBlocks.ASH_BLOCK, 9, "%s_from_block" )
              .recipeSmelting( MDItemTags.ASHABLE, 0.1, 200, "%s_from_smelting" )
              .create();

    public static final Item BLACKWOOD_STICK
        = item( "blackwood_stick", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeVert( () -> MDBuildingBlocks.BLACKWOOD_PLANKS, 4, null )
              .create();

    public static final Item INVER_STICK
        = item( "inver_stick", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeVert( () -> MDBuildingBlocks.INVER_PLANKS, 4, null )
              .create();

    public static final GooBallItem GOO_BALL
        = item( "goo_ball", new GooBallItem( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.GOO_BLOCK, 9, "%s_from_block" )
              .recipeOne( () -> MDPlantBlocks.GOO_DRIPS, 2, "%s_from_drips" )
              .recipeSmelting( () -> MDMineralBlocks.GOO_ORE, 0.1, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.GOO_ORE, 0.1, 100, "%s_from_blasting" )
              .create();

    public static final Item GLAZED_GOO_BALL
        = item( "glazed_goo_ball", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeSmelting( () -> GOO_BALL, 0.1, 200, null )
              .create();

    public static final PoisonousGooBallItem POISONOUS_GOO_BALL
        = item( "poisonous_goo_ball", new PoisonousGooBallItem( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .create();

    public static final ShadeBallItem SHADE_BALL
        = item( "shade_ball", new ShadeBallItem( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeInO8( () -> MDItems.SHADE_BLUE_FLOWER, () -> GLAZED_GOO_BALL, 1, null )
              .create();

    public static final Item BLACKBONE
        = item( "blackbone", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDMineralBlocks.BLACKBONE_BLOCK, 9, "%s_from_block" )
              .recipeSmelting( () -> MDMineralBlocks.FOSSIL, 0.1, 200, "%s_from_smelting" )
              .recipeBlasting( () -> MDMineralBlocks.FOSSIL, 0.1, 100, "%s_from_blasting" )
              .create();



    /* ==== PLANTS & FOOD ==== */

    // Misc
    public static final Item SHADE_BLUE_FLOWER
        = item( "shade_blue_flower", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDPlantBlocks.SHADE_BLUE, 2, null )
              .create();

    public static final Item NUDWART_PETALS
        = item( "nudwart_petals", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDPlantBlocks.NUDWART, 1, null )
              .create();

    public static final Item FOXGLOVE_PETALS
        = item( "foxglove_petals", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDPlantBlocks.FOXGLOVE, 2, null )
              .create();

    public static final Item SEEPWEED_LEAVES
        = item( "seepweed_leaves", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDPlantBlocks.SEEPWEED, 2, null )
              .create();

    public static final Item PLANT_WIRE
        = item( "plant_wire", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MDPlantBlocks.WIREPLANT, 2, "%s_from_wireplant" )
              .recipeOne( () -> MDPlantBlocks.FLOWERED_WIREPLANT, 2, "%s_from_flowered_wireplant" )
              .recipeOne( () -> MDPlantBlocks.WATER_WIRE, 3, "%s_from_water_wire" )
              .create();

    // Food
    public static final Item MURK_ROOT
        = item( "murk_root", new Item( new Item.Properties().group( MDItemGroup.MISC ).food( MDFoods.MURK_ROOTS ) ) )
              .create();

    public static final Item MURK_RICE
        = item( "murk_rice", new Item( new Item.Properties().group( MDItemGroup.MISC ).food( MDFoods.MURK_RICE ) ) )
              .create();

    // Seeds
    public static final Item MURK_ROOTS_SEEDS
        = item( "murk_roots_seeds", new BlockNamedItem( MDPlantBlocks.MURK_ROOTS, new Item.Properties().group( MDItemGroup.MISC ) ) )
              .create();

    public static final Item MURK_RICE_SEEDS
        = item( "murk_rice_seeds", new BlockNamedItem( MDPlantBlocks.MURK_RICE, new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeOne( () -> MURK_RICE, 3, null )
              .create();

    public static final Item SEEPWEED_SEEDS
        = item( "seepweed_seeds", new BlockNamedItem( MDPlantBlocks.SEEPWEED_CROP, new Item.Properties().group( MDItemGroup.MISC ) ) )
              .create();

    public static final Item NUDWART_SEEDS
        = item( "nudwart_seeds", new BlockNamedItem( MDPlantBlocks.NUDWART_CROP, new Item.Properties().group( MDItemGroup.MISC ) ) )
              .create();



    /* ==== TOOLS & ARMOR ==== */

    // Buckets
    public static final BaseBucketItem ALUMINIUM_BUCKET
        = item( "aluminium_bucket", new AluminiumBucketItem( Fluids.EMPTY, new Item.Properties().group( MDItemGroup.MISC ) ) )
              .recipeBucket( () -> ALUMINIUM_INGOT, 1, null )
              .create();

    public static final BaseBucketItem ALUMINIUM_WATER_BUCKET
        = item( "aluminium_water_bucket", new AluminiumBucketItem( MDFluids.MURKY_WATER, new Item.Properties().group( MDItemGroup.MISC ).containerItem( ALUMINIUM_BUCKET ).maxStackSize( 1 ) ) )
              .create();

    public static final BaseBucketItem ALUMINIUM_CLEAN_WATER_BUCKET
        = item( "aluminium_clean_water_bucket", new AluminiumBucketItem( MDFluids.CLEAN_WATER, new Item.Properties().group( MDItemGroup.MISC ).containerItem( ALUMINIUM_BUCKET ).maxStackSize( 1 ) ) )
              .create();

    public static final BaseBucketItem ALUMINIUM_LAVA_BUCKET
        = item( "aluminium_lava_bucket", new AluminiumBucketItem( MDFluids.MOLTEN_ROCK, new Item.Properties().group( MDItemGroup.MISC ).containerItem( ALUMINIUM_BUCKET ).maxStackSize( 1 ) ) )
              .alias( "aluminium_heatrock_bucket" )
              .create();

    // Blackwood Tools
    public static final PickaxeItem BLACKWOOD_PICKAXE
        = item( "blackwood_pickaxe", new PickaxeItem( MDItemTier.BLACKWOOD, 1, - 2.8F, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipePickaxe( () -> MDBuildingBlocks.BLACKWOOD_PLANKS, () -> BLACKWOOD_STICK, 1, null )
              .create();

    public static final AxeItem BLACKWOOD_AXE
        = item( "blackwood_axe", new AxeItem( MDItemTier.BLACKWOOD, 6, - 3.2F, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeAxe( () -> MDBuildingBlocks.BLACKWOOD_PLANKS, () -> BLACKWOOD_STICK, 1, null )
              .create();

    public static final ShovelItem BLACKWOOD_SHOVEL
        = item( "blackwood_shovel", new ShovelItem( MDItemTier.BLACKWOOD, 1.5F, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeShovel( () -> MDBuildingBlocks.BLACKWOOD_PLANKS, () -> BLACKWOOD_STICK, 1, null )
              .create();

    public static final HoeItem BLACKWOOD_HOE
        = item( "blackwood_hoe", new HoeItem( MDItemTier.BLACKWOOD, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeHoe( () -> MDBuildingBlocks.BLACKWOOD_PLANKS, () -> BLACKWOOD_STICK, 1, null )
              .create();

    public static final SwordItem BLACKWOOD_SWORD
        = item( "blackwood_sword", new SwordItem( MDItemTier.BLACKWOOD, 3, - 2.4F, new Item.Properties().group( MDItemGroup.COMBAT ) ) )
              .recipeSword( () -> MDBuildingBlocks.BLACKWOOD_PLANKS, () -> BLACKWOOD_STICK, 1, null )
              .create();


    // Blackwood Tools
    public static final PickaxeItem DARKROCK_PICKAXE
        = item( "darkrock_pickaxe", new PickaxeItem( MDItemTier.DARKROCK, 1, - 2.8F, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipePickaxe( () -> MDNatureBlocks.DARKROCK, MDItemTags.STICKS, 1, null )
              .create();

    public static final AxeItem DARKROCK_AXE
        = item( "darkrock_axe", new AxeItem( MDItemTier.DARKROCK, 7, - 3.2F, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeAxe( () -> MDNatureBlocks.DARKROCK, MDItemTags.STICKS, 1, null )
              .create();

    public static final ShovelItem DARKROCK_SHOVEL
        = item( "darkrock_shovel", new ShovelItem( MDItemTier.DARKROCK, 1.5F, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeShovel( () -> MDNatureBlocks.DARKROCK, MDItemTags.STICKS, 1, null )
              .create();

    public static final HoeItem DARKROCK_HOE
        = item( "darkrock_hoe", new HoeItem( MDItemTier.DARKROCK, - 2, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeHoe( () -> MDNatureBlocks.DARKROCK, MDItemTags.STICKS, 1, null )
              .create();

    public static final SwordItem DARKROCK_SWORD
        = item( "darkrock_sword", new SwordItem( MDItemTier.DARKROCK, 3, - 2.4F, new Item.Properties().group( MDItemGroup.COMBAT ) ) )
              .recipeSword( () -> MDNatureBlocks.DARKROCK, MDItemTags.STICKS, 1, null )
              .create();


    // Aluminium Tools
    public static final PickaxeItem ALUMINIUM_PICKAXE
        = item( "aluminium_pickaxe", new PickaxeItem( MDItemTier.ALUMINIUM, 1, - 2.8F, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipePickaxe( () -> ALUMINIUM_INGOT, MDItemTags.STICKS, 1, null )
              .create();

    public static final AxeItem ALUMINIUM_AXE
        = item( "aluminium_axe", new AxeItem( MDItemTier.ALUMINIUM, 6, - 3.1F, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeAxe( () -> ALUMINIUM_INGOT, MDItemTags.STICKS, 1, null )
              .create();

    public static final ShovelItem ALUMINIUM_SHOVEL
        = item( "aluminium_shovel", new ShovelItem( MDItemTier.ALUMINIUM, 1.5F, - 3, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeShovel( () -> ALUMINIUM_INGOT, MDItemTags.STICKS, 1, null )
              .create();

    public static final HoeItem ALUMINIUM_HOE
        = item( "aluminium_hoe", new HoeItem( MDItemTier.ALUMINIUM, - 1, new Item.Properties().group( MDItemGroup.TOOLS ) ) )
              .recipeHoe( () -> ALUMINIUM_INGOT, MDItemTags.STICKS, 1, null )
              .create();

    public static final SwordItem ALUMINIUM_SWORD
        = item( "aluminium_sword", new SwordItem( MDItemTier.ALUMINIUM, 3, - 2.4F, new Item.Properties().group( MDItemGroup.COMBAT ) ) )
              .recipeSword( () -> ALUMINIUM_INGOT, MDItemTags.STICKS, 1, null )
              .create();


    // Aluminium Armor
    public static final ArmorItem ALUMINIUM_HELMET
        = item( "aluminium_helmet", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.HEAD, new Item.Properties().group( MDItemGroup.COMBAT ) ) )
              .recipeHelmet( () -> ALUMINIUM_INGOT, 1, null )
              .create();

    public static final ArmorItem ALUMINIUM_CHESTPLATE
        = item( "aluminium_chestplate", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.CHEST, new Item.Properties().group( MDItemGroup.COMBAT ) ) )
              .recipeChestplate( () -> ALUMINIUM_INGOT, 1, null )
              .create();

    public static final ArmorItem ALUMINIUM_LEGGINGS
        = item( "aluminium_leggings", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.LEGS, new Item.Properties().group( MDItemGroup.COMBAT ) ) )
              .recipeLeggings( () -> ALUMINIUM_INGOT, 1, null )
              .create();

    public static final ArmorItem ALUMINIUM_BOOTS
        = item( "aluminium_boots", new ArmorItem( MDArmorMaterial.ALUMINIUM, EquipmentSlotType.FEET, new Item.Properties().group( MDItemGroup.COMBAT ) ) )
              .recipeBoots( () -> ALUMINIUM_INGOT, 1, null )
              .create();




    /* ==== PORTAL ==== */

    public static final Item EYE_OF_THE_CURSE
        = item( "eye_of_the_curse", new Item( new Item.Properties().group( MDItemGroup.MISC ) ) )
              .create();








    public static <T extends Item> ItemConfig<T> item( String name, T item ) {
        return new ItemConfig<T>( name ).item( item );
    }

    public static ItemConfig<Item> simple( String name ) {
        return new ItemConfig<>( name ).item( Item::new );
    }

    public static <T extends Item> ItemConfig<T> function( String name, Function<Item.Properties, ? extends T> item ) {
        return new ItemConfig<T>( name ).item( item );
    }

    public static Supplier<IItemProvider> supplyI( String id ) {
        return Lazy.of( () -> ForgeRegistries.ITEMS.getValue( new ResourceLocation( "modernity", id ) ) );
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

        ItemUtil.addShovelFlattenBehaviour( MDNatureBlocks.MURKY_GRASS_BLOCK, MDNatureBlocks.MURKY_GRASS_PATH.getDefaultState() );

        ItemUtil.addHoeTillBehaviour( MDNatureBlocks.MURKY_DIRT, MDNatureBlocks.MURKY_DIRT_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDNatureBlocks.MURKY_GRASS_BLOCK, MDNatureBlocks.MURKY_GRASS_BLOCK_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDNatureBlocks.MURKY_HUMUS, MDNatureBlocks.MURKY_HUMUS_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDNatureBlocks.LEAFY_HUMUS, MDNatureBlocks.LEAFY_HUMUS_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDNatureBlocks.MURKY_PODZOL, MDNatureBlocks.MURKY_PODZOL_FARMLAND.getDefaultState() );
        ItemUtil.addHoeTillBehaviour( MDNatureBlocks.HEATH_BLOCK, MDNatureBlocks.HEATH_FARMLAND.getDefaultState() );
    }

    private MDItems() {
    }

    public static class ItemConfig<T extends Item> {
        private String id;
        private Function<Item.Properties, ? extends T> func;
        private Item.Properties itemProps = new Item.Properties();
        private final List<String> aliases = Lists.newArrayList();
        private final List<IRecipeData> recipes = Lists.newArrayList();

        ItemConfig( String id ) {
            this.id = id;
        }

        private ItemConfig<T> item( T item ) {
            func = props -> item;
            return this;
        }

        private ItemConfig<T> item( Function<Item.Properties, ? extends T> func ) {
            this.func = func;
            return this;
        }

        public ItemConfig<T> props( Item.Properties props ) {
            itemProps = props;
            return this;
        }

        public ItemConfig<T> props( Function<Item.Properties, Item.Properties> func ) {
            itemProps = func.apply( itemProps );
            return this;
        }

        public ItemConfig<T> group( ItemGroup group ) {
            itemProps.group( group );
            return this;
        }

        public ItemConfig<T> tool( ToolType tool, int level ) {
            itemProps.addToolType( tool, level );
            return this;
        }

        public ItemConfig<T> container( Item container ) {
            itemProps.containerItem( container );
            return this;
        }

        public ItemConfig<T> noRepair() {
            itemProps.setNoRepair();
            return this;
        }

        public ItemConfig<T> food( Food food ) {
            itemProps.food( food );
            return this;
        }

        public ItemConfig<T> stackSize( int size ) {
            itemProps.maxStackSize( size );
            return this;
        }

        public ItemConfig<T> rarity( Rarity rarity ) {
            itemProps.rarity( rarity );
            return this;
        }

        public ItemConfig<T> alias( String... aliases ) {
            this.aliases.addAll( Arrays.asList( aliases ) );
            return this;
        }

        public ItemConfig<T> recipe( IRecipeData recipe ) {
            this.recipes.add( recipe );
            return this;
        }

        public ItemConfig<T> recipeBlock4( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.block4( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeBlock9( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.block9( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeOne( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.one( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeOne( Tag<Item> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.one( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeJoin( Supplier<IItemProvider> in1, Supplier<IItemProvider> in2, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.join( in1, in2, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeSlab( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.slab( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeStairs( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.stairs( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeStep( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.step( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeWall( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.wall( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeCorner( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.corner( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeStonecutting( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.stonecutting( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeSmelting( Supplier<IItemProvider> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.smelting( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeSmelting( Tag<Item> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.smelting( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeSmelting( List<Supplier<IItemProvider>> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.smelting( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeSmoking( Supplier<IItemProvider> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.smoking( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeBlasting( Supplier<IItemProvider> in, double exp, int time, String id ) {
            this.recipes.add( RecipeDataTypes.blasting( in, supplyI( this.id ), (float) exp, time, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeFence( Supplier<IItemProvider> stick, Supplier<IItemProvider> wood, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.fence( stick, wood, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeDoor( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.door( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeVert( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.vert( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeHoriz( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.horiz( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeVert3( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.vert3( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeHoriz3( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.horiz3( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeTorch( Supplier<IItemProvider> coal, Supplier<IItemProvider> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.torch( coal, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeTorch( Supplier<IItemProvider> coal, Tag<Item> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.torch( coal, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeJoinVert( Supplier<IItemProvider> u, Supplier<IItemProvider> d, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.joinVert( u, d, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeJoinHoriz( Supplier<IItemProvider> l, Supplier<IItemProvider> r, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.joinHoriz( l, r, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeH( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.h( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeX( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.x( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeO4( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.o4( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeO8( Supplier<IItemProvider> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.o8( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeO8( Tag<Item> in, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.o8( in, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeJoin3( Supplier<IItemProvider> in1, Supplier<IItemProvider> in2, Supplier<IItemProvider> in3, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.join3( in1, in2, in3, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeAsphalt( Supplier<IItemProvider> goo, Supplier<IItemProvider> coal, Supplier<IItemProvider> gravel, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.asphalt( goo, coal, gravel, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeBoots( Supplier<IItemProvider> mat, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.boots( mat, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeHelmet( Supplier<IItemProvider> mat, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.helmet( mat, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeChestplate( Supplier<IItemProvider> mat, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.chestplate( mat, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeLeggings( Supplier<IItemProvider> mat, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.leggings( mat, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeAxe( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.axe( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeAxe( Supplier<IItemProvider> mat, Tag<Item> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.axe( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipePickaxe( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.pickaxe( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipePickaxe( Supplier<IItemProvider> mat, Tag<Item> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.pickaxe( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeHoe( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.hoe( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeHoe( Supplier<IItemProvider> mat, Tag<Item> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.hoe( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeSword( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.sword( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeSword( Supplier<IItemProvider> mat, Tag<Item> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.sword( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeShovel( Supplier<IItemProvider> mat, Supplier<IItemProvider> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.shovel( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeShovel( Supplier<IItemProvider> mat, Tag<Item> stick, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.shovel( mat, stick, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeBucket( Supplier<IItemProvider> mat, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.bucket( mat, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public ItemConfig<T> recipeInO8( Supplier<IItemProvider> o, Supplier<IItemProvider> inner, int outCount, String id ) {
            this.recipes.add( RecipeDataTypes.inO8( o, inner, supplyI( this.id ), outCount, "", id ) );
            return this;
        }

        public T create() {
            if( func == null ) {
                throw new IllegalStateException( "No item function specified" );
            }

            T item = func.apply( itemProps );

            ENTRIES.register( id, item, aliases.toArray( new String[ 0 ] ) );

            for( IRecipeData recipe : recipes ) {
                IModernity.get().getDataService().addRecipe( recipe );
            }

            return item;
        }
    }
}
