/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.block.IColoredBlock;
import modernity.client.ModernityClient;
import modernity.common.block.plant.growing.MurinaGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MurinaBlock extends HangingPlantBlock implements IColoredBlock {
    public MurinaBlock( Properties properties ) {
        super( properties, MURINA_SHAPE );
        setGrowLogic( new MurinaGrowLogic() );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return ModernityClient.get().getGrassColors().getColor( reader, pos );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return ModernityClient.get().getGrassColors().getItemColor();
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
