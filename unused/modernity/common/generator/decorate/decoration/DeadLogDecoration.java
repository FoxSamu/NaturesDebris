/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import modernity.generic.block.ISolidBlock;
import modernity.generic.util.MovingBlockPos;
import modernity.common.block.base.AxisBlock;
import modernity.common.generator.blocks.IBlockGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DeadLogDecoration implements IDecoration {
    private final int minLength;
    private final int maxLength;
    private final IBlockGenerator mushrooms;
    private final BlockState logX;
    private final BlockState logY;
    private final BlockState logZ;

    public DeadLogDecoration( int minLength, int maxLength, IBlockGenerator mushrooms, BlockState log ) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.mushrooms = mushrooms;
        this.logX = log.getBlockState().with( AxisBlock.AXIS, Direction.Axis.X );
        this.logY = log.getBlockState().with( AxisBlock.AXIS, Direction.Axis.Y );
        this.logZ = log.getBlockState().with( AxisBlock.AXIS, Direction.Axis.Z );
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        ArrayList<Direction> directions = new ArrayList<>( Arrays.asList(
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST
        ) );

        int length = rand.nextInt( maxLength - minLength + 1 ) + minLength;

        MovingBlockPos mpos = new MovingBlockPos();

        while( ! directions.isEmpty() ) {
            Direction dir = directions.remove( rand.nextInt( directions.size() ) );
            if( canGenerateInDirection( world, pos, mpos, length, dir ) ) {
                generateInDirection( world, pos, mpos, length, dir, rand );
                return;
            }
        }
    }

    private boolean canGenerateInDirection( IWorld world, BlockPos pos, MovingBlockPos mpos, int length, Direction dir ) {
        for( int i = 0; i < length; i++ ) {
            mpos.setPos( pos ).move( dir, i ).moveDown();
            if( ! ISolidBlock.isSolid( world, mpos, Direction.UP ) ) {
                return false;
            }
            mpos.moveUp();
            if( world.getBlockState( mpos ).getMaterial().blocksMovement() ) {
                return false;
            }
        }

        return true;
    }

    private void generateInDirection( IWorld world, BlockPos pos, MovingBlockPos mpos, int length, Direction dir, Random rand ) {
        for( int i = 0; i < length; i++ ) {
            mpos.setPos( pos ).move( dir, i );
            if( i == 0 ) {
                world.setBlockState( mpos, logY, 2 | 16 );
                if( rand.nextInt( 4 ) == 0 ) {
                    mushrooms.generateBlock( world, mpos.moveUp(), rand );
                }
            } else if( i > 1 ) {
                world.setBlockState( mpos, dir.getAxis() == Direction.Axis.X ? logX : logZ, 2 | 16 );
                if( rand.nextInt( 4 ) == 0 ) {
                    mushrooms.generateBlock( world, mpos.moveUp(), rand );
                }
            }
        }
    }
}
