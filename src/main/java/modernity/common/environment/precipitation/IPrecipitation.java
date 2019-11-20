/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.precipitation;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public interface IPrecipitation {
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getTexture();

    @OnlyIn( Dist.CLIENT )
    boolean hasParticles( Random rand );

    @OnlyIn( Dist.CLIENT )
    boolean hasSound( Random rand );

    @OnlyIn( Dist.CLIENT )
    IParticleData getParticleType( Random rand );

    @OnlyIn( Dist.CLIENT )
    void playSound( double x, double y, double z, boolean above, World world, float strength );

    @OnlyIn( Dist.CLIENT )
    boolean shouldRender();

    @OnlyIn( Dist.CLIENT )
    void computeUVOffset( float[] uv, int tick, float partialTicks, Random rand, int x, int z );

    @OnlyIn( Dist.CLIENT )
    int getColor( World world, int x, int z );

    @OnlyIn( Dist.CLIENT )
    default float getAlpha( World world, int x, int z ) {
        return 1;
    }

    void blockUpdate( World world, BlockPos pos );

    default int getHeight( World world, int x, int z ) {
        return world.getHeight( Heightmap.Type.MOTION_BLOCKING, x, z );
    }

    default boolean isNone() {
        return false;
    }
}
