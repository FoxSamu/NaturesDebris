/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.generic.block.IColoredBlock;
import modernity.common.block.plant.growing.MurinaGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.IWorldReader;
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
    public int colorMultiplier( BlockState state, @Nullable ILightReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return 0;
//        return ModernityClient.get().getGrassColors().getColor( reader, pos );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return 0;
//        return ModernityClient.get().getGrassColors().getItemColor();
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( BlockTags.LEAVES ) || super.canBlockSustain( world, pos, state );
    }
}
