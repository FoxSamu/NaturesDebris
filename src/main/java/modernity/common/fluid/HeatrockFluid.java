/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.fluid;

import modernity.api.block.fluid.IAluminiumBucketTakeable;
import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.BlockFluid;
import modernity.common.item.MDItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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

import javax.annotation.Nullable;
import java.util.Random;

public abstract class HeatrockFluid extends RegularFluid implements ICustomRenderFluid, IAluminiumBucketTakeable {
    public Fluid getFlowingFluid() {
        return MDFluids.HEATROCK_FLUID_FLOWING;
    }

    public Fluid getStillFluid() {
        return MDFluids.HEATROCK_FLUID;
    }

    @OnlyIn( Dist.CLIENT )
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    public Item getFilledBucket() {
        return Items.LAVA_BUCKET;
    }

    @Override
    public Item getFilledAluminiumBucket() {
        return MDItems.ALUMINIUM_HEATROCK_BUCKET;
    }

    @Override
    protected void animateTick( World world, BlockPos pos, IFluidState state, Random random ) {
        BlockPos up = pos.up();
        if( world.getBlockState( up ).isAir( world, up ) && ! world.getBlockState( up ).isOpaqueCube( world, up ) ) {
            if( random.nextInt( 100 ) == 0 ) {
                double x = pos.getX() + random.nextFloat();
                double y = pos.getY() + 1;
                double z = pos.getZ() + random.nextFloat();
                world.addParticle( Particles.LAVA, x, y, z, 0, 0, 0 );
                world.playSound( x, y, z, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false );
            }

            if( random.nextInt( 200 ) == 0 ) {
                world.playSound( pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false );
            }
        }
    }

    public void randomTick( World world, BlockPos pos, IFluidState state, Random random ) {
        if( world.getGameRules().getBoolean( "doFireTick" ) ) {
            int rand = random.nextInt( 3 );
            if( rand > 0 ) {
                BlockPos firePos = pos;

                for( int i = 0; i < rand; ++ i ) {
                    firePos = firePos.add( random.nextInt( 3 ) - 1, 1, random.nextInt( 3 ) - 1 );
                    if( ! world.isBlockPresent( firePos ) ) {
                        return;
                    }

                    IBlockState burnableState = world.getBlockState( firePos );
                    if( burnableState.isAir( world, firePos ) ) {
                        if( isSurroundingBlockFlammable( world, firePos ) ) {
                            world.setBlockState( firePos, Blocks.FIRE.getDefaultState() );
                            return;
                        }
                    } else if( burnableState.getMaterial().blocksMovement() ) {
                        return;
                    }
                }
            } else {
                for( int k = 0; k < 3; ++ k ) {
                    BlockPos firePos = pos.add( random.nextInt( 3 ) - 1, 0, random.nextInt( 3 ) - 1 );
                    if( ! world.isBlockPresent( firePos ) ) {
                        return;
                    }

                    if( world.isAirBlock( firePos.up() ) && getCanBlockBurn( world, firePos ) ) {
                        world.setBlockState( firePos.up(), Blocks.FIRE.getDefaultState() );
                    }
                }
            }

        }
    }

    private boolean isSurroundingBlockFlammable( IWorldReaderBase world, BlockPos pos ) {
        for( EnumFacing facing : EnumFacing.values() ) {
            if( getCanBlockBurn( world, pos.offset( facing ) ) ) {
                return true;
            }
        }

        return false;
    }

    private boolean getCanBlockBurn( IWorldReaderBase world, BlockPos pos ) {
        return pos.getY() < 0 || pos.getY() >= 256 || world.isBlockLoaded( pos ) && world.getBlockState( pos ).getMaterial().isFlammable();
    }

    @Nullable
    @OnlyIn( Dist.CLIENT )
    public IParticleData getDripParticleData() {
        return Particles.DRIPPING_LAVA;
    }

    protected boolean canSourcesMultiply() {
        return false;
    }

    protected void beforeReplacingBlock( IWorld worldIn, BlockPos pos, IBlockState state ) {
        state.dropBlockAsItem( worldIn.getWorld(), pos, 0 );
    }

    public int getSlopeFindDistance( IWorldReaderBase world ) {
        return 2; // TODO: 4 in hell
    }

    public IBlockState getBlockState( IFluidState state ) {
        return MDBlocks.HEATROCK_FLUID.getDefaultState().with( blockLevel, getLevelFromState( state ) );
    }

    public boolean isEquivalentTo( Fluid fluid ) {
        return fluid == MDFluids.HEATROCK_FLUID || fluid == MDFluids.HEATROCK_FLUID_FLOWING;
    }

    public int getLevelDecreasePerBlock( IWorldReaderBase world ) {
        return 2; // TODO: 1 in hell
    }

    public int getTickRate( IWorldReaderBase world ) {
        return 30; // TODO: 10 in hell
    }

    public boolean canOtherFlowInto( IFluidState state, Fluid fluid, EnumFacing direction ) {
        return direction == EnumFacing.UP && ! fluid.isIn( FluidTags.WATER );
    }

    @Override
    public boolean isIn( Tag<Fluid> tag ) {
        return tag == FluidTags.LAVA || tag.contains( this );
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public ResourceLocation getStill() {
        return new ResourceLocation( "modernity:liquid/heatrock_still" );
    }

    @Override
    public ResourceLocation getFlowing() {
        return new ResourceLocation( "modernity:liquid/heatrock_flow" );
    }

    @Override
    public ResourceLocation getOverlay() {
        return null;
    }

    @Override
    public int getColor( IFluidState state, BlockPos pos, IWorldReaderBase world ) {
        return 0xffffff;
    }


    protected void triggerEffects( IWorld world, BlockPos pos ) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        world.playSound( null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + ( world.getRandom().nextFloat() - world.getRandom().nextFloat() ) * 0.8F );

        for( int i = 0; i < 8; ++ i ) {
            world.addParticle( Particles.LARGE_SMOKE, x + Math.random(), y + 1.2, z + Math.random(), 0, 0, 0 );
        }
    }

    protected void flowInto( IWorld world, BlockPos pos, IBlockState bstate, EnumFacing direction, IFluidState fstate ) {
        if( direction == EnumFacing.DOWN ) {
            IFluidState downState = world.getFluidState( pos );
            if( isIn( FluidTags.LAVA ) && downState.isTagged( FluidTags.WATER ) ) {
                if( bstate.getBlock() instanceof BlockFluid ) {
                    world.setBlockState( pos, MDBlocks.ROCK.getDefaultState(), 3 );
                }

                triggerEffects( world, pos );
                return;
            }
        }

        super.flowInto( world, pos, bstate, direction, fstate );
    }

    public boolean reactWithNeighbors( World world, BlockPos pos, IBlockState state ) {
        boolean shouldReact = false;

        for( EnumFacing facing : EnumFacing.values() ) {
            if( facing != EnumFacing.DOWN && world.getFluidState( pos.offset( facing ) ).isTagged( FluidTags.WATER ) ) {
                shouldReact = true;
                break;
            }
        }

        if( shouldReact ) {
            IFluidState fstate = world.getFluidState( pos );
            if( fstate.isSource() ) {
                world.setBlockState( pos, MDBlocks.BASALT.getDefaultState() );
                triggerMixEffects( world, pos );
                return false;
            }

            if( fstate.getHeight() >= 0.4444444F ) {
                world.setBlockState( pos, MDBlocks.DARKROCK.getDefaultState() );
                triggerMixEffects( world, pos );
                return false;
            }
        }

        return true;
    }

    public static class Flowing extends HeatrockFluid {
        protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
            super.fillStateContainer( builder );
            builder.add( BlockStateProperties.LEVEL_1_8 );
        }

        public int getLevel( IFluidState state ) {
            return state.get( BlockStateProperties.LEVEL_1_8 );
        }

        public boolean isSource( IFluidState state ) {
            return false;
        }
    }

    public static class Source extends HeatrockFluid {
        public int getLevel( IFluidState state ) {
            return 8;
        }

        public boolean isSource( IFluidState state ) {
            return true;
        }
    }
}
