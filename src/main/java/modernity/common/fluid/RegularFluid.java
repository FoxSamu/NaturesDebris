/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.fluid;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import modernity.api.block.fluid.IGaseousFluid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.rgsw.MathUtil;

import java.util.Map;

public abstract class RegularFluid extends Fluid {
    public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
    public final IntegerProperty level;
    public final IntegerProperty blockLevel;
    public final int maxLevel;

    // Adjacent fluid cache
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey>> ADJ_FLUID_CACHE = ThreadLocal.withInitial( () -> {
        Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey> map = new Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey>( 200 ) {
            protected void rehash( int hash ) {
            }
        };
        map.defaultReturnValue( (byte) 127 );
        return map;
    } );

    private final EnumFacing down;
    private final EnumFacing up;
    private final boolean isGas;
    private final int fallDirection;

    public RegularFluid( IntegerProperty level, int max ) {
        isGas = this instanceof IGaseousFluid;
        if( isGas ) {
            down = EnumFacing.UP;
            fallDirection = - 1;
        } else {
            down = EnumFacing.DOWN;
            fallDirection = 1;
        }
        up = down.getOpposite();

        this.level = level;
        this.blockLevel = IntegerProperty.create( "level", 0, max );
        this.maxLevel = max;
    }

    public RegularFluid() {
        this( BlockStateProperties.LEVEL_1_8, 8 );
    }

    protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
        builder.add( FALLING );
    }

    public Vec3d getFlow( IWorldReaderBase world, BlockPos pos, IFluidState state ) {
        double xflow = 0.0D;
        double zflow = 0.0D;

        Vec3d normalizedFlow;

        try( BlockPos.PooledMutableBlockPos mpos = BlockPos.PooledMutableBlockPos.retain() ) {
            for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                mpos.setPos( pos ).move( facing );

                IFluidState fluid = world.getFluidState( mpos );
                if( canFlowTo( fluid ) ) {
                    float height = fluid.getHeight();
                    float flowWeight = 0;

                    if( height == 0 ) {
                        if( ! world.getBlockState( mpos ).getMaterial().blocksMovement() ) {
                            IFluidState fluidBelow = world.getFluidState( mpos.down( fallDirection ) );
                            if( canFlowTo( fluidBelow ) ) {
                                height = fluidBelow.getHeight();
                                if( height > 0 ) {
                                    flowWeight = state.getHeight() - ( height - getMaxHeight( fluidBelow ) );
                                }
                            }
                        }
                    } else if( height > 0 ) {
                        flowWeight = state.getHeight() - height;
                    }

                    if( flowWeight != 0 ) {
                        xflow += facing.getXOffset() * flowWeight;
                        zflow += facing.getZOffset() * flowWeight;
                    }
                }
            }

            Vec3d flow = new Vec3d( xflow, 0, zflow );
            if( state.get( FALLING ) ) {
                for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                    mpos.setPos( pos ).move( facing );
                    if( this.causesVerticalCurrent( world, mpos, facing ) || this.causesVerticalCurrent( world, mpos.up( fallDirection ), facing ) ) {
                        flow = flow.normalize().add( 0, - 6 * fallDirection, 0 );
                        break;
                    }
                }
            }

            normalizedFlow = flow.normalize();
        }

        return normalizedFlow;
    }

    private boolean canFlowTo( IFluidState state ) {
        return state.isEmpty() || state.getFluid().isEquivalentTo( this );
    }

    protected boolean causesVerticalCurrent( IBlockReader world, BlockPos pos, EnumFacing facing ) {
        IBlockState bstate = world.getBlockState( pos );
        Block block = bstate.getBlock();
        IFluidState fstate = world.getFluidState( pos );
        if( fstate.getFluid().isEquivalentTo( this ) ) {
            return false;
        } else if( facing == up ) {
            return true;
        } else if( bstate.getMaterial() == Material.ICE ) {
            return false;
        } else {
            boolean b = Block.isExceptBlockForAttachWithPiston( block ) || block instanceof BlockStairs;
            return ! b && bstate.getBlockFaceShape( world, pos, facing ) == BlockFaceShape.SOLID;
        }
    }

    protected void flowAround( IWorld world, BlockPos pos, IFluidState fluid ) {
        if( ! fluid.isEmpty() ) {
            IBlockState block = world.getBlockState( pos );

            BlockPos downPos = pos.down( fallDirection );

            IBlockState downBlock = world.getBlockState( downPos );
            IFluidState downFluid = calculateCorrectFlowingState( world, downPos, downBlock, fluid );

            if( canFlow( world, pos, block, down, downPos, downBlock, world.getFluidState( downPos ), downFluid.getFluid() ) ) {
                // Flow down
                flowInto( world, downPos, downBlock, down, downFluid );
                if( amountOfAdjacentEqualFluids( world, pos ) >= 3 ) {
                    // Enclosed by source blocks (almost), try escaping them
                    flowHorizontal( world, pos, fluid, block );
                }
            } else if( fluid.isSource() || ! canFlowVerticalInto( world, downFluid.getFluid(), pos, block, downPos, downBlock ) ) {
                // No way down or source, try horizontally
                flowHorizontal( world, pos, fluid, block );
            }

        }
    }

    protected IFluidState applyAdditionalState( IFluidState source, IFluidState flow ) {
        return flow;
    }

    private void flowHorizontal( IWorld world, BlockPos pos, IFluidState fluid, IBlockState block ) {
        int nextLevel = fluid.getLevel() - this.getLevelDecreasePerBlock( world );
        if( fluid.get( FALLING ) ) {
            nextLevel = maxLevel - 1;
        }

        if( nextLevel > 0 ) {
            Map<EnumFacing, IFluidState> map = getFlowStates( world, pos, block, fluid );

            for( Map.Entry<EnumFacing, IFluidState> entry : map.entrySet() ) {
                EnumFacing facing = entry.getKey();
                IFluidState state = entry.getValue();
                BlockPos adjPos = pos.offset( facing );
                IBlockState blockState = world.getBlockState( adjPos );
                if( canFlow( world, pos, block, facing, adjPos, blockState, world.getFluidState( adjPos ), state.getFluid() ) ) {
                    flowInto( world, adjPos, blockState, facing, state );
                }
            }

        }
    }

    // Calculate the correct state to be at the specified position
    protected IFluidState calculateCorrectFlowingState( IWorldReaderBase world, BlockPos pos, IBlockState state, IFluidState origin ) {
        int maxLevel = 0;
        int adjacentSourceBlocks = 0;

        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
            BlockPos adjPos = pos.offset( facing );
            IBlockState adjBlock = world.getBlockState( adjPos );
            IFluidState adjFluid = adjBlock.getFluidState();
            if( adjFluid.getFluid().isEquivalentTo( this ) && isAdjacentFluidSameAs( facing, world, pos, state, adjPos, adjBlock ) ) {
                if( adjFluid.isSource() ) {
                    adjacentSourceBlocks++;
                }

                maxLevel = Math.max( maxLevel, adjFluid.getLevel() );
            }
        }

        if( this.canSourcesMultiply() && adjacentSourceBlocks >= 2 ) {
            IBlockState block = world.getBlockState( pos.down( fallDirection ) );
            IFluidState fluid = block.getFluidState();
            if( block.getMaterial().isSolid() || isSourceState( fluid ) ) {
                return applyAdditionalState( origin, getStillFluidState( false ) );
            }
        }

        BlockPos upPos = pos.up( fallDirection );
        IBlockState block = world.getBlockState( upPos );
        IFluidState fluid = block.getFluidState();
        if( ! fluid.isEmpty() && fluid.getFluid().isEquivalentTo( this ) && isAdjacentFluidSameAs( up, world, pos, state, upPos, block ) ) {
            return applyAdditionalState( origin, getFlowingFluidState( this.maxLevel, true ) );
        } else {
            int resultingLevel = maxLevel - getLevelDecreasePerBlock( world );
            if( resultingLevel <= 0 ) {
                return Fluids.EMPTY.getDefaultState();
            } else {
                return applyAdditionalState( origin, getFlowingFluidState( resultingLevel, false ) );
            }
        }
    }

    private boolean isAdjacentFluidSameAs( EnumFacing facing, IBlockReader world, BlockPos pos, IBlockState state, BlockPos adjPos, IBlockState adjState ) {
        Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey> adjacentFluidCache;

        if( ! state.getBlock().isVariableOpacity() && ! adjState.getBlock().isVariableOpacity() ) {
            adjacentFluidCache = ADJ_FLUID_CACHE.get();
        } else {
            adjacentFluidCache = null;
        }

        Block.RenderSideCacheKey key;
        if( adjacentFluidCache != null ) {
            key = new Block.RenderSideCacheKey( state, adjState, facing );
            byte cacheByte = adjacentFluidCache.getAndMoveToFirst( key );
            if( cacheByte != 127 ) {
                return cacheByte != 0;
            }
        } else {
            key = null;
        }

        VoxelShape shape = state.getCollisionShape( world, pos );
        VoxelShape adjShape = adjState.getCollisionShape( world, adjPos );
        boolean connect = ! VoxelShapes.doAdjacentCubeSidesFillSquare( shape, adjShape, facing );
        if( adjacentFluidCache != null ) {
            if( adjacentFluidCache.size() == 200 ) {
                adjacentFluidCache.removeLastByte();
            }

            adjacentFluidCache.putAndMoveToFirst( key, (byte) ( connect ? 1 : 0 ) );
        }

        return connect;
    }

    public abstract Fluid getFlowingFluid();

    public IFluidState getFlowingFluidState( int level, boolean falling ) {
        return this.getFlowingFluid().getDefaultState().with( this.level, level ).with( FALLING, falling );
    }

    public abstract Fluid getStillFluid();

    public IFluidState getStillFluidState( boolean falling ) {
        return this.getStillFluid().getDefaultState().with( FALLING, falling );
    }

    protected abstract boolean canSourcesMultiply();

    protected void flowInto( IWorld world, BlockPos pos, IBlockState state, EnumFacing direction, IFluidState fluid ) {
        if( state.getBlock() instanceof ILiquidContainer ) {
            // Try to flow through this block
            ( (ILiquidContainer) state.getBlock() ).receiveFluid( world, pos, state, fluid );
        } else {
            if( ! state.isAir( world, pos ) ) {
                this.beforeReplacingBlock( world, pos, state );
            }
            world.setBlockState( pos, fluid.getBlockState(), 3 );
        }

    }

    protected abstract void beforeReplacingBlock( IWorld worldIn, BlockPos pos, IBlockState state );

    private static short shortDelta( BlockPos pos, BlockPos adjPos ) {
        int diffX = adjPos.getX() - pos.getX();
        int diffZ = adjPos.getZ() - pos.getZ();
        return (short) ( ( diffX + 128 & 255 ) << 8 | diffZ + 128 & 255 );
    }

    // Called recursively for each horizontal direction to find a slope
    protected int findSlope( IWorldReaderBase world, BlockPos pos, int recursion, EnumFacing facing, IBlockState state, BlockPos adjPos, Short2ObjectMap<Pair<IBlockState, IFluidState>> blockFluidMap, Short2BooleanMap slopeMap ) {
        int weight = 1000;

        for( EnumFacing offset : EnumFacing.Plane.HORIZONTAL ) {
            if( offset != facing ) {
                BlockPos otherPos = pos.offset( offset );
                short delta = shortDelta( adjPos, otherPos );

                // Compute block-fluid pair and cache it
                Pair<IBlockState, IFluidState> blockFluidPair = blockFluidMap.computeIfAbsent( delta, integer -> {
                    IBlockState otherState = world.getBlockState( otherPos );
                    return Pair.of( otherState, otherState.getFluidState() );
                } );
                IBlockState block = blockFluidPair.getFirst();
                IFluidState fluid = blockFluidPair.getSecond();

                if( canFlowHorizontalInto( world, getFlowingFluid(), pos, state, offset, otherPos, block, fluid ) ) {
                    // Compute slope and cache it
                    boolean hasSlope = slopeMap.computeIfAbsent( delta, integer -> {
                        BlockPos downPos = otherPos.down( fallDirection );
                        IBlockState downBlock = world.getBlockState( downPos );
                        return canFlowVerticalInto( world, getFlowingFluid(), otherPos, block, downPos, downBlock );
                    } );

                    // Slope found, return recursion
                    if( hasSlope ) {
                        return recursion;
                    }

                    if( recursion < getSlopeFindDistance( world ) ) {
                        // Recurse to find slope
                        int recurseWeight = this.findSlope( world, otherPos, recursion + 1, offset.getOpposite(), block, adjPos, blockFluidMap, slopeMap );
                        if( recurseWeight < weight ) {
                            weight = recurseWeight;
                        }
                    }
                }
            }
        }

        return weight;
    }

    /**
     * Return true when this fluid can flow vertically into the adjacent block
     * @param world    The world
     * @param fluid    The flowing variant of this fluid, which is this fluid when this fluid already flows
     * @param pos      The position where the fluid is now
     * @param state    The block state of this fluid block
     * @param adjpos   The position to flow into
     * @param adjState The block at the position to flow to
     */
    protected boolean canFlowVerticalInto( IBlockReader world, Fluid fluid, BlockPos pos, IBlockState state, BlockPos adjpos, IBlockState adjState ) {
        if( ! isAdjacentFluidSameAs( down, world, pos, state, adjpos, adjState ) ) {
            return false;
        } else {
            return adjState.getFluidState().getFluid().isEquivalentTo( this ) || canBreakThrough( world, adjpos, adjState, fluid );
        }
    }

    /**
     * Return true when this fluid can flow horizontally into the adjacent block
     * @param world         The world
     * @param fluid         The flowing variant of this fluid, which is this fluid when this fluid already flows
     * @param pos           The position where the fluid is now
     * @param state         The block state of this fluid block
     * @param facing        The facing to flow into
     * @param adjPos        The adjacent position
     * @param adjState      The adjacent block state
     * @param adjFluidState The adjacent fluid state, which is the empty fluid when there is no adjacent fluid
     */
    private boolean canFlowHorizontalInto( IBlockReader world, Fluid fluid, BlockPos pos, IBlockState state, EnumFacing facing, BlockPos adjPos, IBlockState adjState, IFluidState adjFluidState ) {
        return ! isSourceState( adjFluidState ) && isAdjacentFluidSameAs( facing, world, pos, state, adjPos, adjState ) && canBreakThrough( world, adjPos, adjState, fluid );
    }

    private boolean isSourceState( IFluidState state ) {
        return state.getFluid().isEquivalentTo( this ) && state.isSource();
    }

    protected abstract int getSlopeFindDistance( IWorldReaderBase world );

    private int amountOfAdjacentEqualFluids( IWorldReaderBase world, BlockPos pos ) {
        int amount = 0;

        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
            BlockPos offPos = pos.offset( facing );
            IFluidState state = world.getFluidState( offPos );
            if( isSourceState( state ) ) {
                ++ amount;
            }
        }

        return amount;
    }

    protected Map<EnumFacing, IFluidState> getFlowStates( IWorldReaderBase world, BlockPos pos, IBlockState state, IFluidState origin ) {
        int weight = 1000;

        Map<EnumFacing, IFluidState> map = Maps.newEnumMap( EnumFacing.class );

        Short2ObjectMap<Pair<IBlockState, IFluidState>> blockFluidMap = new Short2ObjectOpenHashMap<>();
        Short2BooleanMap slopeMap = new Short2BooleanOpenHashMap();

        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
            BlockPos adjPos = pos.offset( facing );
            short delta = shortDelta( pos, adjPos );

            Pair<IBlockState, IFluidState> pair = blockFluidMap.computeIfAbsent( delta, integer -> {
                IBlockState adjState = world.getBlockState( adjPos );
                return Pair.of( adjState, adjState.getFluidState() );
            } );
            IBlockState block = pair.getFirst();
            IFluidState fluid = pair.getSecond();

            IFluidState correctState = calculateCorrectFlowingState( world, adjPos, block, origin );
            if( this.canFlowHorizontalInto( world, correctState.getFluid(), pos, state, facing, adjPos, block, fluid ) ) {
                BlockPos downPos = adjPos.down( fallDirection );
                boolean canFlowDown = slopeMap.computeIfAbsent( delta, integer -> {
                    IBlockState adjState = world.getBlockState( downPos );
                    return this.canFlowVerticalInto( world, getFlowingFluid(), adjPos, block, downPos, adjState );
                } );
                int flowWeight;
                if( canFlowDown ) {
                    flowWeight = 0;
                } else {
                    flowWeight = findSlope( world, adjPos, 1, facing.getOpposite(), block, pos, blockFluidMap, slopeMap );
                }

                if( flowWeight < weight ) {
                    map.clear();
                }

                if( flowWeight <= weight ) {
                    map.put( facing, correctState );
                    weight = flowWeight;
                }
            }
        }

        return map;
    }

    protected boolean canBreakThrough( IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid ) {
        Block block = state.getBlock();
        if( block instanceof ILiquidContainer ) {
            return ( (ILiquidContainer) block ).canContainFluid( world, pos, state, fluid );
        } else if( ! ( block instanceof BlockDoor ) && block != Blocks.SIGN && block != Blocks.LADDER && block != Blocks.SUGAR_CANE && block != Blocks.BUBBLE_COLUMN ) {
            Material material = state.getMaterial();
            if( material != Material.PORTAL && material != Material.STRUCTURE_VOID && material != Material.OCEAN_PLANT && material != Material.SEA_GRASS ) {
                return ! material.blocksMovement();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected boolean canFlow( IBlockReader worldIn, BlockPos fromPos, IBlockState fromBlockState, EnumFacing direction, BlockPos toPos, IBlockState toBlockState, IFluidState toFluidState, Fluid fluidIn ) {
        return toFluidState.canOtherFlowInto( fluidIn, direction ) && isAdjacentFluidSameAs( direction, worldIn, fromPos, fromBlockState, toPos, toBlockState ) && canBreakThrough( worldIn, toPos, toBlockState, fluidIn );
    }

    protected abstract int getLevelDecreasePerBlock( IWorldReaderBase worldIn );

    protected int getTickRate( World world, IFluidState currentState, IFluidState correctState ) {
        return getTickRate( world );
    }

    public void tick( World world, BlockPos pos, IFluidState state ) {
        if( ! state.isSource() ) {
            IFluidState correctState = calculateCorrectFlowingState( world, pos, world.getBlockState( pos ), state );
            int rate = getTickRate( world, state, correctState );

            if( correctState.isEmpty() ) {
                state = correctState;
                world.setBlockState( pos, Blocks.AIR.getDefaultState(), 3 );
            } else if( ! correctState.equals( state ) ) {
                state = correctState;

                IBlockState blockState = correctState.getBlockState();
                world.setBlockState( pos, blockState, 2 );
                world.getPendingFluidTicks().scheduleTick( pos, correctState.getFluid(), rate );
                world.notifyNeighborsOfStateChange( pos, blockState.getBlock() );
            }
        }

        this.flowAround( world, pos, state );
    }

    protected int getLevelFromState( IFluidState state ) {
        return state.isSource() ? 0 : maxLevel - Math.min( state.getLevel(), maxLevel ) + ( state.get( FALLING ) ? maxLevel : 0 );
    }

    public float getHeight( IFluidState state ) {
        return interpolateHeight( state, state.getLevel() / (float) maxLevel );
    }

    public boolean isGas() {
        return isGas;
    }

    public float interpolateHeight( IFluidState state, float nLv ) {
        return MathUtil.lerp( getMinHeight( state ), getMaxHeight( state ), nLv );
    }

    public float getMinHeight( IFluidState state ) {
        return 0;
    }

    public float getMaxHeight( IFluidState state ) {
        return 0.888888889F;
    }

    @Override
    public abstract IBlockState getBlockState( IFluidState state );

    public boolean reactWithNeighbors( World world, BlockPos pos, IBlockState state ) {
        return true;
    }

    protected void triggerMixEffects( IWorld world, BlockPos pos ) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        world.playSound( null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + ( world.getRandom().nextFloat() - world.getRandom().nextFloat() ) * 0.8F );

        for( int i = 0; i < 8; ++ i ) {
            world.addParticle( Particles.LARGE_SMOKE, x + Math.random(), y + 1.2D, z + Math.random(), 0, 0, 0 );
        }

    }
}
