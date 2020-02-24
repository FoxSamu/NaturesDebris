/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.common.world.dimen;

import modernity.api.dimension.*;
import modernity.client.environment.*;
import modernity.common.biome.MDBiomes;
import modernity.common.biome.ModernityBiome;
import modernity.common.environment.event.EnvironmentEventManager;
import modernity.common.environment.event.MDEnvEvents;
import modernity.common.environment.event.impl.*;
import modernity.common.environment.precipitation.IPrecipitation;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.environment.satellite.SatelliteData;
import modernity.common.generator.MurkSurfaceGeneration;
import modernity.common.handler.WorldTickHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.redgalaxy.util.MathUtil;

import javax.annotation.Nullable;

/**
 * The surface dimension of the Modernity.
 */
public class MurkSurfaceDimension extends Dimension implements IEnvironmentDimension, ISatelliteDimension, IEnvEventsDimension, IClientTickingDimension, IInitializeDimension, IPrecipitationDimension, IShaderDimension, IReverbDimension {

    private SatelliteData satelliteData;
    private EnvironmentEventManager envEventManager;

    public MurkSurfaceDimension( World world, DimensionType type ) {
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
        return MurkSurfaceGeneration.buildChunkGenerator( world );
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
        return MDDimensions.MURK_SURFACE.getType();
    }

    @Override
    public boolean hasSkyLight() {
        return true;
    }

    @Override
    public float getSunBrightness( float partialTicks ) {
        return 1;// 0.15F;
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
            MDEnvEvents.PRECIPITATION,
            MDEnvEvents.SKYLIGHT
        );
    }

    @Override
    public DimensionType getRespawnDimension( ServerPlayerEntity player ) {
        return getType();
    }

    @Override
    protected void generateLightBrightnessTable() {
        LightUtil.genLightBrightnessTable( lightBrightnessTable, 3, 0, 1 );
    }

    @Override
    public void getLightmapColors( float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors ) {
        LightUtil.updateLightColors( colors, partialTicks, sunBrightness, skyLight, blockLight );
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

        SkyLightEnvEvent skylightEv = getEnvEventManager().getByType( MDEnvEvents.SKYLIGHT );
        float skylightFac = skylightEv.getEffect();
        if( skylightFac > 0 ) {
            SkyLightEnvEvent.Color color = skylightEv.getColor();
            fog.color[ 0 ] = MathUtil.lerp( fog.color[ 0 ], color.r, skylightFac * 0.1F * fogFac );
            fog.color[ 1 ] = MathUtil.lerp( fog.color[ 1 ], color.g, skylightFac * 0.1F * fogFac );
            fog.color[ 2 ] = MathUtil.lerp( fog.color[ 2 ], color.b, skylightFac * 0.1F * fogFac );
        }

        float caveFac = EnvironmentRenderingManager.getCaveFactor();
        if( caveFac > 0 ) {
            fog.color[ 0 ] = MathUtil.lerp( fog.color[ 0 ], 0, caveFac );
            fog.color[ 1 ] = MathUtil.lerp( fog.color[ 1 ], 0, caveFac );
            fog.color[ 2 ] = MathUtil.lerp( fog.color[ 2 ], 0, caveFac );
            fog.density = MathUtil.lerp( fog.density, 0.07F, caveFac );
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

        SkyLightEnvEvent skylightEv = envManager.getByType( MDEnvEvents.SKYLIGHT );
        float skylightFac = skylightEv.getEffect();
        if( skylightFac > 0 ) {
            SkyLightEnvEvent.Color color = skylightEv.getColor();
            sky.skylightColor[ 0 ] = MathUtil.lerp( sky.skylightColor[ 0 ], color.r, skylightFac );
            sky.skylightColor[ 1 ] = MathUtil.lerp( sky.skylightColor[ 1 ], color.g, skylightFac );
            sky.skylightColor[ 2 ] = MathUtil.lerp( sky.skylightColor[ 2 ], color.b, skylightFac );
            sky.backlightColor[ 0 ] = MathUtil.lerp( sky.backlightColor[ 0 ], color.r, skylightFac * 0.06F );
            sky.backlightColor[ 1 ] = MathUtil.lerp( sky.backlightColor[ 1 ], color.g, skylightFac * 0.06F );
            sky.backlightColor[ 2 ] = MathUtil.lerp( sky.backlightColor[ 2 ], color.b, skylightFac * 0.06F );
            sky.twilightColor[ 0 ] = MathUtil.lerp( sky.twilightColor[ 0 ], color.r, skylightFac * 0.13F );
            sky.twilightColor[ 1 ] = MathUtil.lerp( sky.twilightColor[ 1 ], color.g, skylightFac * 0.13F );
            sky.twilightColor[ 2 ] = MathUtil.lerp( sky.twilightColor[ 2 ], color.b, skylightFac * 0.13F );
        }

        if( world.getLastLightningBolt() > 0 ) {
            sky.backlightColor[ 0 ] = MathUtil.lerp( sky.backlightColor[ 0 ], 1, 0.5F );
            sky.backlightColor[ 1 ] = MathUtil.lerp( sky.backlightColor[ 1 ], 1, 0.5F );
            sky.backlightColor[ 2 ] = MathUtil.lerp( sky.backlightColor[ 2 ], 1, 0.5F );
        }

        float caveFac = EnvironmentRenderingManager.getCaveFactor();
        if( caveFac > 0 ) {
            sky.moonBrightness = MathUtil.lerp( sky.moonBrightness, 0, caveFac );
            sky.starBrightness = MathUtil.lerp( sky.starBrightness, 0, caveFac );
            sky.twilightBrightness = MathUtil.lerp( sky.twilightBrightness, 0, caveFac );
            sky.cloudBrightness = MathUtil.lerp( sky.cloudBrightness, 0, caveFac );
            sky.backlightBrightness = MathUtil.lerp( sky.backlightBrightness, 0, caveFac );
            sky.skylightBrightness = MathUtil.lerp( sky.skylightBrightness, 0, caveFac );
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void updatePrecipitation( Precipitation prec ) {
        prec.level = 0;
        prec.strength = 0;

        PrecipitationEnvEvent precEv = getEnvEventManager().getByType( MDEnvEvents.PRECIPITATION );
        float precFac = precEv.getEffect();
        int precLv = precEv.getLevel();
        if( precFac > 0 && precLv > 0 ) {
            prec.level = precLv;
            prec.strength = precFac;
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void updateLight( Light light ) {
        light.setSky( 0.07, 0.08, 0.1 );
        light.setAmbient( 0.02, 0.02, 0.02 );
        light.setBlock( 0xfef0de );

        SkyLightEnvEvent skylightEv = getEnvEventManager().getByType( MDEnvEvents.SKYLIGHT );
        float skylightFac = skylightEv.getEffect();
        if( skylightFac > 0 ) {
            SkyLightEnvEvent.Color color = skylightEv.getColor();
            light.sky[ 0 ] = MathUtil.lerp( light.sky[ 0 ], color.r * 0.2F + 0.4F, skylightFac );
            light.sky[ 1 ] = MathUtil.lerp( light.sky[ 1 ], color.g * 0.2F + 0.4F, skylightFac );
            light.sky[ 2 ] = MathUtil.lerp( light.sky[ 2 ], color.b * 0.2F + 0.4F, skylightFac );
        }

        if( world.getLastLightningBolt() > 0 ) {
            light.setSky( 0.7, 0.7, 0.7 );
        }

        float caveFac = EnvironmentRenderingManager.getCaveFactor();
        if( caveFac > 0 ) {
            light.ambient[ 0 ] = MathUtil.lerp( light.ambient[ 0 ], 0, caveFac );
            light.ambient[ 1 ] = MathUtil.lerp( light.ambient[ 1 ], 0, caveFac );
            light.ambient[ 2 ] = MathUtil.lerp( light.ambient[ 2 ], 0, caveFac );
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
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
        Biome b = world.getBiome( pos );
        if( ! ( b instanceof ModernityBiome ) ) return false;
        ModernityBiome biome = (ModernityBiome) b;
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

    @Override
    public int getRainLevel() {
        PrecipitationEnvEvent precEv = envEventManager.getByType( MDEnvEvents.PRECIPITATION );
        return precEv.getLevel();
    }

    @Override
    public double getRainAmount() {
        PrecipitationEnvEvent precEv = envEventManager.getByType( MDEnvEvents.PRECIPITATION );
        return precEv.getEffect();
    }

    @Override
    @SuppressWarnings( "ConstantConditions" )
    // Direct inheritance from world that uses modernity default biome in unloaded regions
    public Biome getBiome( BlockPos pos ) {
        AbstractChunkProvider provider = world.getChunkProvider();
        Chunk chunk = provider.getChunk( pos.getX() >> 4, pos.getZ() >> 4, false );
        if( chunk != null ) {
            return chunk.getBiome( pos );
        } else {
            ChunkGenerator<?> gen = world.getChunkProvider().getChunkGenerator();
            return gen == null ? MDBiomes.DEFAULT : gen.getBiomeProvider().getBiome( pos );
        }
    }

    @Override
    public boolean hasReverb( Vec3d pos ) {
        return true;
    }
}
