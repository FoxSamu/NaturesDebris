/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 23 - 2019
 * Author: rgsw
 */

package modernity.common.handler;

import modernity.common.biome.ModernityBiome;
import modernity.common.environment.event.EnvironmentEventManager;
import modernity.common.environment.event.MDEnvEvents;
import modernity.common.environment.event.impl.PrecipitationEnvEvent;
import modernity.common.environment.precipitation.IPrecipitation;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;

public enum WorldTickHandler {
    INSTANCE;

    private int updateLCG = new Random().nextInt();

    @SubscribeEvent
    public void onWorldTick( TickEvent.WorldTickEvent event ) {
        if( event.world.isRemote || event.phase == TickEvent.Phase.START ) return;
    }

    private void updateChunk( ServerWorld world, Chunk chunk, EnvironmentEventManager eventManager ) {
        Dimension dimension = world.dimension;
        ChunkPos cpos = chunk.getPos();

        PrecipitationEnvEvent event = eventManager.getByType( MDEnvEvents.PRECIPITATION );
        int level = event.getLevel();
        boolean thunder = event.isThunderstorm() && event.getEffect() > 0.9;
        boolean active = event.isActive();

        boolean rain = level > 0 && active;

        int sx = cpos.getXStart();
        int sz = cpos.getZStart();

        IProfiler profiler = world.getProfiler();

        profiler.endStartSection( "mdiceandsnow" );
        if( dimension.canDoRainSnowIce( chunk ) && world.rand.nextInt( 16 ) == 0 ) {
            BlockPos pos = randomPos( sx, 0, sz, 15 );
            BlockPos blockpos2 = world.getHeight( Heightmap.Type.MOTION_BLOCKING, pos );
            BlockPos blockpos3 = blockpos2.down();
            Biome biome = world.getBiome( blockpos2 );
            if( world.isAreaLoaded( blockpos2, 1 ) ) // Forge: check area to avoid loading neighbors in unloaded chunks
                if( biome.doesWaterFreeze( world, blockpos3 ) ) {
                    world.setBlockState( blockpos3, Blocks.ICE.getDefaultState() );
                }

            if( rain && biome.doesSnowGenerate( world, blockpos2 ) ) {
                world.setBlockState( blockpos2, Blocks.SNOW.getDefaultState() );
            }

            if( rain && world.getBiome( blockpos3 ).getPrecipitation() == Biome.RainType.RAIN ) {
                world.getBlockState( blockpos3 ).getBlock().fillWithRain( world, blockpos3 );
            }
        }

        profiler.endSection();
    }

    public void doLightning( ServerWorld world, Chunk chunk, EnvironmentEventManager eventManager ) {
        ChunkPos cpos = chunk.getPos();

        PrecipitationEnvEvent event = eventManager.getByType( MDEnvEvents.PRECIPITATION );
        int level = event.getLevel();
        boolean thunder = event.isThunderstorm() && event.getEffect() > 0.9;
        boolean active = event.isActive();

        int sx = cpos.getXStart();
        int sz = cpos.getZStart();

        boolean rain = level > 0 && active;
        if( rain && thunder && world.rand.nextInt( 100000 ) == 0 ) {
            BlockPos lightingPos = adjustPosToNearbyEntity( world, randomPos( sx, 0, sz, 15 ) );
            DifficultyInstance difficulty = world.getDifficultyForLocation( lightingPos );
            boolean skeletonHorse = world.getGameRules().getBoolean( GameRules.DO_MOB_SPAWNING ) && world.rand.nextDouble() < difficulty.getAdditionalDifficulty() * 0.01;
            if( skeletonHorse ) {
                SkeletonHorseEntity entity = EntityType.SKELETON_HORSE.create( world );
                assert entity != null;
                entity.setTrap( true );
                entity.setGrowingAge( 0 );
                entity.setPosition( lightingPos.getX(), lightingPos.getY(), lightingPos.getZ() );
                world.addEntity( entity );
            }

            world.addLightningBolt( new LightningBoltEntity( world, lightingPos.getX() + 0.5, lightingPos.getY(), lightingPos.getZ() + 0.5, skeletonHorse ) );
        }
    }

    public void doRainSnowIce( ServerWorld world, Chunk chunk, EnvironmentEventManager eventManager ) {
        ChunkPos cpos = chunk.getPos();

        PrecipitationEnvEvent event = eventManager.getByType( MDEnvEvents.PRECIPITATION );
        int level = event.getLevel();
        boolean active = event.isActive();
        boolean rain = level > 0 && active;

        int sx = cpos.getXStart();
        int sz = cpos.getZStart();

        int n = world.getGameRules().get( GameRules.RANDOM_TICK_SPEED ).get();

        for( int i = 0; i < n; i++ ) {
            if( rain && world.rand.nextInt( 16 ) == 0 ) {
                BlockPos pos = randomPos( sx, 0, sz, 15 );
                Biome biome = world.getBiome( pos );

                ModernityBiome mbiome = (ModernityBiome) biome;
                IPrecipitationFunction precFn = mbiome.getPrecipitationFunction();
                IPrecipitation prec = precFn.computePrecipitation( level );

                prec.blockUpdate( world, pos );

//                if( world.isAreaLoaded( heightPos, 1 ) ) {
//                    if( biome.doesWaterFreeze( world, heightBlockPos ) ) {
//                        world.setBlockState( heightBlockPos, Blocks.ICE.getDefaultState() );
//                    }
//                }

//                if( rain && biome.doesSnowGenerate( world, heightPos ) ) {
//                    world.setBlockState( heightPos, Blocks.SNOW.getDefaultState() );
//                }
//
//                if( rain && world.getBiome( heightBlockPos ).getPrecipitation() == Biome.RainType.RAIN ) {
//                    world.getBlockState( heightBlockPos ).getBlock().fillWithRain( world, heightBlockPos );
//                }
            }
        }
    }

    private BlockPos adjustPosToNearbyEntity( World world, BlockPos pos ) {
        BlockPos hpos = world.getHeight( Heightmap.Type.MOTION_BLOCKING, pos );
        AxisAlignedBB box = new AxisAlignedBB( hpos, new BlockPos( hpos.getX(), world.getHeight(), hpos.getZ() ) ).grow( 3 );
        List<LivingEntity> list = world.getEntitiesWithinAABB(
            LivingEntity.class,
            box,
            entity -> entity != null && entity.isAlive() && world.isSkyLightMax( entity.getPosition() )
        );

        if( ! list.isEmpty() ) {
            return list.get( world.rand.nextInt( list.size() ) ).getPosition();
        } else {
            if( hpos.getY() == - 1 ) {
                hpos = hpos.up( 2 );
            }

            return hpos;
        }
    }

    public BlockPos randomPos( int minX, int minY, int minZ, int yMask ) {
        updateLCG = updateLCG * 3 + 1013904223;
        int random = updateLCG >> 2;
        return new BlockPos( minX + ( random & 15 ), minY + ( random >> 16 & yMask ), minZ + ( random >> 8 & 15 ) );
    }
}
