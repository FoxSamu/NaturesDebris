/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.fluid;

import modernity.api.block.fluid.IAluminiumBucketTakeable;
import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.RegularFluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * The lava-like fluid of the Modernity.
 */
public abstract class HeatrockFluid extends RegularFluid implements ICustomRenderFluid, IAluminiumBucketTakeable {
    @Override
    public Fluid getFlowingFluid() {
        return MDFluids.FLOWING_HEATROCK;
    }

    @Override
    public Fluid getStillFluid() {
        return MDFluids.HEATROCK;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public Item getFilledBucket() {
        return Items.LAVA_BUCKET;
    }

    @Override // TODO
    public Item getFilledAluminiumBucket() {
        return Items.LAVA_BUCKET;
    }

    @Override
    protected void animateTick( World world, BlockPos pos, IFluidState state, Random random ) {
        BlockPos up = pos.up();
        if( world.getBlockState( up ).isAir( world, up ) && ! world.getBlockState( up ).isOpaqueCube( world, up ) ) {
            if( random.nextInt( 100 ) == 0 ) {
                double x = pos.getX() + random.nextFloat();
                double y = pos.getY() + 1;
                double z = pos.getZ() + random.nextFloat();
                world.addParticle( ParticleTypes.LAVA, x, y, z, 0, 0, 0 );
                world.playSound( x, y, z, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false );
            }

            if( random.nextInt( 200 ) == 0 ) {
                world.playSound( pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false );
            }
        }
    }

    @Override
    public void randomTick( World world, BlockPos pos, IFluidState state, Random random ) {
        if( world.getGameRules().getBoolean( GameRules.DO_FIRE_TICK ) ) {
            int rand = random.nextInt( 3 );
            if( rand > 0 ) {
                BlockPos firePos = pos;

                for( int i = 0; i < rand; ++ i ) {
                    firePos = firePos.add( random.nextInt( 3 ) - 1, 1, random.nextInt( 3 ) - 1 );
                    if( ! world.isBlockPresent( firePos ) ) {
                        return;
                    }

                    BlockState burnableState = world.getBlockState( firePos );
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

    private boolean isSurroundingBlockFlammable( IWorldReader world, BlockPos pos ) {
        for( Direction facing : Direction.values() ) {
            if( getCanBlockBurn( world, pos.offset( facing ) ) ) {
                return true;
            }
        }

        return false;
    }

    private boolean getCanBlockBurn( IWorldReader world, BlockPos pos ) {
        return pos.getY() < 0 || pos.getY() >= 256 || world.isBlockLoaded( pos ) && world.getBlockState( pos ).getMaterial().isFlammable();
    }

    @Override
    @Nullable
    @OnlyIn( Dist.CLIENT )
    public IParticleData getDripParticleData() {
        return ParticleTypes.DRIPPING_LAVA;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock( IWorld world, BlockPos pos, BlockState state ) {
        this.triggerEffects( world, pos );
    }

    @Override
    public int getSlopeFindDistance( IEnviromentBlockReader world ) {
        return 2; // TODO: 4 in hell
    }

    @Override
    public BlockState getBlockState( IFluidState state ) {
        return MDBlocks.HEATROCK.getDefaultState().with( blockLevel, getLevelFromState( state ) );
    }

    @Override
    public boolean isEquivalentTo( Fluid fluid ) {
        return fluid == MDFluids.HEATROCK || fluid == MDFluids.FLOWING_HEATROCK;
    }

    @Override
    public int getLevelDecreasePerBlock( IEnviromentBlockReader world ) {
        return 2; // TODO: 1 in hell
    }

    @Override
    public int getTickRate( IWorldReader world ) {
        return 30; // TODO: 10 in hell
    }

    @Override
    public boolean isIn( Tag<Fluid> tag ) {
        return tag == FluidTags.LAVA || tag.contains( this );
    }

    @Override
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
    public int getColor( IFluidState state, BlockPos pos, IEnviromentBlockReader world ) {
        return 0xffffff;
    }


    /**
     * Plays mix effects.
     */
    protected void triggerEffects( IWorld world, BlockPos pos ) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        world.playSound( null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + ( world.getRandom().nextFloat() - world.getRandom().nextFloat() ) * 0.8F );

        for( int i = 0; i < 8; ++ i ) {
            world.addParticle( ParticleTypes.LARGE_SMOKE, x + Math.random(), y + 1.2, z + Math.random(), 0, 0, 0 );
        }
    }

    @Override
    protected void flowInto( IWorld world, BlockPos pos, BlockState bstate, Direction direction, IFluidState fstate ) {
        if( direction == Direction.DOWN ) {
            IFluidState downState = world.getFluidState( pos );
            if( isIn( FluidTags.LAVA ) && downState.isTagged( FluidTags.WATER ) ) {
                if( bstate.getBlock() instanceof RegularFluidBlock ) {
                    world.setBlockState( pos, MDBlocks.ROCK.getDefaultState(), 3 );
                }

                triggerEffects( world, pos );
                return;
            }
        }

        super.flowInto( world, pos, bstate, direction, fstate );
    }

    @Override
    public boolean reactWithNeighbors( World world, BlockPos pos, BlockState state ) {
        boolean shouldReact = false;

        for( Direction facing : Direction.values() ) {
            if( facing != Direction.DOWN && world.getFluidState( pos.offset( facing ) ).isTagged( FluidTags.WATER ) ) {
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

            if( fstate.func_223408_f() >= 0.4444444F ) {
                world.setBlockState( pos, MDBlocks.DARKROCK.getDefaultState() );
                triggerMixEffects( world, pos );
                return false;
            }
        }

        return true;
    }

    @Override
    protected boolean canFlowInto( IFluidState state, IBlockReader world, BlockPos pos, Fluid fluid, Direction facing ) {
        return facing == Direction.DOWN && ! fluid.isIn( FluidTags.WATER );
    }

    public static class Flowing extends HeatrockFluid {
        @Override
        protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
            super.fillStateContainer( builder );
            builder.add( BlockStateProperties.LEVEL_1_8 );
        }

        @Override
        public int getLevel( IFluidState state ) {
            return state.get( BlockStateProperties.LEVEL_1_8 );
        }

        @Override
        public boolean isSource( IFluidState state ) {
            return false;
        }
    }

    public static class Source extends HeatrockFluid {
        @Override
        public int getLevel( IFluidState state ) {
            return 8;
        }

        @Override
        public boolean isSource( IFluidState state ) {
            return true;
        }
    }
}
