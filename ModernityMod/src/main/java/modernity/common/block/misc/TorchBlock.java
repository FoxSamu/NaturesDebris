/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.fluid.WaterloggedBlock;
import modernity.generic.block.ISolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

import static net.minecraft.util.Direction.*;

/**
 * Describes a torch block. Unlike vanilla, this torch block describes both standing and hanging torches.
 */
@SuppressWarnings( "deprecation" )
public class TorchBlock extends WaterloggedBlock {
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap( ImmutableMap.of(
        SOUTH, Block.makeCuboidShape( 5.5, 3, 11, 10.5, 13, 16 ),
        NORTH, Block.makeCuboidShape( 5.5, 3, 0, 10.5, 13, 5 ),
        EAST, Block.makeCuboidShape( 11, 3, 5.5, 16, 13, 10.5 ),
        WEST, Block.makeCuboidShape( 0, 3, 5.5, 5, 13, 10.5 ),
        DOWN, Block.makeCuboidShape( 6, 0, 6, 10, 10, 10 )
    ) );

    public static final DirectionProperty FACING = MDBlockStateProperties.FACING_NO_UP;

    private final boolean burning;

    public TorchBlock( boolean burning, Properties properties ) {
        super( properties );
        this.burning = burning;
        setDefaultState( stateContainer.getBaseState().with( FACING, DOWN ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( FACING );
    }

    /**
     * Checks if a torch can hang on the specified facing in the specified context.
     *
     * @param world  The world where this torch hangs in
     * @param pos    The position of the torch
     * @param facing The facing of the torch
     */
    protected boolean canRemainForFacing( IWorldReader world, BlockPos pos, Direction facing ) {
        if( facing == UP ) return false;
        BlockPos offPos = pos.offset( facing );
        if( facing == DOWN ) {
            return hasEnoughSolidSide( world, offPos, Direction.UP ) || ISolidBlock.isSolid( world, offPos, Direction.UP );
        } else {
            return ISolidBlock.isSolid( world, offPos, facing.getOpposite() );
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        BlockState state = super.getStateForPlacement( context );
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Direction[] nearest = context.getNearestLookingDirections();

        for( Direction facing : nearest ) {
            if( canRemainForFacing( world, pos, facing ) ) {
                return state.with( FACING, facing );
            }
        }

        return null;
    }

    @Override
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        if( facing == UP ) return state;
        if( facing == state.get( FACING ) ) {
            if( ! canRemainForFacing( world, currentPos, facing ) ) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return state;
    }

    @Override
    public void animateTick( BlockState state, World world, BlockPos pos, Random rand ) {
        if( burning ) {
            Vec3d partPos = getParticlePos( pos, state.get( FACING ) );
            world.addParticle( ParticleTypes.SMOKE, partPos.x, partPos.y, partPos.z, 0, 0, 0 );
            world.addParticle( ParticleTypes.FLAME, partPos.x, partPos.y, partPos.z, 0, 0, 0 );
        }
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return SHAPES.get( state.get( FACING ) );
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return VoxelShapes.empty();
    }

    /**
     * Returns the position of the particles (flames, smoke) for a specific torch facing.
     *
     * @param pos    The position of the torch
     * @param facing The facing of the torch.
     * @return The position of the torch's particles.
     *
     * @throws UnsupportedOperationException When facing is {@link Direction#UP}.
     */
    public static Vec3d getParticlePos( BlockPos pos, Direction facing ) {
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
