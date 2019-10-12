/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 24 - 2019
 */

package modernity.common.block.base;

import modernity.api.util.EWaterlogType;
import modernity.api.util.MDVoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

@SuppressWarnings( "deprecation" )
public class VerticalSlabBlock extends WaterloggedBlock {
    public static final EnumProperty<Type> TYPE = EnumProperty.create( "type", Type.class );

    public VerticalSlabBlock( Properties properties ) {
        super( properties );

        setDefaultState( stateContainer.getBaseState().with( TYPE, Type.DOWN ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( TYPE );
    }

    // Vanilla has special behavior for vanilla's SlabBlock, we're not that class so special case here...
    @Override
    public int getPackedLightmapCoords( BlockState state, IEnviromentBlockReader source, BlockPos pos ) {
        int i = source.getCombinedLight( pos, state.getLightValue( source, pos ) );
        if( i == 0 && ( state.get( TYPE ).ordinal() < 2 || state.get( TYPE ) == Type.DOUBLE ) ) {
            pos = pos.down();
            state = source.getBlockState( pos );
            return source.getCombinedLight( pos, state.getLightValue( source, pos ) );
        } else {
            return i;
        }
    }

    @Override
    public int getOpacity( BlockState state, IBlockReader reader, BlockPos pos ) {
        return reader.getMaxLightLevel();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext ctx ) {
        IFluidState fluid = ctx.getWorld().getFluidState( ctx.getPos() );
        BlockState state = ctx.getWorld().getBlockState( ctx.getPos() );
        if( state.getBlock() == this ) {
            if( state.get( TYPE ) == Type.DOUBLE ) return null;
            else return state.with( TYPE, Type.DOUBLE ).with( WATERLOGGED, EWaterlogType.NONE );
        }
        boolean sidedPlacing = ctx.getPlayer() != null && ctx.getPlayer().isSneaking();
        if( sidedPlacing ) {
            Direction facing = ctx.getFace().getOpposite();
            switch( facing ) {
                default:
                case UP:
                    return getDefaultState().with( TYPE, Type.UP ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
                case DOWN:
                    return getDefaultState().with( TYPE, Type.DOWN ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
                case NORTH:
                    return getDefaultState().with( TYPE, Type.NORTH ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
                case EAST:
                    return getDefaultState().with( TYPE, Type.EAST ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
                case SOUTH:
                    return getDefaultState().with( TYPE, Type.SOUTH ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
                case WEST:
                    return getDefaultState().with( TYPE, Type.WEST ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
            }
        } else {
            Direction facing = ctx.getFace();
            if( facing == Direction.UP )
                return getDefaultState().with( TYPE, Type.DOWN ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
            else if( facing == Direction.DOWN )
                return getDefaultState().with( TYPE, Type.UP ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
            else {
                double hitY = ctx.getHitVec().y % 1;
                if( hitY < 0.5 )
                    return getDefaultState().with( TYPE, Type.DOWN ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
                else return getDefaultState().with( TYPE, Type.UP ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
            }
        }
    }

    @Override
    public boolean propagatesSkylightDown( BlockState state, IBlockReader reader, BlockPos pos ) {
        return false;
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        return state.get( TYPE ).shape;
    }

    public boolean isFullCube( BlockState state ) {
        return state.get( TYPE ) == Type.DOUBLE;
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext ctx ) {
        ItemStack stack = ctx.getItem();
        Type type = state.get( TYPE );

        if( type != Type.DOUBLE && stack.getItem() == asItem() ) {
            if( ctx.replacingClickedOnBlock() ) {
                boolean up = ctx.getHitVec().y % 1 > 0.5;
                boolean south = ctx.getHitVec().z % 1 > 0.5;
                boolean east = ctx.getHitVec().x % 1 > 0.5;
                Direction face = ctx.getFace();
                if( type == Type.DOWN ) {
                    return face == Direction.UP || up && face.getAxis() != Direction.Axis.Y;
                } else if( type == Type.UP ) {
                    return face == Direction.DOWN || ! up && face.getAxis() != Direction.Axis.Y;
                } else if( type == Type.NORTH ) {
                    return face == Direction.SOUTH || south && face.getAxis() != Direction.Axis.Z;
                } else if( type == Type.SOUTH ) {
                    return face == Direction.NORTH || ! south && face.getAxis() != Direction.Axis.Z;
                } else if( type == Type.EAST ) {
                    return face == Direction.WEST || ! east && face.getAxis() != Direction.Axis.X;
                } else {
                    return face == Direction.EAST || east && face.getAxis() != Direction.Axis.X;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, BlockState state ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return Fluids.EMPTY;
        return super.pickupFluid( world, pos, state );
    }

    @Override
    public boolean canContainFluid( IBlockReader world, BlockPos pos, BlockState state, Fluid fluid ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return false;
        return super.canContainFluid( world, pos, state, fluid );
    }

    @Override
    public boolean receiveFluid( IWorld world, BlockPos pos, BlockState state, IFluidState fluid ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return false;
        return super.receiveFluid( world, pos, state, fluid );
    }

    @Override
    public BlockState mirror( BlockState state, Mirror mirror ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return state;
        return state.with( TYPE, Type.forFacing( mirror.mirror( state.get( TYPE ).facing ) ) );
    }

    @Override
    public BlockState rotate( BlockState state, IWorld world, BlockPos pos, Rotation rotation ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return state;
        return state.with( TYPE, Type.forFacing( rotation.rotate( state.get( TYPE ).facing ) ) );
    }

    // Don't ask me what this means, but vanilla slab block has this method overridden too...
    // It seems to affect lighting...
    @Override
    public boolean func_220074_n( BlockState state ) {
        return state.get( TYPE ).ordinal() < 2;
    }

    public enum Type implements IStringSerializable {
        DOWN( "down", MDVoxelShapes.create16( 0, 0, 0, 16, 8, 16 ), Direction.DOWN ),
        UP( "up", MDVoxelShapes.create16( 0, 8, 0, 16, 16, 16 ), Direction.UP ),
        NORTH( "north", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 8 ), Direction.NORTH ),
        SOUTH( "south", MDVoxelShapes.create16( 0, 0, 8, 16, 16, 16 ), Direction.SOUTH ),
        WEST( "west", MDVoxelShapes.create16( 0, 0, 0, 8, 16, 16 ), Direction.WEST ),
        EAST( "east", MDVoxelShapes.create16( 8, 0, 0, 16, 16, 16 ), Direction.EAST ),
        DOUBLE( "double", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 16 ), null );

        private final String name;
        private final VoxelShape shape;
        private final Direction facing;

        Type( String name, VoxelShape shape, Direction facing ) {
            this.name = name;
            this.shape = shape;
            this.facing = facing;
        }

        @Override
        public String getName() {
            return name;
        }

        public VoxelShape getShape() {
            return shape;
        }

        public Direction getFacing() {
            return facing;
        }

        public static Type forFacing( Direction facing ) {
            if( facing == null ) {
                return DOUBLE;
            }
            switch( facing ) {
                case UP: return UP;
                case DOWN: return DOWN;
                case EAST: return EAST;
                case WEST: return WEST;
                case NORTH: return NORTH;
                case SOUTH: return SOUTH;
                default: return DOUBLE;
            }
        }
    }
}
