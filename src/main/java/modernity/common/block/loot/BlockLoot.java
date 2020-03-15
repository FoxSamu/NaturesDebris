/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.loot;

import com.google.common.collect.ImmutableSet;
import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.base.SlabType;
import modernity.common.loot.func.MulCornerCount;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.IProperty;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.*;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.CopyName;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.redgalaxy.util.Lazy;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class BlockLoot {
    private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder( ItemPredicate.Builder.create().enchantment( new EnchantmentPredicate( Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast( 1 ) ) ) );
    private static final ILootCondition.IBuilder SHEARS = MatchTool.builder( ItemPredicate.Builder.create().item( Items.SHEARS ) );
    private static final ILootCondition.IBuilder SHEARS_OR_SILK_TOUCH = SHEARS.alternative( SILK_TOUCH );

    private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(
        Blocks.DRAGON_EGG,
        Blocks.BEACON,
        Blocks.CONDUIT,
        Blocks.SKELETON_SKULL,
        Blocks.WITHER_SKELETON_SKULL,
        Blocks.PLAYER_HEAD,
        Blocks.ZOMBIE_HEAD,
        Blocks.CREEPER_HEAD,
        Blocks.DRAGON_HEAD,
        Blocks.SHULKER_BOX,
        Blocks.BLACK_SHULKER_BOX,
        Blocks.BLUE_SHULKER_BOX,
        Blocks.BROWN_SHULKER_BOX,
        Blocks.CYAN_SHULKER_BOX,
        Blocks.GRAY_SHULKER_BOX,
        Blocks.GREEN_SHULKER_BOX,
        Blocks.LIGHT_BLUE_SHULKER_BOX,
        Blocks.LIGHT_GRAY_SHULKER_BOX,
        Blocks.LIME_SHULKER_BOX,
        Blocks.MAGENTA_SHULKER_BOX,
        Blocks.ORANGE_SHULKER_BOX,
        Blocks.PINK_SHULKER_BOX,
        Blocks.PURPLE_SHULKER_BOX,
        Blocks.RED_SHULKER_BOX,
        Blocks.WHITE_SHULKER_BOX,
        Blocks.YELLOW_SHULKER_BOX
    ).map( IItemProvider::asItem ).collect( ImmutableSet.toImmutableSet() );

    private BlockLoot() {
    }

    private static <T> T explosionFunc( IItemProvider item, ILootFunctionConsumer<T> consumer ) {
        return ! EXPLOSION_RESISTANT.contains( item.asItem() )
               ? consumer.acceptFunction( ExplosionDecay.func_215863_b() )
               : consumer.cast();
    }

    private static <T> T explosionCond( IItemProvider item, ILootConditionConsumer<T> consumer ) {
        return ! EXPLOSION_RESISTANT.contains( item.asItem() )
               ? consumer.acceptCondition( SurvivesExplosion.builder() )
               : consumer.cast();
    }

    public static IBlockLoot self() {
        return block -> explosionCond( block, ItemLootEntry.builder( block ) );
    }

    public static IBlockLoot item( IItemProvider item ) {
        return block -> explosionCond( item, ItemLootEntry.builder( item ) );
    }

    public static IBlockLoot item( IItemProvider item, int count ) {
        return block -> explosionFunc( item, ItemLootEntry.builder( item ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( count ) ) ) );
    }

    public static IBlockLoot item( IItemProvider item, float chance ) {
        return block -> explosionFunc( item, ItemLootEntry.builder( item ).acceptCondition( RandomChance.builder( chance ) ) );
    }

    public static IBlockLoot item( IItemProvider item, int min, int max ) {
        return block -> explosionFunc( item, ItemLootEntry.builder( item ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( min, max ) ) ) );
    }

    public static IBlockLoot item( IItemProvider item, int n, float p ) {
        return block -> explosionFunc( item, ItemLootEntry.builder( item ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( n, p ) ) ) );
    }

    public static IBlockLoot item( Supplier<IItemProvider> item ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionCond( lazy.get(), ItemLootEntry.builder( lazy.get() ) );
    }

    public static IBlockLoot item( Supplier<IItemProvider> item, int count ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionFunc( lazy.get(), ItemLootEntry.builder( lazy.get() ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( count ) ) ) );
    }

    public static IBlockLoot item( Supplier<IItemProvider> item, float chance ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionFunc( lazy.get(), ItemLootEntry.builder( lazy.get() ).acceptCondition( RandomChance.builder( chance ) ) );
    }

    public static IBlockLoot item( Supplier<IItemProvider> item, int min, int max ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionFunc( lazy.get(), ItemLootEntry.builder( lazy.get() ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( min, max ) ) ) );
    }

    public static IBlockLoot item( Supplier<IItemProvider> item, int n, float p ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionFunc( lazy.get(), ItemLootEntry.builder( lazy.get() ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( n, p ) ) ) );
    }

    public static IBlockLoot slab() {
        return block -> explosionFunc(
            block,
            ItemLootEntry.builder( block ).acceptFunction(
                SetCount.func_215932_a( ConstantRange.of( 2 ) ).acceptCondition(
                    BlockStateProperty.builder( block )
                                      .with( MDBlockStateProperties.SLAB_TYPE, SlabType.DOUBLE )
                )
            )
        );
    }

    public static IBlockLoot corner() {
        return block -> explosionFunc(
            block,
            ItemLootEntry.builder( block ).acceptFunction( MulCornerCount.builder() )
        );
    }


    public static IBlockLoot select( ILootCondition.IBuilder cond, IBlockLoot with ) {
        return block -> with.createLootEntry( block )
                            .acceptCondition( cond );
    }


    public static IBlockLoot select( ILootCondition.IBuilder cond, IBlockLoot with, IBlockLoot without ) {
        return block -> with.createLootEntry( block )
                            .acceptCondition( cond )
                            .func_216080_a( without.createLootEntry( block ) );
    }

    public static IBlockLoot silkTouch( IBlockLoot with, IBlockLoot without ) {
        return select( SILK_TOUCH, with, without );
    }

    public static IBlockLoot silkTouch( IBlockLoot with ) {
        return select( SILK_TOUCH, with );
    }

    public static IBlockLoot shears( IBlockLoot with, IBlockLoot without ) {
        return select( SHEARS, with, without );
    }

    public static IBlockLoot shears( IBlockLoot with ) {
        return select( SHEARS, with );
    }

    public static IBlockLoot silkTouchOrShears( IBlockLoot with, IBlockLoot without ) {
        return select( SHEARS_OR_SILK_TOUCH, with, without );
    }

    public static IBlockLoot silkTouchOrShears( IBlockLoot with ) {
        return select( SHEARS_OR_SILK_TOUCH, with );
    }

    public static <T extends Comparable<T>> IBlockLoot blockProperty( IProperty<T> prop, T val, IBlockLoot with ) {
        return block -> with.createLootEntry( block )
                            .acceptCondition( BlockStateProperty.builder( block ).with( prop, val ) );
    }

    public static <T extends Comparable<T>> IBlockLoot blockProperty( IProperty<T> prop, T val, IBlockLoot with, IBlockLoot without ) {
        return block -> with.createLootEntry( block )
                            .acceptCondition( BlockStateProperty.builder( block ).with( prop, val ) )
                            .func_216080_a( without.createLootEntry( block ) );
    }


    public static IBlockLoot chest() {
        return block -> explosionCond( block, ItemLootEntry.builder( block ).acceptFunction( CopyName.func_215893_a( CopyName.Source.BLOCK_ENTITY ) ) );
    }

    public static IBlockLoot oreItem( Supplier<IItemProvider> item ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionCond( lazy.get(), ItemLootEntry.builder( lazy.get() ).acceptFunction(
            ApplyBonus.func_215869_a( Enchantments.FORTUNE )
        ) );
    }

    public static IBlockLoot oreItem( Supplier<IItemProvider> item, int min, int max ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionCond(
            lazy.get(),
            ItemLootEntry.builder( lazy.get() )
                         .acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( min, max ) ) )
                         .acceptFunction( ApplyBonus.func_215871_b( Enchantments.FORTUNE ) )
        );
    }

    public static IBlockLoot oreItem( Supplier<IItemProvider> item, int min, int max, float moreChance ) {
        Lazy<Item> lazy = Lazy.of( item ).map( IItemProvider::asItem );
        return block -> explosionCond(
            lazy.get(),
            ItemLootEntry.builder( lazy.get() )
                         .acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( min, max ) ) )
                         .acceptFunction( ApplyBonus.func_215870_a( Enchantments.FORTUNE, moreChance, 0 ) )
        );
    }
}
