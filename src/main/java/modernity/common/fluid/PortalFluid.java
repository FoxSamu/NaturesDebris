/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 28 - 2019
 */

package modernity.common.fluid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.common.block.MDBlocks;
import modernity.common.item.MDItems;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class PortalFluid extends RegularFluid implements ICustomRenderFluid {

    private static final IntegerProperty LEVEL = IntegerProperty.create( "level", 1, 8 );

    public PortalFluid() {
        super( LEVEL, 8 );
    }

    public Fluid getFlowingFluid() {
        return MDFluids.PORTAL_FLOWING;
    }

    public Fluid getStillFluid() {
        return MDFluids.PORTAL;
    }

    @OnlyIn( Dist.CLIENT )
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public Item getFilledBucket() {
        return MDItems.PORTAL_BUCKET;
    }

    @OnlyIn( Dist.CLIENT )
    public void animateTick( World world, BlockPos pos, IFluidState state, Random rand ) {
        if( ! state.isSource() && ! state.get( FALLING ) ) {
            if( rand.nextInt( 64 ) == 0 ) {
                world.playSound( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F, false );
            }
        } else if( rand.nextInt( 10 ) == 0 ) {
            world.addParticle( Particles.UNDERWATER, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0, 0, 0 );
        } else if( rand.nextInt( 5 ) == 0 ) {
            world.addParticle( Particles.SMOKE, pos.getX() + rand.nextFloat(), pos.getY() + getHeight( state ), pos.getZ() + rand.nextFloat(), 0, 0, 0 );
        }

    }

    @Nullable
    @OnlyIn( Dist.CLIENT )
    public IParticleData getDripParticleData() {
        return Particles.DRIPPING_WATER;
    }

    protected boolean canSourcesMultiply() {
        return true;
    }

    protected void beforeReplacingBlock( IWorld world, BlockPos pos, IBlockState state ) {
        state.dropBlockAsItem( world.getWorld(), pos, 0 );
    }

    public int getSlopeFindDistance( IWorldReaderBase world ) {
        return 4;
    }

    public IBlockState getBlockState( IFluidState state ) {
        return MDBlocks.PORTAL_FLUID.getDefaultState().with( blockLevel, getLevelFromState( state ) );
    }

    public boolean isEquivalentTo( Fluid fluid ) {
        return fluid == MDFluids.PORTAL || fluid == MDFluids.PORTAL_FLOWING;
    }

    public int getLevelDecreasePerBlock( IWorldReaderBase world ) {
        return 1;
    }

    public int getTickRate( IWorldReaderBase world ) {
        return 5;
    }

    public boolean canOtherFlowInto( IFluidState state, Fluid fluid, EnumFacing direction ) {
        return direction == EnumFacing.UP && ! fluid.isIn( FluidTags.WATER );
    }

    @Override
    public boolean isIn( Tag<Fluid> tag ) {
        return tag == FluidTags.WATER || tag.contains( this );
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public ResourceLocation getStill() {
        return new ResourceLocation( "modernity:liquid/portal_liquid_still" );
    }

    @Override
    public ResourceLocation getFlowing() {
        return new ResourceLocation( "modernity:liquid/portal_liquid_flow" );
    }

    @Override
    public ResourceLocation getOverlay() {
        return new ResourceLocation( "modernity:liquid/portal_liquid_still" );
    }

    @Override
    public int getColor( IFluidState state, BlockPos pos, IWorldReaderBase world ) {
        return 0xffffff;
    }

    public static class Flowing extends PortalFluid {
        protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
            super.fillStateContainer( builder );
            builder.add( LEVEL );
        }

        public int getLevel( IFluidState state ) {
            return state.get( LEVEL );
        }

        public boolean isSource( IFluidState state ) {
            return false;
        }
    }

    public static class Source extends PortalFluid {
        public int getLevel( IFluidState state ) {
            return maxLevel;
        }

        public boolean isSource( IFluidState state ) {
            return true;
        }
    }
}
