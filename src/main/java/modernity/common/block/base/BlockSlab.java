/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 24 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.util.EWaterlogType;
import modernity.api.util.MDVoxelShapes;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSlab extends BlockWaterlogged {
    public static final EnumProperty<Type> TYPE = EnumProperty.create( "type", Type.class );

    public BlockSlab( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );

        setDefaultState( stateContainer.getBaseState().with( TYPE, Type.DOWN ) );
    }

    public BlockSlab( String id, Properties properties ) {
        super( id, properties );

        setDefaultState( stateContainer.getBaseState().with( TYPE, Type.DOWN ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( TYPE );
    }

    // Vanilla has special behavior for vanilla's BlockSlab, we're not that class so special case here...
    @Deprecated
    @OnlyIn( Dist.CLIENT )
    @Override
    public int getPackedLightmapCoords( IBlockState state, IWorldReader source, BlockPos pos ) {
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
    public int getOpacity( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return reader.getMaxLightLevel();
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext ctx ) {
        IFluidState fluid = ctx.getWorld().getFluidState( ctx.getPos() );
        IBlockState state = ctx.getWorld().getBlockState( ctx.getPos() );
        if( state.getBlock() == this ) {
            if( state.get( TYPE ) == Type.DOUBLE ) return null;
            else return state.with( TYPE, Type.DOUBLE ).with( WATERLOGGED, EWaterlogType.NONE );
        }
        boolean sidedPlacing = ctx.getPlayer() != null && ctx.getPlayer().isSneaking();
        if( sidedPlacing ) {
            EnumFacing facing = ctx.getFace().getOpposite();
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
            EnumFacing facing = ctx.getFace();
            if( facing == EnumFacing.UP )
                return getDefaultState().with( TYPE, Type.DOWN ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
            else if( facing == EnumFacing.DOWN )
                return getDefaultState().with( TYPE, Type.UP ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
            else {
                float hitY = ctx.getHitY();
                if( hitY < 0.5 )
                    return getDefaultState().with( TYPE, Type.DOWN ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
                else return getDefaultState().with( TYPE, Type.UP ).with( WATERLOGGED, EWaterlogType.getType( fluid ) );
            }
        }
    }

    @Override
    public boolean propagatesSkylightDown( IBlockState state, IBlockReader reader, BlockPos pos ) {
        return false;
    }

    @Override
    public VoxelShape getShape( IBlockState state, IBlockReader world, BlockPos pos ) {
        return state.get( TYPE ).shape;
    }

    @Override
    public int quantityDropped( IBlockState state, Random rand ) {
        return state.get( TYPE ) == Type.DOUBLE ? 2 : 1;
    }

    @Override
    protected ItemStack getSilkTouchDrop( IBlockState state ) {
        return new ItemStack( this, state.get( TYPE ) == Type.DOUBLE ? 2 : 1 );
    }

    public boolean isFullCube( IBlockState state ) {
        return state.get( TYPE ) == Type.DOUBLE;
    }

    public boolean isReplaceable( IBlockState state, BlockItemUseContext ctx ) {
        ItemStack stack = ctx.getItem();
        Type type = state.get( TYPE );

        if( type != Type.DOUBLE && stack.getItem() == asItem() ) {
            if( ctx.replacingClickedOnBlock() ) {
                boolean up = ctx.getHitY() > 0.5;
                boolean south = ctx.getHitZ() > 0.5;
                boolean east = ctx.getHitX() > 0.5;
                EnumFacing face = ctx.getFace();
                if( type == Type.DOWN ) {
                    return face == EnumFacing.UP || up && face.getAxis() != EnumFacing.Axis.Y;
                } else if( type == Type.UP ) {
                    return face == EnumFacing.DOWN || ! up && face.getAxis() != EnumFacing.Axis.Y;
                } else if( type == Type.NORTH ) {
                    return face == EnumFacing.SOUTH || south && face.getAxis() != EnumFacing.Axis.Z;
                } else if( type == Type.SOUTH ) {
                    return face == EnumFacing.NORTH || ! south && face.getAxis() != EnumFacing.Axis.Z;
                } else if( type == Type.EAST ) {
                    return face == EnumFacing.WEST || ! east && face.getAxis() != EnumFacing.Axis.X;
                } else {
                    return face == EnumFacing.EAST || east && face.getAxis() != EnumFacing.Axis.X;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockReader world, IBlockState state, BlockPos pos, EnumFacing facing ) {
        if( state.get( TYPE ) == Type.DOUBLE ) {
            return BlockFaceShape.SOLID;
        } else {
            EnumFacing slabFacing = state.get( TYPE ).getFacing();
            if( facing == slabFacing ) {
                return BlockFaceShape.SOLID;
            } else {
                return BlockFaceShape.UNDEFINED;
            }
        }
    }

    @Override
    public Fluid pickupFluid( IWorld world, BlockPos pos, IBlockState state ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return Fluids.EMPTY;
        return super.pickupFluid( world, pos, state );
    }

    @Override
    public boolean canContainFluid( IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return false;
        return super.canContainFluid( world, pos, state, fluid );
    }

    @Override
    public boolean receiveFluid( IWorld world, BlockPos pos, IBlockState state, IFluidState fluid ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return false;
        return super.receiveFluid( world, pos, state, fluid );
    }

    @Override
    public IBlockState mirror( IBlockState state, Mirror mirror ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return state;
        return state.with( TYPE, Type.forFacing( mirror.mirror( state.get( TYPE ).facing ) ) );
    }

    @Override
    public IBlockState rotate( IBlockState state, IWorld world, BlockPos pos, Rotation rotation ) {
        if( state.get( TYPE ) == Type.DOUBLE ) return state;
        return state.with( TYPE, Type.forFacing( rotation.rotate( state.get( TYPE ).facing ) ) );
    }

    public enum Type implements IStringSerializable {
        DOWN( "down", MDVoxelShapes.create16( 0, 0, 0, 16, 8, 16 ), EnumFacing.DOWN ),
        UP( "up", MDVoxelShapes.create16( 0, 8, 0, 16, 16, 16 ), EnumFacing.UP ),
        NORTH( "north", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 8 ), EnumFacing.NORTH ),
        SOUTH( "south", MDVoxelShapes.create16( 0, 0, 8, 16, 16, 16 ), EnumFacing.SOUTH ),
        WEST( "west", MDVoxelShapes.create16( 0, 0, 0, 8, 16, 16 ), EnumFacing.WEST ),
        EAST( "east", MDVoxelShapes.create16( 8, 0, 0, 16, 16, 16 ), EnumFacing.EAST ),
        DOUBLE( "double", MDVoxelShapes.create16( 0, 0, 0, 16, 16, 16 ), null );

        private final String name;
        private final VoxelShape shape;
        private final EnumFacing facing;

        Type( String name, VoxelShape shape, EnumFacing facing ) {
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

        public EnumFacing getFacing() {
            return facing;
        }

        public static Type forFacing( EnumFacing facing ) {
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
