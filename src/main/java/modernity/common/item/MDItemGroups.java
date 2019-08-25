/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import modernity.common.block.MDBlocks;

public class MDItemGroups {
    public static final ItemGroup BLOCKS = new ItemGroup( "modernity" ) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack( MDBlocks.ROCK.getBlockItem() );
        }
    };
}
