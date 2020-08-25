/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.precipitation;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public enum EDefaultPrecipitation implements IPrecipitation {
    NONE(new NoPrecipitation()),
    DRIZZLE(new DrizzlePrecipitation()),
    SHOWERS(new ShowersPrecipitation()),
    RAIN(new RainPrecipitation()),
    HAIL(new HailPrecipitation()),
    WET_SNOW(new WetSnowPrecipitation()),
    SNOW(new SnowPrecipitation()),
    HEAVY_SNOW(new HeavySnowPrecipitation());

    private final IPrecipitation wrapped;

    EDefaultPrecipitation(IPrecipitation wrapped) {
        this.wrapped = wrapped;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return wrapped.getTexture();
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasParticles(Random rand) {
        return wrapped.hasParticles(rand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasSound(Random rand) {
        return wrapped.hasSound(rand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IParticleData getParticleType(Random rand) {
        return wrapped.getParticleType(rand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void playSound(double x, double y, double z, boolean above, World world, float strength) {
        wrapped.playSound(x, y, z, above, world, strength);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRender() {
        return wrapped.shouldRender();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void computeUVOffset(float[] uv, int tick, float partialTicks, Random rand, int x, int z) {
        wrapped.computeUVOffset(uv, tick, partialTicks, rand, x, z);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(World world, int x, int z) {
        return wrapped.getColor(world, x, z);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getAlpha(World world, int x, int z) {
        return wrapped.getAlpha(world, x, z);
    }

    @Override
    public void blockUpdate(World world, BlockPos pos) {
        wrapped.blockUpdate(world, pos);
    }

    @Override
    public int getHeight(World world, int x, int z) {
        return wrapped.getHeight(world, x, z);
    }

    @Override
    public boolean isNone() {
        return wrapped.isNone();
    }

    @Override
    public Biome.RainType type() {
        return wrapped.type();
    }

    @Override
    public boolean canFloodFarmland() {
        return wrapped.canFloodFarmland();
    }
}
