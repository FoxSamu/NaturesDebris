/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.block.IColoredBlock;
import modernity.api.util.ColorUtil;
import modernity.client.util.BiomeValues;
import modernity.common.block.MDBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDirt extends BlockBase {
    public static final Type TYPE_DIRT = new Type( "dark_dirt", false, false, BlockRenderLayer.SOLID );
    public static final Type TYPE_GRASS = new Type( "dark_grass", true, true, BlockRenderLayer.CUTOUT_MIPPED );
    public static final Type TYPE_COARSE_DIRT = new Type( "coarse_dark_dirt", false, false, BlockRenderLayer.SOLID );
    public static final Type TYPE_HUMUS = new Type( "humus", false, false, BlockRenderLayer.SOLID );

    public static final BooleanProperty SNOWY = BooleanProperty.create( "snowy" );

    public final Type type;

    public BlockDirt( Type type, Properties properties, Item.Properties itemProps ) {
        super( type.id, properties.tickRandomly(), itemProps );
        this.type = type;

        setDefaultState( getStateContainer().getBaseState().with( SNOWY, false ) );
    }

    public BlockDirt( Type type, Properties properties ) {
        super( type.id, properties.tickRandomly() );
        this.type = type;

        setDefaultState( getStateContainer().getBaseState().with( SNOWY, false ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( SNOWY );
    }

    private static boolean canRemainAt( IWorldReaderBase world, BlockPos pos ) {
        BlockPos up = pos.up();
        return world.getLight( up ) >= 4 || world.getBlockState( up ).getOpacity( world, up ) < world.getMaxLightLevel();
    }

    private static boolean canGrowAt( IWorldReaderBase world, BlockPos pos ) {
        BlockPos up = pos.up();
        return world.getLight( up ) >= 4 && world.getBlockState( up ).getOpacity( world, up ) < world.getMaxLightLevel() && ! world.getFluidState( up ).isTagged( FluidTags.WATER );
    }

    @SuppressWarnings( "deprecation" )
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( ! type.canBeSnowy ) return state;
        if( facing != EnumFacing.UP ) {
            return super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        } else {
            Block block = facingState.getBlock();
            return state.with( SNOWY, block == Blocks.SNOW_BLOCK || block == Blocks.SNOW );
        }
    }

    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        if( ! type.canBeSnowy ) return this.getDefaultState();
        Block block = context.getWorld().getBlockState( context.getPos().up() ).getBlock();
        return this.getDefaultState().with( SNOWY, block == Blocks.SNOW_BLOCK || block == Blocks.SNOW );
    }

    @SuppressWarnings( "deprecation" )
    public void tick( IBlockState state, World world, BlockPos pos, Random random ) {
        if( type.canSpread ) {
            if( ! world.isRemote ) {
                if( ! world.isAreaLoaded( pos, 3 ) )
                    return;
                if( ! canRemainAt( world, pos ) ) {
                    world.setBlockState( pos, MDBlocks.DARK_DIRT.getDefaultState() );
                } else {
                    if( world.getLight( pos.up() ) >= 9 ) {
                        for( int i = 0; i < 4; ++ i ) {
                            BlockPos growPos = pos.add( random.nextInt( 3 ) - 1, random.nextInt( 5 ) - 3, random.nextInt( 3 ) - 1 );
                            if( ! world.isBlockPresent( growPos ) ) {
                                return;
                            }

                            if( world.getBlockState( growPos ).getBlock() == MDBlocks.DARK_DIRT && canGrowAt( world, growPos ) ) {
                                world.setBlockState( growPos, this.getDefaultState() );
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public IItemProvider getItemDropped( IBlockState state, World worldIn, BlockPos pos, int fortune ) {
        return MDBlocks.DARK_DIRT;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return type.renderLayer;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isSolid( IBlockState state ) {
        return true; // Make sure we're solid to enable face culling on grass
    }

    public static final class Type {
        public final String id;
        public final boolean canSpread;
        public final boolean canBeSnowy;
        public final BlockRenderLayer renderLayer;

        public Type( String id, boolean canSpread, boolean canBeSnowy, BlockRenderLayer renderLayer ) {
            this.id = id;
            this.canSpread = canSpread;
            this.canBeSnowy = canBeSnowy;
            this.renderLayer = renderLayer;
        }
    }

    public static class ColoredGrass extends BlockDirt implements IColoredBlock {
        protected static final int GRASS_ITEM_COLOR = ColorUtil.rgb( 0, 109, 38 );

        public ColoredGrass( Type type, Properties properties, Item.Properties itemProps ) {
            super( type, properties, itemProps );
        }

        public ColoredGrass( Type type, Properties properties ) {
            super( type, properties );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex ) {
            return BiomeValues.get( reader, pos, BiomeValues.GRASS_COLOR );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return GRASS_ITEM_COLOR;
        }

        @Override
        public boolean addDestroyEffects( IBlockState state, World world, BlockPos pos, ParticleManager manager ) {
            return false;
        }
    }
}
