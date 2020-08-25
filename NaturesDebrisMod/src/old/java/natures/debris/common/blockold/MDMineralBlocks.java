/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold;

import modernity.common.item.group.MDItemGroup;
import natures.debris.common.blockold.base.AxisBlock;
import natures.debris.common.blockold.base.ExtSlimeBlock;
import natures.debris.common.blockold.base.TranslucentBlock;
import natures.debris.common.blockold.loot.BlockLoot;
import natures.debris.common.blockold.loot.SaltBlockDrops;
import natures.debris.common.blockold.misc.BlackboneBlock;
import natures.debris.common.blockold.misc.OreBlock;
import natures.debris.common.blockold.misc.SaltBlock;
import natures.debris.common.itemold.MDItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

//@ObjectHolder( "modernity" )
public final class MDMineralBlocks {
    // Ores
    public static final OreBlock SALT_ORE
        = MDBlocks.function("salt_ore", props -> new OreBlock(0, 3, props))
                  .rock(MaterialColor.STONE, 3, 3)
                  .item(MDItemGroup.BLOCKS)
                  .drops(new SaltBlockDrops(1, 5, 4, 2, 4))
                  .create();
    public static final Block ALUMINIUM_ORE
        = MDBlocks.simple("aluminium_ore")
                  .rock(MaterialColor.STONE, 3, 3)
                  .item(MDItemGroup.BLOCKS)
                  .tool(ToolType.PICKAXE, 1)
                  .dropSelf()
                  .create();
    public static final OreBlock ANTHRACITE_ORE
        = MDBlocks.function("anthracite_ore", props -> new OreBlock(0, 2, props))
                  .rock(MaterialColor.STONE, 3, 3)
                  .item(MDItemGroup.BLOCKS)
                  .drops(BlockLoot.oreItem(() -> MDItems.ANTHRACITE))
                  .create();
    public static final OreBlock FINNERITE_ORE
        = MDBlocks.function("finnerite_ore", props -> new OreBlock(2, 5, props))
                  .rock(MaterialColor.STONE, 3, 3)
                  .item(MDItemGroup.BLOCKS)
                  .drops(BlockLoot.oreItem(() -> MDItems.FINNERITE))
                  .tool(ToolType.PICKAXE, 2)
                  .create();
    public static final OreBlock IVERITE_ORE
        = MDBlocks.function("iverite_ore", props -> new OreBlock(2, 5, props))
                  .rock(MaterialColor.STONE, 3, 3)
                  .item(MDItemGroup.BLOCKS)
                  .drops(BlockLoot.oreItem(() -> MDItems.IVERITE))
                  .tool(ToolType.PICKAXE, 2)
                  .create();
    public static final OreBlock SAGERITE_ORE
        = MDBlocks.function("sagerite_ore", props -> new OreBlock(2, 5, props))
                  .rock(MaterialColor.STONE, 3, 3)
                  .item(MDItemGroup.BLOCKS)
                  .drops(BlockLoot.oreItem(() -> MDItems.SAGERITE))
                  .tool(ToolType.PICKAXE, 2)
                  .create();
    public static final OreBlock LUMINOSITE_ORE
        = MDBlocks.function("luminosite_ore", props -> new OreBlock(0, 4, props))
                  .rock(MaterialColor.STONE, 3, 3)
                  .light(4)
                  .item(MDItemGroup.BLOCKS)
                  .drops(BlockLoot.oreItem(() -> MDItems.LUMINOSITE_SHARDS, 1, 2, 0.6F))
                  .tool(ToolType.PICKAXE, 1)
                  .create();
    public static final OreBlock GOO_ORE
        = MDBlocks.function("goo_ore", props -> new OreBlock(0, 2, props))
                  .rock(MaterialColor.STONE, 3, 3)
                  .sound(MDSoundTypes.ASPHALT)
                  .item(MDItemGroup.BLOCKS)
                  .drops(BlockLoot.oreItem(() -> MDItems.GOO_BALL, 1, 2, 0.6F))
                  .create();
    public static final OreBlock FOSSIL
        = MDBlocks.function("fossil", props -> new OreBlock(0, 2, props))
                  .rock(MaterialColor.STONE, 1.5F, 5)
                  .item(MDItemGroup.BLOCKS)
                  .drops(BlockLoot.oreItem(() -> MDItems.BLACKBONE))
                  .tool(ToolType.PICKAXE, 1)
                  .create();
    // Blocks
    public static final TranslucentBlock SALT_BLOCK
        = MDBlocks.function("salt_block", SaltBlock::new)
                  .props(Material.GLASS, MaterialColor.SNOW)
                  .hardness(0.3F)
                  .sound(MDSoundTypes.CRYSTAL)
                  .item(MDItemGroup.BLOCKS)
                  .drops(new SaltBlockDrops(1, 5, 4, 4, 9))
                  .recipeBlock9(() -> MDItems.SALT_NUGGET, 1, null)
                  .create();
    public static final Block ALUMINIUM_BLOCK
        = MDBlocks.simple("aluminium_block")
                  .props(Material.IRON, MaterialColor.IRON)
                  .hardness(5F, 6F)
                  .sound(SoundType.METAL)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.ALUMINIUM_INGOT, 1, null)
                  .create();
    public static final Block ANTHRACITE_BLOCK
        = MDBlocks.simple("anthracite_block")
                  .props(Material.ROCK, MaterialColor.BLACK)
                  .hardness(3F, 6F)
                  .sound(SoundType.STONE)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.ANTHRACITE, 1, null)
                  .create();
    public static final Block FINNERITE_BLOCK
        = MDBlocks.simple("finnerite_block")
                  .props(Material.IRON, MaterialColor.BLUE)
                  .hardness(5F, 6F)
                  .sound(SoundType.METAL)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.FINNERITE, 1, null)
                  .create();
    public static final Block IVERITE_BLOCK
        = MDBlocks.simple("iverite_block")
                  .props(Material.IRON, MaterialColor.GREEN)
                  .hardness(5F, 6F)
                  .sound(SoundType.METAL)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.IVERITE, 1, null)
                  .create();
    public static final Block SAGERITE_BLOCK
        = MDBlocks.simple("sagerite_block")
                  .props(Material.IRON, MaterialColor.RED)
                  .hardness(5F, 6F)
                  .sound(SoundType.METAL)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.SAGERITE, 1, null)
                  .create();
    public static final Block LUMINOSITE_BLOCK
        = MDBlocks.simple("luminosite_block")
                  .props(Material.IRON, MaterialColor.SNOW)
                  .hardness(5F, 6F)
                  .light(15)
                  .sound(MDSoundTypes.CRYSTAL)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.LUMINOSITE_SHARDS, 1, null)
                  .create();
    public static final ExtSlimeBlock GOO_BLOCK
        = MDBlocks.function("goo_block", ExtSlimeBlock::new)
                  .props(Material.CLAY, MaterialColor.DIRT)
                  .slipperiness(0.8F)
                  .sound(SoundType.SLIME)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.GOO_BALL, 1, null)
                  .create();
    public static final AxisBlock BLACKBONE_BLOCK
        = MDBlocks.function("blackbone_block", BlackboneBlock::new)
                  .props(Material.ROCK, MaterialColor.BLACK)
                  .hardness(2)
                  .item(MDItemGroup.BLOCKS)
                  .dropSelf()
                  .recipeBlock9(() -> MDItems.BLACKBONE, 1, null)
                  .create();

    public static void init() {
    }
}
