/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.api.util.MovingBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.redgalaxy.util.Lazy;

import java.util.Random;
import java.util.function.Supplier;

public class LeaflessDirtBlock extends SnowyDirtlikeBlock {
    private final Lazy<BlockState> leafyState;
    private final Tag<Block> validLeaves;

    public LeaflessDirtBlock( Properties properties, Supplier<BlockState> leafy, Tag<Block> validLeaves ) {
        super( properties );
        this.leafyState = Lazy.of( leafy );
        this.validLeaves = validLeaves;
    }

    public void makeLeafy( World world, BlockPos pos, BlockState state ) {
        if( state.getBlock() == this ) {
            world.setBlockState( pos, leafyState.get() );
        }
    }

    @Override
    public boolean ticksRandomly( BlockState state ) {
        return true;
    }

    @Override
    public void tick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( rand.nextInt( 2 ) == 0 ) {
            MovingBlockPos mpos = new MovingBlockPos( pos ).moveUp();
            for( int i = 1; i < 18; i++ ) {
                if( world.getBlockState( mpos ).getMaterial().blocksMovement() ) {
                    break;
                }
                if( world.getBlockState( mpos ).isIn( validLeaves ) ) {
                    makeLeafy( world, pos, state );
                    break;
                }
                mpos.moveUp();
            }
        }
    }
}
