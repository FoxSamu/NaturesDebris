/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.block.IColoredBlock;
import modernity.api.util.IBlockProvider;
import modernity.api.util.MDVoxelShapes;
import modernity.api.util.MovingBlockPos;
import modernity.client.ModernityClient;
import modernity.common.block.MDBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
 * Describes a plant that hangs on a block.
 */
@SuppressWarnings( "deprecation" )
public class HangingPlantBlock extends Block implements IBlockProvider {
    public static final BooleanProperty BOTTOM = BooleanProperty.create( "bottom" );
    public static final BooleanProperty TOP = BooleanProperty.create( "top" );
    private int maxLength;

    public HangingPlantBlock( Properties properties ) {
        super( properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( BOTTOM, TOP );
    }

    /**
     * Returns the maximum length this plant can be (longer is not allowed).
     */
    protected int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the maximum length of this plant.
     */
    public void setMaxLength( int len ) {
        this.maxLength = len;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return VoxelShapes.empty(); // No colliding!
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
     * Checks whether this block is the bottom state
     */
    public boolean isBottom( IBlockReader world, BlockPos pos, BlockState state ) {
        return ! isSelfState( world.getBlockState( pos.down() ) );
    }

    /**
     * Checks whether this block is the top state
     */
    public boolean isTop( IBlockReader world, BlockPos pos, BlockState state ) {
        return ! isSelfState( world.getBlockState( pos.up() ) );
    }

    /**
     * Checks whether this block can remain at a specific position. This checks the maximum length and block
     * sustainability.
     */
    public boolean canRemainAt( IBlockReader world, BlockPos pos, BlockState state ) {
        BlockState up = world.getBlockState( pos.up() );
        if( canBlockSustain( up ) ) return true;
        if( ! isSelfState( up ) ) return false;
        int len = getMaxLength();
        if( len > 0 ) {
            MovingBlockPos rpos = new MovingBlockPos();
            for( int i = 0; i < len; i++ ) {
                rpos.moveUp();
                if( canBlockSustain( world.getBlockState( rpos ) ) ) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Is the specified block state the same block as this block?
     */
    public boolean isSelfState( BlockState state ) {
        return state.getBlock() == this;
    }

    /**
     * Is the specified block sustainable for this plant?
     */
    public boolean canBlockSustain( BlockState state ) {
        return state.isSolid();
    }

    /**
     * Checks if we can generate this plant at the specified position.
     */
    public boolean canGenerateAt( IBlockReader reader, BlockPos pos, BlockState state ) {
        return canBlockSustain( reader.getBlockState( pos.up() ) );
    }

    /**
     * Destroys this plant.
     */
    public void destroy( World world, BlockPos pos, BlockState state ) {
        world.removeBlock( pos, false );
        spawnDrops( state, world, pos );
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
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
        return provide( world, pos, rand, rng -> rand.nextInt( 8 ) + 1 );
    }

    /**
     * Blocks this state the generation of this plant?
     */
    private boolean blocked( BlockState state ) {
        return state.getMaterial().blocksMovement() || state.getMaterial().isLiquid() || isSelfState( state );
    }

    /**
     * Places this plant at the specified position
     *
     * @param world     The world to generate in
     * @param pos       The position to generate at
     * @param rand      A random number generator to use
     * @param heightGen A function that generates a random length for this plant.
     * @return True if something is generated.
     */
    public boolean provide( IWorld world, BlockPos pos, Random rand, Function<Random, Integer> heightGen ) {
        if( canGenerateAt( world, pos, world.getBlockState( pos ) ) && ! blocked( world.getBlockState( pos ) ) ) {
            int len = heightGen.apply( rand );
            int max = getMaxLength();
            if( max > 0 && len > max ) len = max;
            int m = 0;
            MovingBlockPos rpos = new MovingBlockPos( pos );

            for( int i = 0; i < len; i++ ) {
                rpos.moveDown();
                boolean end = i == len - 1;
                if( blocked( world.getBlockState( rpos ) ) ) {
                    end = true;
                }
                rpos.moveUp();
                boolean start = i == 0;
                if( ! blocked( world.getBlockState( rpos ) ) ) {
                    world.setBlockState( rpos, getDefaultState().with( BOTTOM, end ).with( TOP, start ), 2 | 16 );
                    m++;
                } else {
                    break;
                }
                rpos.moveDown();
            }
            return m > 0;
        }
        return false;
    }

    /**
     * The murina block.
     */
    public static class ColoredMurina extends HangingPlantBlock implements IColoredBlock {
        public static final VoxelShape MURINA_SHAPE = MDVoxelShapes.create16( 5, 0, 5, 11, 16, 11 );

        public ColoredMurina( Properties properties ) {
            super( properties );
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
            return state.isSolid() || state.isIn( MDBlockTags.LEAVES );
        }

        @Override
        public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
            return MURINA_SHAPE;
        }
    }
}
