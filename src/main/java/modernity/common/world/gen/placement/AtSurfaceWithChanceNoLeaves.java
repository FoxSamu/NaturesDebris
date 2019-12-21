/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 21 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.placement;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.base.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class AtSurfaceWithChanceNoLeaves extends Placement<ChanceConfig> {
    public AtSurfaceWithChanceNoLeaves() {
        super( dyn -> null );
    }

    @Override
    public Stream<BlockPos> getPositions( IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn, Random random, ChanceConfig configIn, BlockPos pos ) {
        if( random.nextFloat() < 1.0F / (float) configIn.chance ) {
            int i = random.nextInt( 16 );
            int j = random.nextInt( 16 );
            BlockPos blockpos = worldIn.getHeight( Heightmap.Type.MOTION_BLOCKING, pos.add( i, 0, j ) );
            MovingBlockPos mpos = new MovingBlockPos( blockpos.down() );
            while( worldIn.getBlockState( mpos ).getBlock() instanceof LeavesBlock || worldIn.getBlockState( mpos ).getBlock() instanceof net.minecraft.block.LeavesBlock ) {
                mpos.moveDown();
            }
            return Stream.of( mpos.moveUp().toImmutable() );
        } else {
            return Stream.empty();
        }
    }
}