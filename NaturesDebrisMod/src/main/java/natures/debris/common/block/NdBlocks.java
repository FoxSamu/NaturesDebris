package natures.debris.common.block;

import natures.debris.common.NaturesDebris;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder("ndebris")
public final class NdBlocks {
    public static final Block ROCK = inj();
    public static final Block MOSSY_ROCK = inj();
    public static final Block ROCK_BRICKS = inj();
    public static final Block MOSSY_ROCK_BRICKS = inj();
    public static final Block CRACKED_ROCK_BRICKS = inj();
    public static final Block ROCK_TILES = inj();
    public static final Block MOSSY_ROCK_TILES = inj();
    public static final Block CRACKED_ROCK_TILES = inj();
    public static final Block SMOOTH_ROCK = inj();
    public static final Block POLISHED_ROCK = inj();
    public static final Block CHISELED_ROCK = inj();
    public static final Block ROCK_PILLAR = inj();

    public static final Block DARKROCK = inj();
    public static final Block MOSSY_DARKROCK = inj();
    public static final Block DARKROCK_BRICKS = inj();
    public static final Block MOSSY_DARKROCK_BRICKS = inj();
    public static final Block CRACKED_DARKROCK_BRICKS = inj();
    public static final Block DARKROCK_TILES = inj();
    public static final Block MOSSY_DARKROCK_TILES = inj();
    public static final Block CRACKED_DARKROCK_TILES = inj();
    public static final Block SMOOTH_DARKROCK = inj();
    public static final Block POLISHED_DARKROCK = inj();
    public static final Block CHISELED_DARKROCK = inj();
    public static final Block DARKROCK_PILLAR = inj();

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
            rock("rock", 1.5, 6, false),
            rock("mossy_rock", 1.5, 6, false),
            rock("rock_bricks", 2, 6, false),
            rock("mossy_rock_bricks", 2, 6, false),
            rock("cracked_rock_bricks", 2, 6, false),
            rock("rock_tiles", 2, 6, false),
            rock("mossy_rock_tiles", 2, 6, false),
            rock("cracked_rock_tiles", 2, 6, false),
            rock("smooth_rock", 1.5, 6, false),
            rock("polished_rock", 2, 6, false),
            rock("chiseled_rock", 2, 6, false),
            rockPillar("rock_pillar", 2, 6, false),

            rock("darkrock", 1.5, 6, true),
            rock("mossy_darkrock", 1.5, 6, true),
            rock("darkrock_bricks", 2, 6, true),
            rock("mossy_darkrock_bricks", 2, 6, true),
            rock("cracked_darkrock_bricks", 2, 6, true),
            rock("darkrock_tiles", 2, 6, true),
            rock("mossy_darkrock_tiles", 2, 6, true),
            rock("cracked_darkrock_tiles", 2, 6, true),
            rock("smooth_darkrock", 1.5, 6, true),
            rock("polished_darkrock", 2, 6, true),
            rock("chiseled_darkrock", 2, 6, true),
            rockPillar("darkrock_pillar", 2, 6, true)
        );
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(
            item(ROCK, ItemGroup.BUILDING_BLOCKS),
            item(MOSSY_ROCK, ItemGroup.BUILDING_BLOCKS),
            item(ROCK_BRICKS, ItemGroup.BUILDING_BLOCKS),
            item(MOSSY_ROCK_BRICKS, ItemGroup.BUILDING_BLOCKS),
            item(CRACKED_ROCK_BRICKS, ItemGroup.BUILDING_BLOCKS),
            item(ROCK_TILES, ItemGroup.BUILDING_BLOCKS),
            item(MOSSY_ROCK_TILES, ItemGroup.BUILDING_BLOCKS),
            item(CRACKED_ROCK_TILES, ItemGroup.BUILDING_BLOCKS),
            item(SMOOTH_ROCK, ItemGroup.BUILDING_BLOCKS),
            item(POLISHED_ROCK, ItemGroup.BUILDING_BLOCKS),
            item(CHISELED_ROCK, ItemGroup.BUILDING_BLOCKS),
            item(ROCK_PILLAR, ItemGroup.BUILDING_BLOCKS),

            item(DARKROCK, ItemGroup.BUILDING_BLOCKS),
            item(MOSSY_DARKROCK, ItemGroup.BUILDING_BLOCKS),
            item(DARKROCK_BRICKS, ItemGroup.BUILDING_BLOCKS),
            item(MOSSY_DARKROCK_BRICKS, ItemGroup.BUILDING_BLOCKS),
            item(CRACKED_DARKROCK_BRICKS, ItemGroup.BUILDING_BLOCKS),
            item(DARKROCK_TILES, ItemGroup.BUILDING_BLOCKS),
            item(MOSSY_DARKROCK_TILES, ItemGroup.BUILDING_BLOCKS),
            item(CRACKED_DARKROCK_TILES, ItemGroup.BUILDING_BLOCKS),
            item(SMOOTH_DARKROCK, ItemGroup.BUILDING_BLOCKS),
            item(POLISHED_DARKROCK, ItemGroup.BUILDING_BLOCKS),
            item(CHISELED_DARKROCK, ItemGroup.BUILDING_BLOCKS),
            item(DARKROCK_PILLAR, ItemGroup.BUILDING_BLOCKS)
        );
    }

    private static Block block(String id, Block block) {
        return block.setRegistryName(NaturesDebris.resLoc(id));
    }

    private static Block rock(String id, double hardness, double resistance, boolean dark) {
        return block(id, new Block(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static Block rockPillar(String id, double hardness, double resistance, boolean dark) {
        return block(id, new RotatedPillarBlock(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static BlockItem item(Block block, Item.Properties props) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;
        BlockItem item = new BlockItem(block, props);
        item.setRegistryName(id);
        return item;
    }

    private static BlockItem item(Block block, ItemGroup group) {
        return item(block, new Item.Properties().group(group));
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static Block inj() {
        return null;
    }
}
