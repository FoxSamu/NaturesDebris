/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.world.dimen;

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

import javax.annotation.Nullable;

/**
 * The surface dimension of the Modernity.
 */
public class MDSurfaceDimension extends Dimension {

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
        return true;
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
}
