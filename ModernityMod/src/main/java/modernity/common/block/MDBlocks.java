/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block;

import modernity.common.Modernity;
import modernity.common.block.fluid.RegularFluidBlock;
import modernity.common.fluid.MDFluids;
import modernity.common.item.group.ItemGroupCategory;
import modernity.common.item.group.MDItemGroup;
import modernity.common.registry.BlockItemRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("modernity")
public final class MDBlocks {

    public static final Block ROCK = Modernity.injected();
    public static final Block DARKROCK = Modernity.injected();

    public static final Block ROCK_BRICKS = Modernity.injected();
    public static final Block MOSSY_ROCK_BRICKS = Modernity.injected();
    public static final Block CRACKED_ROCK_BRICKS = Modernity.injected();
    public static final Block ROCK_TILES = Modernity.injected();
    public static final Block MOSSY_ROCK_TILES = Modernity.injected();
    public static final Block CRACKED_ROCK_TILES = Modernity.injected();
    public static final Block SMOOTH_ROCK = Modernity.injected();
    public static final Block POLISHED_ROCK = Modernity.injected();
    public static final Block CHISELED_ROCK = Modernity.injected();

    public static final Block DARKROCK_BRICKS = Modernity.injected();
    public static final Block MOSSY_DARKROCK_BRICKS = Modernity.injected();
    public static final Block CRACKED_DARKROCK_BRICKS = Modernity.injected();
    public static final Block DARKROCK_TILES = Modernity.injected();
    public static final Block MOSSY_DARKROCK_TILES = Modernity.injected();
    public static final Block CRACKED_DARKROCK_TILES = Modernity.injected();
    public static final Block SMOOTH_DARKROCK = Modernity.injected();
    public static final Block POLISHED_DARKROCK = Modernity.injected();
    public static final Block CHISELED_DARKROCK = Modernity.injected();

    public static final Block MURKY_WATER = Modernity.injected();

    public static void register(BlockItemRegistryHandler reg) {
        registerRock(reg, true);
        registerRock(reg, false);

        reg.add(
            "murky_water",
            new BlockBuilder()
                .blockFactory(props -> new RegularFluidBlock(MDFluids.MURKY_WATER, props))
                .material(Material.WATER)
                .color(MaterialColor.WATER)
                .hardnessResistance(100)
                .noDrops()
                .allowMovement()
                .makeBlock()
        );
    }

    private static void registerRock(BlockItemRegistryHandler reg, boolean darkrock) {
        String id = darkrock ? "darkrock" : "rock";
        MaterialColor color = darkrock ? MaterialColor.BLACK : MaterialColor.STONE;

        reg.configured(
            new BlockBuilder()
                .material(Material.ROCK)
                .color(color)
                .hardnessResistance(1.5, 6)
                .blockFactory(Block::new)
                .group(MDItemGroup.BLOCKS)
                .category(ItemGroupCategory.NATURE)
                .itemFactory(BlockItem::new)
        ).addAll(
            BlockBuilder::makeBlock,
            BlockBuilder::makeItemBlock,
            id,
            "mossy_" + id
        );

        reg.configured(
            new BlockBuilder()
                .material(Material.ROCK)
                .color(color)
                .hardnessResistance(2, 6)
                .blockFactory(Block::new)
                .group(MDItemGroup.BLOCKS)
                .category(ItemGroupCategory.BUILDING)
                .itemFactory(BlockItem::new)
        ).addAll(
            BlockBuilder::makeBlock,
            BlockBuilder::makeItemBlock,
            id + "_bricks",
            "mossy_" + id + "_bricks",
            "cracked_" + id + "_bricks",
            id + "_tiles",
            "mossy_" + id + "_tiles",
            "cracked_" + id + "_tiles",
            "smooth_" + id,
            "polished_" + id,
            "chiseled_" + id
        );
    }

    private MDBlocks() {
    }
}
