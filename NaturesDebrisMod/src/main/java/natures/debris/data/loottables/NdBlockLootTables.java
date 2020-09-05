package natures.debris.data.loottables;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

import natures.debris.common.block.NdBlocks;

public class NdBlockLootTables extends BlockLootTables {

    protected static LootTable.Builder droppingNothing() {
        return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(0)));
    }

    @Override
    protected void addTables() {
        registerDropSelfLootTable(NdBlocks.MURKY_DIRT);
        registerDropSelfLootTable(NdBlocks.MURKY_COARSE_DIRT);
        registerDropSelfLootTable(NdBlocks.MURKY_CLAY);
        registerDropSelfLootTable(NdBlocks.MURKY_TERRACOTTA);
        registerDropSelfLootTable(NdBlocks.MURKY_SAND);
        registerDropping(NdBlocks.MURKY_GRASS_PATH, NdBlocks.MURKY_DIRT);
        registerLootTable(NdBlocks.MURKY_GRASS_BLOCK, block -> droppingWithSilkTouch(block, NdBlocks.MURKY_DIRT));
        registerLootTable(NdBlocks.MURKY_HUMUS, block -> droppingWithSilkTouch(block, NdBlocks.MURKY_DIRT));
        registerLootTable(NdBlocks.MURKY_PODZOL, block -> droppingWithSilkTouch(block, NdBlocks.MURKY_DIRT));
        registerLootTable(NdBlocks.LEAFY_HUMUS, block -> droppingWithSilkTouch(block, NdBlocks.MURKY_DIRT));

        registerDropSelfLootTable(NdBlocks.ROCK);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK);
        registerDropSelfLootTable(NdBlocks.ROCK_BRICKS);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_BRICKS);
        registerDropSelfLootTable(NdBlocks.CRACKED_ROCK_BRICKS);
        registerDropSelfLootTable(NdBlocks.ROCK_TILES);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_TILES);
        registerDropSelfLootTable(NdBlocks.CRACKED_ROCK_TILES);
        registerDropSelfLootTable(NdBlocks.SMOOTH_ROCK);
        registerDropSelfLootTable(NdBlocks.POLISHED_ROCK);
        registerDropSelfLootTable(NdBlocks.CHISELED_ROCK);
        registerDropSelfLootTable(NdBlocks.ROCK_PILLAR);
        registerDropSelfLootTable(NdBlocks.ROCK_LANTERN);

        registerLootTable(NdBlocks.ROCK_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.MOSSY_ROCK_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.ROCK_BRICKS_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.MOSSY_ROCK_BRICKS_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.CRACKED_ROCK_BRICKS_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.ROCK_TILES_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.MOSSY_ROCK_TILES_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.CRACKED_ROCK_TILES_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.SMOOTH_ROCK_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.POLISHED_ROCK_SLAB, BlockLootTables::droppingSlab);

        registerDropSelfLootTable(NdBlocks.ROCK_STAIRS);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_STAIRS);
        registerDropSelfLootTable(NdBlocks.ROCK_BRICKS_STAIRS);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_BRICKS_STAIRS);
        registerDropSelfLootTable(NdBlocks.CRACKED_ROCK_BRICKS_STAIRS);
        registerDropSelfLootTable(NdBlocks.ROCK_TILES_STAIRS);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_TILES_STAIRS);
        registerDropSelfLootTable(NdBlocks.CRACKED_ROCK_TILES_STAIRS);
        registerDropSelfLootTable(NdBlocks.SMOOTH_ROCK_STAIRS);
        registerDropSelfLootTable(NdBlocks.POLISHED_ROCK_STAIRS);

        registerDropSelfLootTable(NdBlocks.ROCK_STEP);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_STEP);
        registerDropSelfLootTable(NdBlocks.ROCK_BRICKS_STEP);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_BRICKS_STEP);
        registerDropSelfLootTable(NdBlocks.CRACKED_ROCK_BRICKS_STEP);
        registerDropSelfLootTable(NdBlocks.ROCK_TILES_STEP);
        registerDropSelfLootTable(NdBlocks.MOSSY_ROCK_TILES_STEP);
        registerDropSelfLootTable(NdBlocks.CRACKED_ROCK_TILES_STEP);
        registerDropSelfLootTable(NdBlocks.SMOOTH_ROCK_STEP);
        registerDropSelfLootTable(NdBlocks.POLISHED_ROCK_STEP);

        registerDropSelfLootTable(NdBlocks.DARKROCK);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK);
        registerDropSelfLootTable(NdBlocks.DARKROCK_BRICKS);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_BRICKS);
        registerDropSelfLootTable(NdBlocks.CRACKED_DARKROCK_BRICKS);
        registerDropSelfLootTable(NdBlocks.DARKROCK_TILES);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_TILES);
        registerDropSelfLootTable(NdBlocks.CRACKED_DARKROCK_TILES);
        registerDropSelfLootTable(NdBlocks.SMOOTH_DARKROCK);
        registerDropSelfLootTable(NdBlocks.POLISHED_DARKROCK);
        registerDropSelfLootTable(NdBlocks.CHISELED_DARKROCK);
        registerDropSelfLootTable(NdBlocks.DARKROCK_PILLAR);
        registerDropSelfLootTable(NdBlocks.DARKROCK_LANTERN);

        registerLootTable(NdBlocks.DARKROCK_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.MOSSY_DARKROCK_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.DARKROCK_BRICKS_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.DARKROCK_TILES_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.MOSSY_DARKROCK_TILES_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.CRACKED_DARKROCK_TILES_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.SMOOTH_DARKROCK_SLAB, BlockLootTables::droppingSlab);
        registerLootTable(NdBlocks.POLISHED_DARKROCK_SLAB, BlockLootTables::droppingSlab);

        registerDropSelfLootTable(NdBlocks.DARKROCK_STAIRS);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_STAIRS);
        registerDropSelfLootTable(NdBlocks.DARKROCK_BRICKS_STAIRS);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS);
        registerDropSelfLootTable(NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS);
        registerDropSelfLootTable(NdBlocks.DARKROCK_TILES_STAIRS);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_TILES_STAIRS);
        registerDropSelfLootTable(NdBlocks.CRACKED_DARKROCK_TILES_STAIRS);
        registerDropSelfLootTable(NdBlocks.SMOOTH_DARKROCK_STAIRS);
        registerDropSelfLootTable(NdBlocks.POLISHED_DARKROCK_STAIRS);

        registerDropSelfLootTable(NdBlocks.DARKROCK_STEP);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_STEP);
        registerDropSelfLootTable(NdBlocks.DARKROCK_BRICKS_STEP);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_BRICKS_STEP);
        registerDropSelfLootTable(NdBlocks.CRACKED_DARKROCK_BRICKS_STEP);
        registerDropSelfLootTable(NdBlocks.DARKROCK_TILES_STEP);
        registerDropSelfLootTable(NdBlocks.MOSSY_DARKROCK_TILES_STEP);
        registerDropSelfLootTable(NdBlocks.CRACKED_DARKROCK_TILES_STEP);
        registerDropSelfLootTable(NdBlocks.SMOOTH_DARKROCK_STEP);
        registerDropSelfLootTable(NdBlocks.POLISHED_DARKROCK_STEP);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)
                            .filter(block -> Objects.requireNonNull(block.getRegistryName()).getNamespace().equals("ndebris"))
                            .collect(Collectors.toList());
    }
}
