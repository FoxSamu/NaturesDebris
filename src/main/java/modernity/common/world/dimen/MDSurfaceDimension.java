/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.world.dimen;

import modernity.api.dimension.*;
import modernity.client.environment.Fog;
import modernity.client.environment.Precipitation;
import modernity.client.environment.Sky;
import modernity.common.biome.ModernityBiome;
import modernity.common.environment.event.EnvironmentEventManager;
import modernity.common.environment.event.MDEnvEvents;
import modernity.common.environment.event.impl.CloudlessEnvEvent;
import modernity.common.environment.event.impl.CloudsEnvEvent;
import modernity.common.environment.event.impl.FogEnvEvent;
import modernity.common.environment.event.impl.PrecipitationEnvEvent;
import modernity.common.environment.precipitation.IPrecipitation;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.environment.satellite.SatelliteData;
import modernity.common.handler.WorldTickHandler;
import modernity.common.world.gen.MDSurfaceChunkGenerator;
import modernity.common.world.gen.MDSurfaceGenSettings;
import modernity.common.world.gen.biome.MDSurfaceBiomeProvider;
import modernity.common.world.gen.biome.MDSurfaceBiomeProviderSettings;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rgsw.MathUtil;

import javax.annotation.Nullable;

/**
 * The surface dimension of the Modernity.
 */
public class MDSurfaceDimension extends Dimension implements IEnvironmentDimension, ISatelliteDimension, IEnvEventsDimension, IClientTickingDimension, IInitializeDimension, IPrecipitationDimension {

    private SatelliteData satelliteData;
    private EnvironmentEventManager envEventManager;

    public MDSurfaceDimension( World world, DimensionType type ) {
        super( world, type );
    }

    @Override
    public void init() {
        if( world instanceof ServerWorld ) {
            ServerWorld sw = (ServerWorld) world;
            satelliteData = sw.getSavedData().getOrCreate( () -> new SatelliteData( 5, world ), SatelliteData.NAME );
            envEventManager = sw.getSavedData().getOrCreate( () -> createEnvEventManager( 5 ), EnvironmentEventManager.NAME );
        } else {
            satelliteData = new SatelliteData( - 1, world );
            envEventManager = createEnvEventManager( - 1 );
        }
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        MDSurfaceGenSettings mgs = new MDSurfaceGenSettings();
        MDSurfaceBiomeProviderSettings bps = new MDSurfaceBiomeProviderSettings( world.getWorldInfo(), mgs );
        MDSurfaceBiomeProvider bp = new MDSurfaceBiomeProvider( bps );
        return new MDSurfaceChunkGenerator( world, bp, mgs );
    }

    @Nullable
    @Override
    public BlockPos findSpawn( ChunkPos pos, boolean checkValid ) {
        return findSpawn( pos.x, pos.z, checkValid );
    }

    @Nullable
    @Override
    public BlockPos findSpawn( int x, int z, boolean checkValid ) {
        return null;
    }

    @Override
    public float calculateCelestialAngle( long worldTime, float partialTicks ) {
        return 0.5F; // 0.5 shows the moon right above us
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public Vec3d getFogColor( float x, float z ) {
        return new Vec3d( 0, 0, 0 );
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean doesXZShowFog( int x, int z ) {
        return false;
    }

    @Override
    public DimensionType getType() {
        return MDDimensions.MODERNITY.getType();
    }

    @Override
    public boolean hasSkyLight() {
        return true;
    }

    @Override
    public float getSunBrightness( float partialTicks ) {
        return 0.15F;
    }

    @Override
    public Vec3d getSkyColor( BlockPos cameraPos, float partialTicks ) {
        return new Vec3d( 12 / 255D, 13 / 255D, 23 / 255D );
    }

    @Override
    public float getStarBrightness( float partialTicks ) {
        return 1;
    }

    @Override
    public boolean isNether() {
        return false;
    }

    @Override
    public boolean canDoRainSnowIce( Chunk chunk ) {
        if( world instanceof ServerWorld ) {
            WorldTickHandler.INSTANCE.doRainSnowIce( (ServerWorld) world, chunk, getEnvEventManager() );
        }
        return false;
    }

    @Override
    public boolean canDoLightning( Chunk chunk ) {
        if( world instanceof ServerWorld ) {
            WorldTickHandler.INSTANCE.doLightning( (ServerWorld) world, chunk, getEnvEventManager() );
        }
        return false;
    }

    @Override
    public int getMoonPhase( long worldTime ) {
        return 0;
    }

    @Override
    public void tick() {
        getSatelliteData().tick();
        getEnvEventManager().tick();

        world.setThunderStrength( 0 );
    }

    @Override
    public SatelliteData getSatelliteData() {
        return satelliteData;
    }

    @Override
    public EnvironmentEventManager getEnvEventManager() {
        return envEventManager;
    }

    private EnvironmentEventManager createEnvEventManager( int updateInterval ) {
        return new EnvironmentEventManager(
            updateInterval, world,
            MDEnvEvents.FOG,
            MDEnvEvents.CLOUDS,
            MDEnvEvents.CLOUDLESS,
            MDEnvEvents.PRECIPITATION
        );
    }

    @Override
    public DimensionType getRespawnDimension( ServerPlayerEntity player ) {
        return getType();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void updateFog( Fog fog ) {
        fog.setColor( 0, 0, 0 );
        fog.type = Fog.Type.EXP2;
        fog.density = 0.01F;

        EnvironmentEventManager envManager = getEnvEventManager();

        PrecipitationEnvEvent precEv = envEventManager.getByType( MDEnvEvents.PRECIPITATION );
        float precFac = precEv.getEffect();
        if( precFac > 0 ) {
            float fogDens = precEv.getLevel() * 0.006F + 0.01F;
            float density = MathUtil.lerp( fog.density, fogDens, precFac );
            if( density > fog.density ) fog.density = density;
            fog.color[ 0 ] = MathUtil.lerp( fog.color[ 0 ], 0.15F, precFac );
            fog.color[ 1 ] = MathUtil.lerp( fog.color[ 1 ], 0.15F, precFac );
            fog.color[ 2 ] = MathUtil.lerp( fog.color[ 2 ], 0.15F, precFac );
        }


        FogEnvEvent fogEv = envManager.getByType( MDEnvEvents.FOG );
        float fogFac = fogEv.getEffect();
        if( fogFac > 0 ) {
            float fogDens = fogEv.getDensity();
            float density = MathUtil.lerp( fog.density, fogDens, fogFac );
            if( density > fog.density ) fog.density = density;
            fog.color[ 0 ] = MathUtil.lerp( fog.color[ 0 ], 0.3F, fogFac );
            fog.color[ 1 ] = MathUtil.lerp( fog.color[ 1 ], 0.3F, fogFac );
            fog.color[ 2 ] = MathUtil.lerp( fog.color[ 2 ], 0.3F, fogFac );
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void updateSky( Sky sky ) {
        sky.setBacklightColor( 12 / 255F, 13 / 255F, 23 / 255F );
        sky.setTwilightColor( 15 / 255F, 15 / 255F, 15 / 255F );
        sky.setSkylightColor( 0, 0, 0 );
        sky.setStarColor( 1, 1, 1 );
        sky.setCloudColor( 1, 1, 1 );
        sky.setMoonColor( 1, 1, 1 );
        sky.cloudAmount = 0.05F;
        sky.meteoriteAmount = 0.001F;
        sky.starBrightness = 1;
        sky.cloudBrightness = 1;
        sky.skylightBrightness = 1;
        sky.backlightBrightness = 1;
        sky.twilightBrightness = 1;
        sky.twilightHeight = 10;
        sky.twilightHeightRandom = 6;
        sky.moonBrightness = 1;

        SatelliteData data = getSatelliteData();
        sky.moonPhase = data.getPhase();
        sky.moonRotation = data.getTick() / 24000F;

        EnvironmentEventManager envManager = getEnvEventManager();


        CloudlessEnvEvent cloudlessEv = envManager.getByType( MDEnvEvents.CLOUDLESS );
        float cloudlessFac = cloudlessEv.getEffect();
        if( cloudlessFac > 0 ) {
            float cloudAmount = cloudlessEv.getCloudAmount();
            sky.cloudAmount = MathUtil.lerp( sky.cloudAmount, cloudAmount, cloudlessFac );
            sky.backlightColor[ 0 ] = MathUtil.lerp( sky.backlightColor[ 0 ], 9 / 255F, cloudlessFac );
            sky.backlightColor[ 1 ] = MathUtil.lerp( sky.backlightColor[ 1 ], 10 / 255F, cloudlessFac );
            sky.backlightColor[ 2 ] = MathUtil.lerp( sky.backlightColor[ 2 ], 15 / 255F, cloudlessFac );
        }

        PrecipitationEnvEvent precEv = envEventManager.getByType( MDEnvEvents.PRECIPITATION );
        float precFac = precEv.getEffect();
        if( precFac > 0 ) {
            sky.twilightBrightness = MathUtil.lerp( sky.twilightBrightness, 0.1F, precFac );
            sky.starBrightness = MathUtil.lerp( sky.starBrightness, 0.0F, precFac );
            sky.moonBrightness = MathUtil.lerp( sky.moonBrightness, 0.1F, precFac );
            sky.backlightBrightness = MathUtil.lerp( sky.backlightBrightness, 0.5F, precFac );
            sky.skylightBrightness = MathUtil.lerp( sky.skylightBrightness, 0.3F, precFac );
            sky.backlightColor[ 0 ] = MathUtil.lerp( sky.backlightColor[ 0 ], 0.15F, precFac );
            sky.backlightColor[ 1 ] = MathUtil.lerp( sky.backlightColor[ 1 ], 0.15F, precFac );
            sky.backlightColor[ 2 ] = MathUtil.lerp( sky.backlightColor[ 2 ], 0.15F, precFac );
        }

        FogEnvEvent fogEv = envManager.getByType( MDEnvEvents.FOG );
        float fogFac = fogEv.getEffect();
        if( fogFac > 0 ) {
            sky.twilightBrightness = MathUtil.lerp( sky.twilightBrightness, 0.1F, fogFac );
            sky.starBrightness = MathUtil.lerp( sky.starBrightness, 0.0F, fogFac );
            sky.moonBrightness = MathUtil.lerp( sky.moonBrightness, 0.1F, fogFac );
            sky.backlightBrightness = MathUtil.lerp( sky.backlightBrightness, 0.5F, fogFac );
            sky.skylightBrightness = MathUtil.lerp( sky.skylightBrightness, 0.3F, fogFac );
            sky.backlightColor[ 0 ] = MathUtil.lerp( sky.backlightColor[ 0 ], 0.15F, fogFac );
            sky.backlightColor[ 1 ] = MathUtil.lerp( sky.backlightColor[ 1 ], 0.15F, fogFac );
            sky.backlightColor[ 2 ] = MathUtil.lerp( sky.backlightColor[ 2 ], 0.15F, fogFac );
        }

        CloudsEnvEvent cloudsEv = envManager.getByType( MDEnvEvents.CLOUDS );
        float cloudsFac = cloudsEv.getEffect();
        if( cloudsFac > 0 ) {
            float cloudAmount = cloudsEv.getCloudAmount();
            float backlightLerp = cloudAmount * 1.5F + 0.3F;
            float starBrightness = Math.max( 0.15F - cloudAmount, 0 );
            float moonBrightness = 0.4F - cloudAmount;
            sky.cloudAmount = MathUtil.lerp( sky.cloudAmount, cloudAmount, cloudsFac );
            sky.backlightColor[ 0 ] = MathUtil.lerp( sky.backlightColor[ 0 ], 0.15F, cloudsFac * backlightLerp );
            sky.backlightColor[ 1 ] = MathUtil.lerp( sky.backlightColor[ 1 ], 0.15F, cloudsFac * backlightLerp );
            sky.backlightColor[ 2 ] = MathUtil.lerp( sky.backlightColor[ 2 ], 0.15F, cloudsFac * backlightLerp );
            sky.moonBrightness = MathUtil.lerp( sky.moonBrightness, moonBrightness, cloudsFac );
            sky.starBrightness = MathUtil.lerp( sky.starBrightness, starBrightness, cloudsFac );
        }
    }

    @Override
    public void updatePrecipitation( Precipitation prec ) {
        prec.level = 0;
        prec.strength = 0;

        PrecipitationEnvEvent precEv = envEventManager.getByType( MDEnvEvents.PRECIPITATION );
        float precFac = precEv.getEffect();
        int precLv = precEv.getLevel();
        if( precFac > 0 && precLv > 0 ) {
            prec.level = precLv;
            prec.strength = precFac;
        }
    }

    @Override
    public void tickClient() {
        tick();
    }

    @Override
    public boolean isRaining() {
        PrecipitationEnvEvent precEv = envEventManager.getByType( MDEnvEvents.PRECIPITATION );
        return precEv.getLevel() > 0 && precEv.getEffect() > 0.2;
    }

    @Override
    public boolean isRainingAt( BlockPos pos ) {
        PrecipitationEnvEvent precEv = envEventManager.getByType( MDEnvEvents.PRECIPITATION );
        ModernityBiome biome = (ModernityBiome) world.getBiome( pos );
        IPrecipitationFunction func = biome.getPrecipitationFunction();
        IPrecipitation prec = func.computePrecipitation( precEv.getLevel() );
        if( precEv.getEffect() <= 0.2 ) {
            return false;
        } else if( prec.getHeight( world, pos.getX(), pos.getZ() ) > pos.getY() ) {
            return false;
        } else {
            return ! prec.isNone();
        }
    }
}
