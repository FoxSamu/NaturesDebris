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
import modernity.api.util.IBlockProvider;
import modernity.api.util.MDVoxelShapes;
import modernity.api.util.MovingBlockPos;
import modernity.client.ModernityClient;
import modernity.common.fluid.MDFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

/**
 * Describes a tall plant block (a block that can grow higher than one block).
 */
@SuppressWarnings( "deprecation" )
public class TallPlantBlock extends Block implements IBlockProvider {
    public static final BooleanProperty BOTTOM = BooleanProperty.create( "bottom" );
    public static final BooleanProperty TOP = BooleanProperty.create( "top" );
    private int maxHeight;

    public TallPlantBlock( Properties properties ) {
        super( properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( BOTTOM, TOP );
    }

    /**
     * Returns the maximum height of this plant (the plant may not grow higher)
     */
    protected int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Sets the maximum height of this plant
     */
    public void setMaxHeight( int maxHeight ) {
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean isSolid( BlockState state ) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return VoxelShapes.empty();
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos ) {
        if( facing == Direction.DOWN ) {
            state = state.with( BOTTOM, isBottom( world, pos, state ) );
        }
        if( facing == Direction.UP ) {
            state = state.with( TOP, isTop( world, pos, state ) );
        }
        return state;
    }

    /**
     * Is this the lowest part of the block?
     */
    public boolean isBottom( IBlockReader world, BlockPos pos, BlockState state ) {
        return ! isSelfState( world.getBlockState( pos.down() ) );
    }

    /**
     * Is this the highest part of the block?
     */
    public boolean isTop( IBlockReader world, BlockPos pos, BlockState state ) {
        return ! isSelfState( world.getBlockState( pos.up() ) );
    }

    /**
     * Checks if the plant can remain in the specified context.
     */
    public boolean canRemainAt( IBlockReader world, BlockPos pos, BlockState state ) {
        BlockState down = world.getBlockState( pos.down() );
        if( canBlockSustain( world, pos.down(), down ) ) return true;
        if( ! isSelfState( down ) ) return false;
        int mh = getMaxHeight();
        if( mh > 0 ) {
            MovingBlockPos rpos = new MovingBlockPos( pos );
            for( int i = 0; i < mh; i++ ) {
                rpos.moveDown();
                if( canBlockSustain( world, rpos, world.getBlockState( rpos ) ) ) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the specified block state is this block.
     */
    public boolean isSelfState( BlockState state ) {
        return state.getBlock() == this;
    }

    /**
     * Checks if the specified block state can sustain this plant.
     */
    public boolean canBlockSustain( BlockState state ) {
        return state.isSolid();
    }

    /**
     * Checks if the specified block state can sustain this plant in the specified context.
     */
    public boolean canBlockSustain( IBlockReader world, BlockPos pos, BlockState state ) {
        return canBlockSustain( state );
    }

    /**
     * Checks if the plant can be generated in the world at the specified location.
     */
    public boolean canGenerateAt( IBlockReader reader, BlockPos pos, BlockState state ) {
        return canBlockSustain( reader, pos.down(), reader.getBlockState( pos.down() ) );
    }

    /**
     * Destroys the plant block at specific position, dropping it's items.
     */
    public void destroy( World world, BlockPos pos, BlockState state ) {
        world.setBlockState( pos, Blocks.AIR.getDefaultState(), 3 );
        Block.spawnDrops( state, world, pos );
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return useContext.getItem().getItem() != asItem();
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader worldIn, BlockPos pos, PathType type ) {
        return true;
    }

    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean moving ) {
        if( ! canRemainAt( world, pos, state ) ) {
            destroy( world, pos, state );
        }
    }

    @Override
    public void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving ) {
        if( ! canRemainAt( world, pos, state ) ) {
            destroy( world, pos, state );
        }
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }


    @Override
    public boolean isValidPosition( BlockState state, IWorldReader world, BlockPos pos ) {
        return canRemainAt( world, pos, state );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        return provide( world, pos, rand, rng -> {
            if( rng.nextInt( 10 ) == 0 ) return 2;
            return 1;
        } );
    }

    private boolean blocked( BlockState state ) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() || isSelfState( state );
    }

    /**
     * Places this plant at the specified position in the specified world.
     * @param world The world to generate in
     * @param pos   The position to generate at
     * @param rand  A random number generator
     * @param heightGen A function that generates a random height.
     * @return True if something is generated.
     */
    public boolean provide( IWorld world, BlockPos pos, Random rand, Function<Random, Integer> heightGen ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
            int height = heightGen.apply( rand );
            if( height > getMaxHeight() ) height = getMaxHeight();
            int m = 0;
            MovingBlockPos rpos = new MovingBlockPos( pos );

            for( int i = 0; i < height; i++ ) {
                rpos.moveUp();
                boolean end = i == height - 1;
                if( blocked( world.getBlockState( rpos ) ) ) {
                    end = true;
                }
                rpos.moveDown();
                boolean start = i == 0;
                if( ! blocked( world.getBlockState( rpos ) ) ) {
                    world.setBlockState( rpos, getDefaultState().with( BOTTOM, start ).with( TOP, end ), 2 | 16 );
                    m++;
                } else {
                    break;
                }
                rpos.moveUp();
            }
            return m > 0;
        }
        return false;
    }

    /**
     * A grass-colored tall plant
     */
    public static class ColoredGrass extends TallPlantBlock implements IColoredBlock {
        public static final VoxelShape GRASS_END_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 10, 14 );
        public static final VoxelShape GRASS_MIDDLE_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 16, 14 );

        public ColoredGrass( Properties properties ) {
            super( properties );
            setMaxHeight( 4 );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
            return ModernityClient.get().getGrassColors().getColor( reader, pos );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return ModernityClient.get().getGrassColors().getItemColor();
        }

        @Override
        public boolean canBlockSustain( BlockState state ) {
            return state.getBlock() instanceof DirtBlock;
        }

        @Override
        public OffsetType getOffsetType() {
            return OffsetType.XZ;
        }

        @Override
        public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
            Vec3d off = state.getOffset( world, pos );
            return ( state.get( TOP ) ? GRASS_END_SHAPE : GRASS_MIDDLE_SHAPE ).withOffset( off.x, off.y, off.z );
        }
    }
}
