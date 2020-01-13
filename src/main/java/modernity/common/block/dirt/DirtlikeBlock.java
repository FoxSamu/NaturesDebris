/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.common.block.base.DigableBlock;
import modernity.common.util.MDLightUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class DirtlikeBlock extends DigableBlock {
    public DirtlikeBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean ticksRandomly( BlockState state ) {
        return this instanceof ISpreadableDirt || this instanceof IDecayableDirt;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void tick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( ! world.isRemote ) {
            if( ! world.isAreaLoaded( pos, 3 ) ) return;
            boolean spread = true;
            if( this instanceof IDecayableDirt ) {
                if( ( (IDecayableDirt) this ).decayTick( world, pos, state, rand ) ) {
                    spread = false;
                }
            }
            if( spread && this instanceof ISpreadableDirt ) {
                ( (ISpreadableDirt) this ).spreadTick( world, pos, state, rand );
            }
        }
    }

    public static boolean canRemain( World world, BlockPos pos, BlockState state ) {
        BlockPos up = pos.up();
        BlockState upState = world.getBlockState( up );
        if( upState.getBlock() == Blocks.SNOW && upState.get( SnowBlock.LAYERS ) == 1 ) {
            return true;
        } else {
            int opacity = MDLightUtil.getEffectiveOpacity( world, state, pos, upState, up, Direction.UP, upState.getOpacity( world, up ) );
            return opacity < world.getMaxLightLevel();
        }
    }
}
