/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 27 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import modernity.generic.util.Events;
import modernity.common.block.fluid.WaterlogType;
import modernity.common.block.fluid.WaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings( "deprecation" )
public class ExtTrapDoorBlock extends WaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected static final VoxelShape EAST_HITBOX = makeCuboidShape( 0, 0, 0, 3, 16, 16 );
    protected static final VoxelShape WEST_HITBOX = makeCuboidShape( 13, 0, 0, 16, 16, 16 );
    protected static final VoxelShape SOUTH_HITBOX = makeCuboidShape( 0, 0, 0, 16, 16, 3 );
    protected static final VoxelShape NORTH_HITBOX = makeCuboidShape( 0, 0, 13, 16, 16, 16 );
    protected static final VoxelShape DOWN_HITBOX = makeCuboidShape( 0, 0, 0, 16, 3, 16 );
    protected static final VoxelShape UP_HITBOX = makeCuboidShape( 0, 13, 0, 16, 16, 16 );

    public ExtTrapDoorBlock( Block.Properties properties ) {
        super( properties );
        setDefaultState(
            getStateContainer()
                .getBaseState()
                .with( FACING, Direction.NORTH )
                .with( OPEN, false ).with( HALF, Half.BOTTOM )
                .with( POWERED, false )
                .with( WATERLOGGED, WaterlogType.NONE )
        );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        if( ! state.get( OPEN ) ) {
            return state.get( HALF ) == Half.TOP ? UP_HITBOX : DOWN_HITBOX;
        } else {
            switch( state.get( FACING ) ) {
                default:
                case NORTH:
                    return NORTH_HITBOX;
                case SOUTH:
                    return SOUTH_HITBOX;
                case WEST:
                    return WEST_HITBOX;
                case EAST:
                    return EAST_HITBOX;
            }
        }
    }

    @Override
    public boolean allowsMovement( BlockState state, IBlockReader world, BlockPos pos, PathType type ) {
        switch( type ) {
            case LAND:
            case AIR:
                return state.get( OPEN );
            case WATER:
                return ! state.get( WATERLOGGED ).isEmpty();
            default:
                return false;
        }
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit ) {
        if( material == Material.IRON ) {
            return false;
        } else {
            state = state.cycle( OPEN );
            world.setBlockState( pos, state, 2 );

            if( ! state.get( WATERLOGGED ).isEmpty() ) {
                Fluid f = world.getFluidState( pos ).getFluid();
                world.getPendingFluidTicks().scheduleTick( pos, f, f.getTickRate( world ) );
            }

            playSound( player, world, pos, state.get( OPEN ) );
            return true;
        }
    }

    protected void playSound( @Nullable PlayerEntity player, World world, BlockPos pos, boolean open ) {
        if( open ) {
            int event = material == Material.IRON ? Events.IRON_TRAPDOOR_OPEN : Events.WOODEN_TRAPDOOR_OPEN;
            world.playEvent( player, event, pos, 0 );
        } else {
            int event = material == Material.IRON ? Events.IRON_TRAPDOOR_CLOSE : Events.WOODEN_TRAPDOOR_CLOSE;
            world.playEvent( player, event, pos, 0 );
        }

    }

    @Override
    public void neighborChanged( BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving ) {
        if( ! world.isRemote ) {
            boolean powered = world.isBlockPowered( pos );
            if( powered != state.get( POWERED ) ) {
                if( state.get( OPEN ) != powered ) {
                    state = state.with( OPEN, powered );
                    playSound( null, world, pos, powered );
                }

                world.setBlockState( pos, state.with( POWERED, powered ), 2 );

                if( ! state.get( WATERLOGGED ).isEmpty() ) {
                    Fluid f = world.getFluidState( pos ).getFluid();
                    world.getPendingFluidTicks().scheduleTick( pos, f, f.getTickRate( world ) );
                }
            }

        }
    }

    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        BlockState def = getDefaultState();
        IFluidState fstate = context.getWorld().getFluidState( context.getPos() );
        Direction face = context.getFace();

        if( ! context.replacingClickedOnBlock() && face.getAxis().isHorizontal() ) {
            def = def.with( FACING, face ).with( HALF, context.getHitVec().y - context.getPos().getY() > 0.5 ? Half.TOP : Half.BOTTOM );
        } else {
            def = def.with( FACING, context.getPlacementHorizontalFacing().getOpposite() ).with( HALF, face == Direction.UP ? Half.BOTTOM : Half.TOP );
        }

        if( context.getWorld().isBlockPowered( context.getPos() ) ) {
            def = def.with( OPEN, true ).with( POWERED, true );
        }

        return def.with( WATERLOGGED, WaterlogType.getType( fstate ) );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( FACING, OPEN, HALF, POWERED, WATERLOGGED );
    }

    @Override
    public boolean isLadder( BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity ) {
        if( state.get( OPEN ) ) {
            BlockState down = world.getBlockState( pos.down() );
            if( down.getBlock() instanceof LadderBlock )
                return down.get( LadderBlock.FACING ) == state.get( FACING );
            if( down.getBlock() instanceof ExtLadderBlock )
                return down.get( ExtLadderBlock.FACING ) == state.get( FACING );
        }
        return false;
    }

    @Override
    public boolean canEntitySpawn( BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type ) {
        return false;
    }

    @Override
    public BlockState rotate( BlockState state, Rotation rot ) {
        return state.with( FACING, rot.rotate( state.get( FACING ) ) );
    }

    @Override
    public BlockState mirror( BlockState state, Mirror mirr ) {
        return state.with( FACING, mirr.mirror( state.get( FACING ) ) );
    }
}