/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 26 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.api.block.IColoredBlock;
import modernity.api.util.IBlockProvider;
import modernity.api.util.MovingBlockPos;
import modernity.client.ModernityClient;
import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.prop.SignedIntegerProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
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

public class PuddleBlock extends Block implements IColoredBlock, IBlockProvider {
    public static final SignedIntegerProperty DISTANCE = MDBlockStateProperties.DISTANCE_N1_3;
    private static final VoxelShape SHAPE = VoxelShapes.empty();

    public PuddleBlock( Properties properties ) {
        super( properties.tickRandomly() );

        setDefaultState( getDefaultState().with( DISTANCE, 3 ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( DISTANCE );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context ) {
        return SHAPE;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isSolid( BlockState state ) {
        return false;
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType( BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity ) {
        return PathNodeType.WATER;
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( entity instanceof EndermanEntity ) {
            entity.attackEntityFrom( DamageSource.DROWN, 0.5f );
        }
    }

    @Override
    public void tick( BlockState state, World world, BlockPos pos, Random random ) {
        updateDistance( world, pos, state );
    }

    @Override
    public void randomTick( BlockState state, World world, BlockPos pos, Random random ) {
        tick( state, world, pos, random );
        state = world.getBlockState( pos );
        if( state.get( DISTANCE ) == 3 ) {
            world.setBlockState( pos, Blocks.AIR.getDefaultState() );
        }
    }

    @Override
    public void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving ) {
        if( isMoving ) return;
        if( ! canRemain( world, pos ) ) {
            world.setBlockState( pos, Blocks.AIR.getDefaultState() );
            return;
        }
        world.getPendingBlockTicks().scheduleTick( pos, this, 0 );
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos ) {
        if( ! canRemain( world, pos ) ) {
            return Blocks.AIR.getDefaultState();
        }
        world.getPendingBlockTicks().scheduleTick( pos, this, 0 );
        return state;
    }

    private void updateDistance( World world, BlockPos pos, BlockState state ) {
        if( state.get( DISTANCE ) == - 1 ) {
            return;
        }
        int dist;
        if( ! world.isRaining() ) {
            dist = 3;
        } else if( world.isRainingAt( pos ) ) {
            dist = 0;
        } else {
            MovingBlockPos mpos = new MovingBlockPos();
            int leastDist = 3;
            for( Direction dir : Direction.Plane.HORIZONTAL ) {
                for( int y = 0; y < 10; y++ ) {
                    mpos.setPos( pos ).move( dir ).moveUp( y );
                    BlockState s = world.getBlockState( mpos );
                    if( s.getBlock() == this ) {
                        int d = s.get( DISTANCE );
                        if( d < leastDist ) leastDist = d;
                    }
                }
            }
            dist = leastDist + 1;
        }
        int oldDist = state.get( DISTANCE );
        if( dist >= 3 ) {
            dist = 3;
        }
        if( oldDist != dist ) {
            world.setBlockState( pos, state.with( DISTANCE, dist ), 7 );
        }
    }

    public void rainTick( World world, BlockPos pos, BlockState state, double spreadChance ) {
        if( state.get( DISTANCE ) == - 1 ) {
            return;
        }
        if( ! world.isAreaLoaded( pos, 2 ) ) return;
        MovingBlockPos mpos = new MovingBlockPos();
        if( state.get( DISTANCE ) < 2 ) {
            for( Direction dir : Direction.Plane.HORIZONTAL ) {
                if( world.rand.nextDouble() < spreadChance ) {
                    for( int y = 0; y > - 10; y-- ) {
                        mpos.setPos( pos ).move( dir ).moveUp( y );
                        BlockState state1 = world.getBlockState( mpos );
                        if( state1.getBlock() == this && state1.get( DISTANCE ) > state.get( DISTANCE ) ) {
                            if( state1.get( DISTANCE ) > state.get( DISTANCE ) + 1 ) {
                                BlockState newState = state1.with( DISTANCE, state.get( DISTANCE ) + 1 );
                                world.setBlockState( mpos, newState );
                                state1 = newState;
                            }
                            rainTick( world, mpos, state1, spreadChance * 2 / 3 );
                            break;
                        }
                        if( ! state1.isAir( world, mpos ) ) {
                            break;
                        }
                        if( canRemain( world, mpos ) ) {
                            world.setBlockState( mpos, state.with( DISTANCE, state.get( DISTANCE ) + 1 ), 7 );
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean canRemain( IWorldReader world, BlockPos pos ) {
        BlockState state = world.getBlockState( pos.down() );
        Block block = state.getBlock();
        if( state.getMaterial().blocksMovement() && block != Blocks.ICE && block != Blocks.PACKED_ICE && block != Blocks.BARRIER && ! state.isIn( BlockTags.LEAVES ) ) {
            return Block.doesSideFillSquare( state.getCollisionShape( world, pos.down() ), Direction.UP );
        }
        return false;
    }


    @Override
    public boolean isValidPosition( BlockState state, IWorldReader world, BlockPos pos ) {
        return canRemain( world, pos );
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return true;
    }

    @Override
    public boolean isSideInvisible( BlockState state, BlockState adjacentState, Direction side ) {
        return side.getHorizontalIndex() != - 1 && adjacentState.getBlock() == this
                   || super.isSideInvisible( state, adjacentState, side );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return ModernityClient.get().getWaterColors().getColor( reader, pos );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        if( world.isAirBlock( pos ) && canRemain( world, pos ) ) {
            world.setBlockState( pos, getDefaultState().with( DISTANCE, - 1 ), 2 | 16 );
        }
        return false;
    }
}
