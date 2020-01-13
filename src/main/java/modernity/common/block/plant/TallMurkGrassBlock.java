/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.block.IColoredBlock;
import modernity.api.util.MDVoxelShapes;
import modernity.client.ModernityClient;
import modernity.common.block.MDBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class TallMurkGrassBlock extends LimitedTallDirectionalPlantBlock implements IColoredBlock {

    public static final VoxelShape GRASS_END_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 10, 14 );
    public static final VoxelShape GRASS_MIDDLE_SHAPE = MDVoxelShapes.create16( 2, 0, 2, 14, 16, 14 );

    public static final IntegerProperty LENGTH = IntegerProperty.create( "length", 1, 4 );

    public TallMurkGrassBlock( Properties properties ) {
        super( properties, Direction.UP );
    }


    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.DIRTLIKE );
    }

    @Override
    public IntegerProperty getLengthProperty() {
        return LENGTH;
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

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isReplaceable( BlockState state, BlockItemUseContext ctx ) {
        return ctx.getItem().getItem() != asItem();
    }

    @Override
    public int getDefaultGenerationHeight( Random rng ) {
        if( rng.nextInt( 10 ) == 0 ) return 2;
        return 1;
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        Vec3d off = state.getOffset( world, pos );
        return ( state.get( END ) ? GRASS_END_SHAPE : GRASS_MIDDLE_SHAPE ).withOffset( off.x, off.y, off.z );
    }
}
