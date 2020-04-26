/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.generic.util;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IItemDrop {
    void drop( BlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune );

    IItemDrop EMPTY = ( state, drops, world, pos, fortune ) -> drops.add( ItemStack.EMPTY );

    static IItemDrop basicDrop( IItemProvider item, int amount, int fortuneMultiplier, int random ) {
        return ( state, drops, world, pos, fortune ) -> {
            int quantity = amount + fortune * fortuneMultiplier + world.rand.nextInt( random );
            drops.add( new ItemStack( item, quantity ) );
        };
    }
}
