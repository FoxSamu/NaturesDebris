/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.Tag;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

import modernity.api.block.IColoredBlock;
import modernity.api.util.ColorUtil;
import modernity.api.util.EcoBlockPos;
import modernity.client.util.BiomeValues;
import modernity.common.block.prop.SignedIntegerProperty;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockHangLeaves extends BlockLeaves {
    public static final int MAX_DIST = 10;
    public static final SignedIntegerProperty DISTANCE = SignedIntegerProperty.create( "distance", - 1, MAX_DIST );

    private final Tag<Block> logTag;

    public BlockHangLeaves( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties, Item.Properties itemProps ) {
        super( id, sapling, properties, itemProps );
        this.logTag = logTag;
        setDefaultState( stateContainer.getBaseState().with( DISTANCE, MAX_DIST ) );
    }

    public BlockHangLeaves( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties ) {
        super( id, sapling, properties );
        this.logTag = logTag;
        setDefaultState( stateContainer.getBaseState().with( DISTANCE, MAX_DIST ) );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean ticksRandomly( IBlockState state ) {
        return state.get( DISTANCE ) == MAX_DIST || super.ticksRandomly( state );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void randomTick( IBlockState state, World world, BlockPos pos, Random random ) {
        super.randomTick( state, world, pos, random );
        if( state.get( DISTANCE ) == MAX_DIST ) {
            state.dropBlockAsItem( world, pos, 0 );
            world.removeBlock( pos );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void tick( IBlockState state, World world, BlockPos pos, Random random ) {
        if( ! world.isRemote ) {
            world.setBlockState( pos, updateDistance( state, world, pos ), 2 | 4 );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        int dist = getDistance( facingState ) + 1;
        if( dist != 1 || state.get( DISTANCE ) != dist ) {
            world.getPendingBlockTicks().scheduleTick( currentPos, this, 1 );
        }

        return state;
    }

    private IBlockState updateDistance( IBlockState state, IWorld world, BlockPos pos ) {
        // Persistent leaves
        if( state.get( DISTANCE ) == 0 ) return state;

        int dist = MAX_DIST;

        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            if( state.get( DISTANCE ) == - 1 ) { // Hanging leaves
                rpos.setPos( pos ).moveUp();
                IBlockState upState = world.getBlockState( rpos );
                if( upState.getBlock() instanceof BlockHangLeaves ) {
                    if( upState.get( DISTANCE ) < 7 ) {
                        dist = - 1;
                    }
                }
            } else { // Canopy leaves
                for( EnumFacing facing : EnumFacing.values() ) {
                    rpos.setPos( pos ).move( facing );
                    dist = Math.min( dist, getDistance( world.getBlockState( rpos ) ) + 1 );
                    if( dist == 1 ) {
                        break;
                    }
                }
            }
        }

        return state.with( DISTANCE, dist );
    }

    private int getDistance( IBlockState neighbor ) {
        if( logTag.contains( neighbor.getBlock() ) ) {
            return 0;
        } else {
            int dist = neighbor.getBlock() instanceof BlockHangLeaves ? neighbor.get( DISTANCE ) : MAX_DIST;
            if( dist == - 1 ) return MAX_DIST;
            if( dist == 0 ) return 1;
            return dist;
        }
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( DISTANCE );
    }

    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        return getDefaultState().with( DISTANCE, 0 );
    }

    public static class ColoredFoliage extends BlockHangLeaves implements IColoredBlock {

        private static final int DEFAULT_COLOR = ColorUtil.rgb( 32, 86, 49 );

        public ColoredFoliage( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties, Item.Properties itemProps ) {
            super( id, logTag, sapling, properties, itemProps );
        }

        public ColoredFoliage( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties ) {
            super( id, logTag, sapling, properties );
        }

        @Override
        public int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex ) {
            return BiomeValues.get( reader, pos, BiomeValues.FOLIAGE_COLOR );
        }

        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return DEFAULT_COLOR;
        }
    }
}
