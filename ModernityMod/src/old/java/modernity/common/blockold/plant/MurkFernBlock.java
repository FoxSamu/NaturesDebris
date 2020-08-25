/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant;

import modernity.common.blockold.MDBlockTags;
import modernity.common.blockold.plant.growing.MurkFernGrowLogic;
import modernity.generic.block.IColoredBlock;
import modernity.generic.block.IReceiveFertilityFromFloorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.ILightReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class MurkFernBlock extends SimplePlantBlock implements IColoredBlock, IReceiveFertilityFromFloorBlock {
    public static final VoxelShape FERN_SHAPE = makePlantShape(14, 14);

    private final MurkFernGrowLogic logic;

    public MurkFernBlock(Properties properties) {
        super(properties, FERN_SHAPE);
        setGrowLogic(logic = new MurkFernGrowLogic());
    }

    @Override
    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state) {
        return state.isIn(MDBlockTags.DIRTLIKE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int colorMultiplier(BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex) {
        return 0;
//        return ModernityClient.get().getFernColors().getColor( reader, pos );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return 0;
//        return ModernityClient.get().getFernColors().getItemColor();
    }

    @Override
    public void receiveFertilityFromFloor(World world, BlockPos pos, BlockState state, Random rand) {
        logic.growFromFloor(world, pos, state, rand);
    }
}
