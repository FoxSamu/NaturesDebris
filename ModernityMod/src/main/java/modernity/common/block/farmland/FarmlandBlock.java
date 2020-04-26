/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import modernity.client.particle.ExtendedDiggingParticle;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.MDBlockTags;
import modernity.common.block.dirt.DirtlikeBlock;
import modernity.common.block.dirt.logic.FarmlandDirtLogic;
import modernity.common.environment.precipitation.IPrecipitation;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.fluid.MDFluidTags;
import modernity.common.item.MDItemTags;
import modernity.generic.block.ICustomColoredParticlesBlock;
import modernity.generic.dimension.IPrecipitationDimension;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ILightReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class FarmlandBlock extends DirtlikeBlock implements ITopTextureConnectionBlock, ICustomColoredParticlesBlock, IFarmlandBlock {
    public static final EnumProperty<Fertility> FERTILITY = MDBlockStateProperties.FERTILITY;
    public static final IntegerProperty LEVEL = MDBlockStateProperties.LEVEL_0_5;
    public static final IntegerProperty DECAY = MDBlockStateProperties.DECAY_0_8;

    private final FarmlandDirtLogic farmlandLogic;

    public FarmlandBlock( FarmlandDirtLogic logic, Properties properties ) {
        super( logic, properties );
        farmlandLogic = logic;

        setDefaultState( stateContainer.getBaseState()
                                       .with( FERTILITY, Fertility.NONE )
                                       .with( LEVEL, 0 )
                                       .with( DECAY, 0 ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( FERTILITY, LEVEL, DECAY );
    }

    @Override
    public boolean canConnectTo( ILightReader world, MovingBlockPos pos, BlockState state ) {
        BlockState up = world.getBlockState( pos.moveUp() );
        return state.getBlock() instanceof FarmlandBlock && ! up.isSolidSide( world, pos, Direction.DOWN );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockState updatePostPlacement( BlockState state, Direction dir, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos ) {
        if( dir == Direction.UP ) {
            if( adjState.isSolidSide( world, pos, Direction.DOWN ) || adjState.getMaterial().isLiquid() ) {
                return farmlandLogic.makeNormal( world, pos, state );
            }
        }
        return state;
    }

    protected boolean canDecay() {
        return true;
    }

    @Override
    public void randomTick( BlockState state, ServerWorld world, BlockPos pos, Random rand ) {
        Fertility fertility = state.get( FERTILITY );
        Fertility lastFertility = fertility;

        int level = state.get( LEVEL );
        int lastLevel = level;

        int decay = state.get( DECAY );
        int lastDecay = decay;

        if( canDecay() ) {
            if( fertility == Fertility.FERTILE && decay < 8 ) {
                decay++;
            }

            if( decay == 8 ) {
                fertility = Fertility.DECAYED;
                level = rand.nextInt( 3 ) + 3;
            }

            if( fertility != Fertility.FERTILE || level == 0 ) {
                decay = 0;
            }
        } else {
            decay = 0;
            if( fertility == Fertility.DECAYED ) {
                fertility = Fertility.FERTILE;
            }
        }

        boolean rain = checkRain( world, pos );
        int resources = checkResources( world, pos, rain );

        boolean cleaning = resources < 0;
        if( cleaning ) resources = - resources;

        if( resources == 1 ) {
            if( fertility == Fertility.NONE ) {
                fertility = Fertility.SALTY;
                level = rand.nextInt( 3 ) + 3;
            }
        }

        if( resources == 2 ) {
            if( fertility == Fertility.NONE || fertility == Fertility.SALTY ) {
                fertility = Fertility.WET;
            }
        }

        if( resources == 3 ) {
            if( fertility == Fertility.NONE || fertility == Fertility.SALTY || fertility == Fertility.WET ) {
                fertility = Fertility.FERTILE;
                level = rand.nextInt( 3 ) + 3;
            }
        }

        if( resources == 4 && ! cleaning ) {
            if( fertility != Fertility.DECAYED ) {
                fertility = Fertility.DECAYED;
                level = rand.nextInt( 3 ) + 3;
            }
        }

        if( cleaning ) {
            if( fertility == Fertility.DECAYED ) {
                fertility = Fertility.WET;
                level = 0;
            }
            decay = 0;
        }


        if( fertility == Fertility.WET && resources != 2 ) {
            fertility = Fertility.NONE;
        }

        if( fertility == Fertility.NONE || fertility == Fertility.WET ) {
            level = 0;
        }

        if( level == 0 && fertility != Fertility.NONE && fertility != Fertility.WET ) {
            fertility = Fertility.NONE;
        }

        if( fertility != lastFertility || level != lastLevel || decay != lastDecay ) {
            world.setBlockState( pos, state.with( FERTILITY, fertility )
                                           .with( LEVEL, level )
                                           .with( DECAY, decay ) );
        }
    }

    private int checkResources( World world, BlockPos pos, boolean rain ) {
        MovingBlockPos mpos = new MovingBlockPos();

        int type = rain ? 2 : 0;
        boolean cleaning = false;

        for( int x = - 4; x <= 4; x++ ) {
            for( int z = - 4; z <= 4; z++ ) {
                for( int y = 0; y <= 1; y++ ) {
                    mpos.setPos( pos ).addPos( x, y, z );

                    BlockState bstate = world.getBlockState( mpos );
                    IFluidState fstate = world.getFluidState( mpos );

                    if( bstate.isIn( MDBlockTags.POISONOUS ) && type < 4 ) {
                        type = 4;
                    }

                    if( bstate.isIn( MDBlockTags.FERTILE ) && type < 3 ) {
                        type = 3;
                    }

                    if( fstate.isTagged( FluidTags.WATER ) ) {
                        if( fstate.isTagged( MDFluidTags.CLEAN_WATER ) ) {
                            cleaning = true;
                        }
                        if( type < 2 ) {
                            type = 2;
                        }
                    }

                    if( bstate.isIn( MDBlockTags.SALT_SOURCE ) && type < 1 ) {
                        type = 1;
                    }
                }
            }
        }

        return cleaning ? - type : type;
    }

    private boolean checkRain( World world, BlockPos pos ) {
        if( world.dimension instanceof IPrecipitationDimension ) {
            Biome biome = world.getBiome( pos );
            if( biome instanceof ModernityBiome ) {
                IPrecipitationFunction function = ( (ModernityBiome) biome ).getPrecipitationFunction();
                int level = ( (IPrecipitationDimension) world.dimension ).getRainLevel();
                if( ( (IPrecipitationDimension) world.dimension ).getRainAmount() < 0.2 ) level = 0;
                IPrecipitation prec = function.computePrecipitation( level );
                return prec.type() == Biome.RainType.RAIN;
            }
        }
        return world.isRainingAt( pos.up() );
    }

    @Override
    public ActionResultType onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        if( player.isShiftKeyDown() ) return ActionResultType.PASS;

        ItemStack stack = player.getHeldItem( hand );
        if( stack.getItem().isIn( MDItemTags.LITTLE_SALTY ) ) {
            if( state.get( FERTILITY ) == Fertility.NONE ) {
                world.setBlockState( pos, state.with( FERTILITY, Fertility.SALTY ).with( LEVEL, world.rand.nextInt( 2 ) + 1 ) );
                return shrink( stack, player, hand );
            }
        }
        if( stack.getItem().isIn( MDItemTags.SALTY ) ) {
            if( state.get( FERTILITY ) == Fertility.NONE ) {
                world.setBlockState( pos, state.with( FERTILITY, Fertility.SALTY ).with( LEVEL, world.rand.nextInt( 3 ) + 3 ) );
                return shrink( stack, player, hand );
            }
        }

        if( stack.getItem().isIn( MDItemTags.LITTLE_FERTILIZER ) ) {
            if( state.get( FERTILITY ).ordinal() < Fertility.FERTILE.ordinal() ) {
                world.setBlockState( pos, state.with( FERTILITY, Fertility.FERTILE ).with( LEVEL, world.rand.nextInt( 2 ) + 1 ) );
                return shrink( stack, player, hand );
            }
        }
        if( stack.getItem().isIn( MDItemTags.FERTILIZER ) ) {
            if( state.get( FERTILITY ).ordinal() < Fertility.FERTILE.ordinal() ) {
                world.setBlockState( pos, state.with( FERTILITY, Fertility.FERTILE ).with( LEVEL, world.rand.nextInt( 3 ) + 3 ) );
                return shrink( stack, player, hand );
            }
        }
        return ActionResultType.PASS;
    }

    private static ActionResultType shrink( ItemStack stack, PlayerEntity player, Hand hand ) {
        if( ! player.abilities.isCreativeMode ) {
            stack.shrink( 1 );
            player.setHeldItem( hand, stack.getCount() == 0 ? ItemStack.EMPTY : stack );
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public FarmlandDirtLogic getLogic() {
        return farmlandLogic;
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

    @Override
    public IFarmland getFarmland( IWorld world, BlockPos pos ) {
        return new FarmlandImpl( world, pos );
    }

    private static class FarmlandImpl implements IFarmland {
        private final IWorld world;
        private final BlockPos pos;
        private BlockState state;

        private FarmlandImpl( IWorld world, BlockPos pos ) {
            this.world = world;
            this.pos = pos;

            state = world.getBlockState( pos );
        }


        @Override
        public Fertility getFertility() {
            return state.get( FERTILITY );
        }

        @Override
        public void setFertility( Fertility fertility ) {
            BlockState n = optimize( state.with( FERTILITY, fertility ) );
            if( n != state ) {
                state = n;
                world.setBlockState( pos, n, 3 );
            }
        }

        @Override
        public int getLevel() {
            return state.get( LEVEL );
        }

        @Override
        public void setLevel( int level ) {
            BlockState n = optimize( state.with( LEVEL, level ) );
            if( n != state ) {
                state = n;
                world.setBlockState( pos, n, 3 );
            }
        }

        private BlockState optimize( BlockState state ) {
            int decay = state.get( DECAY );
            int level = state.get( LEVEL );
            Fertility fertility = state.get( FERTILITY );

            if( fertility != Fertility.FERTILE || level == 0 ) decay = 0;
            if( fertility == Fertility.NONE || fertility == Fertility.WET ) level = 0;
            if( level == 0 && fertility != Fertility.NONE && fertility != Fertility.WET ) fertility = Fertility.NONE;

            return state.with( DECAY, decay ).with( LEVEL, level ).with( FERTILITY, fertility );
        }
    }
}
