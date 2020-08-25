/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.precipitation;

import modernity.common.particle.MDParticleTypes;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class NoPrecipitation implements IPrecipitation {
    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasParticles(Random rand) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasSound(Random rand) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IParticleData getParticleType(Random rand) {
        return MDParticleTypes.RAIN;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void playSound(double x, double y, double z, boolean above, World world, float strength) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRender() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void computeUVOffset(float[] uv, int tick, float partialTicks, Random rand, int x, int z) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(World world, int x, int z) {
        return 0;
    }

    @Override
    public void blockUpdate(World world, BlockPos pos) {

    }

    @Override
    public boolean isNone() {
        return true;
    }
}
