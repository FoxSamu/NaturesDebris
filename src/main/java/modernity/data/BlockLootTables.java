/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.data;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.*;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class BlockLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder( ItemPredicate.Builder.create().enchantment( new EnchantmentPredicate( Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast( 1 ) ) ) );
    private static final ILootCondition.IBuilder NO_SILK_TOUCH = SILK_TOUCH.inverted();
    private static final ILootCondition.IBuilder SHEARS = MatchTool.builder( ItemPredicate.Builder.create().item( Items.SHEARS ) );
    private static final ILootCondition.IBuilder SHEARS_OR_SILK_TOUCH = SHEARS.alternative( SILK_TOUCH );
    private static final ILootCondition.IBuilder NO_SHEARS_OR_SILK_TOUCH = SHEARS_OR_SILK_TOUCH.inverted();

    private static final Set<Item> EXPLOSION_RESISTANT = Stream.of( Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX ).map( IItemProvider::asItem ).collect( ImmutableSet.toImmutableSet() );

    private static final float[] LEAVES_TABLE = { 0.05F, 0.0625F, 0.083333336F, 0.1F };
    private static final float[] JUNGLE_LEAVES_TABLE = { 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F };

    private final Map<ResourceLocation, LootTable.Builder> registeredTables = Maps.newHashMap();

    protected static <T> T explosionFunc( IItemProvider item, ILootFunctionConsumer<T> consumer ) {
        return ! EXPLOSION_RESISTANT.contains( item.asItem() )
               ? consumer.acceptFunction( ExplosionDecay.func_215863_b() )
               : consumer.cast();
    }

    protected static <T> T explosionCond( IItemProvider item, ILootConditionConsumer<T> consumer ) {
        return ! EXPLOSION_RESISTANT.contains( item.asItem() )
               ? consumer.acceptCondition( SurvivesExplosion.builder() )
               : consumer.cast();
    }

    protected static LootTable.Builder simple( IItemProvider item ) {
        return LootTable.builder().addLootPool(
            explosionCond(
                item,
                LootPool.builder()
                        .name( "simple" )
                        .rolls( ConstantRange.of( 1 ) )
                        .addEntry( ItemLootEntry.builder( item ) )
            )
        );
    }

    protected static LootTable.Builder conditional( Block block, ILootCondition.IBuilder condition, LootEntry.Builder<?> alternative ) {
        return LootTable.builder().addLootPool(
            LootPool.builder()
                    .name( "sipmle" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry(
                        ItemLootEntry.builder( block )
                                     .acceptCondition( condition )
                                     .func_216080_a( alternative )
                    )
        );
    }

    protected static LootTable.Builder silkTouch( Block block, LootEntry.Builder<?> alternative ) {
        return conditional( block, SILK_TOUCH, alternative );
    }

    protected static LootTable.Builder shears( Block block, LootEntry.Builder<?> alternative ) {
        return conditional( block, SHEARS, alternative );
    }

    protected static LootTable.Builder silkTouchOrShears( Block block, LootEntry.Builder<?> alternative ) {
        return conditional( block, SHEARS_OR_SILK_TOUCH, alternative );
    }

    protected static LootTable.Builder silkTouchOrItem( Block block, IItemProvider item ) {
        return silkTouch( block, explosionCond( block, ItemLootEntry.builder( item ) ) );
    }

    protected static LootTable.Builder item( IItemProvider item, IRandomRange count ) {
        return LootTable.builder().addLootPool(
            LootPool.builder()
                    .name( "simple" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry( explosionFunc(
                        item,
                        ItemLootEntry.builder( item )
                                     .acceptFunction( SetCount.func_215932_a( count ) )
                    ) )
        );
    }

    protected static LootTable.Builder silkTouchOrItem( Block st, IItemProvider item, IRandomRange count ) {
        return silkTouch( st, explosionFunc(
            st,
            ItemLootEntry.builder( item )
                         .acceptFunction( SetCount.func_215932_a( count ) )
        ) );
    }

    protected static LootTable.Builder silkTouchOnly( IItemProvider item ) {
        return LootTable.builder().addLootPool(
            LootPool.builder()
                    .name( "simple" )
                    .acceptCondition( SILK_TOUCH )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry( ItemLootEntry.builder( item ) )
        );
    }

    protected static LootTable.Builder flowerPot( IItemProvider plant ) {
        return LootTable.builder()
                        .addLootPool(
                            explosionCond(
                                Blocks.FLOWER_POT,
                                LootPool.builder()
                                        .name( "pot" )
                                        .rolls( ConstantRange.of( 1 ) )
                                        .addEntry( ItemLootEntry.builder( Blocks.FLOWER_POT ) )
                            )
                        )
                        .addLootPool(
                            explosionCond(
                                plant,
                                LootPool.builder()
                                        .name( "plant" )
                                        .rolls( ConstantRange.of( 1 ) )
                                        .addEntry( ItemLootEntry.builder( plant ) )
                            )
                        );
    }

    protected static LootTable.Builder slab( Block block ) {
        return LootTable.builder().addLootPool(
            LootPool.builder()
                    .name( "slab" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry( explosionFunc(
                        block,
                        ItemLootEntry.builder( block ).acceptFunction(
                            SetCount.func_215932_a( ConstantRange.of( 2 ) ).acceptCondition(
                                BlockStateProperty.builder( block )
                                                  .with( SlabBlock.TYPE, SlabType.DOUBLE )
                            )
                        )
                    ) )
        );
    }

    protected static <T extends Comparable<T>> LootTable.Builder blockProperty( Block block, IProperty<T> prop, T value ) {
        return LootTable.builder().addLootPool( explosionCond(
            block,
            LootPool.builder()
                    .name( "simple" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry(
                        ItemLootEntry.builder( block ).acceptCondition(
                            BlockStateProperty.builder( block ).with( prop, value )
                        )
                    )
        ) );
    }

    protected static LootTable.Builder copyTileEntityName( Block block ) {
        return LootTable.builder().addLootPool( explosionCond(
            block,
            LootPool.builder()
                    .name( "simple" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry(
                        ItemLootEntry.builder( block ).acceptFunction(
                            CopyName.func_215893_a( CopyName.Source.BLOCK_ENTITY ) )
                    )
        ) );
    }

    protected static LootTable.Builder shulkerBox( Block block ) {
        return LootTable.builder().addLootPool( explosionCond(
            block,
            LootPool.builder()
                    .name( "shulker_box" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry(
                        ItemLootEntry.builder( block )
                                     .acceptFunction( CopyName.func_215893_a( CopyName.Source.BLOCK_ENTITY ) )
                                     .acceptFunction(
                                         CopyNbt.func_215881_a( CopyNbt.Source.BLOCK_ENTITY )
                                                .func_216056_a( "Lock", "BlockEntityTag.Lock" )
                                                .func_216056_a( "LootTable", "BlockEntityTag.LootTable" )
                                                .func_216056_a( "LootTableSeed", "BlockEntityTag.LootTableSeed" )
                                     )
                                     .acceptFunction(
                                         SetContents.func_215920_b()
                                                    .func_216075_a( DynamicLootEntry.func_216162_a( ShulkerBoxBlock.field_220169_b ) )
                                     )
                    )
        ) );
    }

    protected static LootTable.Builder banner( Block block ) {
        return LootTable.builder().addLootPool( explosionCond(
            block,
            LootPool.builder()
                    .name( "banner" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry(
                        ItemLootEntry.builder( block )
                                     .acceptFunction( CopyName.func_215893_a( CopyName.Source.BLOCK_ENTITY ) )
                                     .acceptFunction(
                                         CopyNbt.func_215881_a( CopyNbt.Source.BLOCK_ENTITY )
                                                .func_216056_a( "Patterns", "BlockEntityTag.Patterns" )
                                     )
                    )
        ) );
    }

    protected static LootTable.Builder ore( Block ore, Item drop ) {
        return silkTouch( ore, explosionFunc(
            ore,
            ItemLootEntry.builder( drop )
                         .acceptFunction( ApplyBonus.func_215869_a( Enchantments.FORTUNE ) )
        ) );
    }

    protected static LootTable.Builder mushroomBlock( Block block, IItemProvider item ) {
        return silkTouch( block, explosionFunc(
            block,
            ItemLootEntry.builder( item )
                         .acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( - 6.0F, 2.0F ) ) )
                         .acceptFunction( LimitCount.func_215911_a( IntClamper.func_215848_a( 0 ) ) )
        ) );
    }

    protected static LootTable.Builder seeds( Block block, IItemProvider seeds ) {
        return shears( block, explosionFunc(
            block,
            ItemLootEntry.builder( seeds )
                         .acceptCondition( RandomChance.builder( 0.125F ) )
                         .acceptFunction( ApplyBonus.func_215865_a( Enchantments.FORTUNE, 2 ) )
        ) );
    }

    protected static LootTable.Builder seeds( Block block ) {
        return seeds( block, Items.WHEAT_SEEDS );
    }

    protected static LootTable.Builder stemCrop( Block block, Item seeds ) {
        return LootTable.builder().addLootPool( explosionFunc(
            block,
            LootPool.builder()
                    .name( "simple" )
                    .rolls( ConstantRange.of( 1 ) )
                    .addEntry(
                        ItemLootEntry.builder( seeds )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.06666667F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 0 ) )
                                     )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.13333334F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 1 ) )
                                     )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.2F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 2 ) )
                                     )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.26666668F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 3 ) )
                                     )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.33333334F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 4 ) )
                                     )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.4F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 5 ) )
                                     )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.46666667F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 6 ) )
                                     )
                                     .acceptFunction(
                                         SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.53333336F ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( StemBlock.AGE, 7 ) )
                                     )
                    )
        ) );
    }

    protected static LootTable.Builder shearable( IItemProvider item ) {
        return LootTable.builder().addLootPool(
            LootPool.builder()
                    .name( "simple" )
                    .rolls( ConstantRange.of( 1 ) )
                    .acceptCondition( SHEARS )
                    .addEntry( ItemLootEntry.builder( item ) )
        );
    }

    protected static LootTable.Builder leaves( Block block, Block sapling, float... fortuneTable ) {
        return silkTouchOrShears(
            block,
            explosionCond( block, ItemLootEntry.builder( sapling ) )
                .acceptCondition( TableBonus.builder( Enchantments.FORTUNE, fortuneTable ) )
        ).addLootPool(
            LootPool.builder()
                    .name( "stick" )
                    .rolls( ConstantRange.of( 1 ) )
                    .acceptCondition( NO_SHEARS_OR_SILK_TOUCH )
                    .addEntry( explosionFunc(
                        block,
                        ItemLootEntry.builder( Items.STICK )
                                     .acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 1, 2 ) ) )
                    ).acceptCondition(
                        TableBonus.builder( Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F )
                    ) )
        );
    }

    protected static LootTable.Builder appleLeaves( Block block, Block sapling, float... fortuneTable ) {
        return leaves( block, sapling, fortuneTable ).addLootPool(
            LootPool.builder()
                    .name( "apple" )
                    .rolls( ConstantRange.of( 1 ) )
                    .acceptCondition( NO_SHEARS_OR_SILK_TOUCH )
                    .addEntry(
                        explosionCond( block, ItemLootEntry.builder( Items.APPLE ) )
                            .acceptCondition( TableBonus.builder( Enchantments.FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F ) )
                    )
        );
    }

    protected static LootTable.Builder seedCrops( Block block, Item resource, Item seeds, ILootCondition.IBuilder ageCondition ) {
        return explosionFunc(
            block,
            LootTable.builder()
                     .addLootPool(
                         LootPool.builder()
                                 .addEntry(
                                     ItemLootEntry.builder( resource )
                                                  .acceptCondition( ageCondition )
                                                  .func_216080_a( ItemLootEntry.builder( seeds ) )
                                 )
                     )
                     .addLootPool(
                         LootPool.builder()
                                 .acceptCondition( ageCondition )
                                 .addEntry(
                                     ItemLootEntry.builder( seeds )
                                                  .acceptFunction( ApplyBonus.func_215870_a( Enchantments.FORTUNE, 0.5714286F, 3 ) )
                                 )
                     )
        );
    }

    public static LootTable.Builder empty() {
        return LootTable.builder();
    }

    protected void addTables() {
        addSimple( Blocks.GRANITE );
        addSimple( Blocks.POLISHED_GRANITE );
        addSimple( Blocks.DIORITE );
        addSimple( Blocks.POLISHED_DIORITE );
        addSimple( Blocks.ANDESITE );
        addSimple( Blocks.POLISHED_ANDESITE );

        addSimple( Blocks.DIRT );
        addSimple( Blocks.COARSE_DIRT );

        addSimple( Blocks.COBBLESTONE );
        addSimple( Blocks.MOSSY_COBBLESTONE );

        addSimple( Blocks.OAK_PLANKS );
        addSimple( Blocks.SPRUCE_PLANKS );
        addSimple( Blocks.BIRCH_PLANKS );
        addSimple( Blocks.JUNGLE_PLANKS );
        addSimple( Blocks.ACACIA_PLANKS );
        addSimple( Blocks.DARK_OAK_PLANKS );

        addSimple( Blocks.OAK_SAPLING );
        addSimple( Blocks.SPRUCE_SAPLING );
        addSimple( Blocks.BIRCH_SAPLING );
        addSimple( Blocks.JUNGLE_SAPLING );
        addSimple( Blocks.ACACIA_SAPLING );
        addSimple( Blocks.DARK_OAK_SAPLING );

        addSimple( Blocks.SAND );
        addSimple( Blocks.RED_SAND );

        addSimple( Blocks.GOLD_ORE );
        addSimple( Blocks.IRON_ORE );

        addSimple( Blocks.OAK_LOG );
        addSimple( Blocks.SPRUCE_LOG );
        addSimple( Blocks.BIRCH_LOG );
        addSimple( Blocks.JUNGLE_LOG );
        addSimple( Blocks.ACACIA_LOG );
        addSimple( Blocks.DARK_OAK_LOG );

        addSimple( Blocks.STRIPPED_SPRUCE_LOG );
        addSimple( Blocks.STRIPPED_BIRCH_LOG );
        addSimple( Blocks.STRIPPED_JUNGLE_LOG );
        addSimple( Blocks.STRIPPED_ACACIA_LOG );
        addSimple( Blocks.STRIPPED_DARK_OAK_LOG );
        addSimple( Blocks.STRIPPED_OAK_LOG );

        addSimple( Blocks.OAK_WOOD );
        addSimple( Blocks.SPRUCE_WOOD );
        addSimple( Blocks.BIRCH_WOOD );
        addSimple( Blocks.JUNGLE_WOOD );
        addSimple( Blocks.ACACIA_WOOD );
        addSimple( Blocks.DARK_OAK_WOOD );

        addSimple( Blocks.STRIPPED_OAK_WOOD );
        addSimple( Blocks.STRIPPED_SPRUCE_WOOD );
        addSimple( Blocks.STRIPPED_BIRCH_WOOD );
        addSimple( Blocks.STRIPPED_JUNGLE_WOOD );
        addSimple( Blocks.STRIPPED_ACACIA_WOOD );
        addSimple( Blocks.STRIPPED_DARK_OAK_WOOD );

        addSimple( Blocks.SPONGE );
        addSimple( Blocks.WET_SPONGE );

        addSimple( Blocks.SANDSTONE );
        addSimple( Blocks.CHISELED_SANDSTONE );
        addSimple( Blocks.CUT_SANDSTONE );

        addSimple( Blocks.NOTE_BLOCK );

        addSimple( Blocks.POWERED_RAIL );
        addSimple( Blocks.DETECTOR_RAIL );

        addSimple( Blocks.STICKY_PISTON );
        addSimple( Blocks.PISTON );

        addSimple( Blocks.WHITE_WOOL );
        addSimple( Blocks.ORANGE_WOOL );
        addSimple( Blocks.MAGENTA_WOOL );
        addSimple( Blocks.LIGHT_BLUE_WOOL );
        addSimple( Blocks.YELLOW_WOOL );
        addSimple( Blocks.LIME_WOOL );
        addSimple( Blocks.PINK_WOOL );
        addSimple( Blocks.GRAY_WOOL );
        addSimple( Blocks.LIGHT_GRAY_WOOL );
        addSimple( Blocks.CYAN_WOOL );
        addSimple( Blocks.PURPLE_WOOL );
        addSimple( Blocks.BLUE_WOOL );
        addSimple( Blocks.BROWN_WOOL );
        addSimple( Blocks.GREEN_WOOL );
        addSimple( Blocks.RED_WOOL );
        addSimple( Blocks.BLACK_WOOL );

        addSimple( Blocks.DANDELION );
        addSimple( Blocks.POPPY );
        addSimple( Blocks.BLUE_ORCHID );
        addSimple( Blocks.ALLIUM );
        addSimple( Blocks.AZURE_BLUET );
        addSimple( Blocks.RED_TULIP );
        addSimple( Blocks.ORANGE_TULIP );
        addSimple( Blocks.WHITE_TULIP );
        addSimple( Blocks.PINK_TULIP );
        addSimple( Blocks.OXEYE_DAISY );
        addSimple( Blocks.CORNFLOWER );
        addSimple( Blocks.WITHER_ROSE );
        addSimple( Blocks.LILY_OF_THE_VALLEY );

        addSimple( Blocks.BROWN_MUSHROOM );
        addSimple( Blocks.RED_MUSHROOM );

        addSimple( Blocks.LAPIS_BLOCK );
        addSimple( Blocks.GOLD_BLOCK );
        addSimple( Blocks.IRON_BLOCK );
        addSimple( Blocks.DIAMOND_BLOCK );

        addSimple( Blocks.BRICKS );

        addSimple( Blocks.OBSIDIAN );

        addSimple( Blocks.TORCH );

        addSimple( Blocks.OAK_STAIRS );

        addSimple( Blocks.REDSTONE_WIRE );

        addSimple( Blocks.CRAFTING_TABLE );

        addSimple( Blocks.OAK_SIGN );
        addSimple( Blocks.SPRUCE_SIGN );
        addSimple( Blocks.BIRCH_SIGN );
        addSimple( Blocks.ACACIA_SIGN );
        addSimple( Blocks.JUNGLE_SIGN );
        addSimple( Blocks.DARK_OAK_SIGN );

        addSimple( Blocks.LADDER );
        addSimple( Blocks.RAIL );
        addSimple( Blocks.COBBLESTONE_STAIRS );
        addSimple( Blocks.LEVER );
        addSimple( Blocks.STONE_PRESSURE_PLATE );
        addSimple( Blocks.OAK_PRESSURE_PLATE );
        addSimple( Blocks.SPRUCE_PRESSURE_PLATE );
        addSimple( Blocks.BIRCH_PRESSURE_PLATE );
        addSimple( Blocks.JUNGLE_PRESSURE_PLATE );
        addSimple( Blocks.ACACIA_PRESSURE_PLATE );
        addSimple( Blocks.DARK_OAK_PRESSURE_PLATE );
        addSimple( Blocks.REDSTONE_TORCH );
        addSimple( Blocks.STONE_BUTTON );
        addSimple( Blocks.CACTUS );
        addSimple( Blocks.SUGAR_CANE );
        addSimple( Blocks.JUKEBOX );
        addSimple( Blocks.OAK_FENCE );
        addSimple( Blocks.PUMPKIN );
        addSimple( Blocks.NETHERRACK );
        addSimple( Blocks.SOUL_SAND );
        addSimple( Blocks.CARVED_PUMPKIN );
        addSimple( Blocks.JACK_O_LANTERN );
        addSimple( Blocks.REPEATER );
        addSimple( Blocks.OAK_TRAPDOOR );
        addSimple( Blocks.SPRUCE_TRAPDOOR );
        addSimple( Blocks.BIRCH_TRAPDOOR );
        addSimple( Blocks.JUNGLE_TRAPDOOR );
        addSimple( Blocks.ACACIA_TRAPDOOR );
        addSimple( Blocks.DARK_OAK_TRAPDOOR );
        addSimple( Blocks.STONE_BRICKS );
        addSimple( Blocks.MOSSY_STONE_BRICKS );
        addSimple( Blocks.CRACKED_STONE_BRICKS );
        addSimple( Blocks.CHISELED_STONE_BRICKS );
        addSimple( Blocks.IRON_BARS );
        addSimple( Blocks.OAK_FENCE_GATE );
        addSimple( Blocks.BRICK_STAIRS );
        addSimple( Blocks.STONE_BRICK_STAIRS );
        addSimple( Blocks.LILY_PAD );
        addSimple( Blocks.NETHER_BRICKS );
        addSimple( Blocks.NETHER_BRICK_FENCE );
        addSimple( Blocks.NETHER_BRICK_STAIRS );
        addSimple( Blocks.CAULDRON );
        addSimple( Blocks.END_STONE );
        addSimple( Blocks.REDSTONE_LAMP );
        addSimple( Blocks.SANDSTONE_STAIRS );
        addSimple( Blocks.TRIPWIRE_HOOK );
        addSimple( Blocks.EMERALD_BLOCK );
        addSimple( Blocks.SPRUCE_STAIRS );
        addSimple( Blocks.BIRCH_STAIRS );
        addSimple( Blocks.JUNGLE_STAIRS );
        addSimple( Blocks.COBBLESTONE_WALL );
        addSimple( Blocks.MOSSY_COBBLESTONE_WALL );
        addSimple( Blocks.FLOWER_POT );
        addSimple( Blocks.OAK_BUTTON );
        addSimple( Blocks.SPRUCE_BUTTON );
        addSimple( Blocks.BIRCH_BUTTON );
        addSimple( Blocks.JUNGLE_BUTTON );
        addSimple( Blocks.ACACIA_BUTTON );
        addSimple( Blocks.DARK_OAK_BUTTON );
        addSimple( Blocks.SKELETON_SKULL );
        addSimple( Blocks.WITHER_SKELETON_SKULL );
        addSimple( Blocks.ZOMBIE_HEAD );
        addSimple( Blocks.CREEPER_HEAD );
        addSimple( Blocks.DRAGON_HEAD );
        addSimple( Blocks.ANVIL );
        addSimple( Blocks.CHIPPED_ANVIL );
        addSimple( Blocks.DAMAGED_ANVIL );
        addSimple( Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE );
        addSimple( Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE );
        addSimple( Blocks.COMPARATOR );
        addSimple( Blocks.DAYLIGHT_DETECTOR );
        addSimple( Blocks.REDSTONE_BLOCK );
        addSimple( Blocks.QUARTZ_BLOCK );
        addSimple( Blocks.CHISELED_QUARTZ_BLOCK );
        addSimple( Blocks.QUARTZ_PILLAR );
        addSimple( Blocks.QUARTZ_STAIRS );
        addSimple( Blocks.ACTIVATOR_RAIL );
        addSimple( Blocks.WHITE_TERRACOTTA );
        addSimple( Blocks.ORANGE_TERRACOTTA );
        addSimple( Blocks.MAGENTA_TERRACOTTA );
        addSimple( Blocks.LIGHT_BLUE_TERRACOTTA );
        addSimple( Blocks.YELLOW_TERRACOTTA );
        addSimple( Blocks.LIME_TERRACOTTA );
        addSimple( Blocks.PINK_TERRACOTTA );
        addSimple( Blocks.GRAY_TERRACOTTA );
        addSimple( Blocks.LIGHT_GRAY_TERRACOTTA );
        addSimple( Blocks.CYAN_TERRACOTTA );
        addSimple( Blocks.PURPLE_TERRACOTTA );
        addSimple( Blocks.BLUE_TERRACOTTA );
        addSimple( Blocks.BROWN_TERRACOTTA );
        addSimple( Blocks.GREEN_TERRACOTTA );
        addSimple( Blocks.RED_TERRACOTTA );
        addSimple( Blocks.BLACK_TERRACOTTA );
        addSimple( Blocks.ACACIA_STAIRS );
        addSimple( Blocks.DARK_OAK_STAIRS );
        addSimple( Blocks.SLIME_BLOCK );
        addSimple( Blocks.IRON_TRAPDOOR );
        addSimple( Blocks.PRISMARINE );
        addSimple( Blocks.PRISMARINE_BRICKS );
        addSimple( Blocks.DARK_PRISMARINE );
        addSimple( Blocks.PRISMARINE_STAIRS );
        addSimple( Blocks.PRISMARINE_BRICK_STAIRS );
        addSimple( Blocks.DARK_PRISMARINE_STAIRS );
        addSimple( Blocks.HAY_BLOCK );
        addSimple( Blocks.WHITE_CARPET );
        addSimple( Blocks.ORANGE_CARPET );
        addSimple( Blocks.MAGENTA_CARPET );
        addSimple( Blocks.LIGHT_BLUE_CARPET );
        addSimple( Blocks.YELLOW_CARPET );
        addSimple( Blocks.LIME_CARPET );
        addSimple( Blocks.PINK_CARPET );
        addSimple( Blocks.GRAY_CARPET );
        addSimple( Blocks.LIGHT_GRAY_CARPET );
        addSimple( Blocks.CYAN_CARPET );
        addSimple( Blocks.PURPLE_CARPET );
        addSimple( Blocks.BLUE_CARPET );
        addSimple( Blocks.BROWN_CARPET );
        addSimple( Blocks.GREEN_CARPET );
        addSimple( Blocks.RED_CARPET );
        addSimple( Blocks.BLACK_CARPET );
        addSimple( Blocks.TERRACOTTA );
        addSimple( Blocks.COAL_BLOCK );
        addSimple( Blocks.RED_SANDSTONE );
        addSimple( Blocks.CHISELED_RED_SANDSTONE );
        addSimple( Blocks.CUT_RED_SANDSTONE );
        addSimple( Blocks.RED_SANDSTONE_STAIRS );
        addSimple( Blocks.SMOOTH_STONE );
        addSimple( Blocks.SMOOTH_SANDSTONE );
        addSimple( Blocks.SMOOTH_QUARTZ );
        addSimple( Blocks.SMOOTH_RED_SANDSTONE );
        addSimple( Blocks.SPRUCE_FENCE_GATE );
        addSimple( Blocks.BIRCH_FENCE_GATE );
        addSimple( Blocks.JUNGLE_FENCE_GATE );
        addSimple( Blocks.ACACIA_FENCE_GATE );
        addSimple( Blocks.DARK_OAK_FENCE_GATE );
        addSimple( Blocks.SPRUCE_FENCE );
        addSimple( Blocks.BIRCH_FENCE );
        addSimple( Blocks.JUNGLE_FENCE );
        addSimple( Blocks.ACACIA_FENCE );
        addSimple( Blocks.DARK_OAK_FENCE );
        addSimple( Blocks.END_ROD );
        addSimple( Blocks.PURPUR_BLOCK );
        addSimple( Blocks.PURPUR_PILLAR );
        addSimple( Blocks.PURPUR_STAIRS );
        addSimple( Blocks.END_STONE_BRICKS );
        addSimple( Blocks.MAGMA_BLOCK );
        addSimple( Blocks.NETHER_WART_BLOCK );
        addSimple( Blocks.RED_NETHER_BRICKS );
        addSimple( Blocks.BONE_BLOCK );
        addSimple( Blocks.OBSERVER );
        addSimple( Blocks.WHITE_GLAZED_TERRACOTTA );
        addSimple( Blocks.ORANGE_GLAZED_TERRACOTTA );
        addSimple( Blocks.MAGENTA_GLAZED_TERRACOTTA );
        addSimple( Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA );
        addSimple( Blocks.YELLOW_GLAZED_TERRACOTTA );
        addSimple( Blocks.LIME_GLAZED_TERRACOTTA );
        addSimple( Blocks.PINK_GLAZED_TERRACOTTA );
        addSimple( Blocks.GRAY_GLAZED_TERRACOTTA );
        addSimple( Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA );
        addSimple( Blocks.CYAN_GLAZED_TERRACOTTA );
        addSimple( Blocks.PURPLE_GLAZED_TERRACOTTA );
        addSimple( Blocks.BLUE_GLAZED_TERRACOTTA );
        addSimple( Blocks.BROWN_GLAZED_TERRACOTTA );
        addSimple( Blocks.GREEN_GLAZED_TERRACOTTA );
        addSimple( Blocks.RED_GLAZED_TERRACOTTA );
        addSimple( Blocks.BLACK_GLAZED_TERRACOTTA );
        addSimple( Blocks.WHITE_CONCRETE );
        addSimple( Blocks.ORANGE_CONCRETE );
        addSimple( Blocks.MAGENTA_CONCRETE );
        addSimple( Blocks.LIGHT_BLUE_CONCRETE );
        addSimple( Blocks.YELLOW_CONCRETE );
        addSimple( Blocks.LIME_CONCRETE );
        addSimple( Blocks.PINK_CONCRETE );
        addSimple( Blocks.GRAY_CONCRETE );
        addSimple( Blocks.LIGHT_GRAY_CONCRETE );
        addSimple( Blocks.CYAN_CONCRETE );
        addSimple( Blocks.PURPLE_CONCRETE );
        addSimple( Blocks.BLUE_CONCRETE );
        addSimple( Blocks.BROWN_CONCRETE );
        addSimple( Blocks.GREEN_CONCRETE );
        addSimple( Blocks.RED_CONCRETE );
        addSimple( Blocks.BLACK_CONCRETE );
        addSimple( Blocks.WHITE_CONCRETE_POWDER );
        addSimple( Blocks.ORANGE_CONCRETE_POWDER );
        addSimple( Blocks.MAGENTA_CONCRETE_POWDER );
        addSimple( Blocks.LIGHT_BLUE_CONCRETE_POWDER );
        addSimple( Blocks.YELLOW_CONCRETE_POWDER );
        addSimple( Blocks.LIME_CONCRETE_POWDER );
        addSimple( Blocks.PINK_CONCRETE_POWDER );
        addSimple( Blocks.GRAY_CONCRETE_POWDER );
        addSimple( Blocks.LIGHT_GRAY_CONCRETE_POWDER );
        addSimple( Blocks.CYAN_CONCRETE_POWDER );
        addSimple( Blocks.PURPLE_CONCRETE_POWDER );
        addSimple( Blocks.BLUE_CONCRETE_POWDER );
        addSimple( Blocks.BROWN_CONCRETE_POWDER );
        addSimple( Blocks.GREEN_CONCRETE_POWDER );
        addSimple( Blocks.RED_CONCRETE_POWDER );
        addSimple( Blocks.BLACK_CONCRETE_POWDER );
        addSimple( Blocks.KELP );
        addSimple( Blocks.DRIED_KELP_BLOCK );
        addSimple( Blocks.DEAD_TUBE_CORAL_BLOCK );
        addSimple( Blocks.DEAD_BRAIN_CORAL_BLOCK );
        addSimple( Blocks.DEAD_BUBBLE_CORAL_BLOCK );
        addSimple( Blocks.DEAD_FIRE_CORAL_BLOCK );
        addSimple( Blocks.DEAD_HORN_CORAL_BLOCK );
        addSimple( Blocks.CONDUIT );
        addSimple( Blocks.DRAGON_EGG );
        addSimple( Blocks.BAMBOO );
        addSimple( Blocks.POLISHED_GRANITE_STAIRS );
        addSimple( Blocks.SMOOTH_RED_SANDSTONE_STAIRS );
        addSimple( Blocks.MOSSY_STONE_BRICK_STAIRS );
        addSimple( Blocks.POLISHED_DIORITE_STAIRS );
        addSimple( Blocks.MOSSY_COBBLESTONE_STAIRS );
        addSimple( Blocks.END_STONE_BRICK_STAIRS );
        addSimple( Blocks.STONE_STAIRS );
        addSimple( Blocks.SMOOTH_SANDSTONE_STAIRS );
        addSimple( Blocks.SMOOTH_QUARTZ_STAIRS );
        addSimple( Blocks.GRANITE_STAIRS );
        addSimple( Blocks.ANDESITE_STAIRS );
        addSimple( Blocks.RED_NETHER_BRICK_STAIRS );
        addSimple( Blocks.POLISHED_ANDESITE_STAIRS );
        addSimple( Blocks.DIORITE_STAIRS );
        addSimple( Blocks.BRICK_WALL );
        addSimple( Blocks.PRISMARINE_WALL );
        addSimple( Blocks.RED_SANDSTONE_WALL );
        addSimple( Blocks.MOSSY_STONE_BRICK_WALL );
        addSimple( Blocks.GRANITE_WALL );
        addSimple( Blocks.STONE_BRICK_WALL );
        addSimple( Blocks.NETHER_BRICK_WALL );
        addSimple( Blocks.ANDESITE_WALL );
        addSimple( Blocks.RED_NETHER_BRICK_WALL );
        addSimple( Blocks.SANDSTONE_WALL );
        addSimple( Blocks.END_STONE_BRICK_WALL );
        addSimple( Blocks.DIORITE_WALL );
        addSimple( Blocks.LOOM );
        addSimple( Blocks.SCAFFOLDING );
        addSeparateItem( Blocks.FARMLAND, Blocks.DIRT );
        addSeparateItem( Blocks.TRIPWIRE, Items.STRING );
        addSeparateItem( Blocks.GRASS_PATH, Blocks.DIRT );
        addSeparateItem( Blocks.KELP_PLANT, Blocks.KELP );
        addSeparateItem( Blocks.BAMBOO_SAPLING, Blocks.BAMBOO );
        registerLootTable( Blocks.STONE, block -> silkTouchOrItem( block, Blocks.COBBLESTONE ) );
        this.registerLootTable( Blocks.GRASS_BLOCK, p_218529_0_ -> {
            return silkTouchOrItem( p_218529_0_, Blocks.DIRT );
        } );
        this.registerLootTable( Blocks.PODZOL, p_218514_0_ -> {
            return silkTouchOrItem( p_218514_0_, Blocks.DIRT );
        } );
        this.registerLootTable( Blocks.MYCELIUM, p_218501_0_ -> {
            return silkTouchOrItem( p_218501_0_, Blocks.DIRT );
        } );
        this.registerLootTable( Blocks.TUBE_CORAL_BLOCK, p_218539_0_ -> {
            return silkTouchOrItem( p_218539_0_, Blocks.DEAD_TUBE_CORAL_BLOCK );
        } );
        this.registerLootTable( Blocks.BRAIN_CORAL_BLOCK, p_218462_0_ -> {
            return silkTouchOrItem( p_218462_0_, Blocks.DEAD_BRAIN_CORAL_BLOCK );
        } );
        this.registerLootTable( Blocks.BUBBLE_CORAL_BLOCK, p_218505_0_ -> {
            return silkTouchOrItem( p_218505_0_, Blocks.DEAD_BUBBLE_CORAL_BLOCK );
        } );
        this.registerLootTable( Blocks.FIRE_CORAL_BLOCK, p_218499_0_ -> {
            return silkTouchOrItem( p_218499_0_, Blocks.DEAD_FIRE_CORAL_BLOCK );
        } );
        this.registerLootTable( Blocks.HORN_CORAL_BLOCK, p_218502_0_ -> {
            return silkTouchOrItem( p_218502_0_, Blocks.DEAD_HORN_CORAL_BLOCK );
        } );
        this.registerLootTable( Blocks.BOOKSHELF, p_218534_0_ -> {
            return silkTouchOrItem( p_218534_0_, Items.BOOK, ConstantRange.of( 3 ) );
        } );
        this.registerLootTable( Blocks.CLAY, p_218465_0_ -> {
            return silkTouchOrItem( p_218465_0_, Items.CLAY_BALL, ConstantRange.of( 4 ) );
        } );
        this.registerLootTable( Blocks.ENDER_CHEST, p_218558_0_ -> {
            return silkTouchOrItem( p_218558_0_, Blocks.OBSIDIAN, ConstantRange.of( 8 ) );
        } );
        this.registerLootTable( Blocks.SNOW_BLOCK, p_218556_0_ -> {
            return silkTouchOrItem( p_218556_0_, Items.SNOWBALL, ConstantRange.of( 4 ) );
        } );
        this.registerLootTable( Blocks.CHORUS_PLANT, item( Items.CHORUS_FRUIT, RandomValueRange.func_215837_a( 0.0F, 1.0F ) ) );
        this.addFlowerPot( Blocks.POTTED_OAK_SAPLING );
        this.addFlowerPot( Blocks.POTTED_SPRUCE_SAPLING );
        this.addFlowerPot( Blocks.POTTED_BIRCH_SAPLING );
        this.addFlowerPot( Blocks.POTTED_JUNGLE_SAPLING );
        this.addFlowerPot( Blocks.POTTED_ACACIA_SAPLING );
        this.addFlowerPot( Blocks.POTTED_DARK_OAK_SAPLING );
        this.addFlowerPot( Blocks.POTTED_FERN );
        this.addFlowerPot( Blocks.POTTED_DANDELION );
        this.addFlowerPot( Blocks.POTTED_POPPY );
        this.addFlowerPot( Blocks.POTTED_BLUE_ORCHID );
        this.addFlowerPot( Blocks.POTTED_ALLIUM );
        this.addFlowerPot( Blocks.POTTED_AZURE_BLUET );
        this.addFlowerPot( Blocks.POTTED_RED_TULIP );
        this.addFlowerPot( Blocks.POTTED_ORANGE_TULIP );
        this.addFlowerPot( Blocks.POTTED_WHITE_TULIP );
        this.addFlowerPot( Blocks.POTTED_PINK_TULIP );
        this.addFlowerPot( Blocks.POTTED_OXEYE_DAISY );
        this.addFlowerPot( Blocks.POTTED_CORNFLOWER );
        this.addFlowerPot( Blocks.POTTED_LILY_OF_THE_VALLEY );
        this.addFlowerPot( Blocks.POTTED_WITHER_ROSE );
        this.addFlowerPot( Blocks.POTTED_RED_MUSHROOM );
        this.addFlowerPot( Blocks.POTTED_BROWN_MUSHROOM );
        this.addFlowerPot( Blocks.POTTED_DEAD_BUSH );
        this.addFlowerPot( Blocks.POTTED_CACTUS );
        this.addFlowerPot( Blocks.POTTED_BAMBOO );
        this.registerLootTable( Blocks.ACACIA_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.BIRCH_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.BRICK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.COBBLESTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.DARK_OAK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.DARK_PRISMARINE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.JUNGLE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.NETHER_BRICK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.OAK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.PETRIFIED_OAK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.PRISMARINE_BRICK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.PRISMARINE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.PURPUR_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.QUARTZ_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.RED_SANDSTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.SANDSTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.CUT_RED_SANDSTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.CUT_SANDSTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.SPRUCE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.STONE_BRICK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.STONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.SMOOTH_STONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.POLISHED_GRANITE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.SMOOTH_RED_SANDSTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.MOSSY_STONE_BRICK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.POLISHED_DIORITE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.MOSSY_COBBLESTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.END_STONE_BRICK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.SMOOTH_SANDSTONE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.SMOOTH_QUARTZ_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.GRANITE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.ANDESITE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.RED_NETHER_BRICK_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.POLISHED_ANDESITE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.DIORITE_SLAB, BlockLootTables::slab );
        this.registerLootTable( Blocks.ACACIA_DOOR, p_218483_0_ -> {
            return blockProperty( p_218483_0_, DoorBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.BIRCH_DOOR, p_218528_0_ -> {
            return blockProperty( p_218528_0_, DoorBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.DARK_OAK_DOOR, p_218468_0_ -> {
            return blockProperty( p_218468_0_, DoorBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.IRON_DOOR, p_218510_0_ -> {
            return blockProperty( p_218510_0_, DoorBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.JUNGLE_DOOR, p_218498_0_ -> {
            return blockProperty( p_218498_0_, DoorBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.OAK_DOOR, p_218480_0_ -> {
            return blockProperty( p_218480_0_, DoorBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.SPRUCE_DOOR, p_218527_0_ -> {
            return blockProperty( p_218527_0_, DoorBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.BLACK_BED, p_218567_0_ -> {
            return blockProperty( p_218567_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.BLUE_BED, p_218555_0_ -> {
            return blockProperty( p_218555_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.BROWN_BED, p_218543_0_ -> {
            return blockProperty( p_218543_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.CYAN_BED, p_218479_0_ -> {
            return blockProperty( p_218479_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.GRAY_BED, p_218521_0_ -> {
            return blockProperty( p_218521_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.GREEN_BED, p_218470_0_ -> {
            return blockProperty( p_218470_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.LIGHT_BLUE_BED, p_218536_0_ -> {
            return blockProperty( p_218536_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.LIGHT_GRAY_BED, p_218545_0_ -> {
            return blockProperty( p_218545_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.LIME_BED, p_218557_0_ -> {
            return blockProperty( p_218557_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.MAGENTA_BED, p_218566_0_ -> {
            return blockProperty( p_218566_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.PURPLE_BED, p_218520_0_ -> {
            return blockProperty( p_218520_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.ORANGE_BED, p_218472_0_ -> {
            return blockProperty( p_218472_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.PINK_BED, p_218537_0_ -> {
            return blockProperty( p_218537_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.RED_BED, p_218549_0_ -> {
            return blockProperty( p_218549_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.WHITE_BED, p_218569_0_ -> {
            return blockProperty( p_218569_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.YELLOW_BED, p_218517_0_ -> {
            return blockProperty( p_218517_0_, BedBlock.PART, BedPart.HEAD );
        } );
        this.registerLootTable( Blocks.LILAC, p_218488_0_ -> {
            return blockProperty( p_218488_0_, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.SUNFLOWER, p_218503_0_ -> {
            return blockProperty( p_218503_0_, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.PEONY, p_218497_0_ -> {
            return blockProperty( p_218497_0_, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.ROSE_BUSH, p_218504_0_ -> {
            return blockProperty( p_218504_0_, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER );
        } );
        this.registerLootTable( Blocks.TNT, p_218516_0_ -> {
            return blockProperty( p_218516_0_, TNTBlock.UNSTABLE, false );
        } );
        this.registerLootTable( Blocks.COCOA, p_218478_0_ -> {
            return LootTable.builder().addLootPool( LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( explosionFunc( p_218478_0_, ItemLootEntry.builder( Items.COCOA_BEANS ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( 3 ) ).acceptCondition( BlockStateProperty.builder( p_218478_0_ ).with( CocoaBlock.AGE, 2 ) ) ) ) ) );
        } );
        this.registerLootTable( Blocks.SEA_PICKLE, p_218551_0_ -> {
            return LootTable.builder().addLootPool( LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( explosionFunc( p_218551_0_, ItemLootEntry.builder( p_218551_0_ ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( 2 ) ).acceptCondition( BlockStateProperty.builder( p_218551_0_ ).with( SeaPickleBlock.PICKLES, 2 ) ) ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( 3 ) ).acceptCondition( BlockStateProperty.builder( p_218551_0_ ).with( SeaPickleBlock.PICKLES, 3 ) ) ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( 4 ) ).acceptCondition( BlockStateProperty.builder( p_218551_0_ ).with( SeaPickleBlock.PICKLES, 4 ) ) ) ) ) );
        } );
        this.registerLootTable( Blocks.COMPOSTER, p_218565_0_ -> {
            return LootTable.builder().addLootPool( LootPool.builder().addEntry( explosionFunc( p_218565_0_, ItemLootEntry.builder( Items.COMPOSTER ) ) ) ).addLootPool( LootPool.builder().addEntry( ItemLootEntry.builder( Items.BONE_MEAL ) ).acceptCondition( BlockStateProperty.builder( p_218565_0_ ).with( ComposterBlock.field_220298_a, 8 ) ) );
        } );
        this.registerLootTable( Blocks.BEACON, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.BREWING_STAND, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.CHEST, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.DISPENSER, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.DROPPER, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.ENCHANTING_TABLE, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.FURNACE, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.HOPPER, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.TRAPPED_CHEST, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.SMOKER, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.BLAST_FURNACE, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.BARREL, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.CARTOGRAPHY_TABLE, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.FLETCHING_TABLE, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.GRINDSTONE, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.LECTERN, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.SMITHING_TABLE, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.STONECUTTER, BlockLootTables::copyTileEntityName );
        this.registerLootTable( Blocks.BELL, BlockLootTables::simple );
        this.registerLootTable( Blocks.LANTERN, BlockLootTables::simple );
        this.registerLootTable( Blocks.SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.BLACK_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.BLUE_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.BROWN_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.CYAN_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.GRAY_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.GREEN_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.LIGHT_BLUE_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.LIGHT_GRAY_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.LIME_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.MAGENTA_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.ORANGE_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.PINK_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.PURPLE_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.RED_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.WHITE_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.YELLOW_SHULKER_BOX, BlockLootTables::shulkerBox );
        this.registerLootTable( Blocks.BLACK_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.BLUE_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.BROWN_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.CYAN_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.GRAY_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.GREEN_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.LIGHT_BLUE_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.LIGHT_GRAY_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.LIME_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.MAGENTA_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.ORANGE_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.PINK_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.PURPLE_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.RED_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.WHITE_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.YELLOW_BANNER, BlockLootTables::banner );
        this.registerLootTable( Blocks.PLAYER_HEAD, p_218473_0_ -> LootTable.builder().addLootPool( explosionCond( p_218473_0_, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218473_0_ ).acceptFunction( CopyNbt.func_215881_a( CopyNbt.Source.BLOCK_ENTITY ).func_216056_a( "Owner", "SkullOwner" ) ) ) ) ) );
        this.registerLootTable( Blocks.BIRCH_LEAVES, p_218518_0_ -> leaves( p_218518_0_, Blocks.BIRCH_SAPLING, LEAVES_TABLE ) );
        this.registerLootTable( Blocks.ACACIA_LEAVES, p_218477_0_ -> leaves( p_218477_0_, Blocks.ACACIA_SAPLING, LEAVES_TABLE ) );
        this.registerLootTable( Blocks.JUNGLE_LEAVES, p_218500_0_ -> leaves( p_218500_0_, Blocks.JUNGLE_SAPLING, JUNGLE_LEAVES_TABLE ) );
        this.registerLootTable( Blocks.SPRUCE_LEAVES, p_218506_0_ -> leaves( p_218506_0_, Blocks.SPRUCE_SAPLING, LEAVES_TABLE ) );
        this.registerLootTable( Blocks.OAK_LEAVES, p_218471_0_ -> appleLeaves( p_218471_0_, Blocks.OAK_SAPLING, LEAVES_TABLE ) );
        this.registerLootTable( Blocks.DARK_OAK_LEAVES, p_218538_0_ -> appleLeaves( p_218538_0_, Blocks.DARK_OAK_SAPLING, LEAVES_TABLE ) );

        ILootCondition.IBuilder ilootcondition$ibuilder = BlockStateProperty.builder( Blocks.BEETROOTS ).with( BeetrootBlock.BEETROOT_AGE, 3 );
        this.registerLootTable( Blocks.BEETROOTS, p_218474_1_ -> {
            return seedCrops( p_218474_1_, Items.BEETROOT, Items.BEETROOT_SEEDS, ilootcondition$ibuilder );
        } );
        ILootCondition.IBuilder ilootcondition$ibuilder1 = BlockStateProperty.builder( Blocks.WHEAT ).with( CropsBlock.AGE, 7 );
        this.registerLootTable( Blocks.WHEAT, p_218489_1_ -> {
            return seedCrops( p_218489_1_, Items.WHEAT, Items.WHEAT_SEEDS, ilootcondition$ibuilder1 );
        } );
        ILootCondition.IBuilder ilootcondition$ibuilder2 = BlockStateProperty.builder( Blocks.CARROTS ).with( CarrotBlock.AGE, 7 );
        this.registerLootTable( Blocks.CARROTS, p_218542_1_ -> {
            return explosionFunc( p_218542_1_, LootTable.builder().addLootPool( LootPool.builder().addEntry( ItemLootEntry.builder( Items.CARROT ) ) ).addLootPool( LootPool.builder().acceptCondition( ilootcondition$ibuilder2 ).addEntry( ItemLootEntry.builder( Items.CARROT ).acceptFunction( ApplyBonus.func_215870_a( Enchantments.FORTUNE, 0.5714286F, 3 ) ) ) ) );
        } );
        ILootCondition.IBuilder ilootcondition$ibuilder3 = BlockStateProperty.builder( Blocks.POTATOES ).with( PotatoBlock.AGE, 7 );
        this.registerLootTable( Blocks.POTATOES, p_218563_1_ -> {
            return explosionFunc( p_218563_1_, LootTable.builder().addLootPool( LootPool.builder().addEntry( ItemLootEntry.builder( Items.POTATO ) ) ).addLootPool( LootPool.builder().acceptCondition( ilootcondition$ibuilder3 ).addEntry( ItemLootEntry.builder( Items.POTATO ).acceptFunction( ApplyBonus.func_215870_a( Enchantments.FORTUNE, 0.5714286F, 3 ) ) ) ).addLootPool( LootPool.builder().acceptCondition( ilootcondition$ibuilder3 ).addEntry( ItemLootEntry.builder( Items.POISONOUS_POTATO ).acceptCondition( RandomChance.builder( 0.02F ) ) ) ) );
        } );
        this.registerLootTable( Blocks.SWEET_BERRY_BUSH, p_218554_0_ -> {
            return explosionFunc( p_218554_0_, LootTable.builder().addLootPool( LootPool.builder().acceptCondition( BlockStateProperty.builder( Blocks.SWEET_BERRY_BUSH ).with( SweetBerryBushBlock.AGE, 3 ) ).addEntry( ItemLootEntry.builder( Items.SWEET_BERRIES ) ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 2.0F, 3.0F ) ) ).acceptFunction( ApplyBonus.func_215871_b( Enchantments.FORTUNE ) ) ).addLootPool( LootPool.builder().acceptCondition( BlockStateProperty.builder( Blocks.SWEET_BERRY_BUSH ).with( SweetBerryBushBlock.AGE, 2 ) ).addEntry( ItemLootEntry.builder( Items.SWEET_BERRIES ) ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 1.0F, 2.0F ) ) ).acceptFunction( ApplyBonus.func_215871_b( Enchantments.FORTUNE ) ) ) );
        } );
        this.registerLootTable( Blocks.BROWN_MUSHROOM_BLOCK, p_218568_0_ -> {
            return mushroomBlock( p_218568_0_, Blocks.BROWN_MUSHROOM );
        } );
        this.registerLootTable( Blocks.RED_MUSHROOM_BLOCK, p_218548_0_ -> {
            return mushroomBlock( p_218548_0_, Blocks.RED_MUSHROOM );
        } );
        this.registerLootTable( Blocks.COAL_ORE, p_218487_0_ -> {
            return ore( p_218487_0_, Items.COAL );
        } );
        this.registerLootTable( Blocks.EMERALD_ORE, p_218525_0_ -> {
            return ore( p_218525_0_, Items.EMERALD );
        } );
        this.registerLootTable( Blocks.NETHER_QUARTZ_ORE, p_218572_0_ -> {
            return ore( p_218572_0_, Items.QUARTZ );
        } );
        this.registerLootTable( Blocks.DIAMOND_ORE, p_218550_0_ -> {
            return ore( p_218550_0_, Items.DIAMOND );
        } );
        this.registerLootTable( Blocks.LAPIS_ORE, p_218531_0_ -> {
            return silkTouch( p_218531_0_, explosionFunc( p_218531_0_, ItemLootEntry.builder( Items.LAPIS_LAZULI ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 4.0F, 9.0F ) ) ).acceptFunction( ApplyBonus.func_215869_a( Enchantments.FORTUNE ) ) ) );
        } );
        this.registerLootTable( Blocks.COBWEB, p_218467_0_ -> {
            return silkTouchOrShears( p_218467_0_, explosionCond( p_218467_0_, ItemLootEntry.builder( Items.STRING ) ) );
        } );
        this.registerLootTable( Blocks.DEAD_BUSH, p_218509_0_ -> {
            return shears( p_218509_0_, explosionFunc( p_218509_0_, ItemLootEntry.builder( Items.STICK ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 0.0F, 2.0F ) ) ) ) );
        } );
        this.registerLootTable( Blocks.SEAGRASS, BlockLootTables::shearable );
        this.registerLootTable( Blocks.VINE, BlockLootTables::shearable );
        this.registerLootTable( Blocks.TALL_SEAGRASS, shearable( Blocks.SEAGRASS ) );
        this.registerLootTable( Blocks.LARGE_FERN, shearable( Blocks.FERN ) );
        this.registerLootTable( Blocks.TALL_GRASS, p_218512_0_ -> {
            return shears( Blocks.GRASS, explosionCond( p_218512_0_, ItemLootEntry.builder( Items.WHEAT_SEEDS ) ).acceptCondition( BlockStateProperty.builder( p_218512_0_ ).with( DoublePlantBlock.HALF, DoubleBlockHalf.LOWER ) ).acceptCondition( RandomChance.builder( 0.125F ) ) );
        } );
        this.registerLootTable( Blocks.MELON_STEM, p_218496_0_ -> {
            return stemCrop( p_218496_0_, Items.MELON_SEEDS );
        } );
        this.registerLootTable( Blocks.PUMPKIN_STEM, p_218532_0_ -> {
            return stemCrop( p_218532_0_, Items.PUMPKIN_SEEDS );
        } );
        this.registerLootTable( Blocks.CHORUS_FLOWER, p_218464_0_ -> {
            return LootTable.builder().addLootPool( LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( explosionCond( p_218464_0_, ItemLootEntry.builder( p_218464_0_ ) ).acceptCondition( EntityHasProperty.func_215998_a( LootContext.EntityTarget.THIS ) ) ) );
        } );
        this.registerLootTable( Blocks.FERN, BlockLootTables::seeds );
        this.registerLootTable( Blocks.GRASS, BlockLootTables::seeds );
        this.registerLootTable( Blocks.GLOWSTONE, p_218571_0_ -> {
            return silkTouch( p_218571_0_, explosionFunc( p_218571_0_, ItemLootEntry.builder( Items.GLOWSTONE_DUST ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 2.0F, 4.0F ) ) ).acceptFunction( ApplyBonus.func_215871_b( Enchantments.FORTUNE ) ).acceptFunction( LimitCount.func_215911_a( IntClamper.func_215843_a( 1, 4 ) ) ) ) );
        } );
        this.registerLootTable( Blocks.MELON, p_218553_0_ -> {
            return silkTouch( p_218553_0_, explosionFunc( p_218553_0_, ItemLootEntry.builder( Items.MELON_SLICE ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 3.0F, 7.0F ) ) ).acceptFunction( ApplyBonus.func_215871_b( Enchantments.FORTUNE ) ).acceptFunction( LimitCount.func_215911_a( IntClamper.func_215851_b( 9 ) ) ) ) );
        } );
        this.registerLootTable( Blocks.REDSTONE_ORE, p_218485_0_ -> {
            return silkTouch( p_218485_0_, explosionFunc( p_218485_0_, ItemLootEntry.builder( Items.REDSTONE ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 4.0F, 5.0F ) ) ).acceptFunction( ApplyBonus.func_215871_b( Enchantments.FORTUNE ) ) ) );
        } );
        registerLootTable( Blocks.SEA_LANTERN, p_218533_0_ -> silkTouch( p_218533_0_, explosionFunc( p_218533_0_, ItemLootEntry.builder( Items.PRISMARINE_CRYSTALS ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 2.0F, 3.0F ) ) ).acceptFunction( ApplyBonus.func_215871_b( Enchantments.FORTUNE ) ).acceptFunction( LimitCount.func_215911_a( IntClamper.func_215843_a( 1, 5 ) ) ) ) ) );
        registerLootTable(
            Blocks.NETHER_WART,
            block -> LootTable.builder().addLootPool( explosionFunc(
                block,
                LootPool.builder()
                        .rolls( ConstantRange.of( 1 ) )
                        .name( "wart" )
                        .addEntry(
                            ItemLootEntry.builder( Items.NETHER_WART )
                                         .acceptFunction(
                                             SetCount.func_215932_a( RandomValueRange.func_215837_a( 2, 4 ) )
                                                     .acceptCondition( BlockStateProperty.builder( block ).with( NetherWartBlock.AGE, 3 ) )
                                         )
                                         .acceptFunction(
                                             ApplyBonus.func_215871_b( Enchantments.FORTUNE )
                                                       .acceptCondition( BlockStateProperty.builder( block ).with( NetherWartBlock.AGE, 3 ) )
                                         )
                        )
            ) )
        );
        registerLootTable(
            Blocks.SNOW,
            block -> LootTable.builder().addLootPool(
                LootPool.builder()
                        .name( "snow" )
                        .acceptCondition( EntityHasProperty.func_215998_a( LootContext.EntityTarget.THIS ) )
                        .addEntry(
                            AlternativesLootEntry.func_216149_a(
                                AlternativesLootEntry.func_216149_a(
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 1 ) ),
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 2 ) )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 2 ) ) ),
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 3 ) )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 3 ) ) ),
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 4 ) )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 4 ) ) ),
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 5 ) )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 5 ) ) ),
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 6 ) )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 6 ) ) ),
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 7 ) )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 7 ) ) ),
                                    ItemLootEntry.builder( Items.SNOWBALL )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 8 ) ) )
                                ).acceptCondition( NO_SILK_TOUCH ),
                                AlternativesLootEntry.func_216149_a(
                                    ItemLootEntry.builder( block )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 1 ) ),
                                    ItemLootEntry.builder( block )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 2 ) ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 2 ) ),
                                    ItemLootEntry.builder( block )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 3 ) ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 3 ) ),
                                    ItemLootEntry.builder( block )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 4 ) ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 4 ) ),
                                    ItemLootEntry.builder( block )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 5 ) ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 5 ) ),
                                    ItemLootEntry.builder( block )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 6 ) ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 6 ) ),
                                    ItemLootEntry.builder( block )
                                                 .acceptFunction( SetCount.func_215932_a( ConstantRange.of( 7 ) ) )
                                                 .acceptCondition( BlockStateProperty.builder( block ).with( SnowBlock.LAYERS, 7 ) ),
                                    ItemLootEntry.builder( Blocks.SNOW_BLOCK )
                                )
                            )
                        )
            )
        );
        registerLootTable( Blocks.GRAVEL, block -> silkTouch( block, explosionCond( block, ItemLootEntry.builder( Items.FLINT ).acceptCondition( TableBonus.builder( Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F ) ).func_216080_a( ItemLootEntry.builder( block ) ) ) ) );
        registerLootTable( Blocks.CAMPFIRE, block -> silkTouch( block, explosionCond( block, ItemLootEntry.builder( Items.CHARCOAL ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( 2 ) ) ) ) ) );

        addSilkTouch( Blocks.GLASS );
        addSilkTouch( Blocks.WHITE_STAINED_GLASS );
        addSilkTouch( Blocks.ORANGE_STAINED_GLASS );
        addSilkTouch( Blocks.MAGENTA_STAINED_GLASS );
        addSilkTouch( Blocks.LIGHT_BLUE_STAINED_GLASS );
        addSilkTouch( Blocks.YELLOW_STAINED_GLASS );
        addSilkTouch( Blocks.LIME_STAINED_GLASS );
        addSilkTouch( Blocks.PINK_STAINED_GLASS );
        addSilkTouch( Blocks.GRAY_STAINED_GLASS );
        addSilkTouch( Blocks.LIGHT_GRAY_STAINED_GLASS );
        addSilkTouch( Blocks.CYAN_STAINED_GLASS );
        addSilkTouch( Blocks.PURPLE_STAINED_GLASS );
        addSilkTouch( Blocks.BLUE_STAINED_GLASS );
        addSilkTouch( Blocks.BROWN_STAINED_GLASS );
        addSilkTouch( Blocks.GREEN_STAINED_GLASS );
        addSilkTouch( Blocks.RED_STAINED_GLASS );
        addSilkTouch( Blocks.BLACK_STAINED_GLASS );

        addSilkTouch( Blocks.GLASS_PANE );
        addSilkTouch( Blocks.WHITE_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.ORANGE_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.MAGENTA_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.LIGHT_BLUE_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.YELLOW_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.LIME_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.PINK_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.GRAY_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.LIGHT_GRAY_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.CYAN_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.PURPLE_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.BLUE_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.BROWN_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.GREEN_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.RED_STAINED_GLASS_PANE );
        addSilkTouch( Blocks.BLACK_STAINED_GLASS_PANE );

        addSilkTouch( Blocks.ICE );
        addSilkTouch( Blocks.PACKED_ICE );
        addSilkTouch( Blocks.BLUE_ICE );

        addSilkTouch( Blocks.TURTLE_EGG );

        addSilkTouch( Blocks.MUSHROOM_STEM );
        addSilkTouch( Blocks.DEAD_TUBE_CORAL );
        addSilkTouch( Blocks.DEAD_BRAIN_CORAL );
        addSilkTouch( Blocks.DEAD_BUBBLE_CORAL );
        addSilkTouch( Blocks.DEAD_FIRE_CORAL );
        addSilkTouch( Blocks.DEAD_HORN_CORAL );
        addSilkTouch( Blocks.TUBE_CORAL );
        addSilkTouch( Blocks.BRAIN_CORAL );
        addSilkTouch( Blocks.BUBBLE_CORAL );
        addSilkTouch( Blocks.FIRE_CORAL );
        addSilkTouch( Blocks.HORN_CORAL );

        addSilkTouch( Blocks.DEAD_TUBE_CORAL_FAN );
        addSilkTouch( Blocks.DEAD_BRAIN_CORAL_FAN );
        addSilkTouch( Blocks.DEAD_BUBBLE_CORAL_FAN );
        addSilkTouch( Blocks.DEAD_FIRE_CORAL_FAN );
        addSilkTouch( Blocks.DEAD_HORN_CORAL_FAN );
        addSilkTouch( Blocks.TUBE_CORAL_FAN );
        addSilkTouch( Blocks.BRAIN_CORAL_FAN );
        addSilkTouch( Blocks.BUBBLE_CORAL_FAN );
        addSilkTouch( Blocks.FIRE_CORAL_FAN );
        addSilkTouch( Blocks.HORN_CORAL_FAN );

        addSilkTouch( Blocks.INFESTED_STONE, Blocks.STONE );
        addSilkTouch( Blocks.INFESTED_COBBLESTONE, Blocks.COBBLESTONE );
        addSilkTouch( Blocks.INFESTED_STONE_BRICKS, Blocks.STONE_BRICKS );
        addSilkTouch( Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS );
        addSilkTouch( Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS );
        addSilkTouch( Blocks.INFESTED_CHISELED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS );

        registerLootTable( Blocks.CAKE, empty() );
        registerLootTable( Blocks.ATTACHED_PUMPKIN_STEM, empty() );
        registerLootTable( Blocks.ATTACHED_MELON_STEM, empty() );
        registerLootTable( Blocks.FROSTED_ICE, empty() );
        registerLootTable( Blocks.SPAWNER, empty() );
    }

    @Override
    public void accept( BiConsumer<ResourceLocation, LootTable.Builder> export ) {
        addTables();
        Set<ResourceLocation> set = Sets.newHashSet();

        for( Block block : getKnownBlocks() ) {
            ResourceLocation resourcelocation = block.getLootTable();
            if( resourcelocation != LootTables.EMPTY && set.add( resourcelocation ) ) {
                LootTable.Builder loottable$builder = this.registeredTables.remove( resourcelocation );
                if( loottable$builder == null ) {
                    throw new IllegalStateException( String.format( "Missing loottable '%s' for '%s'", resourcelocation, Registry.BLOCK.getKey( block ) ) );
                }

                export.accept( resourcelocation, loottable$builder );
            }
        }

        if( ! registeredTables.isEmpty() ) {
            throw new IllegalStateException( "Created block loot tables for non-blocks: " + registeredTables.keySet() );
        }
    }

    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS;
    }

    public void addFlowerPot( Block flowerPot ) {
        registerLootTable( flowerPot, block -> flowerPot( ( (FlowerPotBlock) block ).func_220276_d() ) );
    }

    public void addSilkTouch( Block block, Block drop ) {
        registerLootTable( block, silkTouchOnly( drop ) );
    }

    public void addSeparateItem( Block block, IItemProvider alternative ) {
        registerLootTable( block, simple( alternative ) );
    }

    public void addSilkTouch( Block block ) {
        addSilkTouch( block, block );
    }

    public void addSimple( Block block ) {
        addSeparateItem( block, block );
    }

    protected void registerLootTable( Block block, Function<Block, LootTable.Builder> lootFunc ) {
        registerLootTable( block, lootFunc.apply( block ) );
    }

    protected void registerLootTable( Block block, LootTable.Builder builder ) {
        registeredTables.put( block.getLootTable(), builder );
    }
}