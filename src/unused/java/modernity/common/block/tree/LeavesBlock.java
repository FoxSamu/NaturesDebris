/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.block.tree;

import modernity.api.util.ColorUtil;
import modernity.common.particle.MDParticleTypes;
import modernity.common.particle.RgbParticleData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;

import java.util.Random;

/**
 * Describes a non-decayable leaves block
 */
@SuppressWarnings( "deprecation" )
public class LeavesBlock extends Block implements IShearable {
    public LeavesBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public int getOpacity( BlockState state, IBlockReader world, BlockPos pos ) {
        return 1;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void animateTick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( world.isRainingAt( pos.up() ) && ! world.getBlockState( pos.down() ).func_224755_d( world, pos.down(), Direction.UP ) && rand.nextInt( 15 ) == 1 ) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + rand.nextFloat();
            world.addParticle( ParticleTypes.DRIPPING_WATER, x, y, z, 0, 0, 0 );
        }
        if( hasFallingLeaf( state, world, pos, rand ) && ! world.getBlockState( pos.down() ).func_224755_d( world, pos.down(), Direction.UP ) ) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + rand.nextFloat();
            int color = getFallingLeafColor( state, world, pos, rand );
            world.addParticle( new RgbParticleData( MDParticleTypes.FALLING_LEAF, color ), x, y, z, 0, 0, 0 );
        }
    }

    @OnlyIn( Dist.CLIENT )
    protected int getFallingLeafColor( BlockState state, World world, BlockPos pos, Random rand ) {
        int color = Minecraft.getInstance().getBlockColors().getColor( state, world, pos, 0 );
        if( color == - 1 ) color = 0xffffff;
        return ColorUtil.darken( color, rand.nextDouble() * 0.5 - 0.25 );
    }

    @OnlyIn( Dist.CLIENT )
    public void spawnDecayLeaves( BlockPos pos, Random rand, World world, BlockState state ) {
        for( int i = 0; i < 10; i++ ) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() + rand.nextFloat();
            double z = pos.getZ() + rand.nextFloat();
            int color = getFallingLeafColor( state, world, pos, rand );
            world.addParticle( new RgbParticleData( MDParticleTypes.FALLING_LEAF, color ), x, y, z, 0, 0, 0 );
        }
    }

    /**
     * Return true randomly when a leaf particle needs to be dropped. Decaying leaves override this to drop more leaves
     * in the need-to-decay state.
     */
    protected boolean hasFallingLeaf( BlockState state, World world, BlockPos pos, Random rand ) {
        return rand.nextInt( 256 ) == 1;
    }

    @Override
    public void randomTick( BlockState state, World world, BlockPos pos, Random rand ) {
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean causesSuffocation( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        return false;
    }
}
