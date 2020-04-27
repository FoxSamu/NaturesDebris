/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.base;

import modernity.common.entity.FallBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * Describes a falling block. This is basically vanilla's falling block but with a few tweaks.
 */
@SuppressWarnings( "deprecation" )
public class FallBlock extends Block {
    private final int dustColor;

    public FallBlock( int dustColor, Properties properties ) {
        super( properties );
        this.dustColor = dustColor;
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        world.getPendingBlockTicks().scheduleTick( currentPos, this, tickRate( world ) );
        return super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
    }

    @Override
    public void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving ) {
        world.getPendingBlockTicks().scheduleTick( pos, this, tickRate( world ) );
    }

    @Override
    public void tick( BlockState state, ServerWorld world, BlockPos pos, Random rand ) {
        checkFallable( world, pos );
    }

    /**
     * Check if we can fall.
     */
    private void checkFallable( World world, BlockPos pos ) {
        if( world.isAirBlock( pos.down() ) || canFallThrough( world.getBlockState( pos.down() ) ) && pos.getY() >= 0 ) {
            if( ! world.isRemote ) {
                FallBlockEntity entity = new FallBlockEntity( world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.getBlockState( pos ) );
                entity.setFloatIn( floatIn() );
                entity.setGravityScale( (float) gravityScale() );
                onStartFalling( entity );
                world.addEntity( entity );
            }

        }
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate( IWorldReader world ) {
        return 2;
    }

    public static boolean canFallThrough( BlockState state ) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return state.isAir() || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
    }

    /**
     * Called when the block starts falling
     */
    public void onStartFalling( FallBlockEntity fallingEntity ) {
    }

    /**
     * Called when the block lands
     */
    public void onEndFalling( World world, BlockPos pos, BlockState fallingState, BlockState hitState ) {
    }

    /**
     * Called when the block is broken by any other block (e.g. a torch)
     */
    public void onBroken( World world, BlockPos pos ) {
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void animateTick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( rand.nextInt( 16 ) == 0 ) {
            BlockPos down = pos.down();
            if( canFallThrough( world.getBlockState( down ) ) ) {
                double x = pos.getX() + rand.nextFloat();
                double y = pos.getY() - 0.05;
                double z = pos.getZ() + rand.nextFloat();
                world.addParticle( new BlockParticleData( ParticleTypes.FALLING_DUST, state ), x, y, z, 0, 0, 0 );
            }
        }

    }

    /**
     * Returns the gravity scale of this falling block.
     */
    public double gravityScale() {
        return 1;
    }

    /**
     * Returns a fluid tag this block floats in, or null if it falls through all fluids.
     */
    public Tag<Fluid> floatIn() {
        return null;
    }

    /**
     * Returns the dust color of this falling block.
     */
    @OnlyIn( Dist.CLIENT )
    public int getDustColor( BlockState state ) {
        return dustColor;
    }
}
