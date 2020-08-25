/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.precipitation;

import modernity.client.render.environment.SurfaceWeatherRenderer;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class HeavySnowPrecipitation implements IPrecipitation {
    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return SurfaceWeatherRenderer.HEAVY_SNOW_TEXTURES;
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
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void computeUVOffset(float[] uv, int tick, float partialTicks, Random rand, int x, int z) {
        float renderTick = tick + partialTicks;
        double yOffset = -((tick & 511) + partialTicks) / 128;
        double uOffset = rand.nextDouble() + renderTick * 0.04 * rand.nextGaussian();
        double vOffset = rand.nextDouble() + renderTick * rand.nextGaussian() * 0.01;

        uv[0] = (float) uOffset;
        uv[1] = (float) (yOffset + vOffset);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(World world, int x, int z) {
        return 0xffffff;
    }

    @Override
    public void blockUpdate(World world, BlockPos pos) {
        int height = getHeight(world, pos.getX(), pos.getZ());

        if(pos.getY() == height && doesSnowGenerate(world, pos)) {
            BlockState state = world.getBlockState(pos);
            int level = state.isAir(world, pos) ? 0 : state.get(SnowBlock.LAYERS);
            level += level < 4 ? 1 : 0;
            world.setBlockState(pos, Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, level));
        }
    }

    private boolean doesSnowGenerate(World world, BlockPos pos) {
        if(!world.isAreaLoaded(pos, 1)) return false;
        if(pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(LightType.BLOCK, pos) < 10) {
            BlockState state = world.getBlockState(pos);
            boolean canReplace = state.isAir(world, pos) || state.getBlock() == Blocks.SNOW && state.get(SnowBlock.LAYERS) < 4;
            return canReplace && Blocks.SNOW.getDefaultState().isValidPosition(world, pos);
        }

        return false;
    }

    @Override
    public Biome.RainType type() {
        return Biome.RainType.SNOW;
    }
}
