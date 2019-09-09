/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.EWaterlogType;
import modernity.client.particle.SaltParticle;
import modernity.common.block.MDBlockTags;
import modernity.common.fluid.MDFluids;
import modernity.common.item.MDItems;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings( "deprecation" )
public class BlockSaltCrystal extends BlockSinglePlant implements ILiquidContainer, IBucketPickupHandler {
    public static final IntegerProperty AGE = IntegerProperty.create( "age", 0, 11 );
    public static final BooleanProperty NATURAL = BooleanProperty.create( "natural" );
    public static final EnumProperty<EWaterlogType> WATERLOGGED = EnumProperty.create( "waterlogged", EWaterlogType.class );

    private static final int[] STAGE_HEIGHTS = {
            2,
            2,
            2,
            4,
            4,
            5,
            5,
            7,
            7,
            9,
            9,
            12
    };

    private static final AxisAlignedBB[] STATE_BOXES = new AxisAlignedBB[ 12 ];
    private static final VoxelShape[] STATE_SHAPES = new VoxelShape[ 12 ];

    static {
        for( int i = 0; i <= 11; i++ ) {
            STATE_BOXES[ i ] = new AxisAlignedBB( 1 / 16D, 0, 1 / 16D, 15 / 16D, STAGE_HEIGHTS[ i ] / 16D, 15 / 16D );
            STATE_SHAPES[ i ] = VoxelShapes.create( STATE_BOXES[ i ] );
        }
    }

    // Salt crystal grow area (s = source):
    //   o o o
    // o o o o o
    // o o s o o
    // o o o o o
    //   o o o

    private static final BlockPos[] GROW_AREA = {
            new BlockPos( - 1, 0, 0 ),
            new BlockPos( - 1, 0, 1 ),
            new BlockPos( 0, 0, 1 ),
            new BlockPos( 1, 0, 1 ),
            new BlockPos( 1, 0, 0 ),
            new BlockPos( 1, 0, - 1 ),
            new BlockPos( 0, 0, - 1 ),
            new BlockPos( - 1, 0, - 1 ),

            new BlockPos( - 2, 0, - 1 ),
            new BlockPos( - 2, 0, 0 ),
            new BlockPos( - 2, 0, 1 ),

            new BlockPos( - 1, 0, 2 ),
            new BlockPos( - 0, 0, 2 ),
            new BlockPos( 1, 0, 2 ),

            new BlockPos( 2, 0, 1 ),
            new BlockPos( 2, 0, 0 ),
            new BlockPos( 2, 0, - 1 ),

            new BlockPos( 1, 0, - 2 ),
            new BlockPos( 0, 0, - 2 ),
            new BlockPos( - 1, 0, - 2 )
    };

    public BlockSaltCrystal( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        setDefaultState( stateContainer.getBaseState().with( AGE, 0 ).with( NATURAL, true ).with( WATERLOGGED, EWaterlogType.NONE ) );
    }

    public BlockSaltCrystal( String id, Properties properties ) {
        super( id, properties );
        setDefaultState( stateContainer.getBaseState().with( AGE, 0 ).with( NATURAL, true ).with( WATERLOGGED, EWaterlogType.NONE ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( AGE, NATURAL, WATERLOGGED );
    }

    public boolean isSaltSource( IBlockState state ) {
        return state.isIn( MDBlockTags.SALT_SOURCE );
    }

    @Override
    public boolean ticksRandomly( IBlockState state ) {
        return true;
    }

    @Override
    public void randomTick( IBlockState state, World world, BlockPos pos, Random rand ) {

        // Grow older
        int age = state.get( AGE );
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        int growWeight = calculateGrowWeight( world, pos, mpos );
        if( age < 11 && rand.nextInt( 88 ) < growWeight ) {
            age++;
            world.setBlockState( pos, state.with( AGE, age ), 3 );
        }

        // Do not extend natural crystals
        if( state.get( NATURAL ) ) return;

        // Grow chance: 1/3
        if( rand.nextInt( 3 ) != 0 ) return;

        // Grow to close blocks
        if( rand.nextInt( 88 ) < growWeight && rand.nextInt( 12 ) < age ) {
            int totalWeight = 0;
            int[] weights = new int[ 20 ];
            int i = 0;

            // Compute weights for each possible grow position
            BlockPos.MutableBlockPos npos = new BlockPos.MutableBlockPos();
            for( BlockPos loc : GROW_AREA ) {
                npos.setPos( pos );
                npos.move( loc.getX(), 0, loc.getZ() );

                // Try to grow on a higher or lower block
                npos.move( EnumFacing.UP );
                if( ! canRemainAt( world, npos, getDefaultState() ) ) {
                    npos.move( EnumFacing.DOWN );
                    if( ! canRemainAt( world, npos, getDefaultState() ) )
                        npos.move( EnumFacing.DOWN );
                }

                // Compute weight
                IBlockState s = world.getBlockState( npos );
                if( canRemainAt( world, npos, getDefaultState() ) && ( s.getMaterial().isLiquid() || s.isAir( world, npos ) ) ) {
                    int w = calculateGrowWeight( world, npos, mpos );
                    if( i > 8 ) w /= 2;
                    totalWeight += w;
                    weights[ i ] = w;
                }

                i++;
            }

            // No growing when no place to grow...
            if( totalWeight == 0 ) return;

            // Find best growing place using weighted random: more saline places have higher weights...
            int random = rand.nextInt( totalWeight );
            int weight = 0;

            i = 0;
            for( ; i < 20; i++ ) {
                weight += weights[ i ];
                if( weight > random ) {
                    break;
                }
            }

            // Find the location to grow at: can we grow here
            BlockPos loc = GROW_AREA[ i ];
            npos.setPos( pos );
            npos.move( loc.getX(), 0, loc.getZ() );

            npos.move( EnumFacing.UP );
            if( ! canRemainAt( world, npos, getDefaultState() ) ) {
                npos.move( EnumFacing.DOWN );
                if( ! canRemainAt( world, npos, getDefaultState() ) )
                    npos.move( EnumFacing.DOWN );
            }

            // Grow to the selected block...
            IBlockState s = world.getBlockState( npos );
            if( canRemainAt( world, npos, getDefaultState() ) && ( s.getMaterial().isLiquid() || s.isAir( world, npos ) ) ) {
                world.setBlockState( npos, getDefaultState().with( NATURAL, rand.nextInt( 5 ) == 0 ).with( WATERLOGGED, EWaterlogType.getType( world.getFluidState( pos ) ) ) );
            }
        }
    }

    public int calculateGrowWeight( World world, BlockPos pos, BlockPos.MutableBlockPos mpos ) {
        int chance = 0;
        // Use modernized water as salt source
        if( world.getFluidState( pos ).getFluid() == MDFluids.MODERNIZED_WATER ) {
            chance += 44;
        }

        // Find block sources
        int sources = 0;
        for( int x = - 1; x <= 1; x++ ) {
            for( int z = - 1; z <= 1; z++ ) {
                mpos.setPos( pos );
                if( isSaltSource( world.getBlockState( mpos.move( x, - 1, z ) ) ) ) {
                    if( x == 0 && z == 0 ) {
                        sources += 24;
                    } else if( x == 0 || z == 0 ) {
                        sources += 4;
                    } else {
                        sources += 1;
                    }
                }
            }
        }

        chance += sources;

        return chance;
    }


    @Override
    public boolean canContainFluid( IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid ) {
        return fluid == MDFluids.MODERNIZED_WATER || fluid == Fluids.WATER;
    }

    @Override
    public boolean receiveFluid( IWorld world, BlockPos pos, IBlockState state, IFluidState fluidState ) {
        if( state.get( WATERLOGGED ) == EWaterlogType.NONE ) {
            if( fluidState.getFluid() == Fluids.WATER ) {
                if( ! world.isRemote() ) {
                    world.setBlockState( pos, state.with( WATERLOGGED, EWaterlogType.WATER ), 3 );
                    world.getPendingFluidTicks().scheduleTick( pos, Fluids.WATER, Fluids.WATER.getTickRate( world ) );
                }
            } else if( fluidState.getFluid() == MDFluids.MODERNIZED_WATER ) {
                if( ! world.isRemote() ) {
                    world.setBlockState( pos, state.with( WATERLOGGED, EWaterlogType.MODERNIZED_WATER ), 3 );
                    world.getPendingFluidTicks().scheduleTick( pos, MDFluids.MODERNIZED_WATER, MDFluids.MODERNIZED_WATER.getTickRate( world ) );
                }
            } else {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, IBlockState state ) {
        if( state.get( WATERLOGGED ) == EWaterlogType.WATER ) {
            world.setBlockState( pos, state.with( WATERLOGGED, EWaterlogType.NONE ), 3 );
            return Fluids.WATER;
        }
        return Fluids.EMPTY;
    }

    @Override
    public IFluidState getFluidState( IBlockState state ) {
        return state.get( WATERLOGGED ).getFluidState();
    }

    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( state.get( WATERLOGGED ) == EWaterlogType.WATER ) {
            world.getPendingFluidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickRate( world ) );
        }
        if( state.get( WATERLOGGED ) == EWaterlogType.MODERNIZED_WATER ) {
            world.getPendingFluidTicks().scheduleTick( currentPos, MDFluids.MODERNIZED_WATER, MDFluids.MODERNIZED_WATER.getTickRate( world ) );
        }
        return state;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        IFluidState fluid = context.getWorld().getFluidState( context.getPos() );
        return getDefaultState().with( WATERLOGGED, EWaterlogType.getType( fluid ) ).with( NATURAL, false );
    }

    public IBlockState getGenerationStage( Random rand, IFluidState state ) {
        int randAge = rand.nextInt( 6 );
        if( rand.nextBoolean() ) {
            randAge += rand.nextInt( 7 );
        }
        return getDefaultState().with( AGE, randAge ).with( WATERLOGGED, EWaterlogType.getType( state ) );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        if( canRemainAt( world, pos, world.getBlockState( pos ) ) && ! world.getBlockState( pos ).getMaterial().blocksMovement() ) {
            world.setBlockState( pos, getGenerationStage( rand, world.getFluidState( pos ) ), 2 | 16 );
            return true;
        }
        return false;
    }

    @Override
    public void getDrops( IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune ) {
        int nuggetAmount = world.rand.nextInt( Math.max( 6 - fortune, 1 ) * 2 ) == 0 ? world.rand.nextInt( 2 ) : 0;
        int dustAmount = world.rand.nextInt( fortune + 1 ) + 1;
        int crystalAmount = world.rand.nextInt( 100 ) < fortune + 1 ? 1 : 0;

        if( state.get( AGE ) != 11 ) {
            nuggetAmount = 0;
            crystalAmount = 0;
            dustAmount = world.rand.nextInt( 7 / ( fortune + 1 ) ) == 0 ? 1 : 0;
        }

        if( nuggetAmount > 0 ) {
            drops.add( new ItemStack( MDItems.SALT_NUGGET, nuggetAmount ) );
        }
        if( dustAmount > 0 ) {
            drops.add( new ItemStack( MDItems.SALT_DUST, dustAmount ) );
        }
        if( crystalAmount > 0 ) {
            drops.add( new ItemStack( this, crystalAmount ) );
        }
    }

    @Override
    protected ItemStack getSilkTouchDrop( IBlockState state ) {
        return new ItemStack( this );
    }

    @Override
    public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        return STATE_SHAPES[ state.get( AGE ) ];
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void animateTick( IBlockState state, World world, BlockPos pos, Random rand ) {
        if( rand.nextInt( 10 ) == 0 ) {
            AxisAlignedBB aabb = STATE_BOXES[ state.get( AGE ) ];

            double x = rand.nextDouble() * ( aabb.maxX - aabb.minX ) + aabb.minX + pos.getX();
            double y = rand.nextDouble() * ( aabb.maxY - aabb.minY ) + aabb.minY + pos.getY();
            double z = rand.nextDouble() * ( aabb.maxZ - aabb.minZ ) + aabb.minZ + pos.getZ();

            Minecraft.getInstance().particles.addEffect( new SaltParticle( world, x, y, z, rand.nextDouble() * 0.04 - 0.02, rand.nextDouble() * 0.04 - 0.02, rand.nextDouble() * 0.04 - 0.02 ) );
        }
    }
}
