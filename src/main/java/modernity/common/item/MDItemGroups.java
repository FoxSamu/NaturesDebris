/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.item;

import modernity.common.block.MDBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Holder class for Modernity creative tabs.
 */
public final class MDItemGroups {
    public static final ItemGroup BLOCKS = new ItemGroup( "blocks" ) {
        @Override
        public String getTranslationKey() {
            return "itemgroup.modernity.blocks";
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDBlocks.DARK_GRASS_BLOCK.asItem() );
        }
    };
    public static final ItemGroup DECORATIVES = new ItemGroup( "decoratives" ) {
        @Override
        public String getTranslationKey() {
            return "itemgroup.modernity.decoratives";
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDBlocks.LIGHTROCK_TORCH.asItem() );
        }
    };
    public static final ItemGroup MISC = new ItemGroup( "misc" ) {
        @Override
        public String getTranslationKey() {
            return "itemgroup.modernity.misc";
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDItems.ALUMINIUM_INGOT );
        }
    };
    public static final ItemGroup PLANTS = new ItemGroup( "plants" ) {
        @Override
        public String getTranslationKey() {
            return "itemgroup.modernity.plants";
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDBlocks.MINT_PLANT.asItem() );
        }
    };
    public static final ItemGroup COMBAT = new ItemGroup( "combat" ) {
        @Override
        public String getTranslationKey() {
            return "itemgroup.modernity.combat";
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDItems.ALUMINIUM_SWORD );
        }
    };
    public static final ItemGroup TOOLS = new ItemGroup( "tools" ) {
        @Override
        public String getTranslationKey() {
            return "itemgroup.modernity.tools";
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDItems.ALUMINIUM_SWORD );
        }
    };

    private MDItemGroups() {
    }
}
