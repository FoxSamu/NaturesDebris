package natures.debris.common.item.group;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import natures.debris.common.block.NdBlocks;

public class NdItemGroup extends ItemGroup {
    private static final List<NdItemGroup> GROUPS = Lists.newArrayList();

    public static final NdItemGroup BUILDING_BLOCKS = new NdItemGroup(
        "blocks",
        () -> NdBlocks.MURKY_GRASS_BLOCK.asItem().getDefaultInstance(),
        ItemSubgroup.NATURE,
        ItemSubgroup.LOGS,
        ItemSubgroup.PLANKS,
        ItemSubgroup.ROCK,
        ItemSubgroup.DARKROCK,
        ItemSubgroup.PLANK_SLABS,
        ItemSubgroup.ROCK_SLABS,
        ItemSubgroup.DARKROCK_SLABS,
        ItemSubgroup.PLANK_STAIRS,
        ItemSubgroup.ROCK_STAIRS,
        ItemSubgroup.DARKROCK_STAIRS,
        ItemSubgroup.PLANK_STEPS,
        ItemSubgroup.ROCK_STEPS,
        ItemSubgroup.DARKROCK_STEPS
    );
    public static final NdItemGroup DECORATIONS = new NdItemGroup(
        "decorations",
        () -> NdBlocks.INVER_FENCE.asItem().getDefaultInstance(),
        ItemSubgroup.DECORATIONS,
        ItemSubgroup.FENCES,
        ItemSubgroup.ROCK_WALLS,
        ItemSubgroup.DARKROCK_WALLS
    );

    private final ItemSubgroup[] subgroups;
    private final Supplier<ItemStack> iconFactory;

    public NdItemGroup(String label, Supplier<ItemStack> iconFactory, ItemSubgroup... subgroups) {
        super(label);
        this.iconFactory = iconFactory;
        this.subgroups = subgroups;
        GROUPS.add(this);
    }

    public NdItemGroup(int index, String label, Supplier<ItemStack> iconFactory, ItemSubgroup... subgroups) {
        super(index, label);
        this.iconFactory = iconFactory;
        this.subgroups = subgroups;
        GROUPS.add(this);
    }

    @Override
    public String getPath() {
        return super.getPath();
    }

    @Override
    public String getTranslationKey() {
        return "itemgroup.ndebris." + this.getTabLabel();
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        Set<Item> added = Sets.newHashSet();
        for (ItemSubgroup subgroup : subgroups) {
            subgroup.getSortedItems()
                    .forEach(item -> {
                        item.fillItemGroup(this, items);
                        added.add(item);
                    });
        }
        for (Item item : ForgeRegistries.ITEMS) {
            if (!added.contains(item)) {
                item.fillItemGroup(this, items);
            }
        }
    }

    @Override
    public ItemStack createIcon() {
        return iconFactory.get();
    }

    public static List<NdItemGroup> getGroups() {
        return Collections.unmodifiableList(GROUPS);
    }
}
