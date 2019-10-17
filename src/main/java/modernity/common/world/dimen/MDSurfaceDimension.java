/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.world.dimen;

import modernity.api.dimension.IEnvironmentDimension;
import modernity.client.environment.Fog;
import modernity.client.environment.Sky;
import modernity.common.world.gen.MDSurfaceChunkGenerator;
import modernity.common.world.gen.MDSurfaceGenSettings;
import modernity.common.world.gen.biome.MDSurfaceBiomeProvider;
import modernity.common.world.gen.biome.MDSurfaceBiomeProviderSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * The surface dimension of the Modernity.
 */
public class MDSurfaceDimension extends Dimension implements IEnvironmentDimension {

    public MDSurfaceDimension( World world, DimensionType type ) {
        super( world, type );
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
        return false;
    }

    @Override
    public int getMoonPhase( long worldTime ) {
        return 0;
    }

//    @Override
//    public int getMoonPhase( long worldTime ) {
//        return Modernity.serverSettings().moonPhase.get();
//    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void updateFog( Fog fog ) {
        fog.setColor( 0, 0, 0 );
        fog.type = Fog.Type.EXP2;
        fog.density = 0.01F;
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
        sky.moonPhase = 0;
        sky.moonRotation = 0.5F;
    }
}
