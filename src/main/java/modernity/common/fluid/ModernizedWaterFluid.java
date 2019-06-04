package modernity.common.fluid;

import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
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
import modernity.client.util.MDBiomeValues;
import modernity.common.block.MDBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class ModernizedWaterFluid extends ImprovedFluid implements ICustomRenderFluid {
    public Fluid getFlowingFluid() {
        return MDFluids.MODERNIZED_WATER_FLOWING;
    }

    public Fluid getStillFluid() {
        return MDFluids.MODERNIZED_WATER;
    }

    @OnlyIn( Dist.CLIENT )
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public Item getFilledBucket() {
        return Items.WATER_BUCKET;
    }

    @OnlyIn( Dist.CLIENT )
    public void animateTick( World worldIn, BlockPos pos, IFluidState state, Random random ) {
        if( ! state.isSource() && ! state.get( FALLING ) ) {
            if( random.nextInt( 64 ) == 0 ) {
                worldIn.playSound( (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false );
            }
        } else if( random.nextInt( 10 ) == 0 ) {
            worldIn.addParticle( Particles.UNDERWATER, (double) ( (float) pos.getX() + random.nextFloat() ), (double) ( (float) pos.getY() + random.nextFloat() ), (double) ( (float) pos.getZ() + random.nextFloat() ), 0.0D, 0.0D, 0.0D );
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

    protected void beforeReplacingBlock( IWorld worldIn, BlockPos pos, IBlockState state ) {
        state.dropBlockAsItem( worldIn.getWorld(), pos, 0 );
    }

    public int getSlopeFindDistance( IWorldReaderBase world ) {
        return 4;
    }

    public IBlockState getBlockState( IFluidState state ) {
        return MDBlocks.MODERNIZED_WATER.getDefaultState().with( BlockFlowingFluid.LEVEL, getLevelFromState( state ) );
    }

    public boolean isEquivalentTo( Fluid fluid ) {
        return fluid == MDFluids.MODERNIZED_WATER || fluid == MDFluids.MODERNIZED_WATER_FLOWING;
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
        return tag == FluidTags.WATER;
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public ResourceLocation getStill() {
        return new ResourceLocation( "minecraft:block/water_still" );
    }

    @Override
    public ResourceLocation getFlowing() {
        return new ResourceLocation( "minecraft:block/water_flow" );
    }

    @Override
    public ResourceLocation getOverlay() {
        return new ResourceLocation( "minecraft:block/water_overlay" );
    }

    @Override
    public int getColor( IFluidState state, BlockPos pos, IWorldReaderBase world ) {
        return MDBiomeValues.get( world, pos, MDBiomeValues.WATER_COLOR );
    }

    public static class Flowing extends ModernizedWaterFluid {
        protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
            super.fillStateContainer( builder );
            builder.add( LEVEL_1_TO_8 );
        }

        public int getLevel( IFluidState state ) {
            return state.get( LEVEL_1_TO_8 );
        }

        public boolean isSource( IFluidState state ) {
            return false;
        }
    }

    public static class Source extends ModernizedWaterFluid {
        public int getLevel( IFluidState state ) {
            return 8;
        }

        public boolean isSource( IFluidState state ) {
            return true;
        }
    }
}
