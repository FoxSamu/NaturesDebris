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
import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rgsw.MathUtil;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A black, viscous fluid that can burn.
 */
public abstract class OilFluid extends RegularFluid implements ICustomRenderFluid, IAluminiumBucketTakeable {
    public static final BooleanProperty BURNING = BooleanProperty.create( "burning" );
    public static final IntegerProperty LEVEL = IntegerProperty.create( "level", 1, 16 );

    @Override
    public Fluid getFlowingFluid() {
        return MDFluids.FLOWING_OIL;
    }

    @Override
    public Fluid getStillFluid() {
        return MDFluids.OIL;
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

    @Override
    public Item getFilledAluminiumBucket() {
        return Items.LAVA_BUCKET; // TODO
    }

    public OilFluid() {
        super( LEVEL, 16 );
        setDefaultState( stateContainer.getBaseState().with( BURNING, false ) );
    }

    @Override
    protected void animateTick( World world, BlockPos pos, IFluidState state, Random random ) {
        if( state.get( BURNING ) ) {
            BlockPos up = pos.up();
            float h00 = MDFluids.getFluidHeight( world, pos, this, state, - 1 );
            float h01 = MDFluids.getFluidHeight( world, pos.south(), this, state, - 1 );
            float h10 = MDFluids.getFluidHeight( world, pos.east(), this, state, - 1 );
            float h11 = MDFluids.getFluidHeight( world, pos.east().south(), this, state, - 1 );

            if( world.getBlockState( up ).isAir( world, up ) && ! world.getBlockState( up ).isOpaqueCube( world, up ) ) {
                for( int i = 0; i < 30; i++ ) {
                    float lx = random.nextFloat();
                    float lz = random.nextFloat();

                    float h0 = MathUtil.lerp( h00, h01, lz );
                    float h1 = MathUtil.lerp( h10, h11, lz );
                    float height = MathUtil.lerp( h0, h1, lx );
                    double x = pos.getX() + lx;
                    double y = pos.getY() + height;
                    double z = pos.getZ() + lz;
                    // Make sure the particle is forced to render: we want an obvious indication of burning oil
                    world.addParticle( ParticleTypes.FLAME, true, x, y, z, 0, 0, 0 );
                    if( random.nextInt( 6 ) == 0 ) {
                        world.addParticle( ParticleTypes.SMOKE, x, y, z, 0, 0, 0 );
                    }
                }
            }

            if( random.nextInt( 100 ) == 0 ) {
                world.playSound( pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false );
            }

            if( random.nextInt( 200 ) == 0 ) {
                world.playSound( pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false );
            }
        }
    }

    @Override
    public void randomTick( World world, BlockPos pos, IFluidState state, Random random ) {
        if( state.get( BURNING ) ) {
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

                for( Direction facing : Direction.Plane.HORIZONTAL ) {
                    if( random.nextInt( 2 ) == 0 ) {
                        IFluidState fs = world.getFluidState( pos.offset( facing ) );
                        if( isEquivalentTo( fs.getFluid() ) ) {
                            world.setBlockState( pos.offset( facing ), fs.with( BURNING, true ).getBlockState() );
                        }
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
        return MDParticleTypes.OIL_DRIPPING;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock( IWorld world, BlockPos pos, BlockState state ) {
        TileEntity te = state.getBlock().hasTileEntity( world.getBlockState( pos ) )
                        ? world.getTileEntity( pos )
                        : null;
        Block.spawnDrops( state, world.getWorld(), pos, te );
    }

    @Override
    public int getSlopeFindDistance( IEnviromentBlockReader world ) {
        return 3; // TODO: 4 in hell
    }

    @Override
    public BlockState getBlockState( IFluidState state ) {
        return MDBlocks.OIL.getDefaultState().with( blockLevel, getLevelFromState( state ) ).with( BURNING, state.get( BURNING ) );
    }

    @Override
    public boolean isEquivalentTo( Fluid fluid ) {
        return fluid == MDFluids.OIL || fluid == MDFluids.FLOWING_OIL;
    }

    @Override
    public int getLevelDecreasePerBlock( IEnviromentBlockReader world ) {
        return 3; // TODO: 2 in hell
    }

    @Override
    public int getTickRate( IWorldReader world ) {
        return 15; // TODO: 7 in hell
    }


    public boolean canOtherFlowInto( IFluidState state, Fluid fluid, Direction direction ) {
        return false;
    }

    @Override
    public boolean isIn( Tag<Fluid> tag ) {
        return tag.contains( this );
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public ResourceLocation getStill() {
        return new ResourceLocation( "modernity:liquid/oil_still" );
    }

    @Override
    public ResourceLocation getFlowing() {
        return new ResourceLocation( "modernity:liquid/oil_flow" );
    }

    @Override
    public ResourceLocation getOverlay() {
        return null;
    }

    @Override
    public int getColor( IFluidState state, BlockPos pos, IEnviromentBlockReader world ) {
        return 0xffffff;
    }


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
    protected IFluidState applyAdditionalState( IFluidState source, IFluidState flow ) {
        return flow.with( BURNING, source.get( BURNING ) );
    }

    private boolean isSelfOrEmpty( IFluidState state ) {
        return state.isEmpty() || isEquivalentTo( state.getFluid() );
    }

    @Override
    protected boolean canFlow( IBlockReader world, BlockPos fromPos, BlockState fromBlockState, Direction direction, BlockPos toPos, BlockState toBlockState, IFluidState toFluidState, Fluid fluid ) {
        return super.canFlow( world, fromPos, fromBlockState, direction, toPos, toBlockState, toFluidState, fluid ) && isSelfOrEmpty( toFluidState );
    }

    @Override
    protected boolean canFlowVerticalInto( IBlockReader world, Fluid fluid, BlockPos pos, BlockState state, BlockPos adjpos, BlockState adjState ) {
        return super.canFlowVerticalInto( world, fluid, pos, state, adjpos, adjState ) && isSelfOrEmpty( adjState.getFluidState() );
    }

    @Override
    public boolean reactWithNeighbors( World world, BlockPos pos, BlockState state ) {
        boolean shouldCatchFire = false;
        boolean shouldExtinguish = false;

        for( Direction facing : Direction.values() ) {
            if( facing != Direction.DOWN && world.getFluidState( pos.offset( facing ) ).isTagged( FluidTags.WATER ) ) {
                shouldExtinguish = true;
            }
            if( world.getFluidState( pos.offset( facing ) ).isTagged( FluidTags.LAVA ) ) {
                shouldCatchFire = true;
            }
            if( world.getBlockState( pos.offset( facing ) ).getBlock() == Blocks.FIRE ) {
                shouldCatchFire = true;
            }
        }

        if( shouldExtinguish ) {
            IFluidState fstate = world.getFluidState( pos );
            world.setBlockState( pos, fstate.with( BURNING, false ).getBlockState() );
            triggerMixEffects( world, pos );
            return true;
        }

        if( shouldCatchFire ) {
            IFluidState fstate = world.getFluidState( pos );
            world.setBlockState( pos, fstate.with( BURNING, true ).getBlockState() );
        }

        return true;
    }

    @Override
    protected boolean canFlowInto( IFluidState state, IBlockReader world, BlockPos pos, Fluid fluid, Direction facing ) {
        return facing == Direction.DOWN && ! fluid.isIn( FluidTags.WATER );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
        super.fillStateContainer( builder );
        builder.add( BURNING );
    }

    public static class Flowing extends OilFluid {
        @Override
        protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
            super.fillStateContainer( builder );
            builder.add( LEVEL );
        }

        @Override
        public int getLevel( IFluidState state ) {
            return state.get( LEVEL );
        }

        @Override
        public boolean isSource( IFluidState state ) {
            return false;
        }
    }

    public static class Source extends OilFluid {
        @Override
        public int getLevel( IFluidState state ) {
            return 16;
        }

        @Override
        public boolean isSource( IFluidState state ) {
            return true;
        }
    }
}
