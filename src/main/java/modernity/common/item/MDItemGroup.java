/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 17 - 2020
 * Author: rgsw
 */

package modernity.common.item;

import modernity.common.block.MDBlocks;
import modernity.common.item.sorting.AllSortingBlock;
import modernity.common.item.sorting.ItemSortingBlock;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Holder class for Modernity creative tabs.
 */
public class MDItemGroup extends ItemGroup {
    public static final MDItemGroup BLOCKS = builder( "modernity.blocks" )
                                                 .iconItem( () -> MDBlocks.MURKY_GRASS_BLOCK )
                                                 .build();

    public static final MDItemGroup DECORATIVES = builder( "modernity.decoratives" )
                                                      .iconItem( () -> MDBlocks.PEBBLES )
                                                      .build();

    public static final MDItemGroup MISC = builder( "modernity.misc" )
                                               .iconItem( () -> MDItems.ALUMINIUM_INGOT )
                                               .build();

    public static final MDItemGroup PLANTS = builder( "modernity.plants" )
                                                 .iconItem( () -> MDBlocks.MINT_PLANT )
                                                 .build();

    public static final MDItemGroup COMBAT = builder( "modernity.combat" )
                                                 .iconItem( () -> MDItems.ALUMINIUM_SWORD )
                                                 .build();

    public static final MDItemGroup TOOLS = builder( "modernity.tools" )
                                                .iconItem( () -> MDItems.ALUMINIUM_PICKAXE )
                                                .build();

    private final Supplier<ItemStack> icon;
    private int labelColor;
    private int slotColor;
    private ResourceLocation tabsTexture;
    private ResourceLocation backgroundTexture;
    private String translationKey;
    private final ItemSortingBlock[] sortingBlocks;

    private MDItemGroup( String label, Supplier<ItemStack> icon, ItemSortingBlock[] sortingBlocks ) {
        super( label );
        this.icon = icon;
        this.sortingBlocks = sortingBlocks;
    }

    @Override
    public int getLabelColor() {
        return labelColor;
    }

    @Override
    public int getSlotColor() {
        return slotColor;
    }

    public MDItemGroup setLabelColor( int labelColor ) {
        this.labelColor = labelColor;
        return this;
    }

    public MDItemGroup setSlotColor( int slotColor ) {
        this.slotColor = slotColor;
        return this;
    }

    @Override
    public ItemStack createIcon() {
        return icon.get();
    }

    protected MDItemGroup setTabsTexture( ResourceLocation tabsTexture ) {
        this.tabsTexture = tabsTexture;
        return this;
    }

    protected MDItemGroup setBackgroundTexture( ResourceLocation backgroundTexture ) {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    public ResourceLocation getTabsTexture() {
        return tabsTexture == null ? super.getTabsImage() : tabsTexture;
    }

    public ResourceLocation getBackgroundTexture() {
        return backgroundTexture == null ? super.getBackgroundImage() : backgroundTexture;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    protected void setTranslationKey( String translationKey ) {
        this.translationKey = translationKey;
    }

    @Override
    public void fill( NonNullList<ItemStack> items ) {
        for( ItemSortingBlock block : sortingBlocks ) {
            block.reset();
        }
        NonNullList<ItemStack> itemStacks = NonNullList.create();
        for( Item item : ForgeRegistries.ITEMS ) {
            if( item == MDBlocks.MURKY_DIRT.asItem() ) {

            }
            item.fillItemGroup( this, itemStacks );
        }
        for( ItemStack stack : itemStacks ) {
            for( ItemSortingBlock block : sortingBlocks ) {
                if( block.acceptItem( stack ) ) {
                    break;
                }
            }
        }
        for( ItemSortingBlock block : sortingBlocks ) {
            block.fill( items );
        }
    }

    public static Builder builder( String label ) {
        return new Builder( label );
    }

    public static class Builder {
        private final String label;
        private String path;
        private String translationKey;

        private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;

        private String backgroundTexture;
        private ResourceLocation customBackgroundTexture;
        private ResourceLocation customTabsTexture;

        private boolean scrollbar = true;
        private boolean title = true;

        private EnchantmentType[] relevantEnchantmentTypes = new EnchantmentType[ 0 ];

        private int labelColor = 0x404040;
        private int slotSelectColor = 0x80ffffff;

        private final ArrayList<ItemSortingBlock> sortingBlocks = new ArrayList<>();

        private Builder( String label ) {
            this.label = label;
            this.translationKey = "itemgroup." + label;
            this.path = label;
        }

        public Builder icon( Supplier<ItemStack> value ) {
            if( value == null ) throw new NullPointerException();
            icon = value;
            return this;
        }

        public Builder iconItem( Supplier<IItemProvider> value ) {
            if( value == null ) throw new NullPointerException();
            icon = () -> new ItemStack( value.get() );
            return this;
        }

        public Builder translationKey( String value ) {
            if( value == null ) throw new NullPointerException();
            translationKey = value;
            return this;
        }

        public Builder path( String value ) {
            if( value == null ) throw new NullPointerException();
            path = value;
            return this;
        }

        public Builder backgroundTexture( String value ) {
            if( value == null ) throw new NullPointerException();
            backgroundTexture = value;
            customBackgroundTexture = null;
            return this;
        }

        public Builder backgroundTexture( ResourceLocation value ) {
            if( value == null ) throw new NullPointerException();
            backgroundTexture = null;
            customBackgroundTexture = value;
            return this;
        }

        public Builder tabsTexture( ResourceLocation value ) {
            if( value == null ) throw new NullPointerException();
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

        public Builder relevantEnchantmentTypes( EnchantmentType... types ) {
            if( types == null ) throw new NullPointerException();
            relevantEnchantmentTypes = types;
            return this;
        }

        public Builder labelColor( int value ) {
            labelColor = value;
            return this;
        }

        public Builder slotSelectionColor( int value ) {
            slotSelectColor = value;
            return this;
        }

        public Builder sortingBlock( ItemSortingBlock block ) {
            sortingBlocks.add( block );
            return this;
        }

        public MDItemGroup build() {
            sortingBlock( new AllSortingBlock() );
            MDItemGroup group = new MDItemGroup( label, icon, sortingBlocks.toArray( new ItemSortingBlock[ 0 ] ) );
            group.setTabPath( path );
            group.setTranslationKey( translationKey );
            group.setLabelColor( labelColor ).setSlotColor( slotSelectColor );
            group.setBackgroundTexture( customBackgroundTexture );
            if( backgroundTexture != null ) group.setBackgroundImageName( backgroundTexture );
            group.setTabsTexture( customTabsTexture );
            if( ! scrollbar ) group.setNoScrollbar();
            if( ! title ) group.setNoTitle();
            group.setRelevantEnchantmentTypes( relevantEnchantmentTypes );
            return group;
        }
    }
}
