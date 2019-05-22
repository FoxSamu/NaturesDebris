package modernity.common.world.dim;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

import modernity.common.biome.MDBiomes;
import modernity.common.world.gen.ModernityChunkGenerator;

import javax.annotation.Nullable;

public class ModernityDimension extends Dimension {
    @Override
    protected void init() {

    }

    @Override
    public IChunkGenerator<?> createChunkGenerator() {
        return new ModernityChunkGenerator( world, new SingleBiomeProvider( new SingleBiomeProviderSettings().setBiome( MDBiomes.MEADOW ) ) );
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
        return 0;
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
        return false;
    }

    @Override
    public boolean doesXZShowFog( int x, int z ) {
        return true;
    }

    @Override
    public DimensionType getType() {
        return MDDimensions.MODERNITY.getType();
    }
}
