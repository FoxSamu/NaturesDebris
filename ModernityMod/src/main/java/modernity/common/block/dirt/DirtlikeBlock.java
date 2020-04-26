/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.common.block.base.DigableBlock;
import modernity.common.block.dirt.logic.DirtLogic;
import modernity.generic.util.Events;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class DirtlikeBlock extends DigableBlock {
    protected final DirtLogic logic;

    public DirtlikeBlock( DirtLogic logic, Properties properties ) {
        super( properties );
        this.logic = logic;
    }

    @Override
    public boolean ticksRandomly( BlockState state ) {
        return logic.randomTicks();
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void randomTick( BlockState state, ServerWorld world, BlockPos pos, Random rand ) {
        logic.blockUpdate( world, pos, state, rand );
    }

    public DirtLogic getLogic() {
        return logic;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public ActionResultType onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        ItemStack stack = player.getHeldItem( hand );
        if( logic.canGrow( stack ) ) {
            if( ! world.isRemote ) {
                logic.grow( world, pos, state, world.rand );
                world.playEvent( Events.FERTILIZER_USE, pos, 0 );
                if( ! player.abilities.isCreativeMode ) {
                    stack.shrink( 1 );
                    if( stack.isEmpty() ) player.setHeldItem( hand, ItemStack.EMPTY );
                }
            }
            return ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }
}
