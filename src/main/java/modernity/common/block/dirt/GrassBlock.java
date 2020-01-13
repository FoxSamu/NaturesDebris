/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt;

import modernity.api.biome.BiomeColoringProfile;
import modernity.api.block.IColoredBlock;
import modernity.client.ModernityClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.redgalaxy.util.Lazy;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GrassBlock extends SnowyDirtlikeBlock implements ISpreadableDirt, IDecayableDirt, IColoredBlock {
    private final Lazy<BlockState> dirtState;
    private final Lazy<Block> dirtBlock;

    public GrassBlock( Properties properties, Supplier<BlockState> dirt ) {
        super( properties );
        this.dirtState = Lazy.of( dirt );
        this.dirtBlock = dirtState.map( BlockState::getBlock );
    }

    @OnlyIn( Dist.CLIENT )
    protected BiomeColoringProfile getColorMap() {
        return ModernityClient.get().getGrassColors();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return getColorMap().getColor( reader, pos );
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public int colorMultiplier( ItemStack stack, int tintIndex ) {
        return getColorMap().getItemColor();
    }

    @Override
    public BlockState getDecayState( World world, BlockPos pos, BlockState state ) {
        return dirtState.get();
    }

    @Override
    public boolean canGrowUpon( BlockState state ) {
        return state.getBlock() == dirtBlock.get();
    }

    @Override
    public BlockState getGrowState( World world, BlockPos pos, BlockState state ) {
        boolean snowy = false;
        BlockState upState = world.getBlockState( pos.up() );
        if( upState.getBlock() == Blocks.SNOW || upState.getBlock() == Blocks.SNOW_BLOCK ) {
            snowy = true;
        }
        return getDefaultState().with( SNOWY, snowy );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isSolid( BlockState state ) {
        return true; // Must override because render layer is not solid
    }
}
