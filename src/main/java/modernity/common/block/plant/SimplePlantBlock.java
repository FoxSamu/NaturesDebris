/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockTags;
import modernity.common.block.plant.growing.IPlantResources;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class SimplePlantBlock extends SingleDirectionalPlantBlock {

    public static final VoxelShape MELION_SHAPE = makePlantShape( 12, 12 );
    public static final VoxelShape MILLIUM_SHAPE = makePlantShape( 14, 8 );
    public static final VoxelShape MINT_SHAPE = makePlantShape( 14, 9 );
    public static final VoxelShape HORSETAIL_SHAPE = makePlantShape( 14, 12 );
    public static final VoxelShape LAKEWEED_SHAPE = makePlantShape( 14, 10 );
    public static final VoxelShape HEATH_SHAPE = makePlantShape( 16, 7 );
    public static final VoxelShape SEEPWEED_SHAPE = makePlantShape( 15, 16 );
    public static final VoxelShape NUDWART_SHAPE = makePlantShape( 14, 9 );
    public static final VoxelShape RED_GRASS_SHAPE = makePlantShape( 15, 13 );
    public static final VoxelShape MURK_FLOWER_SHAPE = makePlantShape( 14, 12 );
    public static final VoxelShape MILK_EYE_SHAPE = makePlantShape( 14, 8 );
    public static final VoxelShape EGIUM_SHAPE = makePlantShape( 14, 10 );
    public static final VoxelShape BULBFLOWER_SHAPE = makePlantShape( 9, 11 );
    public static final VoxelShape MOOR_CREEP_SHAPE = makePlantShape( 14, 5 );

    public static final Type MINT = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 3 ).chance( 6, 16 ).requireWet() );
        return MINT_SHAPE;
    };
    public static final Type MELION = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ).requireWet() );
        return MELION_SHAPE;
    };
    public static final Type MILLIUM = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ).requireWet() );
        return MILLIUM_SHAPE;
    };
    public static final Type HORSETAIL = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 2, 3 ).chance( 6, 16 ).requireWet() );
        return HORSETAIL_SHAPE;
    };
    public static final Type LAKEWEED = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ) );
        return LAKEWEED_SHAPE;
    };
    public static final Type HEATH = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 0, 1 ).chance( 6, 16 ).requireWet() );
        return HEATH_SHAPE;
    };
    public static final Type DEAD_HEATH = plant -> HEATH_SHAPE;

    public static final Type SEEPWEED = plant -> SEEPWEED_SHAPE;
    public static final Type NUDWART = plant -> NUDWART_SHAPE;

    public static final Type RED_GRASS = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ).requireWet() );
        return RED_GRASS_SHAPE;
    };
    public static final Type MURK_FLOWER = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 2, 3 ).chance( 6, 16 ).requireWet() );
        return MURK_FLOWER_SHAPE;
    };
    public static final Type MILK_EYE = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 3 ).chance( 6, 16 ).requireWet() );
        return MILK_EYE_SHAPE;
    };
    public static final Type EGIUM = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ).requireWet() );
        return EGIUM_SHAPE;
    };
    public static final Type BULBFLOWER = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ).requireWet() );
        return BULBFLOWER_SHAPE;
    };
    public static final Type MOOR_CREEP = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 0, 2 ).chance( 6, 16 ).requireWet() );
        return MOOR_CREEP_SHAPE;
    };

    protected final VoxelShape shape;

    @Deprecated
    public SimplePlantBlock( Properties properties, VoxelShape shape ) {
        super( properties, Direction.UP );
        this.shape = shape;
    }

    public SimplePlantBlock( Properties properties, Type type ) {
        super( properties, Direction.UP );
        this.shape = type.setup( this );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.DIRTLIKE );
    }

    @Override
    public VoxelShape getShape( BlockState state ) {
        return shape;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    public interface Type {
        VoxelShape setup( SimplePlantBlock block );
    }
}
