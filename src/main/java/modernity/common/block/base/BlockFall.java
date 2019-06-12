/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 12 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.item.Item;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.tags.Tag;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.common.entity.EntityFallBlock;

import java.util.Random;

@SuppressWarnings( "deprecation" )
public class BlockFall extends BlockBase {
    private final int dustColor;

    public BlockFall( String id, int dustColor, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        this.dustColor = dustColor;
    }

    public BlockFall( String id, int dustColor, Properties properties ) {
        super( id, properties );
        this.dustColor = dustColor;
    }

    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        world.getPendingBlockTicks().scheduleTick( currentPos, this, tickRate( world ) );
        return super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
    }

    @Override
    public void onBlockAdded( IBlockState state, World world, BlockPos pos, IBlockState oldState ) {
        world.getPendingBlockTicks().scheduleTick( pos, this, tickRate( world ) );
    }

    public void tick( IBlockState state, World world, BlockPos pos, Random random ) {
        if( ! world.isRemote ) {
            fall( world, pos );
        }
    }

    private void fall( World world, BlockPos pos ) {
        if( canFallThrough( world.getBlockState( pos.down() ) ) && pos.getY() >= 0 ) {
            if( ! BlockFalling.fallInstantly && world.isAreaLoaded( pos.add( - 32, - 32, - 32 ), pos.add( 32, 32, 32 ) ) ) {
                if( ! world.isRemote ) {
                    EntityFallBlock entity = new EntityFallBlock( world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.getBlockState( pos ) );
                    entity.setFloatIn( floatIn() );
                    entity.setGravityScale( (float) gravityScale() );
                    onStartFalling( entity );
                    world.spawnEntity( entity );
                }
            } else {
                IBlockState state = getDefaultState();
                if( world.getBlockState( pos ).getBlock() == this ) {
                    state = world.getBlockState( pos );
                    world.removeBlock( pos );
                }

                BlockPos landPos;
                landPos = pos.down();
                while( canFallThrough( world.getBlockState( landPos ) ) && landPos.getY() > 0 ) {
                    landPos = landPos.down();
                }

                if( landPos.getY() > 0 ) {
                    world.setBlockState( landPos.up(), state );
                }
            }

        }
    }

    protected void onStartFalling( EntityFallBlock fallingEntity ) {
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate( IWorldReaderBase world ) {
        return 2;
    }

    public static boolean canFallThrough( IBlockState state ) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return state.isAir() || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
    }

    public void onEndFalling( World world, BlockPos pos, IBlockState fallingState, IBlockState hitState ) {
    }

    public void onBroken( World world, BlockPos pos ) {
    }

    @OnlyIn( Dist.CLIENT )
    public void animateTick( IBlockState stateIn, World world, BlockPos pos, Random rand ) {
        if( rand.nextInt( 16 ) == 0 ) {
            BlockPos down = pos.down();
            if( canFallThrough( world.getBlockState( down ) ) ) {
                double x = pos.getX() + rand.nextFloat();
                double y = pos.getY() - 0.05;
                double z = pos.getZ() + rand.nextFloat();
                world.addParticle( new BlockParticleData( Particles.FALLING_DUST, stateIn ), x, y, z, 0, 0, 0 );
            }
        }

    }

    public double gravityScale() {
        return 1;
    }

    public Tag<Fluid> floatIn() {
        return null;
    }

    @OnlyIn( Dist.CLIENT )
    public int getDustColor( IBlockState state ) {
        return dustColor;
    }
}
