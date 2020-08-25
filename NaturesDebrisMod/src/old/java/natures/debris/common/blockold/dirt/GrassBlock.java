/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.dirt;

import natures.debris.client.colors.IColorProfile;
import natures.debris.client.particle.ExtendedDiggingParticle;
import natures.debris.common.blockold.dirt.logic.DirtLogic;
import natures.debris.generic.block.IColoredBlock;
import natures.debris.generic.block.ICustomColoredParticlesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class GrassBlock extends SnowyDirtlikeBlock implements IColoredBlock, ICustomColoredParticlesBlock {

    public GrassBlock(DirtLogic logic, Properties properties) {
        super(logic, properties);
    }

    @OnlyIn(Dist.CLIENT)
    protected IColorProfile getColorMap() {
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int colorMultiplier(BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex) {
        return getColorMap().getColor(reader, pos);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return getColorMap().getItemColor();
    }

    @Override
    public int getColor(World world, @Nullable BlockPos pos, BlockState state) {
        return 0xFFFFFF;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        BlockRayTraceResult brtr = (BlockRayTraceResult) target;
        ExtendedDiggingParticle.addBlockHitEffects(manager, world, brtr.getPos(), brtr.getFace());
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        ExtendedDiggingParticle.addBlockDestroyEffects(manager, world, pos, state);
        return true;
    }
}
