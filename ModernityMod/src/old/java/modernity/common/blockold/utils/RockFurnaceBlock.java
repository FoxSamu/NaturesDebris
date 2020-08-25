/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.utils;

import modernity.common.tileentity.RockFurnaceTileEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class RockFurnaceBlock extends AbstractFurnaceBlock {
    public RockFurnaceBlock(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new RockFurnaceTileEntity();
    }

    @Override
    protected void interactWith(World world, BlockPos pos, PlayerEntity player) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof RockFurnaceTileEntity) {
            player.openContainer((INamedContainerProvider) te);
            player.addStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if(state.get(LIT)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY();
            double z = pos.getZ() + 0.5;
            if(rand.nextDouble() < 0.1) {
                world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1, 1, false);
            }

            Direction dir = state.get(FACING);
            Direction.Axis axis = dir.getAxis();
            double horizOff = rand.nextDouble() * 0.6 - 0.3;
            double xOff = axis == Direction.Axis.X ? dir.getXOffset() * 0.52 : horizOff;
            double yOff = rand.nextDouble() * 6 / 16;
            double zOff = axis == Direction.Axis.Z ? dir.getZOffset() * 0.52 : horizOff;
            world.addParticle(ParticleTypes.SMOKE, x + xOff, y + yOff, z + zOff, 0, 0, 0);
            world.addParticle(ParticleTypes.FLAME, x + xOff, y + yOff, z + zOff, 0, 0, 0);
        }
    }
}
