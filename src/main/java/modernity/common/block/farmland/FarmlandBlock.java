/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.base.ITopTextureConnectionBlock;
import modernity.common.block.dirt.DirtlikeBlock;
import modernity.common.block.dirt.logic.FarmlandDirtLogic;
import modernity.common.tileentity.FarmlandTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class FarmlandBlock extends DirtlikeBlock implements ITopTextureConnectionBlock {
    public static final EnumProperty<Fertility> FERTILITY = EnumProperty.create( "fertility", Fertility.class );
    public static final EnumProperty<Wetness> WETNESS = EnumProperty.create( "wetness", Wetness.class );

    private final FarmlandDirtLogic farmlandLogic;

    public FarmlandBlock( FarmlandDirtLogic logic, Properties properties ) {
        super( logic, properties );
        farmlandLogic = logic;

        setDefaultState( stateContainer.getBaseState()
                                       .with( FERTILITY, Fertility.NOT_FERTILE )
                                       .with( WETNESS, Wetness.DRY ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( FERTILITY, WETNESS );
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new FarmlandTileEntity();
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void onReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving ) {
        if( ! ( newState.getBlock() instanceof FarmlandBlock ) ) {
            world.removeTileEntity( pos );
        }
    }

    @Override
    public boolean canConnectTo( IEnviromentBlockReader world, MovingBlockPos pos, BlockState state ) {
        BlockState up = world.getBlockState( pos.moveUp() );
        return state.getBlock() instanceof FarmlandBlock && ! up.func_224755_d( world, pos, Direction.DOWN );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        if( dir == Direction.UP ) {
            if( adjState.func_224755_d( world, pos, Direction.DOWN ) ) {
                return farmlandLogic.makeNormal( world, pos, state );
            }
            if( adjState.getFluidState().isTagged( FluidTags.WATER ) ) {
                IFarmlandLogic logic = IFarmlandLogic.get( world, pos );
                if( logic != null ) {
                    logic.flood();
                }
            }
        }
        return state;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void onBlockAdded( BlockState state, World world, BlockPos pos, BlockState old, boolean moving ) {
        if( world.getFluidState( pos.up() ).isTagged( FluidTags.WATER ) ) {
            IFarmlandLogic logic = IFarmlandLogic.get( world, pos );
            if( logic != null ) {
                logic.flood();
            }
        }
    }

    @Override
    public void randomTick( BlockState state, World world, BlockPos pos, Random rand ) {
        IFarmlandLogic logic = IFarmlandLogic.get( world, pos );
        if( logic != null ) {
            logic.randomUpdate( rand );
        }
    }
}
