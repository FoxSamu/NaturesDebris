/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockNoDrop extends BlockBase {
    public BlockNoDrop( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockNoDrop( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    public IItemProvider getItemDropped( IBlockState state, World worldIn, BlockPos pos, int fortune ) {
        return Items.AIR;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public int quantityDropped( IBlockState state, Random random ) {
        return 0;
    }
}
