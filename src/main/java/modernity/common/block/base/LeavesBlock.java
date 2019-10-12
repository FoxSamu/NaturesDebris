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
import modernity.client.ModernityClient;
import modernity.common.block.MDBlocks;
import modernity.common.particle.MDParticleTypes;
import modernity.common.particle.RgbParticleData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings( "deprecation" )
public class LeavesBlock extends Block implements IShearable {
    public LeavesBlock( Properties properties ) {
        super( properties );
    }

    @Override
    @SuppressWarnings( "deprecation" )
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

    protected boolean hasFallingLeaf( BlockState state, World world, BlockPos pos, Random rand ) {
        return rand.nextInt( 256 ) == 1;
    }

    protected boolean generatesHumus( BlockState state ) {
        return true;
    }

    protected void generateHumus( BlockState state, World world, BlockPos pos ) {
        MovingBlockPos mpos = new MovingBlockPos( pos.down() );
        for( int i = 0; i < 17; i++ ) {
            BlockState belowState = world.getBlockState( mpos );

            if( ! belowState.func_224755_d( world, pos, Direction.UP ) && ! belowState.getMaterial().isLiquid() && belowState.getBlock() != this ) {
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
    public boolean ticksRandomly( BlockState state ) {
        return generatesHumus( state );
    }

    @Override
    public void randomTick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( generatesHumus( state ) ) {
            generateHumus( state, world, pos );
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean causesSuffocation( BlockState state, IBlockReader worldIn, BlockPos pos ) {
        return false;
    }

    public static class ColoredBlackwood extends LeavesBlock implements IColoredBlock {

        public ColoredBlackwood( String id, Properties properties ) {
            super( properties );
        }

        @Override
        public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
            return ModernityClient.get().getBlackwoodColors().getColor( reader, pos );
        }

        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return ModernityClient.get().getBlackwoodColors().getItemColor();
        }
    }
}
