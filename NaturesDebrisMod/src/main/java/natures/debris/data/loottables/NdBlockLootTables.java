package natures.debris.data.loottables;

import natures.debris.common.block.NdBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NdBlockLootTables extends BlockLootTables {

    protected static LootTable.Builder droppingNothing() {
        return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(0)));
    }

    @Override
    protected void addTables() {
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
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)
                            .filter(block -> Objects.requireNonNull(block.getRegistryName()).getNamespace().equals("ndebris"))
                            .collect(Collectors.toList());
    }
}
