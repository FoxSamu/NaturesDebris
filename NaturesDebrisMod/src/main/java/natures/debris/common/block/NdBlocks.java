package natures.debris.common.block;

import natures.debris.common.NaturesDebris;
import net.minecraft.block.Block;
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

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
            rock("rock", 1.5, 6, false)
        );
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(
            item(ROCK, ItemGroup.BUILDING_BLOCKS)
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
