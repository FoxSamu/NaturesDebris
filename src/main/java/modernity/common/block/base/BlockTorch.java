/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 29 - 2019
 */

package modernity.common.block.base;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Particles;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

import static net.minecraft.block.state.BlockFaceShape.*;
import static net.minecraft.util.EnumFacing.*;

public class BlockTorch extends BlockWaterlogged {
    private static final Map<EnumFacing, VoxelShape> SHAPES = Maps.newEnumMap( ImmutableMap.of(
            SOUTH, Block.makeCuboidShape( 5.5, 3, 11, 10.5, 13, 16 ),
            NORTH, Block.makeCuboidShape( 5.5, 3, 0, 10.5, 13, 5 ),
            EAST, Block.makeCuboidShape( 11, 3, 5.5, 16, 13, 10.5 ),
            WEST, Block.makeCuboidShape( 0, 3, 5.5, 5, 13, 10.5 ),
            DOWN, Block.makeCuboidShape( 6, 0, 6, 10, 10, 10 )
    ) );

    public static final DirectionProperty FACING = DirectionProperty.create( "facing", facing -> facing != EnumFacing.UP );

    private static final EnumFacing[] ALLOWED_FACINGS = {
            DOWN,
            NORTH,
            EAST,
            SOUTH,
            WEST
    };

    private final boolean burning;

    public BlockTorch( String id, boolean burning, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        this.burning = burning;
        setDefaultState( stateContainer.getBaseState().with( FACING, DOWN ) );
    }

    public BlockTorch( String id, boolean burning, Properties properties ) {
        super( id, properties );
        this.burning = burning;
        setDefaultState( stateContainer.getBaseState().with( FACING, DOWN ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( FACING );
    }

    protected boolean canRemainForFacing( IBlockReader world, BlockPos pos, EnumFacing facing ) {
        if( facing == UP ) return false;
        BlockPos offPos = pos.offset( facing );
        BlockFaceShape bfs = world.getBlockState( offPos ).getBlockFaceShape( world, offPos, facing.getOpposite() );
        if( facing == DOWN ) {
            return bfs == CENTER || bfs == CENTER_SMALL || bfs == CENTER_BIG || bfs == SOLID;
        } else {
            return bfs == SOLID;
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        IBlockState state = super.getStateForPlacement( context );
        if( state == null ) return null;
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        EnumFacing[] nearest = context.getNearestLookingDirections();

        for( EnumFacing facing : nearest ) {
            if( canRemainForFacing( world, pos, facing ) ) {
                return state.with( FACING, facing );
            }
        }

//        if( ! canRemainForFacing( world, pos, selectedFace.getOpposite() ) ) {
//            for( EnumFacing facing : ALLOWED_FACINGS ) {
//                if( canRemainForFacing( world, pos, facing ) ) {
//                    return getDefaultState().with( FACING, facing );
//                }
//            }
//            return null;
//        }
//        return getDefaultState().with( FACING, selectedFace.getOpposite() );
        return null;
    }

    @Override
    public void tick( IBlockState state, World world, BlockPos pos, Random random ) {
        state.dropBlockAsItem( world, pos, 0 );
        world.removeBlock( pos );
    }

    @Override
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        if( facing == UP ) return state;
        if( facing == state.get( FACING ) ) {
            if( ! canRemainForFacing( world, currentPos, facing ) ) {
                world.getPendingBlockTicks().scheduleTick( currentPos, this, 0 );
                return state;
            }
        }
        return state;
    }

    @Override
    public void animateTick( IBlockState state, World world, BlockPos pos, Random rand ) {
        if( burning ) {
            Vec3d partPos = getParticlePos( pos, state.get( FACING ) );
            world.addParticle( Particles.SMOKE, partPos.x, partPos.y, partPos.z, 0, 0, 0 );
            world.addParticle( Particles.FLAME, partPos.x, partPos.y, partPos.z, 0, 0, 0 );
        }
    }

    @Override
    public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        return SHAPES.get( state.get( FACING ) );
    }

    @Override
    public VoxelShape getCollisionShape( IBlockState state, IBlockReader worldIn, BlockPos pos ) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isFullCube( IBlockState state ) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face ) {
        return UNDEFINED;
    }

    public static Vec3d getParticlePos( BlockPos pos, EnumFacing facing ) {
        double x = pos.getX() + .5;
        double y = pos.getY() + .7;
        double z = pos.getZ() + .5;
        if( facing == DOWN ) {
            return new Vec3d( x, y, z );
        } else if( facing == UP ) {
            throw new UnsupportedOperationException( "getParticlePos( pos, UP )" );
        } else {
            return new Vec3d(
                    .27 * facing.getXOffset() + x,
                    y + .22,
                    .27 * facing.getZOffset() + z
            );
        }
    }
}
