/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import modernity.common.block.MDBlockTags;

import java.util.Random;

public class BlockSaltCrystal extends BlockSinglePlant {
    public static final IntegerProperty AGE = IntegerProperty.create( "age", 0, 11 );

    public BlockSaltCrystal( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockSaltCrystal( String id, Properties properties ) {
        super( id, properties );
    }

    public boolean isSaltSource( IBlockState state ) {
        return state.isIn( MDBlockTags.SALT_SOURCE );
    }

    @Override
    public boolean ticksRandomly( IBlockState state ) {
        return true;
    }

    @Override
    public void tick( IBlockState state, World worldIn, BlockPos pos, Random random ) {
        super.tick( state, worldIn, pos, random );
    }
}
