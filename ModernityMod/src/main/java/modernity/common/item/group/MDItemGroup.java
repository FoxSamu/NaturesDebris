/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.item.group;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Holder class for Modernity creative tabs.
 */
// TODO: API for item groups
public class MDItemGroup extends ItemGroup {
    public static final MDItemGroup BLOCKS = builder("modernity.blocks")
                                                 .path("blocks")
                                                 .iconItem(() -> Items.AIR) // murky grass block
                                                 .itemCategory(ItemGroupCategory.NATURE)
                                                 .itemCategory(ItemGroupCategory.MINERALS)
                                                 .itemCategory(ItemGroupCategory.BUILDING)
                                                 .itemCategory(ItemGroupCategory.BUILDING_SLABS)
                                                 .itemCategory(ItemGroupCategory.BUILDING_STAIRS)
                                                 .itemCategory(ItemGroupCategory.BUILDING_STEPS)
                                                 .itemCategory(ItemGroupCategory.BUILDING_CORNERS)
                                                 .build();
    public static final MDItemGroup DECORATIVES = builder("modernity.decoratives")
                                                      .path("decoratives")
                                                      .iconItem(() -> Items.AIR) // pebbles
                                                      .build();
    public static final MDItemGroup MISC = builder("modernity.misc")
                                               .path("misc")
                                               .iconItem(() -> Items.AIR) // aluminium ingot
                                               .build();
    public static final MDItemGroup PLANTS = builder("modernity.plants")
                                                 .path("plants")
                                                 .iconItem(() -> Items.AIR) // mint plant
                                                 .build();
    public static final MDItemGroup COMBAT = builder("modernity.combat")
                                                 .path("combat")
                                                 .iconItem(() -> Items.AIR) // aluminium sword
                                                 .build();
    public static final MDItemGroup TOOLS = builder("modernity.tools")
                                                .path("tools")
                                                .iconItem(() -> Items.AIR) // aluminium pickaxe
                                                .build();
    private static final ArrayList<MDItemGroup> MD_ITEM_GROUPS = new ArrayList<>();
    private final Supplier<ItemStack> icon;
    private final ItemGroupCategory[] categories;
    private int labelColor;
    private int slotColor;
    private ResourceLocation tabsTexture;
    private ResourceLocation backgroundTexture;
    private String translationKey;

    private MDItemGroup(String label, Supplier<ItemStack> icon, ItemGroupCategory[] categories) {
        super(label);
        this.icon = icon;
        this.categories = categories;

        MD_ITEM_GROUPS.add(this);
    }

    public static List<MDItemGroup> getModernityItemGroups() {
        return Collections.unmodifiableList(MD_ITEM_GROUPS);
    }

    public static Builder builder(String label) {
        return new Builder(label);
    }

    @Override
    public int getLabelColor() {
        return labelColor;
    }

    public MDItemGroup setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        return this;
    }

    @Override
    public int getSlotColor() {
        return slotColor;
    }

    public MDItemGroup setSlotColor(int slotColor) {
        this.slotColor = slotColor;
        return this;
    }

    @Override
    public ItemStack createIcon() {
        return icon.get();
    }

    public ResourceLocation getTabsTexture() {
        return tabsTexture == null ? super.getTabsImage() : tabsTexture;
    }

    protected MDItemGroup setTabsTexture(ResourceLocation tabsTexture) {
        this.tabsTexture = tabsTexture;
        return this;
    }

    public ResourceLocation getBackgroundTexture() {
        return backgroundTexture == null ? super.getBackgroundImage() : backgroundTexture;
    }

    protected MDItemGroup setBackgroundTexture(ResourceLocation backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    protected void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        List<Item> itemList = Lists.newArrayList();
        for(Item item : ForgeRegistries.ITEMS) {
            itemList.add(item);
        }

        for(ItemGroupCategory cgry : categories) {
            cgry.export(item -> {
                item.fillItemGroup(this, items);
                itemList.remove(item);
            });
        }

        for(Item item : itemList) {
            item.fillItemGroup(this, items);
        }
    }

    public static class Builder {
        private final String label;
        private final ArrayList<ItemGroupCategory> sortingBlocks = new ArrayList<>();
        private String path;
        private String translationKey;
        private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
        private String backgroundTexture;
        private ResourceLocation customBackgroundTexture;
        private ResourceLocation customTabsTexture;
        private boolean scrollbar = true;
        private boolean title = true;
        private EnchantmentType[] relevantEnchantmentTypes = new EnchantmentType[0];
        private int labelColor = 0x404040;
        private int slotSelectColor = 0x80ffffff;

        private Builder(String label) {
            this.label = label;
            this.translationKey = "itemgroup." + label;
            this.path = label;
        }

        public Builder icon(Supplier<ItemStack> value) {
            if(value == null) throw new NullPointerException();
            icon = value;
            return this;
        }

        public Builder iconItem(Supplier<IItemProvider> value) {
            if(value == null) throw new NullPointerException();
            icon = () -> new ItemStack(value.get());
            return this;
        }

        public Builder translationKey(String value) {
            if(value == null) throw new NullPointerException();
            translationKey = value;
            return this;
        }

        public Builder path(String value) {
            if(value == null) throw new NullPointerException();
            path = value;
            return this;
        }

        public Builder backgroundTexture(String value) {
            if(value == null) throw new NullPointerException();
            backgroundTexture = value;
            customBackgroundTexture = null;
            return this;
        }

        public Builder backgroundTexture(ResourceLocation value) {
            if(value == null) throw new NullPointerException();
            backgroundTexture = null;
            customBackgroundTexture = value;
            return this;
        }

        public Builder tabsTexture(ResourceLocation value) {
            if(value == null) throw new NullPointerException();
            customTabsTexture = value;
            return this;
        }

        public Builder noScrollbar() {
            scrollbar = false;
            return this;
        }

        public Builder noTitle() {
            title = false;
            return this;
        }

        public Builder relevantEnchantmentTypes(EnchantmentType... types) {
            if(types == null) throw new NullPointerException();
            relevantEnchantmentTypes = types;
            return this;
        }

        public Builder labelColor(int value) {
            labelColor = value;
            return this;
        }

        public Builder slotSelectionColor(int value) {
            slotSelectColor = value;
            return this;
        }

        public Builder itemCategory(ItemGroupCategory block) {
            sortingBlocks.add(block);
            return this;
        }

        public MDItemGroup build() {
            MDItemGroup group = new MDItemGroup(label, icon, sortingBlocks.toArray(new ItemGroupCategory[0]));
            group.setTabPath(path);
            group.setTranslationKey(translationKey);
            group.setLabelColor(labelColor).setSlotColor(slotSelectColor);
            group.setBackgroundTexture(customBackgroundTexture);
            if(backgroundTexture != null) group.setBackgroundImageName(backgroundTexture);
            group.setTabsTexture(customTabsTexture);
            if(!scrollbar) group.setNoScrollbar();
            if(!title) group.setNoTitle();
            group.setRelevantEnchantmentTypes(relevantEnchantmentTypes);
            return group;
        }
    }
}
