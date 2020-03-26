/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.common.fluid;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.api.block.fluid.IGaseousFluid;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.redgalaxy.util.MathUtil;

import java.util.Map;

/**
 * Root of all fluid classes. This fluid is basically vanilla's {@link FlowingFluid} but with support for inverse
 * gravity and custom flow quanta.
 */
public abstract class RegularFluid extends Fluid {
    public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
    public final IntegerProperty level;
    public final IntegerProperty blockLevel;
    public final int maxLevel;

    private final Map<IFluidState, VoxelShape> shapeCache = Maps.newIdentityHashMap();

    // Adjacent fluid cache
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey>> SIDE_BLOCKING_CACHE = ThreadLocal.withInitial( () -> {
        Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey> map = new Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey>( 200 ) {
            @Override
            protected void rehash( int hash ) {
            }
        };
        map.defaultReturnValue( (byte) 127 );
        return map;
    } );

    private final Direction down;
    private final Direction up;
    private final boolean isGas;
    private final int fallDirection;

    public RegularFluid( IntegerProperty level, int max ) {
        isGas = this instanceof IGaseousFluid;
        if( isGas ) {
            down = Direction.UP;
            fallDirection = - 1;
        } else {
            down = Direction.DOWN;
            fallDirection = 1;
        }
        up = down.getOpposite();

        this.level = level;
        this.blockLevel = IntegerProperty.create( "level", 0, max );
        this.maxLevel = max;

        setDefaultState( getStateContainer().getBaseState().with( FALLING, false ) );
    }

    public RegularFluid() {
        this( BlockStateProperties.LEVEL_1_8, 8 );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Fluid, IFluidState> builder ) {
        builder.add( FALLING );
    }

    @Override
    public Vec3d getFlow( IBlockReader world, BlockPos pos, IFluidState state ) {
        double xflow = 0.0D;
        double zflow = 0.0D;

        Vec3d normalizedFlow;

        try( BlockPos.PooledMutable mpos = BlockPos.PooledMutable.retain() ) {
            for( Direction facing : Direction.Plane.HORIZONTAL ) {
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
                for( Direction facing : Direction.Plane.HORIZONTAL ) {
                    mpos.setPos( pos ).move( facing );
                    if( causesVerticalCurrent( world, mpos, facing ) || causesVerticalCurrent( world, mpos.up( fallDirection ), facing ) ) {
                        flow = flow.normalize().add( 0, - 6 * fallDirection, 0 );
                        break;
                    }
                }
            }

            normalizedFlow = flow.normalize();
        }

        return normalizedFlow;
    }

    /**
     * Checks if this fluid can flow into the specified fluid.
     */
    private boolean canFlowTo( IFluidState state ) {
        return state.isEmpty() || state.getFluid().isEquivalentTo( this );
    }

    /**
     * Does downward current exist in the specified context?
     */
    protected boolean causesVerticalCurrent( IBlockReader world, BlockPos pos, Direction facing ) {
        BlockState blockstate = world.getBlockState( pos );
        IFluidState ifluidstate = world.getFluidState( pos );
        if( ifluidstate.getFluid().isEquivalentTo( this ) ) {
            return false;
        } else if( facing == up ) {
            return true;
        } else {
            return blockstate.getMaterial() != Material.ICE && blockstate.isSolidSide( world, pos, facing );
        }
    }

    /**
     * Makes this fluid flow in the specified context.
     *
     * @param world The world to flow in
     * @param pos   The pos to flow at
     * @param fluid The current fluid state
     */
    protected void flowAround( IWorld world, BlockPos pos, IFluidState fluid ) {
        if( ! fluid.isEmpty() ) {
            BlockState block = world.getBlockState( pos );

            BlockPos downPos = pos.down( fallDirection );

            BlockState downBlock = world.getBlockState( downPos );
            IFluidState downFluid = calculateCorrectState( world, downPos, downBlock, fluid );

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

    /**
     * Copy additional state properties from the origin to the new flowing block.
     *
     * @param source The origin fluid
     * @param flow   The new fluid
     * @return The new fluid with additional states
     */
    protected IFluidState applyAdditionalState( IFluidState source, IFluidState flow ) {
        return flow;
    }

    /**
     * Makes the fluid spread horizontally
     *
     * @param world The world to spread in
     * @param pos   The position to spread at
     * @param fluid The current fluid state
     * @param block The current block state
     */
    private void flowHorizontal( IWorld world, BlockPos pos, IFluidState fluid, BlockState block ) {
        int nextLevel = fluid.getLevel() - getLevelDecreasePerBlock( world );
        if( fluid.get( FALLING ) ) {
            nextLevel = maxLevel - 1;
        }

        if( nextLevel > 0 ) {
            Map<Direction, IFluidState> map = getFlowStates( world, pos, block, fluid );

            for( Map.Entry<Direction, IFluidState> entry : map.entrySet() ) {
                Direction facing = entry.getKey();
                IFluidState state = entry.getValue();
                BlockPos adjPos = pos.offset( facing );
                BlockState blockState = world.getBlockState( adjPos );
                if( canFlow( world, pos, block, facing, adjPos, blockState, world.getFluidState( adjPos ), state.getFluid() ) ) {
                    flowInto( world, adjPos, blockState, facing, state );
                }
            }

        }
    }

    /**
     * Calculates the correct fluid state for a specific position, based on surrounding fluids.
     */
    protected IFluidState calculateCorrectState( IWorld world, BlockPos pos, BlockState state, IFluidState origin ) {
        int maxLevel = 0;
        int adjacentSourceBlocks = 0;

        for( Direction facing : Direction.Plane.HORIZONTAL ) {
            BlockPos adjPos = pos.offset( facing );
            BlockState adjBlock = world.getBlockState( adjPos );
            IFluidState adjFluid = adjBlock.getFluidState();
            if( adjFluid.getFluid().isEquivalentTo( this ) && doBlockShapesAllowFlowing( facing, world, pos, state, adjPos, adjBlock ) ) {
                if( adjFluid.isSource() ) {
                    adjacentSourceBlocks++;
                }

                maxLevel = Math.max( maxLevel, adjFluid.getLevel() );
            }
        }

        if( canSourcesMultiply() && adjacentSourceBlocks >= 2 ) {
            BlockState block = world.getBlockState( pos.down( fallDirection ) );
            IFluidState fluid = block.getFluidState();
            if( block.getMaterial().isSolid() || isSourceState( fluid ) ) {
                return applyAdditionalState( origin, getStillFluidState( false ) );
            }
        }

        BlockPos upPos = pos.up( fallDirection );
        BlockState block = world.getBlockState( upPos );
        IFluidState fluid = block.getFluidState();
        if( ! fluid.isEmpty() && fluid.getFluid().isEquivalentTo( this ) && doBlockShapesAllowFlowing( up, world, pos, state, upPos, block ) ) {
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

    /**
     * Checks if the shapes of the specified contained and adjacent block cover a full square at the specified facing,
     * and thus block the fluid from moving that way. Returns false if so...
     */
    private boolean doBlockShapesAllowFlowing( Direction facing, IBlockReader world, BlockPos pos, BlockState state, BlockPos adjPos, BlockState adjState ) {
        Object2ByteLinkedOpenHashMap<Block.RenderSideCacheKey> adjacentFluidCache;

        if( ! state.getBlock().isVariableOpacity() && ! adjState.getBlock().isVariableOpacity() ) {
            adjacentFluidCache = SIDE_BLOCKING_CACHE.get();
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
        boolean fillFullSquare = ! VoxelShapes.doAdjacentCubeSidesFillSquare( shape, adjShape, facing );
        if( adjacentFluidCache != null ) {
            if( adjacentFluidCache.size() == 200 ) {
                adjacentFluidCache.removeLastByte();
            }

            adjacentFluidCache.putAndMoveToFirst( key, (byte) ( fillFullSquare ? 1 : 0 ) );
        }

        return fillFullSquare;
    }

    /**
     * Returns the flowing variant of this fluid.
     */
    public abstract Fluid getFlowingFluid();

    /**
     * Returns a flowing fluid state of this fluid.
     */
    public IFluidState getFlowingFluidState( int level, boolean falling ) {
        return getFlowingFluid().getDefaultState().with( this.level, level ).with( FALLING, falling );
    }

    /**
     * Returns the still variant of this fluid.
     */
    public abstract Fluid getStillFluid();

    /**
     * Returns a still fluid state of this fluid.
     */
    public IFluidState getStillFluidState( boolean falling ) {
        return getStillFluid().getDefaultState().with( FALLING, falling );
    }

    /**
     * Returns true when this fluid can generate new sources.
     */
    protected abstract boolean canSourcesMultiply();

    /**
     * Called when a fluid block needs to be placed.
     *
     * @param world     The world to place in
     * @param pos       The position to flow to
     * @param state     The block state that is being replaced
     * @param direction The direction we're flowing to
     * @param fluid     The correct flow state
     */
    protected void flowInto( IWorld world, BlockPos pos, BlockState state, Direction direction, IFluidState fluid ) {
        // Check for a waterlogged block if it can contain this fluid before we place the fluid so that we can have
        // non-waterlogged behaviour when the block cannot contain this fluid
        if( state.getBlock() instanceof ILiquidContainer && ( (ILiquidContainer) state.getBlock() ).canContainFluid( world, pos, state, fluid.getFluid() ) ) {
            // Try to flow through this block
            ( (ILiquidContainer) state.getBlock() ).receiveFluid( world, pos, state, fluid );
        } else {
            if( ! state.isAir( world, pos ) ) {
                beforeReplacingBlock( world, pos, state );
            }
            world.setBlockState( pos, fluid.getBlockState(), 3 );
        }

    }

    /**
     * Called before a block is being replaced by this fluid, unless that block is a fluid container or air.
     */
    protected abstract void beforeReplacingBlock( IWorld worldIn, BlockPos pos, BlockState state );

    private static short shortDelta( BlockPos pos, BlockPos adjPos ) {
        int diffX = adjPos.getX() - pos.getX();
        int diffZ = adjPos.getZ() - pos.getZ();
        return (short) ( ( diffX + 128 & 255 ) << 8 | diffZ + 128 & 255 );
    }

    /**
     * Called recursively for each horizontal direction to find a block to flow down into...
     */
    protected int findSlope( IWorld world, BlockPos pos, int recursion, Direction facing, BlockState state, BlockPos adjPos, Short2ObjectMap<Pair<BlockState, IFluidState>> blockFluidMap, Short2BooleanMap slopeMap ) {
        int weight = 1000;

        for( Direction offset : Direction.Plane.HORIZONTAL ) {
            if( offset != facing ) {
                BlockPos otherPos = pos.offset( offset );
                short delta = shortDelta( adjPos, otherPos );

                // Compute block-fluid pair and cache it
                Pair<BlockState, IFluidState> blockFluidPair = blockFluidMap.computeIfAbsent( delta, integer -> {
                    BlockState otherState = world.getBlockState( otherPos );
                    return Pair.of( otherState, otherState.getFluidState() );
                } );
                BlockState block = blockFluidPair.getFirst();
                IFluidState fluid = blockFluidPair.getSecond();

                if( canFlowHorizontalInto( world, getFlowingFluid(), pos, state, offset, otherPos, block, fluid ) ) {
                    // Compute slope and cache it
                    boolean hasSlope = slopeMap.computeIfAbsent( delta, integer -> {
                        BlockPos downPos = otherPos.down( fallDirection );
                        BlockState downBlock = world.getBlockState( downPos );
                        return canFlowVerticalInto( world, getFlowingFluid(), otherPos, block, downPos, downBlock );
                    } );

                    // Slope found, return recursion
                    if( hasSlope ) {
                        return recursion;
                    }

                    if( recursion < getSlopeFindDistance( world ) ) {
                        // Recurse to find slope
                        int recurseWeight = findSlope( world, otherPos, recursion + 1, offset.getOpposite(), block, adjPos, blockFluidMap, slopeMap );
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
     *
     * @param world    The world
     * @param fluid    The flowing variant of this fluid, which is this fluid when this fluid already flows
     * @param pos      The position where the fluid is now
     * @param state    The block state of this fluid block
     * @param adjpos   The position to flow into
     * @param adjState The block at the position to flow to
     */
    protected boolean canFlowVerticalInto( IBlockReader world, Fluid fluid, BlockPos pos, BlockState state, BlockPos adjpos, BlockState adjState ) {
        if( ! doBlockShapesAllowFlowing( down, world, pos, state, adjpos, adjState ) ) {
            return false;
        } else {
            return adjState.getFluidState().getFluid().isEquivalentTo( this ) || canBreakThrough( world, adjpos, adjState, fluid );
        }
    }

    /**
     * Return true when this fluid can flow horizontally into the adjacent block
     *
     * @param world         The world
     * @param fluid         The flowing variant of this fluid, which is this fluid when this fluid already flows
     * @param pos           The position where the fluid is now
     * @param state         The block state of this fluid block
     * @param facing        The facing to flow into
     * @param adjPos        The adjacent position
     * @param adjState      The adjacent block state
     * @param adjFluidState The adjacent fluid state, which is the empty fluid when there is no adjacent fluid
     */
    protected boolean canFlowHorizontalInto( IBlockReader world, Fluid fluid, BlockPos pos, BlockState state, Direction facing, BlockPos adjPos, BlockState adjState, IFluidState adjFluidState ) {
        return ! isSourceState( adjFluidState ) && doBlockShapesAllowFlowing( facing, world, pos, state, adjPos, adjState ) && canBreakThrough( world, adjPos, adjState, fluid );
    }

    private boolean isSourceState( IFluidState state ) {
        return state.getFluid().isEquivalentTo( this ) && state.isSource();
    }

    /**
     * Returns the maximum distance this fluid checks to find a slope (a near place to flow down).
     */
    protected abstract int getSlopeFindDistance( IWorld world );

    private int amountOfAdjacentEqualFluids( IWorld world, BlockPos pos ) {
        int amount = 0;

        for( Direction facing : Direction.Plane.HORIZONTAL ) {
            BlockPos offPos = pos.offset( facing );
            IFluidState state = world.getFluidState( offPos );
            if( isSourceState( state ) ) {
                ++ amount;
            }
        }

        return amount;
    }

    /**
     * Returns a map of flow states for each horizontal direction.
     *
     * @param world  The world to flow in
     * @param pos    The pos to flow at
     * @param state  The origin block
     * @param origin The origin fluid
     */
    protected Map<Direction, IFluidState> getFlowStates( IWorld world, BlockPos pos, BlockState state, IFluidState origin ) {
        int weight = 1000;

        Map<Direction, IFluidState> map = Maps.newEnumMap( Direction.class );

        Short2ObjectMap<Pair<BlockState, IFluidState>> blockFluidMap = new Short2ObjectOpenHashMap<>();
        Short2BooleanMap slopeMap = new Short2BooleanOpenHashMap();

        for( Direction facing : Direction.Plane.HORIZONTAL ) {
            BlockPos adjPos = pos.offset( facing );
            short delta = shortDelta( pos, adjPos );

            Pair<BlockState, IFluidState> pair = blockFluidMap.computeIfAbsent( delta, integer -> {
                BlockState adjState = world.getBlockState( adjPos );
                return Pair.of( adjState, adjState.getFluidState() );
            } );
            BlockState block = pair.getFirst();
            IFluidState fluid = pair.getSecond();

            IFluidState correctState = calculateCorrectState( world, adjPos, block, origin );
            if( canFlowHorizontalInto( world, correctState.getFluid(), pos, state, facing, adjPos, block, fluid ) ) {
                BlockPos downPos = adjPos.down( fallDirection );
                boolean canFlowDown = slopeMap.computeIfAbsent( delta, integer -> {
                    BlockState adjState = world.getBlockState( downPos );
                    return canFlowVerticalInto( world, getFlowingFluid(), adjPos, block, downPos, adjState );
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

    /**
     * Checks if this fluid can replace a specific block
     *
     * @param world The world to flow in
     * @param pos   The pos to flow to
     * @param state The state that is being replaced
     * @param fluid The fluid that replaces this block
     */
    protected boolean canBreakThrough( IBlockReader world, BlockPos pos, BlockState state, Fluid fluid ) {
        Block block = state.getBlock();
        // Check waterlogging all in if statement so that when a fluid can not be placed inside a block, the
        // non-waterlogging behaviour applies
        if( block instanceof ILiquidContainer && ( (ILiquidContainer) block ).canContainFluid( world, pos, state, fluid ) ) {
            return true;
        } else if( ! ( block instanceof DoorBlock ) && block != Blocks.LADDER && block != Blocks.SUGAR_CANE && block != Blocks.BUBBLE_COLUMN ) {
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

    /**
     * Checks if this fluid can flow from one place to another
     *
     * @param world          The world to flow in
     * @param fromPos        The origin pos
     * @param fromBlockState The origin state
     * @param direction      The direction to flow to
     * @param toPos          The flow pos
     * @param toBlockState   The block state to replace
     * @param toFluidState   The fluid state to replace
     * @param fluid          The current fluid
     */
    protected boolean canFlow( IBlockReader world, BlockPos fromPos, BlockState fromBlockState, Direction direction, BlockPos toPos, BlockState toBlockState, IFluidState toFluidState, Fluid fluid ) {
        return toFluidState.canDisplace( world, toPos, fluid, direction ) && doBlockShapesAllowFlowing( direction, world, fromPos, fromBlockState, toPos, toBlockState ) && canBreakThrough( world, toPos, toBlockState, fluid );
    }

    /**
     * Returns the decrease in level per block.
     */
    protected abstract int getLevelDecreasePerBlock( IWorld worldIn );

    /**
     * Returns the tick rate for a specific context.
     */
    protected int getTickRate( World world, IFluidState currentState, IFluidState correctState ) {
        return getTickRate( world );
    }

    @Override
    public void tick( World world, BlockPos pos, IFluidState state ) {
        if( ! state.isSource() ) {
            IFluidState correctState = calculateCorrectState( world, pos, world.getBlockState( pos ), state );
            int rate = getTickRate( world, state, correctState );

            if( correctState.isEmpty() ) {
                state = correctState;
                world.setBlockState( pos, Blocks.AIR.getDefaultState(), 3 );
            } else if( ! correctState.equals( state ) ) {
                state = correctState;

                BlockState blockState = correctState.getBlockState();
                world.setBlockState( pos, blockState, 2 );
                world.getPendingFluidTicks().scheduleTick( pos, correctState.getFluid(), rate );
                world.notifyNeighborsOfStateChange( pos, blockState.getBlock() );
            }
        }

        flowAround( world, pos, state );
    }

    /**
     * Returns the level from a specific fluid state.
     */
    protected int getLevelFromState( IFluidState state ) {
        return state.isSource()
               ? 0
               : maxLevel - Math.min( state.getLevel(), maxLevel ) + ( state.get( FALLING ) ? maxLevel : 0 );
    }

    /**
     * Returns the height of a fluid state.
     */
    @Override
    public float getHeight( IFluidState state ) {
        return interpolateHeight( state, state.getLevel() / (float) maxLevel );
    }

    /**
     * Checks if this fluid is a gas.
     */
    public boolean isGas() {
        return isGas;
    }

    /**
     * Interpolates between min and max height.
     */
    public float interpolateHeight( IFluidState state, float nLv ) {
        return MathUtil.lerp( getMinHeight( state ), getMaxHeight( state ), nLv );
    }

    /**
     * Returns the minimum height of the fluid.
     */
    public float getMinHeight( IFluidState state ) {
        return 0;
    }

    /**
     * Returns the maximum height of the fluid.
     */
    public float getMaxHeight( IFluidState state ) {
        return 0.888888889F;
    }

    /**
     * Do reactions with neighbors, such as heatrock turning into rock or basalt.
     */
    public boolean reactWithNeighbors( World world, BlockPos pos, BlockState state ) {
        return true;
    }

    /**
     * Play mixing sounds, such as extinguish sound and smoke on heatrock.
     */
    protected void triggerMixEffects( IWorld world, BlockPos pos ) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        world.playSound( null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + ( world.getRandom().nextFloat() - world.getRandom().nextFloat() ) * 0.8F );

        for( int i = 0; i < 8; ++ i ) {
            world.addParticle( ParticleTypes.LARGE_SMOKE, x + Math.random(), y + 1.2D, z + Math.random(), 0, 0, 0 );
        }

    }

    private static boolean isSimilarFluidUp( IFluidState fluid, IBlockReader world, BlockPos pos ) {
        return fluid.getFluid().isEquivalentTo( world.getFluidState( pos.up() ).getFluid() );
    }

    @Override
    public float getActualHeight( IFluidState state, IBlockReader world, BlockPos pos ) {
        return isSimilarFluidUp( state, world, pos )
               ? 1
               : state.getHeight();
    }

    /**
     * Returns the shape of this fluid.
     */
    public VoxelShape getShape( IFluidState state, IBlockReader world, BlockPos pos ) {
        return state.getLevel() == 9 && isSimilarFluidUp( state, world, pos )
               ? VoxelShapes.fullCube()
               : shapeCache.computeIfAbsent(
                   state, s -> {
                       double h = s.getActualHeight( world, pos );
                       double p = isGas ? 1 : h;
                       double n = ! isGas ? 0 : 1 - h;
                       return VoxelShapes.create( 0, n, 0, 1, p, 1 );
                   }
               );
    }

    @Override // Made public for access in FluidFallFeature
    public abstract BlockState getBlockState( IFluidState state );

    // Missing mapping for getShape
    @Override
    public VoxelShape func_215664_b( IFluidState state, IBlockReader world, BlockPos pos ) {
        return getShape( state, world, pos );
    }

    /**
     * Add additional fluid attributes here.
     */
    protected void addAdditionalAttributes( FluidAttributes.Builder builder ) {
    }

    @Override
    protected final FluidAttributes createAttributes() {
        return createAttributes( this );
    }

    private FluidAttributes createAttributes( Fluid fluid ) {
        ResourceLocation still = new ResourceLocation( "minecraft:block/water_still" );
        ResourceLocation flow = new ResourceLocation( "minecraft:block/water_still" );
        ResourceLocation overlay = new ResourceLocation( "minecraft:block/water_overlay" );
        if( fluid instanceof ICustomRenderFluid ) {
            still = ( (ICustomRenderFluid) fluid ).getStill();
            flow = ( (ICustomRenderFluid) fluid ).getFlowing();
            overlay = ( (ICustomRenderFluid) fluid ).getOverlay();
        }
        FluidAttributes.Builder builder = FluidAttributes.builder( still, flow );
        if( overlay != null ) {
            builder.overlay( overlay );
        }
        if( fluid instanceof IGaseousFluid ) {
            builder.gaseous();
        }
        addAdditionalAttributes( builder );
        return builder.build( fluid );
    }
}
