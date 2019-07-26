/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.world.dim;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

import modernity.common.biome.ModernityBiomeProvider;
import modernity.common.util.ProxyCommon;
import modernity.common.world.gen.ModernityChunkGenerator;
import modernity.common.world.gen.ModernityGenSettings;

import javax.annotation.Nullable;

public class ModernityDimension extends Dimension {

    @Override
    protected void init() {
    }

    @Override
    public IChunkGenerator<?> createChunkGenerator() {
        return new ModernityChunkGenerator( world, new ModernityBiomeProvider( world.getWorldInfo() ), new ModernityGenSettings() );
    }

    @Nullable
    @Override
    public BlockPos findSpawn( ChunkPos pos, boolean checkValid ) {
        return null;
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
        return true;
    }

    @Override
    public Vec3d getFogColor( float x, float z ) {
        return new Vec3d( 0, 0, 0.2 );
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
    public float getSunBrightnessFactor( float partialTicks ) {
        return 0.15F;
    }

    @Override
    public Vec3d getSkyColor( Entity cameraEntity, float partialTicks ) {
        return new Vec3d( 0, 0, 0.2 );
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
        return ProxyCommon.serverSettings().moonPhase.get();
    }
}
