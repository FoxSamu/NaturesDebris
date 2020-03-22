/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.data.loot;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import modernity.common.block.loot.IBlockDrops;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.*;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class BlockLootData implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder( ItemPredicate.Builder.create().enchantment( new EnchantmentPredicate( Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast( 1 ) ) ) );
    private static final ILootCondition.IBuilder NO_SILK_TOUCH = SILK_TOUCH.inverted();
    private static final ILootCondition.IBuilder SHEARS = MatchTool.builder( ItemPredicate.Builder.create().item( Items.SHEARS ) );
    private static final ILootCondition.IBuilder SHEARS_OR_SILK_TOUCH = SHEARS.alternative( SILK_TOUCH );
    private static final ILootCondition.IBuilder NO_SHEARS_OR_SILK_TOUCH = SHEARS_OR_SILK_TOUCH.inverted();

    private static final Set<Item> EXPLOSION_RESISTENT = Stream.of( Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX ).map( IItemProvider::asItem ).collect( ImmutableSet.toImmutableSet() );

    private static final float[] field_218579_g = { 0.05F, 0.0625F, 0.083333336F, 0.1F };
    private static final float[] field_218580_h = { 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F };
    private final Map<ResourceLocation, LootTable.Builder> lootTables = Maps.newHashMap();

    private static final Map<Block, IBlockDrops> BLOCK_MAP = Maps.newHashMap();

    protected static <T> T applyExplosionFunction( IItemProvider item, ILootFunctionConsumer<T> consumer ) {
        return ! EXPLOSION_RESISTENT.contains( item.asItem() )
               ? consumer.acceptFunction( ExplosionDecay.func_215863_b() )
               : consumer.cast();
    }

    protected static <T> T applyExplosionCondition( IItemProvider item, ILootConditionConsumer<T> consumer ) {
        return ! EXPLOSION_RESISTENT.contains( item.asItem() )
               ? consumer.acceptCondition( SurvivesExplosion.builder() )
               : consumer.cast();
    }

    protected static LootTable.Builder simple( IItemProvider item ) {
        return LootTable.builder()
                        .addLootPool( applyExplosionCondition(
                            item,
                            LootPool.builder()
                                    .rolls( ConstantRange.of( 1 ) )
                                    .addEntry( ItemLootEntry.builder( item ) )
                        ) );
    }

    protected static LootTable.Builder withCondition( Block block, ILootCondition.IBuilder condition, LootEntry.Builder<?> orElse ) {
        return LootTable.builder()
                        .addLootPool(
                            LootPool.builder()
                                    .rolls( ConstantRange.of( 1 ) )
                                    .addEntry(
                                        ItemLootEntry.builder( block )
                                                     .acceptCondition( condition )
                                                     .func_216080_a( orElse )
                                    )
                        );
    }

    protected static LootTable.Builder withSilkTouch( Block block, LootEntry.Builder<?> orElse ) {
        return withCondition( block, SILK_TOUCH, orElse );
    }

    protected static LootTable.Builder withShears( Block block, LootEntry.Builder<?> orElse ) {
        return withCondition( block, SHEARS, orElse );
    }

    protected static LootTable.Builder withShearsOrSilkTouch( Block block, LootEntry.Builder<?> orElse ) {
        return withCondition( block, SHEARS_OR_SILK_TOUCH, orElse );
    }

    protected static LootTable.Builder withSilkTouch( Block block, IItemProvider orElse ) {
        return withSilkTouch( block, applyExplosionCondition( block, ItemLootEntry.builder( orElse ) ) );
    }

    protected static LootTable.Builder randomRange( IItemProvider item, IRandomRange countRange ) {
        return LootTable.builder()
                        .addLootPool(
                            LootPool.builder()
                                    .rolls( ConstantRange.of( 1 ) )
                                    .addEntry( applyExplosionFunction( item, ItemLootEntry.builder( item ).acceptFunction( SetCount.func_215932_a( countRange ) ) ) ) );
    }

    protected static LootTable.Builder func_218530_a( Block p_218530_0_, IItemProvider p_218530_1_, IRandomRange p_218530_2_ ) {
        return withSilkTouch( p_218530_0_, applyExplosionFunction( p_218530_0_, ItemLootEntry.builder( p_218530_1_ ).acceptFunction( SetCount.func_215932_a( p_218530_2_ ) ) ) );
    }

    protected static LootTable.Builder func_218561_b( IItemProvider p_218561_0_ ) {
        return LootTable.builder().addLootPool( LootPool.builder().acceptCondition( SILK_TOUCH ).rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218561_0_ ) ) );
    }

    protected static LootTable.Builder func_218523_c( IItemProvider p_218523_0_ ) {
        return LootTable.builder().addLootPool( applyExplosionCondition( Blocks.FLOWER_POT, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( Blocks.FLOWER_POT ) ) ) ).addLootPool( applyExplosionCondition( p_218523_0_, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218523_0_ ) ) ) );
    }

    protected static LootTable.Builder func_218513_d( Block p_218513_0_ ) {
        return LootTable.builder().addLootPool( LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( applyExplosionFunction( p_218513_0_, ItemLootEntry.builder( p_218513_0_ ).acceptFunction( SetCount.func_215932_a( ConstantRange.of( 2 ) ).acceptCondition( BlockStateProperty.builder( p_218513_0_ ).with( SlabBlock.TYPE, SlabType.DOUBLE ) ) ) ) ) );
    }

    protected static <T extends Comparable<T>> LootTable.Builder func_218562_a( Block p_218562_0_, IProperty<T> p_218562_1_, T p_218562_2_ ) {
        return LootTable.builder().addLootPool( applyExplosionCondition( p_218562_0_, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218562_0_ ).acceptCondition( BlockStateProperty.builder( p_218562_0_ ).with( p_218562_1_, p_218562_2_ ) ) ) ) );
    }

    protected static LootTable.Builder func_218481_e( Block p_218481_0_ ) {
        return LootTable.builder().addLootPool( applyExplosionCondition( p_218481_0_, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218481_0_ ).acceptFunction( CopyName.func_215893_a( CopyName.Source.BLOCK_ENTITY ) ) ) ) );
    }

    protected static LootTable.Builder func_218544_f( Block p_218544_0_ ) {
        return LootTable.builder().addLootPool( applyExplosionCondition( p_218544_0_, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218544_0_ ).acceptFunction( CopyName.func_215893_a( CopyName.Source.BLOCK_ENTITY ) ).acceptFunction( CopyNbt.func_215881_a( CopyNbt.Source.BLOCK_ENTITY ).func_216056_a( "Lock", "BlockEntityTag.Lock" ).func_216056_a( "LootTable", "BlockEntityTag.LootTable" ).func_216056_a( "LootTableSeed", "BlockEntityTag.LootTableSeed" ) ).acceptFunction( SetContents.func_215920_b().func_216075_a( DynamicLootEntry.func_216162_a( ShulkerBoxBlock.field_220169_b ) ) ) ) ) );
    }

    protected static LootTable.Builder func_218559_g( Block p_218559_0_ ) {
        return LootTable.builder().addLootPool( applyExplosionCondition( p_218559_0_, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218559_0_ ).acceptFunction( CopyName.func_215893_a( CopyName.Source.BLOCK_ENTITY ) ).acceptFunction( CopyNbt.func_215881_a( CopyNbt.Source.BLOCK_ENTITY ).func_216056_a( "Patterns", "BlockEntityTag.Patterns" ) ) ) ) );
    }

    protected static LootTable.Builder func_218476_a( Block p_218476_0_, Item p_218476_1_ ) {
        return withSilkTouch( p_218476_0_, applyExplosionFunction( p_218476_0_, ItemLootEntry.builder( p_218476_1_ ).acceptFunction( ApplyBonus.func_215869_a( Enchantments.FORTUNE ) ) ) );
    }

    protected static LootTable.Builder func_218491_c( Block p_218491_0_, IItemProvider p_218491_1_ ) {
        return withSilkTouch( p_218491_0_, applyExplosionFunction( p_218491_0_, ItemLootEntry.builder( p_218491_1_ ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( - 6.0F, 2.0F ) ) ).acceptFunction( LimitCount.func_215911_a( IntClamper.func_215848_a( 0 ) ) ) ) );
    }

    protected static LootTable.Builder func_218570_h( Block p_218570_0_ ) {
        return withShears( p_218570_0_, applyExplosionFunction( p_218570_0_, ItemLootEntry.builder( Items.WHEAT_SEEDS ).acceptCondition( RandomChance.builder( 0.125F ) ).acceptFunction( ApplyBonus.func_215865_a( Enchantments.FORTUNE, 2 ) ) ) );
    }

    protected static LootTable.Builder func_218475_b( Block p_218475_0_, Item p_218475_1_ ) {
        return LootTable.builder().addLootPool( applyExplosionFunction( p_218475_0_, LootPool.builder().rolls( ConstantRange.of( 1 ) ).addEntry( ItemLootEntry.builder( p_218475_1_ ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.06666667F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 0 ) ) ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.13333334F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 1 ) ) ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.2F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 2 ) ) ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.26666668F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 3 ) ) ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.33333334F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 4 ) ) ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.4F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 5 ) ) ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.46666667F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 6 ) ) ).acceptFunction( SetCount.func_215932_a( BinomialRange.func_215838_a( 3, 0.53333336F ) ).acceptCondition( BlockStateProperty.builder( p_218475_0_ ).with( StemBlock.AGE, 7 ) ) ) ) ) );
    }

    protected static LootTable.Builder func_218486_d( IItemProvider p_218486_0_ ) {
        return LootTable.builder().addLootPool( LootPool.builder().rolls( ConstantRange.of( 1 ) ).acceptCondition( SHEARS ).addEntry( ItemLootEntry.builder( p_218486_0_ ) ) );
    }

    protected static LootTable.Builder func_218540_a( Block p_218540_0_, Block p_218540_1_, float... p_218540_2_ ) {
        return withShearsOrSilkTouch( p_218540_0_, applyExplosionCondition( p_218540_0_, ItemLootEntry.builder( p_218540_1_ ) ).acceptCondition( TableBonus.builder( Enchantments.FORTUNE, p_218540_2_ ) ) ).addLootPool( LootPool.builder().rolls( ConstantRange.of( 1 ) ).acceptCondition( NO_SHEARS_OR_SILK_TOUCH ).addEntry( applyExplosionFunction( p_218540_0_, ItemLootEntry.builder( Items.STICK ).acceptFunction( SetCount.func_215932_a( RandomValueRange.func_215837_a( 1.0F, 2.0F ) ) ) ).acceptCondition( TableBonus.builder( Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F ) ) ) );
    }

    protected static LootTable.Builder func_218526_b( Block p_218526_0_, Block p_218526_1_, float... p_218526_2_ ) {
        return func_218540_a( p_218526_0_, p_218526_1_, p_218526_2_ ).addLootPool( LootPool.builder().rolls( ConstantRange.of( 1 ) ).acceptCondition( NO_SHEARS_OR_SILK_TOUCH ).addEntry( applyExplosionCondition( p_218526_0_, ItemLootEntry.builder( Items.APPLE ) ).acceptCondition( TableBonus.builder( Enchantments.FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F ) ) ) );
    }

    protected static LootTable.Builder func_218541_a( Block p_218541_0_, Item p_218541_1_, Item p_218541_2_, ILootCondition.IBuilder p_218541_3_ ) {
        return applyExplosionFunction( p_218541_0_, LootTable.builder().addLootPool( LootPool.builder().addEntry( ItemLootEntry.builder( p_218541_1_ ).acceptCondition( p_218541_3_ ).func_216080_a( ItemLootEntry.builder( p_218541_2_ ) ) ) ).addLootPool( LootPool.builder().acceptCondition( p_218541_3_ ).addEntry( ItemLootEntry.builder( p_218541_2_ ).acceptFunction( ApplyBonus.func_215870_a( Enchantments.FORTUNE, 0.5714286F, 3 ) ) ) ) );
    }

    public static LootTable.Builder builder() {
        return LootTable.builder();
    }



    protected void addTables() {
        BLOCK_MAP.forEach( ( key, value ) -> add(
            key,
            value.createLootTable( key )
        ) );
    }

    protected void addSimple( Block block ) {
        add( block, simple( block ) );
    }

    protected void addSimple( Block block, IItemProvider item ) {
        add( block, simple( item ) );
    }

    protected void add( Block block, Function<Block, LootTable.Builder> func ) {
        add( block, func.apply( block ) );
    }

    protected void add( Block block, LootTable.Builder builder ) {
        lootTables.put( block.getLootTable(), builder );
    }

    @Override
    public void accept( BiConsumer<ResourceLocation, LootTable.Builder> consumer ) {
        addTables();
        Set<ResourceLocation> locations = Sets.newHashSet();

        for( Block block : getKnownBlocks() ) {
            if( Objects.requireNonNull( block.getRegistryName() ).getNamespace().equals( "modernity" ) ) {
                ResourceLocation lt = block.getLootTable();
                if( lt != LootTables.EMPTY && locations.add( lt ) ) {
                    LootTable.Builder builder = lootTables.remove( lt );
                    if( builder != null ) {
                        consumer.accept( lt, builder );
                    }
                }
            }
        }

        if( ! lootTables.isEmpty() ) {
            throw new IllegalStateException( "Created block loot tables for non-blocks: " + lootTables.keySet() );
        }
    }

    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS;
    }

    public static void addBlock( Block block, IBlockDrops loot ) {
        BLOCK_MAP.put( block, loot );
    }
}