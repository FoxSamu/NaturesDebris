/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.generator.blocks;

import modernity.common.generator.blocks.condition.IBlockGenCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public class ConditionalBlockGenerator implements IBlockGenerator {
    private final IBlockGenCondition condition;
    private final IBlockGenerator child;

    public ConditionalBlockGenerator( IBlockGenCondition condition, IBlockGenerator child ) {
        this.condition = condition;
        this.child = child;
    }

    @Override
    public boolean generateBlock( IWorld world, BlockPos pos, Random rand ) {
        if( condition.test( world, pos, rand ) ) {
            return child.generateBlock( world, pos, rand );
        }
        return false;
    }
}
