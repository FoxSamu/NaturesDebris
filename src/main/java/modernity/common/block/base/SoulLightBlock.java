/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 26 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.common.tileentity.SoulLightTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SoulLightBlock extends Block {
    private static final VoxelShape HITBOX = makeCuboidShape( 4, 4, 4, 12, 12, 12 );
    private static final VoxelShape COLLIDER = VoxelShapes.empty();

    public SoulLightBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new SoulLightTileEntity();
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return HITBOX;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return COLLIDER;
    }

    @Override
    public BlockRenderType getRenderType( BlockState state ) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
