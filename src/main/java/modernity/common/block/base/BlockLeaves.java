/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.block.base;

import modernity.api.block.IColoredBlock;
import modernity.api.util.ColorUtil;
import modernity.api.util.MovingBlockPos;
import modernity.client.particle.LeafParticle;
import modernity.client.util.ProxyClient;
import modernity.common.block.MDBlocks;
import modernity.common.util.ProxyCommon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockLeaves extends BlockBase implements IShearable {

    private final IItemProvider sapling;

    public BlockLeaves( String id, IItemProvider sapling, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        this.sapling = sapling;
    }

    public BlockLeaves( String id, IItemProvider sapling, Properties properties ) {
        super( id, properties );
        this.sapling = sapling;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public int getOpacity( IBlockState state, IBlockReader world, BlockPos pos ) {
        return 1;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void animateTick( IBlockState state, World world, BlockPos pos, Random rand ) {
        if( world.isRainingAt( pos.up() ) && ! world.getBlockState( pos.down() ).isTopSolid() && rand.nextInt( 15 ) == 1 ) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + rand.nextFloat();
            world.addParticle( Particles.DRIPPING_WATER, x, y, z, 0, 0, 0 );
        }
        if( hasFallingLeaf( state, world, pos, rand ) && ! world.getBlockState( pos.down() ).isTopSolid() ) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + rand.nextFloat();
            int color = getFallingLeafColor( state, world, pos, rand );
            double r = ( color >>> 16 & 0xff ) / 255D;
            double g = ( color >>> 8 & 0xff ) / 255D;
            double b = ( color & 0xff ) / 255D;
            int p = Minecraft.getInstance().gameSettings.particleSetting;
            if( p == 0 || p == 1 && rand.nextBoolean() ) {
                Minecraft.getInstance().particles.addEffect( new LeafParticle( world, x, y, z, 0, 0, 0, r, g, b ) );
            }
        }

    }

    @OnlyIn( Dist.CLIENT )
    protected int getFallingLeafColor( IBlockState state, World world, BlockPos pos, Random rand ) {
        int color = Minecraft.getInstance().getBlockColors().getColor( state, world, pos, 0 );
        if( color == - 1 ) color = 0xffffff;
        return ColorUtil.darken( color, rand.nextDouble() * 0.5 - 0.25 );
    }

    @OnlyIn( Dist.CLIENT )
    public void spawnDecayLeaves( BlockPos pos, Random rand, World world, IBlockState state ) {
        for( int i = 0; i < 10; i ++ ) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() + rand.nextFloat();
            double z = pos.getZ() + rand.nextFloat();
            int color = getFallingLeafColor( state, world, pos, rand );
            double r = ( color >>> 16 & 0xff ) / 255D;
            double g = ( color >>> 8 & 0xff ) / 255D;
            double b = ( color & 0xff ) / 255D;
            int p = Minecraft.getInstance().gameSettings.particleSetting;
            if( p == 0 || p == 1 && rand.nextBoolean() ) {
                Minecraft.getInstance().particles.addEffect( new LeafParticle( world, x, y, z, 0, 0, 0, r, g, b ) );
            }
        }
    }

    protected boolean hasFallingLeaf( IBlockState state, World world, BlockPos pos, Random rand ) {
        return rand.nextInt( 256 ) == 1;
    }

    protected boolean generatesHumus( IBlockState state ) {
        return true;
    }

    protected void generateHumus( IBlockState state, World world, BlockPos pos ) {
        MovingBlockPos mpos = new MovingBlockPos( pos.down() );
        for( int i = 0; i < 17; i++ ) {
            IBlockState belowState = world.getBlockState( mpos );

            if( ! belowState.isTopSolid( world, pos ) && ! belowState.getMaterial().isLiquid() && belowState.getBlock() != this ) {
                mpos.moveDown();
                continue;
            }

            // TODO: Tag
            if( belowState.getBlock() == MDBlocks.DARK_DIRT ) {
                world.setBlockState( mpos, MDBlocks.HUMUS.getDefaultState() );
            }
            break;
        }
    }

    @Override
    public boolean ticksRandomly( IBlockState state ) {
        return generatesHumus( state );
    }

    @Override
    public void randomTick( IBlockState state, World world, BlockPos pos, Random rand ) {
        if( generatesHumus( state ) ) {
            generateHumus( state, world, pos );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public int quantityDropped( IBlockState state, Random random ) {
        return random.nextInt( 20 ) == 0 ? 1 : 0;
    }

    @Override
    public IItemProvider getItemDropped( IBlockState state, World world, BlockPos pos, int fortune ) {
        return sapling;
    }

    @Override
    public void getDrops( IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune ) {
        int saplingDropChance = getSaplingDropChance( state );
        if( fortune > 0 ) {
            saplingDropChance -= 2 << fortune;
            if( saplingDropChance < 10 ) {
                saplingDropChance = 10;
            }
        }

        if( world.rand.nextInt( saplingDropChance ) == 0 ) {
            ItemStack drop = new ItemStack( getItemDropped( state, world, pos, fortune ) );
            if( ! drop.isEmpty() )
                drops.add( drop );
        }

        saplingDropChance = 200;
        if( fortune > 0 ) {
            saplingDropChance -= 10 << fortune;
            if( saplingDropChance < 40 ) {
                saplingDropChance = 40;
            }
        }

        captureDrops( true );
        dropApple( world, pos, state, saplingDropChance );
        drops.addAll( captureDrops( false ) );
    }

    protected void dropApple( World world, BlockPos pos, IBlockState state, int chance ) {
    }

    protected int getSaplingDropChance( IBlockState state ) {
        return 20;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return ProxyCommon.get().fancyGraphics() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean causesSuffocation( IBlockState state ) {
        return false;
    }

    @Override
    public List<ItemStack> onSheared( @Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune ) {
        world.setBlockState( pos, Blocks.AIR.getDefaultState(), 11 );
        return Collections.singletonList( new ItemStack( this ) );
    }

    public static class ColoredBlackwood extends BlockLeaves implements IColoredBlock {

        public ColoredBlackwood( String id, IItemProvider sapling, Properties properties, Item.Properties itemProps ) {
            super( id, sapling, properties, itemProps );
        }

        public ColoredBlackwood( String id, IItemProvider sapling, Properties properties ) {
            super( id, sapling, properties );
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
}
