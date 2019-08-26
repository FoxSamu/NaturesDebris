/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
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
import modernity.client.util.ProxyClient;
import modernity.common.util.MDEvents;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDecayLeaves extends BlockLeaves {
    public static final int MAX_DIST = 10;
    public static final IntegerProperty DISTANCE = IntegerProperty.create( "distance", 0, MAX_DIST );

    private final Tag<Block> logTag;

    public BlockDecayLeaves( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties, Item.Properties itemProps ) {
        super( id, sapling, properties, itemProps );
        this.logTag = logTag;
        setDefaultState( stateContainer.getBaseState().with( DISTANCE, MAX_DIST ) );
    }

    public BlockDecayLeaves( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties ) {
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
            world.playEvent( MDEvents.LEAVES_DECAY, pos, Block.BLOCK_STATE_IDS.get( state ) );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void tick( IBlockState state, World world, BlockPos pos, Random random ) {
        world.setBlockState( pos, updateDistance( state, world, pos ), 2 | 4 );
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
            for( EnumFacing facing : EnumFacing.values() ) {
                rpos.setPos( pos ).move( facing );
                dist = Math.min( dist, getDistance( world.getBlockState( rpos ) ) + 1 );
                if( dist == 1 ) {
                    break;
                }
            }
        }

        return state.with( DISTANCE, dist );
    }

    private int getDistance( IBlockState neighbor ) {
        if( logTag.contains( neighbor.getBlock() ) ) {
            return 0;
        } else {
            int dist = neighbor.getBlock() instanceof BlockDecayLeaves ? neighbor.get( DISTANCE ) : MAX_DIST;
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

    @Override
    protected boolean hasFallingLeaf( IBlockState state, World world, BlockPos pos, Random rand ) {
        if( state.get( DISTANCE ) == MAX_DIST ) {
            return rand.nextInt( 48 ) == 1;
        }
        return super.hasFallingLeaf( state, world, pos, rand );
    }

    public static class ColoredBlackwood extends BlockDecayLeaves implements IColoredBlock {

        private static final int DEFAULT_COLOR = ColorUtil.rgb( 32, 86, 49 );

        public ColoredBlackwood( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties, Item.Properties itemProps ) {
            super( id, logTag, sapling, properties, itemProps );
        }

        public ColoredBlackwood( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties ) {
            super( id, logTag, sapling, properties );
        }

        @Override
        public int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex ) {
            return ProxyClient.get().getBlackwoodColors().getColor( reader, pos );
        }

        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return ProxyClient.get().getBlackwoodColors().getItemColor();
        }
    }

    public static class ColoredInver extends BlockDecayLeaves implements IColoredBlock {

        public ColoredInver( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties, Item.Properties itemProps ) {
            super( id, logTag, sapling, properties, itemProps );
        }

        public ColoredInver( String id, Tag<Block> logTag, IItemProvider sapling, Properties properties ) {
            super( id, logTag, sapling, properties );
        }

        @Override
        public int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex ) {
            return ProxyClient.get().getInverColors().getColor( reader, pos );
        }

        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return ProxyClient.get().getInverColors().getItemColor();
        }
    }
}
