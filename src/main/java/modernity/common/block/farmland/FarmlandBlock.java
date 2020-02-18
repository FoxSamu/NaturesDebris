/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import modernity.api.block.ICustomColoredParticlesBlock;
import modernity.api.util.MovingBlockPos;
import modernity.client.particle.ExtendedDiggingParticle;
import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.dirt.DirtlikeBlock;
import modernity.common.block.dirt.logic.FarmlandDirtLogic;
import modernity.common.item.MDItemTags;
import modernity.common.tileentity.FarmlandTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class FarmlandBlock extends DirtlikeBlock implements ITopTextureConnectionBlock, ICustomColoredParticlesBlock {
    public static final EnumProperty<Fertility> FERTILITY = MDBlockStateProperties.FERTILITY;
    public static final EnumProperty<Wetness> WETNESS = MDBlockStateProperties.WETNESS;

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

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        if( player.isSneaking() ) return false;
        IFarmlandLogic logic = IFarmlandLogic.get( world, pos );
        if( logic == null ) return false;
        ItemStack stack = player.getHeldItem( hand );
        if( stack.getItem().isIn( MDItemTags.LITTLE_SALTY ) ) {
            if( world.rand.nextInt( 3 ) != 0 ) {
                if( logic.addSaltiness( 1 ) ) return shrink( stack, player, hand );
            }
        }
        if( stack.getItem().isIn( MDItemTags.SALTY ) ) {
            if( logic.addSaltiness( world.rand.nextInt( 4 ) + 2 ) ) return shrink( stack, player, hand );
        }
        if( stack.getItem().isIn( MDItemTags.LITTLE_FERTILIZER ) ) {
            if( world.rand.nextInt( 3 ) != 0 ) {
                if( logic.addFertility( 1 ) ) return shrink( stack, player, hand );
            }
        }
        if( stack.getItem().isIn( MDItemTags.FERTILIZER ) ) {
            if( logic.addFertility( world.rand.nextInt( 4 ) + 2 ) ) return shrink( stack, player, hand );
        }
        return false;
    }

    private static boolean shrink( ItemStack stack, PlayerEntity player, Hand hand ) {
        if( ! player.abilities.isCreativeMode ) {
            stack.shrink( 1 );
            player.setHeldItem( hand, stack.getCount() == 0 ? ItemStack.EMPTY : stack );
        }
        return true;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean addHitEffects( BlockState state, World world, RayTraceResult target, ParticleManager manager ) {
        BlockRayTraceResult brtr = (BlockRayTraceResult) target;
        ExtendedDiggingParticle.addBlockHitEffects( manager, world, brtr.getPos(), brtr.getFace() );
        return true;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean addDestroyEffects( BlockState state, World world, BlockPos pos, ParticleManager manager ) {
        ExtendedDiggingParticle.addBlockDestroyEffects( manager, world, pos, state );
        return true;
    }

    @Override
    public int getColor( World world, @Nullable BlockPos pos, BlockState state ) {
        return 0xffffff;
    }
}
