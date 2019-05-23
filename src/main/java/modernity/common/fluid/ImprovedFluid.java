package modernity.common.fluid;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
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
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

import modernity.api.block.fluid.IGaseousFluid;

import java.util.Map;

public abstract class ImprovedFluid extends Fluid {
    public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
    public static final IntegerProperty LEVEL_1_TO_8 = BlockStateProperties.LEVEL_1_8;

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

    public ImprovedFluid() {
        isGas = this instanceof IGaseousFluid;
        if( isGas ) {
            down = EnumFacing.UP;
            fallDirection = - 1;
        } else {
            down = EnumFacing.DOWN;
            fallDirection = 1;
        }
        up = down.getOpposite();
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
                if( this.canFlowTo( fluid ) ) {
                    float height = fluid.getHeight();
                    float flowWeight = 0.0F;

                    if( height == 0.0F ) {
                        if( ! world.getBlockState( mpos ).getMaterial().blocksMovement() ) {
                            IFluidState fluidBelow = world.getFluidState( mpos.down( fallDirection ) );
                            if( this.canFlowTo( fluidBelow ) ) {
                                height = fluidBelow.getHeight();
                                if( height > 0.0F ) {
                                    flowWeight = state.getHeight() - ( height - 0.8888889F );
                                }
                            }
                        }
                    } else if( height > 0.0F ) {
                        flowWeight = state.getHeight() - height;
                    }

                    if( flowWeight != 0.0F ) {
                        xflow += facing.getXOffset() * flowWeight;
                        zflow += facing.getZOffset() * flowWeight;
                    }
                }
            }

            Vec3d flow = new Vec3d( xflow, 0.0D, zflow );
            if( state.get( FALLING ) ) {
                for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                    mpos.setPos( pos ).move( facing );
                    if( this.causesVerticalCurrent( world, mpos, facing ) || this.causesVerticalCurrent( world, mpos.up( fallDirection ), facing ) ) {
                        flow = flow.normalize().add( 0.0D, - 6.0D * fallDirection, 0.0D );
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
            boolean flag = Block.isExceptBlockForAttachWithPiston( block ) || block instanceof BlockStairs;
            return ! flag && bstate.getBlockFaceShape( world, pos, facing ) == BlockFaceShape.SOLID;
        }
    }

    protected void flowAround( IWorld world, BlockPos pos, IFluidState fluid ) {
        if( ! fluid.isEmpty() ) {
            IBlockState block = world.getBlockState( pos );

            BlockPos downPos = pos.down( fallDirection );

            IBlockState downBlock = world.getBlockState( downPos );
            IFluidState downFluid = this.calculateCorrectFlowingState( world, downPos, downBlock );

            if( this.canFlow( world, pos, block, down, downPos, downBlock, world.getFluidState( downPos ), downFluid.getFluid() ) ) {
                // Flow down
                this.flowInto( world, downPos, downBlock, down, downFluid );
                if( this.amountOfAdjacentEqualFluids( world, pos ) >= 3 ) {
                    // Enclosed by source blocks (almost), try escaping them
                    this.flowHorizontal( world, pos, fluid, block );
                }
            } else if( fluid.isSource() || ! this.canFlowVerticalInto( world, downFluid.getFluid(), pos, block, downPos, downBlock ) ) {
                // No way down or source, try horizontally
                this.flowHorizontal( world, pos, fluid, block );
            }

        }
    }

    private void flowHorizontal( IWorld world, BlockPos pos, IFluidState fluid, IBlockState block ) {
        int nextLevel = fluid.getLevel() - this.getLevelDecreasePerBlock( world );
        if( fluid.get( FALLING ) ) {
            nextLevel = 7;
        }

        if( nextLevel > 0 ) {
            Map<EnumFacing, IFluidState> map = this.getFlowStates( world, pos, block );

            for( Map.Entry<EnumFacing, IFluidState> entry : map.entrySet() ) {
                EnumFacing facing = entry.getKey();
                IFluidState state = entry.getValue();
                BlockPos adjPos = pos.offset( facing );
                IBlockState blockState = world.getBlockState( adjPos );
                if( this.canFlow( world, pos, block, facing, adjPos, blockState, world.getFluidState( adjPos ), state.getFluid() ) ) {
                    this.flowInto( world, adjPos, blockState, facing, state );
                }
            }

        }
    }

    // Calculate the correct state to be at the specified position
    protected IFluidState calculateCorrectFlowingState( IWorldReaderBase world, BlockPos pos, IBlockState state ) {
        int maxLevel = 0;
        int adjacentSourceBlocks = 0;

        for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
            BlockPos adjPos = pos.offset( facing );
            IBlockState adjBlock = world.getBlockState( adjPos );
            IFluidState adjFluid = adjBlock.getFluidState();
            if( adjFluid.getFluid().isEquivalentTo( this ) && this.isAdjacentFluidSameAs( facing, world, pos, state, adjPos, adjBlock ) ) {
                if( adjFluid.isSource() ) {
                    adjacentSourceBlocks++;
                }

                maxLevel = Math.max( maxLevel, adjFluid.getLevel() );
            }
        }

        if( this.canSourcesMultiply() && adjacentSourceBlocks >= 2 ) {
            IBlockState block = world.getBlockState( pos.down( fallDirection ) );
            IFluidState fluid = block.getFluidState();
            if( block.getMaterial().isSolid() || this.isSourceState( fluid ) ) {
                return this.getStillFluidState( false );
            }
        }

        BlockPos upPos = pos.up( fallDirection );
        IBlockState block = world.getBlockState( upPos );
        IFluidState fluid = block.getFluidState();
        if( ! fluid.isEmpty() && fluid.getFluid().isEquivalentTo( this ) && this.isAdjacentFluidSameAs( up, world, pos, state, upPos, block ) ) {
            return this.getFlowingFluidState( 8, true );
        } else {
            int resultingLevel = maxLevel - this.getLevelDecreasePerBlock( world );
            if( resultingLevel <= 0 ) {
                return Fluids.EMPTY.getDefaultState();
            } else {
                return this.getFlowingFluidState( resultingLevel, false );
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
            byte b0 = adjacentFluidCache.getAndMoveToFirst( key );
            if( b0 != 127 ) {
                return b0 != 0;
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
        return this.getFlowingFluid().getDefaultState().with( LEVEL_1_TO_8, level ).with( FALLING, falling );
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

                if( this.canFlowHorizontalInto( world, this.getFlowingFluid(), pos, state, offset, otherPos, block, fluid ) ) {
                    // Compute slope and cache it
                    boolean hasSlope = slopeMap.computeIfAbsent( delta, integer -> {
                        BlockPos downPos = otherPos.down( fallDirection );
                        IBlockState downBlock = world.getBlockState( downPos );
                        return this.canFlowVerticalInto( world, this.getFlowingFluid(), otherPos, block, downPos, downBlock );
                    } );

                    // Slope found, return recursion
                    if( hasSlope ) {
                        return recursion;
                    }

                    if( recursion < this.getSlopeFindDistance( world ) ) {
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
    private boolean canFlowVerticalInto( IBlockReader world, Fluid fluid, BlockPos pos, IBlockState state, BlockPos adjpos, IBlockState adjState ) {
        if( ! this.isAdjacentFluidSameAs( down, world, pos, state, adjpos, adjState ) ) {
            return false;
        } else {
            return adjState.getFluidState().getFluid().isEquivalentTo( this ) || this.canBreakThrough( world, adjpos, adjState, fluid );
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
        return ! this.isSourceState( adjFluidState ) && this.isAdjacentFluidSameAs( facing, world, pos, state, adjPos, adjState ) && this.canBreakThrough( world, adjPos, adjState, fluid );
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
            if( this.isSourceState( state ) ) {
                ++ amount;
            }
        }

        return amount;
    }

    protected Map<EnumFacing, IFluidState> getFlowStates( IWorldReaderBase world, BlockPos pos, IBlockState state ) {
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

            IFluidState correctState = this.calculateCorrectFlowingState( world, adjPos, block );
            if( this.canFlowHorizontalInto( world, correctState.getFluid(), pos, state, facing, adjPos, block, fluid ) ) {
                BlockPos downPos = adjPos.down( fallDirection );
                boolean canFlowDown = slopeMap.computeIfAbsent( delta, integer -> {
                    IBlockState adjState = world.getBlockState( downPos );
                    return this.canFlowVerticalInto( world, this.getFlowingFluid(), adjPos, block, downPos, adjState );
                } );
                int flowWeight;
                if( canFlowDown ) {
                    flowWeight = 0;
                } else {
                    flowWeight = this.findSlope( world, adjPos, 1, facing.getOpposite(), block, pos, blockFluidMap, slopeMap );
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

    private boolean canBreakThrough( IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid ) {
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
        return toFluidState.canOtherFlowInto( fluidIn, direction ) && this.isAdjacentFluidSameAs( direction, worldIn, fromPos, fromBlockState, toPos, toBlockState ) && this.canBreakThrough( worldIn, toPos, toBlockState, fluidIn );
    }

    protected abstract int getLevelDecreasePerBlock( IWorldReaderBase worldIn );

    protected int getTickRate( World world, IFluidState currentState, IFluidState correctState ) {
        return this.getTickRate( world );
    }

    public void tick( World world, BlockPos pos, IFluidState state ) {
        if( ! state.isSource() ) {
            IFluidState correctState = this.calculateCorrectFlowingState( world, pos, world.getBlockState( pos ) );
            int rate = this.getTickRate( world, state, correctState );

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

    protected static int getLevelFromState( IFluidState state ) {
        return state.isSource() ? 0 : 8 - Math.min( state.getLevel(), 8 ) + ( state.get( FALLING ) ? 8 : 0 );
    }

    public float getHeight( IFluidState state ) {
        return (float) state.getLevel() / 9.0F;
    }

    public boolean isGas() {
        return isGas;
    }
}
